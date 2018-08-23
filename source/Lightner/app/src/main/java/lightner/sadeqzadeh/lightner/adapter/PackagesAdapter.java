package lightner.sadeqzadeh.lightner.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;
import lightner.sadeqzadeh.lightner.fragment.CategoryHomeFragment;
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.LightnerPackage;
import lightner.sadeqzadeh.lightner.rest.PackageFlashcard;
import lightner.sadeqzadeh.lightner.rest.PackageWordsResponse;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import lightner.sadeqzadeh.lightner.util.IabHelper;
import lightner.sadeqzadeh.lightner.util.IabResult;
import lightner.sadeqzadeh.lightner.util.Purchase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackagesAdapter  extends RecyclerView.Adapter<PackagesAdapter.ViewHolder> {
    private static final String TAG = PackagesAdapter.class.getName();
    MainActivity mainActivity;
    Context context;
    List<LightnerPackage> lightnerPackages;
    List<Purchase> userPackages;
    Button buyPackages;
    FlashcardDao flashcardDao;
    long currentCategoryId;
    public PackagesAdapter(Context context, MainActivity mainActivity, List<LightnerPackage> lightnerPackages, List<Purchase> userPackages, FlashcardDao flashcardDao, long currentCategory) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.lightnerPackages = lightnerPackages;
        this.userPackages  =  userPackages;
        this.buyPackages = buyPackages;
        this.flashcardDao  = flashcardDao;
        this.currentCategoryId = currentCategory;
    }

    @Override
    public PackagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_card, parent, false);
        return new PackagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PackagesAdapter.ViewHolder holder, int position) {
        final LightnerPackage lightnerPackage = lightnerPackages.get(position);
        holder.name.setText(lightnerPackage.getName());
        holder.price.setText(String.format("%s %s %s",holder.price.getText(),lightnerPackage.getPrice(), context.getString(R.string.toman)));
        for(Purchase  userPackage: userPackages){
            if(userPackage.getSku().equals(String.valueOf(lightnerPackage.getId()))){
                holder.buybtn.setVisibility(View.GONE);
                holder.insertFlashcards.setVisibility(View.VISIBLE);
                break;
            }
        }
        holder.buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
                    public void onIabPurchaseFinished(IabResult result, Purchase purchase)
                    {
                        mainActivity.hideProgressbar();
                        if (result.isFailure()) {
                            Log.d(TAG, "Error purchasing: " + result);
                            Toast.makeText(mainActivity.getApplicationContext(),mainActivity.getString(R.string.error_in_purchase), Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(mainActivity.getApplicationContext(),mainActivity.getString(R.string.buy_success), Toast.LENGTH_LONG).show();
                        holder.buybtn.setVisibility(View.GONE);
                        holder.insertFlashcards.setVisibility(View.VISIBLE);
                    }
                };
                if(mainActivity.iapStatus){
                    mainActivity.showProgressbar();
                    mainActivity.mHelper.flagEndAsync();
                    try {
                        mainActivity.mHelper.launchPurchaseFlow(mainActivity,String.valueOf(lightnerPackage.getId()),1000,mPurchaseFinishedListener);
                    }catch (Exception e){
                        Log.d(TAG,e.getMessage());
                    }
                }
/*

                Intent intent = new Intent(mainActivity.getApplicationContext(), IAPActivity.class);
                mainActivity.startActivity(intent);
*/
            }
        });

        holder.insertFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showProgressbar();
                LightnerAPI lightnerAPI = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);
                String userCode = Util.fetchAndDecrypt(mainActivity.getApplicationContext(), Const.USER_CODE);

                Call<PackageWordsResponse> call = lightnerAPI.getPackageFlashcards(userCode, lightnerPackage.getId());
                call.enqueue(new Callback<PackageWordsResponse>() {
                    @Override
                    public void onResponse(Call<PackageWordsResponse> call, Response<PackageWordsResponse> response) {
                        if( response.code() == 400 ){
                            mainActivity.hideProgressbar();
                            Toast.makeText(mainActivity.getApplicationContext(),mainActivity.getString(R.string.bad_request),Toast.LENGTH_LONG).show();
                            return;
                        }

                        if( response.body().getStatus() != null &&  response.body().getStatus().equals("invalid user") ){
                            mainActivity.hideProgressbar();
                            Toast.makeText(mainActivity.getApplicationContext(),mainActivity.getString(R.string.invalid_user_code),Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(response.body().getStatus() != null && response.body().getStatus().equals("invalid package") ){
                            mainActivity.hideProgressbar();
                            Toast.makeText(mainActivity.getApplicationContext(),mainActivity.getString(R.string.invalid_package),Toast.LENGTH_LONG).show();
                            return;
                        }
                        List<PackageFlashcard> flashcardList  =  response.body().getFlashcards();
                        if (flashcardList == null){
                            return;
                        }

                        for(PackageFlashcard flashcard : flashcardList){
                            Flashcard f = new Flashcard(flashcard.getQuestion(), flashcard.getAnswer(), 1, null,new Date(), currentCategoryId);
                            flashcardDao.insert(f);
                        }
                        mainActivity.hideProgressbar();
                        Toast.makeText(mainActivity.getApplicationContext(),String.format(mainActivity.getString(R.string.flashcard_inserted_successfull),flashcardList.size()),Toast.LENGTH_LONG).show();
                        Bundle args = new Bundle();
                        args.putLong(Const.CATEGORY_ID,currentCategoryId);
                        CategoryHomeFragment categoryHomeFragment  = new CategoryHomeFragment();
                        categoryHomeFragment.setArguments(args);
                        mainActivity.replaceFragment(categoryHomeFragment, CategoryHomeFragment.TAG);

                    }

                    @Override
                    public void onFailure(Call<PackageWordsResponse> call, Throwable t) {
                        mainActivity.hideProgressbar();
                        Toast.makeText(mainActivity.getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return lightnerPackages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CardView cardView;
        TextView price;
        TextView buybtn;
        TextView insertFlashcards;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.card_view);
            price = itemView.findViewById(R.id.price);
            buybtn =  itemView.findViewById(R.id.add_package_to_basket);
            insertFlashcards  =  itemView.findViewById(R.id.insert_flashcards);
        }
    }

}

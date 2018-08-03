package lightner.sadeqzadeh.lightner.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.rest.LightnerPackage;
import lightner.sadeqzadeh.lightner.rest.UserPackage;
import lightner.sadeqzadeh.lightner.util.IabHelper;

public class PackagesAdapter  extends RecyclerView.Adapter<PackagesAdapter.ViewHolder> {
    private static final String TAG = PackagesAdapter.class.getName();
    MainActivity mainActivity;
    Context context;
    List<LightnerPackage> lightnerPackages;
    List<UserPackage> userPackages;
    HashMap<Long,Boolean> selectedPackageIdstoBuy = new HashMap<>();
    Button buyPackages;
    public PackagesAdapter(Context context, MainActivity mainActivity, List<LightnerPackage> lightnerPackages, List<UserPackage> userPackages, HashMap<Long,Boolean> selectedPackageIdstoBuy, Button buyPackages) {
        this.context = context;
        this.mainActivity = mainActivity;
        this.lightnerPackages = lightnerPackages;
        this.userPackages  =  userPackages;
        this.buyPackages = buyPackages;
        this.selectedPackageIdstoBuy = selectedPackageIdstoBuy;
    }

    @Override
    public PackagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_card, parent, false);
        return new PackagesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PackagesAdapter.ViewHolder holder, int position) {
        final LightnerPackage lightnerPackage = lightnerPackages.get(position);
        holder.name.setText(lightnerPackage.getName());
        holder.price.setText(String.format("%s %s %s",holder.price.getText(),lightnerPackage.getPrice(), context.getString(R.string.toman)));
        for(UserPackage  userPackage: userPackages){
            if(userPackage.getPackageId()  ==  lightnerPackage.getId()){
                holder.addPackageToBasket.setVisibility(View.GONE);
                holder.insertFlashcards.setVisibility(View.VISIBLE);
                break;
            }
        }
        holder.addPackageToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPackageIdstoBuy.put(lightnerPackage.getId(), true);
                buyPackages.setText(String.format(mainActivity.getResources().getString(R.string.buy_with_number),selectedPackageIdstoBuy.entrySet().size()));
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
        TextView addPackageToBasket;
        TextView insertFlashcards;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.card_view);
            price = itemView.findViewById(R.id.price);
            addPackageToBasket =  itemView.findViewById(R.id.add_package_to_basket);
            insertFlashcards  =  itemView.findViewById(R.id.insert_flashcards);
        }
    }

}

package lightner.sadeqzadeh.lightner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.adapter.PackagesAdapter;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.PackageFlashcard;
import lightner.sadeqzadeh.lightner.rest.PackageWordsResponse;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import lightner.sadeqzadeh.lightner.util.IabHelper;
import lightner.sadeqzadeh.lightner.util.IabResult;
import lightner.sadeqzadeh.lightner.util.Inventory;
import lightner.sadeqzadeh.lightner.util.Purchase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFlashcardMethodsFragment extends Fragment {

    public static final String TAG = AddFlashcardMethodsFragment.class.getName();
    MainActivity mainActivity;
    private Bundle args;
    private Long categoryId;
    private Button addManuallyBtn;
    private Button buyFlashcardBtn;
    private Button usePackageBzarBtn;

    private FlashcardDao flashcardDao;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        args = getArguments();
        categoryId  = args.getLong(Const.CATEGORY_ID);
        flashcardDao =  mainActivity.getDaoSession().getFlashcardDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_flashcard_method_layout, container, false);
        addManuallyBtn = view.findViewById(R.id.add_manually_flashcard_btn);
        addManuallyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle argsFragment = new Bundle();
                argsFragment.putLong(Const.CATEGORY_ID,categoryId);
                NewFlashCardFragment fragment = new NewFlashCardFragment();
                fragment.setArguments(argsFragment);
                mainActivity.replaceFragment(fragment, fragment.TAG,false);
            }
        });

        buyFlashcardBtn = view.findViewById(R.id.buy_flashcard_btn);
        buyFlashcardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadPackageFragment fragment = new DownloadPackageFragment();
                Bundle args  = new Bundle();
                args.putLong(Const.CATEGORY_ID,categoryId);
                fragment.setArguments(args);
                mainActivity.replaceFragment(fragment, fragment.TAG,false);
            }
        });

        usePackageBzarBtn = view.findViewById(R.id.use_package_in_bazar_btn);
        usePackageBzarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get  purchased packages
                IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
                    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                        mainActivity.hideProgressbar();
                        Log.d(TAG, "Query inventory finished.");
                        if (result.isFailure()) {
                            Log.d(TAG, "Failed to query inventory: " + result);
                            return;
                        }
                        else {
                            Log.d(TAG, "Query inventory was successful.");

                            IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
                                    new IabHelper.OnConsumeFinishedListener() {
                                        public void onConsumeFinished(Purchase purchase, IabResult result) {
                                            if (result.isSuccess()) {
                                                Toast.makeText(mainActivity.getApplicationContext(),"مصرف شد",Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                Toast.makeText(mainActivity.getApplicationContext(),result.getMessage(),Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    };
                            if(mainActivity.iapStatus){
                                mainActivity.mHelper.flagEndAsync();
                                mainActivity.mHelper.consumeAsync(inventory.getPurchase("1"),mConsumeFinishedListener);
                            }


                        }
                        Log.d(TAG, "Initial inventory query finished; enabling main UI.");
                    }
                };
                if(mainActivity.iapStatus){
                    mainActivity.mHelper.flagEndAsync();
                    mainActivity.mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            }

        });

        //if user pressed back after going to buy
        mainActivity.hideProgressbar();
        return  view;
    }

}

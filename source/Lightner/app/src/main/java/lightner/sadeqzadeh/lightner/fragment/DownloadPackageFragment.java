package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.adapter.PackagesAdapter;
import lightner.sadeqzadeh.lightner.entity.Category;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.PackagesDataResponse;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import lightner.sadeqzadeh.lightner.util.IabHelper;
import lightner.sadeqzadeh.lightner.util.IabResult;
import lightner.sadeqzadeh.lightner.util.Inventory;
import lightner.sadeqzadeh.lightner.util.Purchase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadPackageFragment extends Fragment {

    public static final String TAG = DownloadPackageFragment.class.getName();
    RecyclerView recyclerView;
    MainActivity mainActivity;
    long currentCategoryId;
    FlashcardDao flashcardDao;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity  = (MainActivity) getActivity();
        flashcardDao = mainActivity.getDaoSession().getFlashcardDao();
        currentCategoryId = getArguments().getLong(Const.CATEGORY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_package_layout, container, false);
        recyclerView = view.findViewById(R.id.packages_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEnabled(false);
        LightnerAPI  lightnerAPI  = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);

        String userCode = Util.fetchAndDecrypt(mainActivity.getApplicationContext(), Const.USER_CODE);
        Call<PackagesDataResponse> packagesDataResponseCall = lightnerAPI.getPackagesList(userCode);
        mainActivity.showProgressbar();
        packagesDataResponseCall.enqueue(new Callback<PackagesDataResponse>() {
            @Override
            public void onResponse(Call<PackagesDataResponse> call, final Response<PackagesDataResponse> response) {
                if(response.body().getStatus() != null && response.body().getStatus().equals("invalid user")){
                    Toast.makeText(mainActivity.getApplicationContext(), getString(R.string.invalid_user_code),Toast.LENGTH_LONG).show();
                    return;
                }
                IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
                    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                        mainActivity.hideProgressbar();
                        Log.d(TAG, "Query inventory finished.");
                        if (result.isFailure()) {
                            Log.d(TAG, "Failed to query inventory: " + result);
                            Toast.makeText(mainActivity.getApplicationContext(),getString(R.string.error_in_get_inventory) + result,Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            Log.d(TAG, "Query inventory was successful.");
                            List<Purchase> userOwnedSku = inventory.getAllPurchases();
                            PackagesAdapter packagesAdapter = new PackagesAdapter(mainActivity.getApplicationContext(), mainActivity,response.body().getPackages(), userOwnedSku,flashcardDao, currentCategoryId);
                            recyclerView.setAdapter(packagesAdapter);
                        }
                        Log.d(TAG, "Initial inventory query finished; enabling main UI.");
                    }
                };
                if(mainActivity.iapStatus){
                    mainActivity.mHelper.flagEndAsync();
                    mainActivity.mHelper.queryInventoryAsync(mGotInventoryListener);
                }
            }

            @Override
            public void onFailure(Call<PackagesDataResponse> call, Throwable t) {
                mainActivity.hideProgressbar();
                Toast.makeText(mainActivity.getApplicationContext(), getString(R.string.error_in_getting_packages),Toast.LENGTH_LONG).show();
            }
        });

        return  view;
    }
}

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.adapter.PackagesAdapter;
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.PackagesDataResponse;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import lightner.sadeqzadeh.lightner.util.IabHelper;
import lightner.sadeqzadeh.lightner.util.IabResult;
import lightner.sadeqzadeh.lightner.util.Purchase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadPackageFragment extends Fragment {

    public static final String TAG = DownloadPackageFragment.class.getName();
    RecyclerView recyclerView;
    MainActivity mainActivity;
    Button buyPackages;
    IabHelper mHelper;
    HashMap<Long, Boolean> selectedPackageIdstoBuy = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity  = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_package_layout, container, false);
        recyclerView = view.findViewById(R.id.packages_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setEnabled(false);
        buyPackages  = view.findViewById(R.id.buy_flashcard_btn);
        LightnerAPI  lightnerAPI  = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);

        String userCode = Util.fetchAndDecrypt(mainActivity.getApplicationContext(), Const.USER_CODE);
        Call<PackagesDataResponse> packagesDataResponseCall = lightnerAPI.getPackagesList(userCode);
        mainActivity.showProgressbar();
        packagesDataResponseCall.enqueue(new Callback<PackagesDataResponse>() {
            @Override
            public void onResponse(Call<PackagesDataResponse> call, Response<PackagesDataResponse> response) {
                mainActivity.hideProgressbar();
                if(response.body().getStatus() != null && response.body().getStatus().equals("invalid user")){
                    Toast.makeText(mainActivity.getApplicationContext(), getString(R.string.invalid_user_code),Toast.LENGTH_LONG).show();
                    return;
                }

                PackagesAdapter packagesAdapter = new PackagesAdapter(getActivity().getApplicationContext(), mainActivity,response.body().getPackages(), response.body().getUserPackages(), selectedPackageIdstoBuy, buyPackages);
                recyclerView.setAdapter(packagesAdapter);
            }

            @Override
            public void onFailure(Call<PackagesDataResponse> call, Throwable t) {
                mainActivity.hideProgressbar();
                Toast.makeText(mainActivity.getApplicationContext(), getString(R.string.error_in_getting_packages),Toast.LENGTH_LONG).show();
            }
        });


        buyPackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showProgressbar();
                String base64EncodedPublicKey= "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC3jgRLYRj+0xcjee9urNoEuRo72pWKQk2gd36hdkDLYxBicuIwGWzV9hfmmSu/36llEf4wHpZt44iS7PfZouD9tbL1i2oorVg9O+FkNR/OeJVn0nRajT+gbY2nURDdsh4pZe+qvb0+70//nbD3YrZUhlDa7HhUvokbJqu4UmGnaNF0TUU+EtmtkrpwSt6xu2Buv+nQvcUkT2+RMkpW+TXSUodEVyMISghtWo2JwbMCAwEAAQ==";
                mHelper = new IabHelper(mainActivity.getApplicationContext(), base64EncodedPublicKey);
                if(selectedPackageIdstoBuy.size()  == 0){
                    return;
                }
                mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                    public void onIabSetupFinished(IabResult result) {
                        if (!result.isSuccess()) {
                            // Oh noes, there was a problem.
                            Log.d(TAG, "Problem setting up In-app Billing: " + result);
                        }

                        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                                = new IabHelper.OnIabPurchaseFinishedListener() {
                            public void onIabPurchaseFinished(IabResult result, Purchase purchase)
                            {
                                if (result.isFailure()) {
                                    Log.d(TAG, "Error purchasing: " + result);
                                    return;
                                }
                                else if (purchase.getSku().equals("1")) {//user bought packge 1

                                }
                                else if (purchase.getSku().equals("2")) {//user bought packge 2

                                }
                            }
                        };

                        mHelper.launchPurchaseFlow(mainActivity.getParent(),"1",1,mPurchaseFinishedListener);
                    }
                });
            }
        });

        return  view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
}

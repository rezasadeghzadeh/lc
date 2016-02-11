package lightner.sadeqzadeh.lightner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import lightner.sadeqzadeh.lightner.IAPActivity;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.adapter.PackagesAdapter;
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.PackagesDataResponse;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadPackageFragment extends Fragment {

    public static final String TAG = DownloadPackageFragment.class.getName();
    RecyclerView recyclerView;
    MainActivity mainActivity;
    Button buyPackages;
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
                Intent intent = new Intent(getActivity(), IAPActivity.class);
                startActivity(intent);
            }
        });

        return  view;
    }
}

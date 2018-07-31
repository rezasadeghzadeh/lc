package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity  = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.download_package_layout, container, false);
        recyclerView = view.findViewById(R.id.packages_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LightnerAPI  lightnerAPI  = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);

        String userCode = Util.fetchAndDecrypt(mainActivity.getApplicationContext(), Const.USER_CODE);
        Call<PackagesDataResponse> packagesDataResponseCall = lightnerAPI.getPackagesList(userCode);
        mainActivity.showProgressbar();
        packagesDataResponseCall.enqueue(new Callback<PackagesDataResponse>() {
            @Override
            public void onResponse(Call<PackagesDataResponse> call, Response<PackagesDataResponse> response) {
                mainActivity.hideProgressbar();
                PackagesAdapter packagesAdapter = new PackagesAdapter(getActivity().getApplicationContext(), mainActivity,response.body().getPackages());
                recyclerView.setAdapter(packagesAdapter);
            }

            @Override
            public void onFailure(Call<PackagesDataResponse> call, Throwable t) {
                mainActivity.hideProgressbar();
                Toast.makeText(mainActivity.getApplicationContext(), getString(R.string.error_in_getting_packages),Toast.LENGTH_SHORT);
            }
        });

        return  view;
    }


}

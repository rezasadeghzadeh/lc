package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.adapter.KeyValueAdapter;
import lightner.sadeqzadeh.lightner.rest.GetUserDataResponse;
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import lightner.sadeqzadeh.lightner.rest.SaveUserDataResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";
    MainActivity mainActivity;
    Button saveBtn;
    Spinner educationBaseSpin;
    Spinner educationFieldSpin;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment_layout, container, false);
        mainActivity = (MainActivity) getActivity();
        educationBaseSpin = view.findViewById(R.id.education_base_spin);
        final String[] baseKeys  =  getResources().getStringArray(R.array.educationBaseKeys);
        String[] baseValues =  getResources().getStringArray(R.array.educationBaseValues);
        KeyValueAdapter baseKeyValueAdapter = new KeyValueAdapter(mainActivity,baseKeys,baseValues);
        educationBaseSpin.setAdapter(baseKeyValueAdapter);

        educationFieldSpin  = view.findViewById(R.id.education_field_spin);
        final String[] fieldKeys  =  getResources().getStringArray(R.array.educationFieldKeys);
        String[] fieldValues  =  getResources().getStringArray(R.array.educationFieldValues);
        KeyValueAdapter fieldKeyValueAdapter  = new KeyValueAdapter(mainActivity,fieldKeys, fieldValues);
        educationFieldSpin.setAdapter(fieldKeyValueAdapter);
        final String  userCode= Util.fetchAndDecrypt(mainActivity.getApplicationContext(),Const.USER_CODE);

        final LightnerAPI lightnerAPI = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);
        mainActivity.showProgressbar();
        Call<GetUserDataResponse> userDataResponseCall = lightnerAPI.getUserData(userCode);
        userDataResponseCall.enqueue(new Callback<GetUserDataResponse>() {
            @Override
            public void onResponse(Call<GetUserDataResponse> call, Response<GetUserDataResponse> response) {
                mainActivity.hideProgressbar();
                if(response.body().found){
                    for(int i=0;i<baseKeys.length;i++){
                        if(baseKeys[i].equals(String.valueOf(response.body().educationBaseId))){
                            educationBaseSpin.setSelection(i);
                            break;
                        }
                    }

                    for(int i=0;i<fieldKeys.length;i++){
                        if(fieldKeys[i].equals(String.valueOf(response.body().educationFieldId))){
                            educationFieldSpin.setSelection(i);
                            break;
                        }
                    }
                }else {
                    Toast.makeText(getActivity(), getText(R.string.user_data_not_found), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserDataResponse> call, Throwable t) {
                mainActivity.hideProgressbar();
                Toast.makeText(getActivity(), getText(R.string.error_in_getting_user_data), Toast.LENGTH_LONG).show();
            }
        });
        saveBtn = view.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int educationBaseId = Integer.parseInt(educationBaseSpin.getSelectedItem().toString());
                int educationFieldId = Integer.parseInt(educationFieldSpin.getSelectedItem().toString());
                final String msisdn  = Util.fetchFromPreferences(Const.MSISDN);
                Call<SaveUserDataResponse> responseCall = lightnerAPI.saveUserData(userCode ,msisdn, educationBaseId, educationFieldId);
                mainActivity.showProgressbar();
                responseCall.enqueue(new Callback<SaveUserDataResponse>() {
                    @Override
                    public void onResponse(Call<SaveUserDataResponse> call, Response<SaveUserDataResponse> response) {
                        mainActivity.hideProgressbar();
                        if(response.body().isResult()){
                            mainActivity.drawer.closeDrawer(GravityCompat.START);
                            Toast.makeText(getActivity(), getText(R.string.user_data_saved_successfull), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), getText(R.string.error_in_saving_user_data), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SaveUserDataResponse> call, Throwable t) {
                        mainActivity.hideProgressbar();
                        Toast.makeText(getActivity(), getText(R.string.error_in_saving_user_data), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return  view;

    }
}

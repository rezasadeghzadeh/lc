package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import lightner.sadeqzadeh.lightner.rest.SaveUserDataResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationFragment extends Fragment {
    MainActivity mainActivity;
    Button saveBtn;
    Spinner educationBaseSpin;
    Spinner educationFieldSpin;
    Bundle args;
    String msisdn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
        if(args != null){
            msisdn =  args.getString(Const.MSISDN);
        }
    }

    public static final String TAG = RegistrationFragment.class.getName();
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        educationBaseSpin = view.findViewById(R.id.education_base_spin);
        String[] baseKeys  =  getResources().getStringArray(R.array.educationBaseKeys);
        String[] baseValues =  getResources().getStringArray(R.array.educationBaseValues);
        KeyValueAdapter baseKeyValueAdapter = new KeyValueAdapter(mainActivity,baseKeys,baseValues);
        educationBaseSpin.setAdapter(baseKeyValueAdapter);

        educationFieldSpin  = view.findViewById(R.id.education_field_spin);
        String[] fieldKeys  =  getResources().getStringArray(R.array.educationFieldKeys);
        String[] fieldValues  =  getResources().getStringArray(R.array.educationFieldValues);
        KeyValueAdapter fieldKeyValueAdapter  = new KeyValueAdapter(mainActivity,fieldKeys, fieldValues);
        educationFieldSpin.setAdapter(fieldKeyValueAdapter);

        saveBtn = view.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LightnerAPI lightnerAPI = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);
                int educationBaseId = Integer.parseInt(educationBaseSpin.getSelectedItem().toString());
                int educationFieldId = Integer.parseInt(educationFieldSpin.getSelectedItem().toString());
                final String  userCode= Util.randomToken(50);
                Call<SaveUserDataResponse> responseCall = lightnerAPI.saveUserData(userCode ,msisdn, educationBaseId, educationFieldId);
                mainActivity.showProgressbar();
                responseCall.enqueue(new Callback<SaveUserDataResponse>() {
                    @Override
                    public void onResponse(Call<SaveUserDataResponse> call, Response<SaveUserDataResponse> response) {
                        mainActivity.hideProgressbar();
                        if(response.body().isResult()){
                            //save msisdn in preferences
                            Util.saveInPreferences(Const.MSISDN,msisdn);
                            Util.encryptAndSave(mainActivity.getApplicationContext(),Const.USER_CODE, userCode);
                            AdsFragment adsFragment = new AdsFragment();
                            mainActivity.replaceFragment(adsFragment, AdsFragment.TAG,false);
                            Util.fetchFromPreferences(Const.USER_CODE);
                        }else{
                            Toast.makeText(getActivity(), getText(R.string.error_in_saving_user_data), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SaveUserDataResponse> call, Throwable t) {
                        mainActivity.hideProgressbar();
                        Toast.makeText(getActivity(), getText(R.string.error_in_saving_user_data), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;
    }
}

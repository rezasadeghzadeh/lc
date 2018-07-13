package lightner.sadeqzadeh.lightner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.rest.LightnerAPI;
import lightner.sadeqzadeh.lightner.rest.OtpValidateResponse;
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetOtpFragment extends Fragment{
    public static final String TAG = GetOtpFragment.class.getName();
    MainActivity mainActivity;
    Bundle args;
    private Button checkOtpBtn;
    private EditText otpEdit;
    private String mobileNumber;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.get_otp_layout, container, false);
        setupArgs();
        checkOtpBtn  =  view.findViewById(R.id.check_otp_btn);
        otpEdit  = view.findViewById(R.id.otp_edit);
        checkOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LightnerAPI lightnerAPI = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);
                Call<OtpValidateResponse> otpValidateCall  = lightnerAPI.validateOtp(mobileNumber, otpEdit.getText().toString());
                mainActivity.showProgressbar();
                otpValidateCall.enqueue(new Callback<OtpValidateResponse>() {
                    @Override
                    public void onResponse(Call<OtpValidateResponse> call, Response<OtpValidateResponse> response) {
                        mainActivity.hideProgressbar();

                        if(response.body().isResult()){
                            Fragment registrationFragment = new RegistrationFragment();
                            registrationFragment.setArguments(args);
                            mainActivity.replaceFragment(registrationFragment, RegistrationFragment.TAG);
                        }else {
                            Toast.makeText(getActivity(), getText(R.string.otp_is_not_correct), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OtpValidateResponse> call, Throwable t) {

                    }
                });
            }
        });
        return view;
    }

    private void setupArgs() {
        args = getArguments();
        if(args != null){
           mobileNumber =  args.getString(Const.MSISDN);
        }
    }
}

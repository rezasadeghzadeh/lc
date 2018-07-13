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
import lightner.sadeqzadeh.lightner.rest.RetrofitClientInstance;
import lightner.sadeqzadeh.lightner.rest.SendSmsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMobileNumberFragment extends Fragment{

    public static final String TAG = GetMobileNumberFragment.class.getName();
    private Button sendOtpBtn;
    private EditText mobileNumberEdit;
    private MainActivity mainActivity;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.get_mobile_number_layout, container, false);
        sendOtpBtn  = view.findViewById(R.id.send_otp_btn);
        mobileNumberEdit = view.findViewById(R.id.mobile_number_edit);
        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mobileNumber  =  mobileNumberEdit.getText().toString();
                //validate mobile number
                if(mobileNumber== null || mobileNumber.isEmpty() || mobileNumber.length() != 11){
                    mobileNumberEdit.setError(getText(R.string.mobile_number_is_not_valid));
                    return;
                }

                //call webservice
                LightnerAPI service = RetrofitClientInstance.getRetrofitInstance().create(LightnerAPI.class);
                Call<SendSmsResponse> sendSmsResponseCall = service.sendSmsToMobile(mobileNumber);
                mainActivity.showProgressbar();
                sendSmsResponseCall.enqueue(new Callback<SendSmsResponse>() {
                    @Override
                    public void onResponse(Call<SendSmsResponse> call, Response<SendSmsResponse> response) {
                        if(response.body().isStatus()){
                            mainActivity.hideProgressbar();
                            GetOtpFragment getOtpFragment = new GetOtpFragment();
                            Bundle args = new Bundle();
                            args.putString(Const.MSISDN,mobileNumber);
                            getOtpFragment.setArguments(args);
                            mainActivity.replaceFragment(getOtpFragment, GetOtpFragment.TAG);
                        }else {
                            mainActivity.hideProgressbar();
                            Toast.makeText(getActivity(), getText(R.string.send_sms_failed_try_agin), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SendSmsResponse> call, Throwable t) {
                        mainActivity.hideProgressbar();
                        Toast.makeText(getActivity(), getText(R.string.send_sms_failed_try_agin), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return  view;
    }

}

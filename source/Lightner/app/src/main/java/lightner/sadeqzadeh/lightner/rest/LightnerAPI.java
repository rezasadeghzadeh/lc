package lightner.sadeqzadeh.lightner.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public  interface LightnerAPI {
    @GET("sendSms/{mobileNumber}")
    Call<SendSmsResponse> sendSmsToMobile(@Path("mobileNumber") String mobileNumber);

}

package lightner.sadeqzadeh.lightner.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public  interface LightnerAPI {
    @GET("sendSms/{mobileNumber}")
    Call<SendSmsResponse> sendSmsToMobile(@Path("mobileNumber") String mobileNumber);

    @GET("validateOtp/{mobileNumber}/{otpNumber}")
    Call<OtpValidateResponse> validateOtp(@Path("mobileNumber") String mobileNumber, @Path("otpNumber") String otpNumber);
    @POST("saveUserData")
    @FormUrlEncoded
    Call<SaveUserDataResponse> saveUserData(@Field("msisdn") String msisdn, @Field("educationBase") int educationBase, @Field("educationField") int educationField);

}

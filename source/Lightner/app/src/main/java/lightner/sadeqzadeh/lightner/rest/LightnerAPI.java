package lightner.sadeqzadeh.lightner.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public  interface LightnerAPI {
    @GET("sendSms/{mobileNumber}")
    Call<SendSmsResponse> sendSmsToMobile(@Path("mobileNumber") String mobileNumber);

    @GET("validateOtp/{mobileNumber}/{otpNumber}")
    Call<OtpValidateResponse> validateOtp(@Path("mobileNumber") String mobileNumber, @Path("otpNumber") String otpNumber);

    @POST("saveUserData")
    @FormUrlEncoded
    Call<SaveUserDataResponse> saveUserData(@Field("userCode") String userCode, @Field("msisdn") String msisdn, @Field("educationBase") int educationBase, @Field("educationField") int educationField);

    @POST("packagesInfo")
    @FormUrlEncoded
    Call<PackagesDataResponse> getPackagesList(@Field("userCode") String userCode);

    @GET("packages/flashcards")
    Call<PackageWordsResponse> getPackageFlashcards(@Query("userCode") String userCode, @Query("packageId") long packageId);

    @GET("user/data")
    Call<GetUserDataResponse> getUserData(@Query("userCode") String userCode);
}

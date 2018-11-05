package com.alanaandnazar.qrscanner.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BalAPI {

    @POST("/api/auth")
    @Multipart
   //@FormUrlEncoded
    Call<TokenResponse> authPost(@Part("login") RequestBody login,
                                 @Part("pass") RequestBody password);

    @POST("/api/logout")
    @Multipart
    Call<TokenResponse> logoutPost(@Part("token") RequestBody token);

    @POST("/api/move")
    @Multipart
    Call<TokenResponse> infoPost(@Part("id") RequestBody id,
                                 @Part("status") RequestBody status,
                                 @Part("type") RequestBody type,
                                 @Part("time") RequestBody time,
                                 @Part("token") RequestBody token,
                                 @Part() MultipartBody.Part mFile);
}

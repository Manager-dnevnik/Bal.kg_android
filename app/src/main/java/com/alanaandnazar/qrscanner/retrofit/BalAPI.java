package com.alanaandnazar.qrscanner.retrofit;

import android.database.Observable;

import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.model.Children;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

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

    @GET("/api/mychildrens")
    Call<List<Children>> getMyChildrens(@Query("token") String token);

    @GET("/api/childinfo")
    Call<Children> getChildInfo(@Query("token") String token, @Query("id") int id);

    @GET("/api/childmove")
    Call<List<ChildMove>> getChildMove(@Query("token") String token, @Query("id") int id);
}

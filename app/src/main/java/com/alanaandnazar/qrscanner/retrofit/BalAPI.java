package com.alanaandnazar.qrscanner.retrofit;

import android.database.Observable;

import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.model.Children;
import com.alanaandnazar.qrscanner.model.Classe;
import com.alanaandnazar.qrscanner.model.Homework;
import com.alanaandnazar.qrscanner.model.Mark;
import com.alanaandnazar.qrscanner.model.Note;
import com.alanaandnazar.qrscanner.model.Shedule;
import com.alanaandnazar.qrscanner.model.Subject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    @GET("/api/childschedule")
    Call<List<Shedule>> getShedules(@Query("token") String token, @Query("id") int id);

    @GET("/api/marks")
    Call<List<Mark>> getSubject(@Query("token") String token, @Query("id") int id, @Query("subject_id") int subject_id);

    @GET("/api/homework")
    Call<List<Homework>> getHomeWork(@Query("token") String token, @Query("id") int id, @Query("subject_id") int subject_id);


    @GET("/api/classes")
    Call<List<Classe>> getClasses(@Query("token") String token);

    @GET("/api/childrens")
    Call<List<Children>> getChildrens(@Query("token") String token, @Query("class_id") int class_id);

    @GET("/api/subjects")
    Call<List<Subject>> getSubjectsMark(@Query("token") String token);

    @GET("/api/note")
    Call<List<Note>> getNote(@Query("token") String token);

//    @GET("/api/homework")
//    Call<List<Homework>> getHomework(@Query("token") String token,
//                                     @Query("id") int id,
//                                     @Query("subject_id") int subject_id);

    @FormUrlEncoded
    @POST("/api/marks")
    Call<ResponseBody> createMark(@Field("token") String token,
                                  @Field("id") int id,
                                  @Field("subject_id") int subject_id,
                                  @Field("mark") String mark,
                                  @Field("date") String date,
                                  @Field("type_mark") String type_mark,
                                  @Field("part") String part,
                                  @Field("comm") String comm);

    @FormUrlEncoded
    @POST("/api/homework")
    Call<ResponseBody> createHomework(@Field("token") String token,
                                      @Field("class_id") int class_id,
                                      @Field("subject_id") int subject_id,
                                      @Field("date") String date,
                                      @Field("text") String text);

}

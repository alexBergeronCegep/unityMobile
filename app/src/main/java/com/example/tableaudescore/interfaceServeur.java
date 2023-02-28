package com.example.tableaudescore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface interfaceServeur {

    @POST("unityMobile.php")
    @FormUrlEncoded
    Call<List<User>> getUsers(@Field("requete") String requete);

    @POST("unityMobile.php")
    @FormUrlEncoded
    Call<String> getScore(@Field("requete") String requete, @Field("id") int id);

    @POST("unityMobile.php")
    @FormUrlEncoded
    Call<String> getConnexion(@Field("requete") String requete ,@Field("user") String user, @Field("password") String password);

    @POST("unityMobile.php")
    @FormUrlEncoded
    Call<String> getInscription(@Field("requete") String requete ,@Field("user") String user, @Field("password") String password);
}

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
    Call<List<User>> getUser(@Field("requete") String requete ,@Field("id") int id);
}

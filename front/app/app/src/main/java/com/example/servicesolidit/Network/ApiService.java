package com.example.servicesolidit.Network;



import com.example.servicesolidit.Model.Responses.LoginResponse;
import com.example.servicesolidit.Model.Dtos.User;
import com.example.servicesolidit.Model.Dtos.UserRegisterModel;
import com.example.servicesolidit.Model.Responses.RegisterResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("users/login")
    Call<LoginResponse> login(@Body User user);

    @POST("users/envioToken")
    Call<RegisterResponseDto> register(@Body UserRegisterModel user);

    @GET("users/prueba")
    Call<List<UserRegisterModel>> getAllClients();

}

package com.example.servicesolidit.Network;



import com.example.servicesolidit.Model.LoginResponse;
import com.example.servicesolidit.Model.User;
import com.example.servicesolidit.Utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("users/login")
    Call<LoginResponse> login(@Body User user);
}

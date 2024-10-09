package com.example.servicesolidit.Network;



import com.example.servicesolidit.Model.Requests.LoginRequestDto;
import com.example.servicesolidit.Model.Requests.RegisterRequestDto;
import com.example.servicesolidit.Model.Responses.Feed.FeedResponseDto;
import com.example.servicesolidit.Model.Responses.LoginResponseDto;
import com.example.servicesolidit.Model.Responses.Messages.MessagesResponseDto;
import com.example.servicesolidit.Model.Responses.RegisterResponseDto;
import com.example.servicesolidit.Model.Responses.UserInfoProfileDto;
import com.example.servicesolidit.Model.Responses.UserInfoProfileResponseDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("users/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto request);

    @POST("users/register")
    Call<RegisterResponseDto> register(@Body RegisterRequestDto request);

    @GET("users/information/{idToFind}")
    Call<UserInfoProfileResponseDto> information(@Path("idToFind")int idUserToFind);

    @GET("provider/todos/services/feed")
    Call<FeedResponseDto> feed();

    @GET("message/mensajes/{idOrigen}/{idDestino}")
    Call<MessagesResponseDto> getMessages(@Path("idOrigen") int idOrigen, @Path("idDestino") int idDestino);
}

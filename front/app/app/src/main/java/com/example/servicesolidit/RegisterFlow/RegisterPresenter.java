package com.example.servicesolidit.RegisterFlow;

import com.example.servicesolidit.LoginFlow.LoginView;
import com.example.servicesolidit.Model.Dtos.User;
import com.example.servicesolidit.Model.Dtos.UserRegisterModel;
import com.example.servicesolidit.Model.Responses.LoginResponse;
import com.example.servicesolidit.Model.Responses.RegisterResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresenter {
    private ApiService service;
    private RegisterView view;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        this.service = RetrofitClient.getClient().create(ApiService.class);
    }

    public void getAllClientes(){
        view.showProgress();
        Call<List<UserRegisterModel>> call = service.getAllClients();
        call.enqueue(new Callback<List<UserRegisterModel>>() {
            @Override
            public void onResponse(Call<List<UserRegisterModel>> call, Response<List<UserRegisterModel>> response) {
                view.hideProgress();
                if(response.isSuccessful() && response.body() !=  null){
                    view.onGetAllClientsSuccess(response.body());
                    List<UserRegisterModel> clients = response.body();
                    for (UserRegisterModel client : clients) {
                        System.out.println("Name: " + client.getName_user());
                        System.out.println("Last Name: " + client.getLastname());
                        System.out.println("Email: " + client.getEmail());
                        System.out.println("Password: " + client.getPassword());
                        System.out.println("Age: " + client.getAge());
                        System.out.println("Phone Number: " + client.getPhoneNumber());
                        System.out.println("street_1: " + client.getStreet_1());
                        System.out.println("street_2: " + client.getStreet_2());
                        System.out.println("Locality: " + client.getLocalidad());
                        System.out.println("name town: " + client.getName_Town());
                        System.out.println("name state: " + client.getName_state());
                        System.out.println("zipcode: " + client.getZipcode());
                    }
                }else {
                    view.onRegisterError("Error fetching clients");
                }
            }

            @Override
            public void onFailure(Call<List<UserRegisterModel>> call, Throwable t) {
                view.hideProgress();
                view.onRegisterError(t.getMessage());
            }
        });
    }
    public void register(UserRegisterModel userRequest) {
        view.showProgress();
        Call<RegisterResponseDto> call = service.register(userRequest);
        call.enqueue(new Callback<RegisterResponseDto>() {
            @Override
            public void onResponse(Call<RegisterResponseDto> call, Response<RegisterResponseDto> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    view.onRegisterSuccess("Code: "+ response.body().getCode());
                } else {
                    view.onRegisterError("Login failed");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseDto> call, Throwable t) {
                System.out.println("Error on failure: " + t.getMessage());
                view.hideProgress();
                view.onRegisterError(t.getMessage());
            }
        });
    }

}

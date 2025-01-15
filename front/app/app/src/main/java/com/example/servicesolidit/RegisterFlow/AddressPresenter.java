package com.example.servicesolidit.RegisterFlow;

import com.example.servicesolidit.Network.ApiLocationsService;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.Network.RetrofitLocationsClient;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Dtos.Responses.Locations.CitiesDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Locations.StatesDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressPresenter {
    private AddressView view;
    private ApiLocationsService service;

    public AddressPresenter(AddressView view) {
        this.view = view;
        this.service = RetrofitLocationsClient.getClient(Constants.bearerToken).create(ApiLocationsService.class);
    }

    public void obtainStates(){
        Call<ArrayList<StatesDto>> call = this.service.ObtainStates();
        call.enqueue(new Callback<ArrayList<StatesDto>>() {
            @Override
            public void onResponse(Call<ArrayList<StatesDto>> call, Response<ArrayList<StatesDto>> response) {
                if(response.isSuccessful() && response.body() != null){
                    ArrayList<StatesDto> result = response.body();
                    view.onObtainStatesSuccess(result);
                }else{
                    view.onObtainStatesError("Ocurrió un error al realizar la consulta de estados...");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StatesDto>> call, Throwable t) {
                view.onObtainStatesError("Ocurrió un error al realizar la consulta de estados...");
            }
        });
    }

    public void obtainCities(String selectedItem) {
        Call<ArrayList<CitiesDto>> call = this.service.ObtainCities(selectedItem);
        call.enqueue(new Callback<ArrayList<CitiesDto>>() {
            @Override
            public void onResponse(Call<ArrayList<CitiesDto>> call, Response<ArrayList<CitiesDto>> response) {
                if(response.isSuccessful() && response.body() != null){
                    ArrayList<CitiesDto> result = response.body();
                    view.onObtainCitiesSuccess(result);
                }else{
                    view.onObtainCitiesError("Ocurrió un error al realizar la consulta de ciudades...");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CitiesDto>> call, Throwable t) {
                view.onObtainCitiesError("Ocurrió un error al realizar la consulta de ciudades...");
            }
        });
    }
}

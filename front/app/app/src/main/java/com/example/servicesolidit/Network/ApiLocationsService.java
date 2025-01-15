package com.example.servicesolidit.Network;

import com.example.servicesolidit.Utils.Dtos.Responses.Locations.CitiesDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Locations.StatesDto;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiLocationsService {
    @GET("states/Mexico")
    Call<ArrayList<StatesDto>> ObtainStates();

    @GET("cities/{city}")
    Call<ArrayList<CitiesDto>> ObtainCities(@Path("city") String city);
}

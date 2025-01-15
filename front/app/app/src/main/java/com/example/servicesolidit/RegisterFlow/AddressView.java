package com.example.servicesolidit.RegisterFlow;

import com.example.servicesolidit.Utils.Dtos.Responses.Locations.CitiesDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Locations.StatesDto;

import java.util.ArrayList;

public interface AddressView {
    void onObtainStatesSuccess(ArrayList<StatesDto> result);
    void onObtainStatesError(String s);
    void onShowProgress();
    void onHideProgress();

    void onObtainCitiesError(String s);

    void onObtainCitiesSuccess(ArrayList<CitiesDto> result);
}

package com.example.servicesolidit.HouseFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servicesolidit.HomeFlow.HomePresenter;
import com.example.servicesolidit.HomeFlow.HomeView;
import com.example.servicesolidit.Model.Responses.Feed.ProviderResponseDto;
import com.example.servicesolidit.R;

import java.util.ArrayList;
import java.util.List;


public class House extends Fragment implements HomeView {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List<CardModel> cardList;
    private HomePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_house, container, false);
        presenter = new HomePresenter(this);


        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);

        recyclerView.setLayoutManager(gridLayoutManager);

        cardList = new ArrayList<>();
        adapter = new CardAdapter(cardList, this.getContext());
        recyclerView.setAdapter(adapter);
        showProgres();
        presenter.feed("0", "10");
        return view;
    }


    @Override
    public void showProgres() {
        Log.i("HouseClass", "Should show progress");
    }

    @Override
    public void hideProgess() {
        Log.i("HouseClass", "Should hide progress");
    }

    @Override
    public void onFeedSuccess(ArrayList<ProviderResponseDto> feedResponse) {
        printFeed(feedResponse);
        hideProgess();
    }

    @Override
    public void onFeedError(String error) {
        Log.i("HomeClass", "Should show error: " + error);
    }

    public void printFeed(ArrayList<ProviderResponseDto> feedResponse){
        Log.i("HouseClass", "Feed Loaded with: " + feedResponse.get(0).getIdProvider());
        cardList = this.getCardListFromResponse(feedResponse);
        adapter = new CardAdapter(cardList, this.getContext());
        recyclerView.setAdapter(adapter);
    }

    private List<CardModel> getCardListFromResponse(ArrayList<ProviderResponseDto> feedResponse) {
        List<CardModel> listToPrint = new ArrayList<CardModel>();
        for (ProviderResponseDto item: feedResponse) {
            CardModel modelFromResponse = new CardModel();
            modelFromResponse.setLocation(item.getAddress().getLocalidad());
            modelFromResponse.setNameBussines(item.getWorkshopName());
            modelFromResponse.setDescription("Con " + item.getExperienceYears() + " años de experiencia");
            modelFromResponse.setImageUrl("https://img.freepik.com/fotos-premium/pico-montana-nevada-galaxia-estrellada-majestad-ia-generativa_1038396-50.jpg");
            listToPrint.add(modelFromResponse);
        }
        return listToPrint;
    }
}
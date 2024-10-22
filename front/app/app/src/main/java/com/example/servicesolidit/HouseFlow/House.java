package com.example.servicesolidit.HouseFlow;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.ProviderInformationFlow.VisitProvider;

import java.util.ArrayList;
import java.util.List;


public class House extends Fragment implements HomeView, CardAdapter.OnCardClickListener {

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
        adapter = new CardAdapter(cardList, this.getContext(), this);
        recyclerView.setAdapter(adapter);
        showProgres();
        presenter.feed("0", "10");
        ReadShared();
        return view;
    }

    public void ReadShared(){
        try {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
            int userIdLogged = sharedPreferences.getInt(Constants.GET_LOGGED_USER_ID, 0);
            Log.i("HouseClass", "El valor id del user almacenado es: " + userIdLogged);
        }catch (Exception e){
            Log.i("HouseClass", "Error on read shared: " + e.getMessage());
        }
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
        Log.i("HouseClass", "Feed Loaded with: " + feedResponse.get(1).getIdProvider());
        cardList = this.getCardListFromResponse(feedResponse);
        adapter = new CardAdapter(cardList, this.getContext(),this);
        recyclerView.setAdapter(adapter);
    }

    private List<CardModel> getCardListFromResponse(ArrayList<ProviderResponseDto> feedResponse) {
        List<CardModel> listToPrint = new ArrayList<CardModel>();
        for (ProviderResponseDto item: feedResponse) {
            CardModel modelFromResponse = new CardModel();
            modelFromResponse.setLocation(item.getAddress().getLocalidad());
            modelFromResponse.setNameBussines(item.getWorkshopName());
            modelFromResponse.setDescription("Con " + item.getExperienceYears() + " años de experiencia");
            modelFromResponse.setIdProvider(item.getIdProvider());
            modelFromResponse.setImageUrl("http://189.190.215.79:4000/api/v1/images/print/1726996926660-mydatabase-public.png");
            listToPrint.add(modelFromResponse);

        }

        return listToPrint;
    }

    public void onCardClick(int idProvider){
        VisitProvider visitProvider = new VisitProvider(idProvider);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_content_visit_provider, visitProvider);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
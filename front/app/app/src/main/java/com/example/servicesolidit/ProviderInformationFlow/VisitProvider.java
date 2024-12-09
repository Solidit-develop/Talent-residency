package com.example.servicesolidit.ProviderInformationFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Models.Requests.RelationalImagesRequestDto;
import com.example.servicesolidit.Utils.Models.Responses.ImagesRelational.RelationalImagesResponseDto;
import com.squareup.picasso.Picasso;


public class VisitProvider extends Fragment implements VisitProviderView{

    private VisitProviderPresenter presenter;
    private int idProviderToLoad;
    private ImageView imageExampleOnCarousell;
    private Button btnInfProfile;
    private TextView tvNoImagesFound;

    public VisitProvider(int idProviderToLoad) {
        this.idProviderToLoad = idProviderToLoad;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit_provider, container, false);
        btnInfProfile = view.findViewById(R.id.btn_inf_profile_provider);
        imageExampleOnCarousell = view.findViewById(R.id.imageExampleOnCarousell);
        tvNoImagesFound = view.findViewById(R.id.tvNoImagesFound);

        this.presenter = new VisitProviderPresenter(this);

        onShowProgress();
        RelationalImagesRequestDto requestDto = new RelationalImagesRequestDto();
        requestDto.setTable("providers");
        requestDto.setFuncionalida("comments");
        requestDto.setIdUsedOn(String.valueOf(this.idProviderToLoad));
        this.presenter.getProviderImagesByComments(requestDto);

        btnInfProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerToProvider fragmentProviderView = new CustomerToProvider(idProviderToLoad);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container,fragmentProviderView);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Log.i("VisitProviderClass", "idProvider recibido:  " + idProviderToLoad);

        return view;
    }

    @Override
    public void onSuccessGetImageInformation(RelationalImagesResponseDto response) {
        onHideProgress();
        if(response.getImageName() != null){
            //Print image
            tvNoImagesFound.setVisibility(View.GONE);
            imageExampleOnCarousell.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(Constants.BASE_URL + "images/print/"+response.getImageName())
                    .placeholder(R.drawable.load)
                    .error(R.drawable.lost)
                    .into(imageExampleOnCarousell);
        }else{
            tvNoImagesFound.setVisibility(View.VISIBLE);
            imageExampleOnCarousell.setVisibility(View.GONE);
            onErrorGetImageInformation("Ocurrió un error al obtener la imagen: " + response.getMessage());
        }
    }

    @Override
    public void onErrorGetImageInformation(String error) {
        Log.i("VisitProviderClass","Error: " + error);
        onHideProgress();
    }

    @Override
    public void onShowProgress() {
        Log.i("VisitProviderClass","OnShowProgress");

    }

    @Override
    public void onHideProgress() {
        Log.i("VisitProviderClass","OnHideProgress");

    }
}
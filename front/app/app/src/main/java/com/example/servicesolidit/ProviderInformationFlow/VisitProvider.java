package com.example.servicesolidit.ProviderInformationFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.servicesolidit.R;


public class VisitProvider extends Fragment {

    private int idProviderToLoad;

    public VisitProvider(int idProviderToLoad) {
        this.idProviderToLoad = idProviderToLoad;
    }

    private Button btnInfProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_visit_provider, container, false);
        btnInfProfile = view.findViewById(R.id.btn_inf_profile_provider);

        btnInfProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerToProvider fragmentProviderView = new CustomerToProvider(idProviderToLoad);
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_provider_view,fragmentProviderView);
                fragmentTransaction.commit();
            }
        });
        Log.i("VisitProviderClass", "idProvider recibido:  " + idProviderToLoad);

        return view;
    }
}
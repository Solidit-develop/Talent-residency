package com.example.servicesolidit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private Button btnCustomer;
    private Button btnProvider;
    private MaterialButtonToggleGroup buttonToggleGroup;
    private ImageView btnMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private final PersonalData personalData = new PersonalData();
    private final BussinesData bussinesData = new BussinesData();
    private final Map<Integer, Runnable> navigationAction = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnCustomer = view.findViewById(R.id.btn_customer);
        btnProvider = view.findViewById(R.id.btn_provider);
        buttonToggleGroup = view.findViewById(R.id.toggleButton);

        drawerLayout = view.findViewById(R.id.main2);
        navigationView = view.findViewById(R.id.slide_drawn);
        btnMenu = view.findViewById(R.id.icon_image_menu);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        buttonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked){
               checkButtonData(isChecked,checkedId);
            }
        });

        navigationAction.put(R.id.item_message, ()->{
            Toast.makeText(this.getContext(), "Hola desde mensajes", Toast.LENGTH_SHORT).show();
        });
        navigationAction.put(R.id.item_appointment, ()->{
            Toast.makeText(this.getContext(), "Hola desde citas", Toast.LENGTH_SHORT).show();
        });
        navigationAction.put(R.id.item_agreements, () -> {
            Toast.makeText(this.getContext(), "Hola desde acuerdos", Toast.LENGTH_SHORT).show();
        });
        navigationAction.put(R.id.item_record, () -> {
            Toast.makeText(this.getContext(), "Hola desde historial", Toast.LENGTH_SHORT).show();
        });
        navigationAction.put(R.id.item_view_edit, () -> {
            Toast.makeText(this.getContext(), "Hola desde ver y editar servicios", Toast.LENGTH_SHORT).show();
        });
        navigationAction.put(R.id.item_publish_service, () -> {
            Toast.makeText(this.getContext(), "Hola desde publicar servicio", Toast.LENGTH_SHORT).show();
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Runnable action = navigationAction.get(item.getItemId());
                if(action !=null){
                    action.run();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });


        return view;
    }
  
    public void checkButtonData (boolean isChecked,int checkedId){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        if (checkedId == R.id.btn_customer){
            PersonalData personalData = new PersonalData();
            transaction.replace(R.id.fragment_container,personalData);
            transaction.addToBackStack(null);
            transaction.remove(bussinesData);
            transaction.commit();
        } if (checkedId == R.id.btn_provider){
            BussinesData bussinesData = new BussinesData();
            transaction.replace(R.id.fragment_container,bussinesData);
            transaction.addToBackStack(null);
            transaction.remove(personalData);
            transaction.commit();
        }
    }
}
package com.example.servicesolidit;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    private final House firstFragment = new House();
    private final Search secondFragment = new Search();
    private final Profile thirdFragment = new Profile();
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();


    private DrawerLayout drawerLayout;
    private ImageView imagen;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        fragmentMap.put(R.id.firstFragment, firstFragment);
        fragmentMap.put(R.id.secondFragment, secondFragment);
        fragmentMap.put(R.id.thirdFragment, thirdFragment);

        drawerLayout = findViewById(R.id.main1);
        imagen = findViewById(R.id.icon_menu);
        navigationView = findViewById(R.id.slide_drawn);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(firstFragment);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    //Metodos para la funcionalidad de las transiciones de la barra inferior
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = fragmentMap.get(item.getItemId());
            Log.d("Prueba", "Mapeo: " + fragmentMap);
            if (fragment != null) {
                if (fragment != thirdFragment){
                    loadFragment(fragment);

                }if (fragment == thirdFragment){
                    loadFragmentProfile(fragment);
                }
                return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.remove(thirdFragment);
        transaction.commit();

    }
    public void loadFragmentProfile (Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_profile, fragment);
        transaction.commit();
    }
}

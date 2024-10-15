package com.example.servicesolidit.HomeFlow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.servicesolidit.HeadDrawn;
import com.example.servicesolidit.HouseFlow.House;
import com.example.servicesolidit.MessageFlow.MessageActivity;
import com.example.servicesolidit.ProfileFlow.Profile;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Search;
import com.example.servicesolidit.Utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity{

    private final HeadDrawn headDrawn = new HeadDrawn();

    private final House firstFragment = new House();
    private final Search secondFragment = new Search();
    private final Profile thirdFragment = new Profile();
    private final Map<Integer, Fragment> fragmentMap = new HashMap<>();

    private final Map<Integer, Runnable> navigationActions = new HashMap<>();


    private DrawerLayout drawerLayout;
    private ImageView imagen;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        fragmentMap.put(R.id.firstFragment, firstFragment);
        fragmentMap.put(R.id.secondFragment, secondFragment);
        fragmentMap.put(R.id.thirdFragment, thirdFragment);

        drawerLayout = findViewById(R.id.drawer_home);
        imagen = findViewById(R.id.icon_menu);
        navigationView = findViewById(R.id.slide_drawn);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(firstFragment);

        toolbar = findViewById(R.id.menu_toolbar);
        setSupportActionBar(toolbar);


        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        View header = navigationView.getHeaderView(0);
        TextView userNameOnHeader = header.findViewById(R.id.txt_name_user_logged_on_header);
        TextView emailOnHeader = header.findViewById(R.id.txt_mail_user_logged_on_header);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, MODE_PRIVATE);
        String userNameFromShared = sharedPreferences.getString(Constants.GET_NAME_USER, "");
        String emailFromShared = sharedPreferences.getString(Constants.GET_EMAIL_USER, "");

        userNameOnHeader.setText(userNameFromShared);
        emailOnHeader.setText(emailFromShared);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return false;
            }
        });

        //************************  metodos para el mapeo del slide de izquierda - derecha  ***************************************/
        navigationActions.put(R.id.item_message, () -> {
            Toast.makeText(this, "Hola desde mensajes", Toast.LENGTH_SHORT).show();
        });

        navigationActions.put(R.id.item_appointment, () -> {
            Toast.makeText(this, "Hola desde citas", Toast.LENGTH_SHORT).show();
        });

        navigationActions.put(R.id.item_agreements, () -> {
            Toast.makeText(this, "Hola desde acuerdos", Toast.LENGTH_SHORT).show();
        });

        navigationActions.put(R.id.item_record, () -> {
            Toast.makeText(this, "Hola desde historial", Toast.LENGTH_SHORT).show();
        });

        navigationActions.put(R.id.item_view_edit, () -> {
            Toast.makeText(this, "Hola desde ver y editar servicios", Toast.LENGTH_SHORT).show();
        });

        navigationActions.put(R.id.item_publish_service, () -> {
            Toast.makeText(this, "Hola desde publicar servicio", Toast.LENGTH_SHORT).show();
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Runnable action = navigationActions.get(item.getItemId());
                if(action !=null){
                    action.run();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });

    }

    //Metodos para la funcionalidad de las transiciones de la barra inferior
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = fragmentMap.get(item.getItemId());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_head, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        if (item.getItemId() == R.id.configuration) {
            // Acción cuando se selecciona el ícono de engranaje
            Toast.makeText(this, "Configuración seleccionada", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.servicesolidit.RegisterFlow;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.servicesolidit.Utils.Dtos.Requests.RegisterRequestDto;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Dtos.Responses.Locations.CitiesDto;
import com.example.servicesolidit.Utils.Dtos.Responses.Locations.StatesDto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Address extends Fragment implements AddressView {

    private RegisterRequestDto requestFromRegistry;
    private Button btnSiguiente;
    private AddressPresenter presenter;
    // Spinners (AutoCompleteTextView)
    private AutoCompleteTextView spinnerState, spinnerCity;
    private TextInputEditText edtxtZipCode, edtxtStreet1, edtxtStreet2, edtxtLocality;

    private TextInputLayout containerSpinnerStates;
    private TextInputLayout containerSpinnerCities;

    private ProgressBar loadingItem;


    public Address(RegisterRequestDto requestDto){
        this.requestFromRegistry = requestDto;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        // Inicializar vistas
        btnSiguiente = view.findViewById(R.id.btn_siguiente_address);
        edtxtZipCode = view.findViewById(R.id.edtxt_zip_code);
        edtxtStreet1 = view.findViewById(R.id.edtxt_calle1);
        edtxtStreet2 = view.findViewById(R.id.edtxt_calle2);
        edtxtLocality = view.findViewById(R.id.edtxt_locality);
        spinnerState = view.findViewById(R.id.spinner_state);
        spinnerCity = view.findViewById(R.id.spinner_city);
        loadingItem = view.findViewById(R.id.progressbar_address_register_custeomr);
        containerSpinnerStates = view.findViewById(R.id.containerSpinnerStates);
        containerSpinnerCities = view.findViewById(R.id.containerSpinnerCities);

        this.presenter = new AddressPresenter(this);
        // Llenar spinners
        cargarOpcionesSpinners();

        // Limitar el código postal a 5 dígitos
        edtxtZipCode.addTextChangedListener(new TextWatcher() {

            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdating) return;
                isUpdating = true;

                // Remover caracteres que no sean números
                String cleanText = s.toString().replaceAll("[^\\d]", "");

                // Limitar a 5 dígitos
                if (cleanText.length() > 5) {
                    cleanText = cleanText.substring(0, 5);
                }

                edtxtZipCode.setText(cleanText);
                edtxtZipCode.setSelection(cleanText.length());
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Mostrar información del objeto request
        Log.i("AddressClass", "Inicializado: " + requestFromRegistry.getLastname());

        // Botón siguiente
        btnSiguiente.setOnClickListener(v -> {
            if (validarCampos()) {
                // Asignar valores de los campos al request si la validación es exitosa
                requestFromRegistry.setZipcode("0000");
                requestFromRegistry.setName_state(spinnerState.getText().toString());
                requestFromRegistry.setName_Town(spinnerCity.getText().toString());
                requestFromRegistry.setStreet_1(edtxtStreet1.getText().toString());
                requestFromRegistry.setStreet_2(edtxtStreet2.getText().toString());
                requestFromRegistry.setLocalidad(edtxtLocality.getText().toString());

                navigateToThirdRegistry(requestFromRegistry);
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerState.setOnItemClickListener((parent, v, position, id) -> {
            String selectedItem = parent.getItemAtPosition(position).toString();
            onShowProgress();
            this.presenter.obtainCities(selectedItem);
        });

        this.onShowProgress();
        this.presenter.obtainStates();
        return view;
    }

    /**
     * Método para validar los campos del formulario.
     *
     * @return true si todos los campos son válidos, false de lo contrario.
     */
    private boolean validarCampos() {
        boolean esValido = true;

        // Validar estado
        if (spinnerState.getText().toString().trim().isEmpty()) {
            spinnerState.setError("El estado es obligatorio");
            esValido = false;
        } else {
            spinnerState.setError(null);
        }

        // Validar ciudad
        if (spinnerCity.getText().toString().trim().isEmpty()) {
            spinnerCity.setError("La ciudad es obligatoria");
            esValido = false;
        } else {
            spinnerCity.setError(null);
        }

        // Validar calle 1
        if (edtxtStreet1.getText().toString().trim().isEmpty()) {
            edtxtStreet1.setError("La calle 1 es obligatoria");
            esValido = false;
        } else {
            edtxtStreet1.setError(null);
        }

        // Validar localidad
        if (edtxtLocality.getText().toString().trim().isEmpty()) {
            edtxtLocality.setError("La localidad es obligatoria");
            esValido = false;
        } else {
            edtxtLocality.setError(null);
        }

        return esValido;
    }

    /**
     * Método para cargar las opciones predeterminadas en los spinners.
     */
    private void cargarOpcionesSpinners() {
        // Opciones de estado
        String[] estados = new String[] {"Hidalgo", "Ciudad de México", "Jalisco"};
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                estados
        );
        spinnerState.setAdapter(adapterEstados);

        // Opciones de ciudad (ejemplo para ciudades de Hidalgo)
        String[] ciudades = new String[] {"Pachuca", "Tulancingo", "Tula"};
        ArrayAdapter<String> adapterCiudades = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                ciudades
        );
        spinnerCity.setAdapter(adapterCiudades);
    }

    public void navigateToThirdRegistry(RegisterRequestDto request){
        Password fragmentPassword = new Password(request);
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLogin, fragmentPassword);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onObtainStatesSuccess(ArrayList<StatesDto> result) {
        Gson g = new Gson();
        this.onHideProgress();
        this.containerSpinnerStates.setEnabled(true);
        ArrayList<String> states = new ArrayList<>();

        result.forEach(state ->{
            states.add(state.getState_name());
        });
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                states
        );
        spinnerState.setAdapter(adapterEstados);

        Log.i("AddressRegister", "onSuccessObtainStates" + g.toJson(result));


    }

    @Override
    public void onObtainStatesError(String s) {
        this.onHideProgress();
        Log.i("AddressRegister", "onErrorobtainStates: " + s);
    }

    @Override
    public void onShowProgress() {
        this.loadingItem.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        this.loadingItem.setVisibility(View.GONE);
    }

    @Override
    public void onObtainCitiesError(String s) {

    }

    @Override
    public void onObtainCitiesSuccess(ArrayList<CitiesDto> result) {
        Gson g = new Gson();
        this.onHideProgress();
        this.containerSpinnerCities.setEnabled(true);
        ArrayList<String> cities = new ArrayList<>();

        result.forEach(state ->{
            cities.add(state.getCity_name());
        });
        ArrayAdapter<String> adapterCiudades = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                cities
        );
        spinnerCity.setAdapter(adapterCiudades);

        Log.i("AddressRegister", "onSuccessObtainCities" + g.toJson(result));
    }
}
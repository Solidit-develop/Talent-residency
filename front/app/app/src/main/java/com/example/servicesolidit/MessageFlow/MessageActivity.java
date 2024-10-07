package com.example.servicesolidit.MessageFlow;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.servicesolidit.Model.Responses.Messages.ConversationDto;
import com.example.servicesolidit.Model.Responses.Messages.MessageDto;
import com.example.servicesolidit.Model.Responses.Messages.MessagesResponseDto;
import com.example.servicesolidit.Network.ApiService;
import com.example.servicesolidit.Network.RetrofitClient;
import com.example.servicesolidit.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageActivity extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<MessageDto> messageList = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessageActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageActivity newInstance(String param1, String param2) {
        MessageActivity fragment = new MessageActivity();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_activity, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int idOrigen = 1; //usuario logueado
        int idDestino = 2; //usuario destino

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<MessagesResponseDto> call = apiService.getMessages(idOrigen,idDestino);

        call.enqueue(new Callback<MessagesResponseDto>() {
            @Override
            public void onResponse(Call<MessagesResponseDto> call, Response<MessagesResponseDto> response) {
                if (response.isSuccessful() && response.body() != null){
                    for (ConversationDto resultado : response.body().getResultados()) {
                        messageList.addAll(resultado.getMessage());
                    }

                    adapter = new MessageAdapter(messageList, idOrigen);
                    recyclerView.setAdapter(adapter);
                }else {
                    //manejo del error
                }
            }

            @Override
            public void onFailure(Call<MessagesResponseDto> call, Throwable t) {
                //manejo de errores
            }
        });

        return view;
    }
}
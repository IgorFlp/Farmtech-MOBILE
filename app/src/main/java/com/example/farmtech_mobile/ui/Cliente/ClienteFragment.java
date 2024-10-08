package com.example.farmtech_mobile.ui.Cliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.farmtech_mobile.MainActivity;
import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.databinding.FragmentClienteBinding;
import com.example.farmtech_mobile.databinding.FragmentLoginBinding;
import com.example.farmtech_mobile.ui.login.LoginViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClienteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClienteFragment extends Fragment {

    private FragmentClienteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClienteBinding.inflate(inflater, container, false);
        return binding.getRoot();
        // Inflate the layout for this fragment
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btnNovoCliente = binding.btnNovoCliente;

        btnNovoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment", "ClienteNovo");
                startActivity(intent);
                getActivity().finish();
            }
        });

    }



    public ClienteFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClienteFragment newInstance(String param1, String param2) {
        ClienteFragment fragment = new ClienteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
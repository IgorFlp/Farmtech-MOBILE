package com.example.farmtech_mobile.ui.fornecedor;

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
import com.example.farmtech_mobile.databinding.FragmentFornecedorBinding;
import com.example.farmtech_mobile.ui.fornecedor.FornecedorFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FornecedorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FornecedorFragment extends Fragment {

        private FragmentFornecedorBinding binding;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            binding = FragmentFornecedorBinding.inflate(inflater, container, false);
            return binding.getRoot();

        }
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            final Button btnNovoFornecedor = binding.btnNovoFornecedor;

            btnNovoFornecedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("fragment", "FornecedorNovo");
                    startActivity(intent);
                    getActivity().finish();
                }
            });

        }



        public FornecedorFragment() {
            // Required empty public constructor
        }

        // TODO: Rename and change types and number of parameters
        public static FornecedorFragment newInstance(String param1, String param2) {
            FornecedorFragment fragment = new FornecedorFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }


    }

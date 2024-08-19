package com.example.farmtech_mobile.ui.vender;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.farmtech_mobile.databinding.FragmentVenderBinding;

public class VenderFragment extends Fragment {

    private FragmentVenderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VenderViewModel venderViewModel =
                new ViewModelProvider(this).get(VenderViewModel.class);

        binding = FragmentVenderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textVender;
        venderViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
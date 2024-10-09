package com.example.farmtech_mobile.ui.fornecedor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.farmtech_mobile.MainActivity;
import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Fornecedor;
import com.example.farmtech_mobile.data.model.FornecedorEndereco;
import com.example.farmtech_mobile.databinding.FragmentFornecedorBinding;
import com.example.farmtech_mobile.ui.fornecedor.FornecedorFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btnNovoFornecedor = binding.btnNovoFornecedor;
        final Button btnAlterarFornecedor = binding.btnAlterarFornecedor;
        LinearLayout fornecedorLista = binding.fornecedoresLista;

        ApiService apiService = RetrofitClient.getApiService();


        Call<List<Fornecedor>> call = apiService.getFornecedores();
        call.enqueue(new Callback<List<Fornecedor>>() {
            @Override
            public void onResponse(Call<List<Fornecedor>> call, Response<List<Fornecedor>> response) {
                List<Fornecedor> fornecedores = response.body();
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(fornecedores);
                    Log.d("getFornecedores", "Fornecedores consultados com sucesso"+json);

                    Call <List<FornecedorEndereco>> call2 = apiService.getFornecedoresEnderecos();
                    call2.enqueue(new Callback<List<FornecedorEndereco>>() {
                        @Override
                        public void onResponse(Call<List<FornecedorEndereco>> call, Response<List<FornecedorEndereco>> response) {
                            List<FornecedorEndereco> enderecos = response.body();
                            if (response.isSuccessful()) {
                                Gson gson = new Gson();
                                String json = gson.toJson(enderecos);
                                Log.d("getEnderecos", "Endereços de fornecedores consultados com sucesso"+json);
                                int i = 0;
                                Context contextLista = fornecedorLista.getContext();
                                for(Fornecedor fornecedor: fornecedores){


                                    LinearLayout fornecedorContainer = new LinearLayout(contextLista);
                                    fornecedorContainer.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    ));
                                    fornecedorContainer.setOrientation(LinearLayout.VERTICAL);
                                    fornecedorContainer.setTag("fornecedorContainer");
                                    fornecedorContainer.setId(View.generateViewId());
                                    int containerId = fornecedorContainer.getId();

                                    Context contextContainer = fornecedorContainer.getContext();

                                    LinearLayout fornecedorRow = new LinearLayout(contextContainer);
                                    fornecedorRow.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT

                                    ));
                                    fornecedorRow.setOrientation(LinearLayout.HORIZONTAL);
                                    fornecedorRow.setGravity(Gravity.CENTER_VERTICAL);
                                    fornecedorRow.setTag("fornecedorRow");
                                    fornecedorRow.setId(View.generateViewId());
                                    int rowId = fornecedorRow.getId();
                                    Context contextRow = fornecedorRow.getContext();

                                    CheckBox cbFornecedor = new CheckBox(contextRow);
                                    cbFornecedor.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    cbFornecedor.setTag("cbFornecedor");
                                    cbFornecedor.setId(View.generateViewId());


                                    TextView lblFornecedor = new TextView(contextRow);
                                    LinearLayout.LayoutParams  lblFornecedorParams = new LinearLayout.LayoutParams(
                                            dpToPx(257), // largura em pixels
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    lblFornecedorParams.setMargins(20, 0, 0, 0);
                                    lblFornecedor.setLayoutParams(lblFornecedorParams);
                                    lblFornecedor.setText(fornecedor.getNomeFantasia());
                                    lblFornecedor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                    lblFornecedor.setId(View.generateViewId());
                                    lblFornecedor .setTag("fornecedorLabel");

                                    ImageView dropdownButton = new ImageView(contextRow);
                                    LinearLayout.LayoutParams dropdownLayoutParams = new LinearLayout.LayoutParams(
                                            dpToPx(48), // largura em pixels
                                            dpToPx(48));
                                    dropdownLayoutParams.setMargins(dpToPx(40), 0, 0, 0);
                                    dropdownButton.setLayoutParams(dropdownLayoutParams);
                                    dropdownButton.setImageResource(R.mipmap.dropdown_foreground);
                                    dropdownButton.setTag("dropdownButton");
                                    dropdownButton.setId(View.generateViewId());

                                    fornecedorRow.addView(cbFornecedor);
                                    fornecedorRow.addView(lblFornecedor);
                                    fornecedorRow.addView(dropdownButton);

                                    LinearLayout fornecedorData = new LinearLayout(contextContainer);
                                    fornecedorData.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    fornecedorData.setOrientation(LinearLayout.VERTICAL);
                                    fornecedorData.setTag("fornecedorData");
                                    fornecedorData.setVisibility(View.GONE);
                                    Context contextData = fornecedorData.getContext();

                                    // Crie o TextView para os dados do fornecedor
                                    TextView dadosFornecedor = new TextView(contextData);
                                    dadosFornecedor.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    String fornecedorJson = gson.toJson(fornecedor);
                                    dadosFornecedor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                    dadosFornecedor.setTag(fornecedor.getCnpj());
                                    dadosFornecedor.setText("Nome Fantasia: "+fornecedor.getNomeFantasia() +"\nRazão Social: "+fornecedor.getRazaoSocial()+"\nCNPJ: "+fornecedor.getCnpj()+"\nTelefone: "+fornecedor.getTelefone()+"\nEmail: "+fornecedor.getEmail()+"\n");
                                    fornecedorData.addView(dadosFornecedor);

                                    TextView dadosEndereco = new TextView(contextData);
                                    dadosEndereco.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                                    String enderecoJson = gson.toJson(enderecos.get(i));
                                    dadosEndereco.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                    dadosEndereco.setText("Rua: "+enderecos.get(i).getRua()+"\nBairro: "+enderecos.get(i).getBairro()+"\nCidade: "+enderecos.get(i).getCidade()+"\nEstado: "+enderecos.get(i).getEstado()+"\nCEP: "+enderecos.get(i).getCep()+"\n");
                                    fornecedorData.addView(dadosEndereco);


                                    fornecedorContainer.addView(fornecedorRow);
                                    fornecedorContainer.addView(fornecedorData);

                                    fornecedorLista.addView(fornecedorContainer);

                                    i++;
                                }
                                criaListeners();
                            }
                        }
                        @Override
                        public void onFailure(Call<List<FornecedorEndereco>> call, Throwable t) {
                            Log.e("getFornecedorEndereco", "Erro: " + t.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<Fornecedor>> call, Throwable t) {
                Log.e("getFornecedor", "Erro: " + t.getMessage());
            }
        });


        btnNovoFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment", "FornecedorNovo");
                startActivity(intent);
                getActivity().finish();
            }
        });
        Button btnDeleteFornecedor = binding.btnDeleteFornecedor;
        btnDeleteFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;

                for (int i = 0; i < fornecedorLista.getChildCount(); i++) {
                    View container = fornecedorLista.getChildAt(i);
                    View row = container.findViewWithTag("fornecedorRow");
                    CheckBox cb = row.findViewWithTag("cbFornecedor");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout fornecedorContainer = (LinearLayout) selecionado.getParent().getParent();
                    LinearLayout fornecedorData = fornecedorContainer.findViewWithTag("fornecedorData");
                    String cpf = (String) fornecedorData.getChildAt(0).getTag();
                    Log.d("Delete", "CPF Selecionado: " + cpf);

                    Call<FornecedorEndereco> deleteEndereco = apiService.deleteFornecedorEndereco(cpf);
                    deleteEndereco.enqueue(new Callback<FornecedorEndereco>() {
                        @Override
                        public void onResponse(Call<FornecedorEndereco> delete, Response<FornecedorEndereco> response) {
                            if (response.isSuccessful()) {
                                Log.d("EnderecoDelete", "Endereco deletado com sucesso ");
                                Call<Fornecedor> deleteFornecedor = apiService.deleteFornecedor(cpf);
                                deleteFornecedor.enqueue(new Callback<Fornecedor>() {
                                    @Override
                                    public void onResponse(Call<Fornecedor> delete, Response<Fornecedor> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("FornecedorDelete", "Fornecedor deletado com sucesso ");
                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("Cadastro de fornecedor")
                                                    .setMessage("Fornecedor deletado com sucesso!")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                                            navController.navigate(R.id.nav_fornecedor);
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Fornecedor> delete, Throwable t) {
                                        Log.e("FornecedorDelete, ", "Erro: " + t.getMessage());
                                    };
                                });
                            }
                        }
                        @Override
                        public void onFailure(Call<FornecedorEndereco> delete, Throwable t) {
                            Log.e("EnderecoDelete", "Erro: " + t.getMessage());
                        };;
                    });
                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de fornecedor")
                            .setMessage("Selecione uma checkbox!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                    navController.navigate(R.id.nav_fornecedor);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        btnAlterarFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;
                for (int i = 0; i < fornecedorLista.getChildCount(); i++) {
                    View container = fornecedorLista.getChildAt(i);
                    View row = container.findViewWithTag("fornecedorRow");
                    CheckBox cb = row.findViewWithTag("cbFornecedor");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout fornecedorContainer = (LinearLayout) selecionado.getParent().getParent();
                    LinearLayout fornecedorData = fornecedorContainer.findViewWithTag("fornecedorData");
                    String cnpj = (String) fornecedorData.getChildAt(0).getTag();
                    Log.d("Alterar", "CNPJ Selecionado: " + cnpj);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("fragment", "FornecedorAlterar");
                    intent.putExtra("cnpj", cnpj);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de fornecedor")
                            .setMessage("Selecione uma checkbox!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                    navController.navigate(R.id.nav_fornecedor);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
    }
    private void criaListeners() {
        LinearLayout fornecedorLista = binding.fornecedoresLista;
        View.OnClickListener dropdownListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout fornecedorRow = (LinearLayout) v.getParent();
                LinearLayout container = (LinearLayout) fornecedorRow.getParent();
                ImageView dropdownButton = container.findViewWithTag("dropdownButton");
                String tag = "fornecedorData";
                LinearLayout fornecedorData = container.findViewWithTag(tag);

                if (fornecedorData.getVisibility() == View.GONE) {
                    fornecedorData.setVisibility(View.VISIBLE);
                    dropdownButton.setImageResource(R.mipmap.dropup_foreground);
                } else {
                    fornecedorData.setVisibility(View.GONE);
                    dropdownButton.setImageResource(R.mipmap.dropdown_foreground);
                }
                int fornecedorId = container.getId();
            }
        };
        List<CheckBox> checkBoxes = new ArrayList<>();
        LinearLayout fornecedorRow = binding.fornecedoresLista.findViewWithTag("fornecedorRow");
        for (int i = 0; i < fornecedorLista.getChildCount(); i++) {
            View container = fornecedorLista.getChildAt(i);
            View row = container.findViewWithTag("fornecedorRow");
            CheckBox cb = row.findViewWithTag("cbFornecedor");
            checkBoxes.add(cb);
            Log.d("Checkboxes", "Adicionou: "+cb.getTag());
        }
        for(CheckBox checkBox: checkBoxes){
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        CheckBox selecionado = checkBox;
                        for (CheckBox checkBox : checkBoxes) {
                            if(checkBox != selecionado){
                                checkBox.setChecked(false);
                            }
                        }
                    }
                }
            });
        }


        for (int i = 0; i < fornecedorLista.getChildCount(); i++) {

            String tag = "dropdownButton";

            ImageView dropdownButton = fornecedorLista.getChildAt(i).findViewWithTag(tag);

            dropdownButton.setOnClickListener(dropdownListener);
        }

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

package com.example.farmtech_mobile.ui.fornecedorNovo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.farmtech_mobile.MainActivity;
import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.SecundaryActivity;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Fornecedor;
import com.example.farmtech_mobile.data.model.FornecedorEndereco;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FornecedorNovoFragment extends Fragment {
    private Toolbar toolbar;
    public FornecedorNovoFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Captura o evento de voltar
        getActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), SecundaryActivity.class);
                intent.putExtra("fragment", "Fornecedor");
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fornecedor_novo, container, false);
        return inflater.inflate(R.layout.fragment_fornecedor_novo, container, false);

    }
    private EditText txtNomeFantasia,txtRazaoSocial, txtCnpj, txtEmail, txtTelefone;
    private Button btnNovoFornecedor;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText txtNomeFantasia = view.findViewById(R.id.txtNomeFantasia);
        EditText txtRazaoSocial = view.findViewById(R.id.txtRazaoSocial);
        EditText txtCnpj = view.findViewById(R.id.txtCnpj);
        EditText txtEmail = view.findViewById(R.id.txtEmail);
        EditText txtTelefone = view.findViewById(R.id.txtTelefone);

        EditText txtRua = view.findViewById(R.id.txtRua);
        EditText txtBairro = view.findViewById(R.id.txtBairro);
        EditText txtCidade = view.findViewById(R.id.txtCidade);
        Spinner slcEstado = view.findViewById(R.id.slcEstado);
        EditText txtCep = view.findViewById(R.id.txtCep);


        btnNovoFornecedor    = view.findViewById(R.id.btnConfirma);


        btnNovoFornecedor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Ação de clique do botão
                ApiService apiService = RetrofitClient.getApiService();

                // Coletando os valores preenchidos nos campos

                String nomeFantasia = txtNomeFantasia.getText().toString();
                String razaoSocial = txtRazaoSocial .getText().toString();
                String email = txtEmail.getText().toString();
                String telefone = txtTelefone.getText().toString();
                String cnpj = txtCnpj.getText().toString();

                String rua = txtRua.getText().toString();
                String bairro = txtBairro.getText().toString();
                String cidade = txtCidade.getText().toString();
                String estado = slcEstado.getSelectedItem().toString();
                String cep= txtCep.getText().toString();

                boolean b = cnpj != null && !cnpj.isEmpty() &&
                        nomeFantasia != null && !nomeFantasia.isEmpty() &&
                        razaoSocial != null && !razaoSocial.isEmpty() &&
                        email != null && !email.isEmpty() &&
                        telefone != null && !telefone.isEmpty() &&
                        rua != null && !rua.isEmpty() &&
                        bairro != null && !bairro.isEmpty() &&
                        cidade != null && !cidade.isEmpty() &&
                        estado != null && !estado.isEmpty() &&
                        cep != null && !cep.isEmpty();

                Log.d("FornecedorNovoFragment","B: "+b);
                if(b == true){
                    Fornecedor fornecedor = new Fornecedor(nomeFantasia,razaoSocial,cnpj,email,telefone);
                    Gson gson = new Gson();
                    String json = gson.toJson(fornecedor);
                    Log.d("FornecedorNovoFragment","Fornecedor: "+json);

                    Call<Fornecedor> call = apiService.criarFornecedor(fornecedor);
                    call.enqueue(new Callback<Fornecedor>() {
                        @Override
                        public void onResponse(Call<Fornecedor> call, Response<Fornecedor> response) {
                            if (response.isSuccessful()) {
                                Log.d("FornecedorNovoFragment", "Fornecedor criado com sucesso: " + response.body());

                                FornecedorEndereco fornecedorEndereco  = new FornecedorEndereco(cnpj,rua,bairro,cidade,estado,cep);

                                String json2 = gson.toJson(fornecedorEndereco);
                                Log.d("FornecedorNovoFragment","Endereco: "+json2);

                                Call <FornecedorEndereco> call2 = apiService.criarFornecedorEndereco(fornecedorEndereco);
                                call2.enqueue(new Callback<FornecedorEndereco>() {
                                    @Override
                                    public void onResponse(Call<FornecedorEndereco> call, Response<FornecedorEndereco> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("FornecedorNovoFragment", "Endereco criado com sucesso: " + response.body());

                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("Cadastro de fornecedor")
                                                    .setMessage("Fornecedor cadastrado com sucesso!")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(getActivity(), SecundaryActivity.class);
                                                            intent.putExtra("fragment", "Fornecedor");
                                                            startActivity(intent);
                                                            getActivity().finish();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        } else {
                                            Log.e("FornecedorNovoFragment", "Falha na criação do endereco: " + response.message());
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<FornecedorEndereco> call, Throwable t) {
                                        Log.e("FornecedorNovoFragment", "Erro: " + t.getMessage());
                                    }
                                });

                            } else {
                                Log.e("FornecedorNovoFragment", "Falha na criação do fornecedor: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Fornecedor> call, Throwable t) {
                            Log.e("FornecedorNovoFragment", "Erro: " + t.getMessage());
                        }
                    });

                }else{
                    Log.d("FornecedorNovoFragment","Campos vazios");
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de fornecedor")
                            .setMessage("Existem campos vazios ou incorretos, favor corrigir!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Ação ao clicar no botão "OK"

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                //Log.d("FornecedorNovoFragment","Fornecedor: "+json);

            }
        });


    }

}
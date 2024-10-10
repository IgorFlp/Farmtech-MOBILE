package com.example.farmtech_mobile.ui.fornecedorAlterar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.SecundaryActivity;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Fornecedor;
import com.example.farmtech_mobile.data.model.FornecedorEndereco;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FornecedorAlterarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FornecedorAlterarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ApiService apiService = RetrofitClient.getApiService();

    public FornecedorAlterarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FornecedorAlterarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FornecedorAlterarFragment newInstance(String param1, String param2) {
        FornecedorAlterarFragment fragment = new FornecedorAlterarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View view = inflater.inflate(R.layout.fragment_fornecedor_alterar, container, false);

        return inflater.inflate(R.layout.fragment_fornecedor_alterar, container, false);

    }
    private EditText txtNomeFantasia,txtRazaoSocial , txtCnpj, txtEmail, txtTelefone;
    private Button btnCadastrar;
    private Button btnCancelar;
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


        Intent intent = getActivity().getIntent();
        String cnpj = intent.getStringExtra("cnpj");
        Log.d("FornecedorAlterar", "CNPJ recebido: "+cnpj);

        final Fornecedor[] fornecedorArr = new Fornecedor[1];
        final FornecedorEndereco[] fornecedorEnderecoArr = new FornecedorEndereco[1];

        Call<Fornecedor> fornecedorCall = apiService.getFornecedor(cnpj);
        fornecedorCall.enqueue(new Callback<Fornecedor>() {
            @Override
            public void onResponse(Call<Fornecedor> call, Response<Fornecedor> response) {
                if(response.isSuccessful()){
                    fornecedorArr[0] = response.body();
                    Log.d("FornecedorAlterar", "Fornecedor recebido: "+ fornecedorArr[0]);

                    Call<FornecedorEndereco> enderecoCall = apiService.getFornecedorEndereco(cnpj);
                    enderecoCall.enqueue(new Callback<FornecedorEndereco>() {
                        @Override
                        public void onResponse(Call<FornecedorEndereco> call, Response<FornecedorEndereco> response) {
                            if(response.isSuccessful()){
                                fornecedorEnderecoArr[0] = response.body();
                                Log.d("FornecedorAlterar", "Endereco recebido: "+ fornecedorEnderecoArr[0]);

                                Fornecedor fornecedor = fornecedorArr[0];
                                FornecedorEndereco fornecedorEndereco = fornecedorEnderecoArr[0];

                                String json = new Gson().toJson(fornecedor);
                                Log.d("FornecedorAlterar", "Fornecedor: "+json);

                                String[] estadosArray = getResources().getStringArray(R.array.estados_spinner);
                                List<String> estadosList = new ArrayList<>(Arrays.asList(estadosArray));

                                // Criar o adaptador para o Spinner
                                ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estadosList);

                                slcEstado.setAdapter(adapterEstados);

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

                                txtNomeFantasia.setText(fornecedor.getNomeFantasia());
                                txtRazaoSocial.setText(fornecedor.getRazaoSocial());
                                txtCnpj.setText(fornecedor.getCnpj());
                                txtCnpj.setEnabled(false);
                                txtEmail.setText(fornecedor.getEmail());
                                txtTelefone.setText(fornecedor.getTelefone());

                                txtRua.setText(fornecedorEndereco.getRua());
                                txtBairro.setText(fornecedorEndereco.getBairro());
                                txtCidade.setText(fornecedorEndereco.getCidade());
                                slcEstado.setSelection(adapterEstados.getPosition(fornecedorEndereco.getEstado()));
                                txtCep.setText(fornecedorEndereco.getCep());

                            }else{
                                Log.d("FornecedorAlterar", "Endereco Call não retornou");
                            }
                        }
                        @Override
                        public void onFailure(Call<FornecedorEndereco> call, Throwable t) {
                            Log.d("FornecedorAlterar", "Erro na call de endereco fornecedor");
                        }
                    });
                }else{
                    Log.d("FornecedorAlterar", "Fornecedor Call não retornou");
                }
            }
            @Override
            public void onFailure(Call<Fornecedor> call, Throwable t) {
                Log.d("FornecedorAlterar", "Erro na call de fornecedor");
            }
        });


        btnCadastrar = view.findViewById(R.id.btnConfirma);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Ação de clique do botão
                //ApiService apiService = RetrofitClient.getApiService();

                // Coletando os valores preenchidos nos campos

                String nomeFantasia = txtNomeFantasia.getText().toString();
                String razaoSocial = txtRazaoSocial.getText().toString();
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
                        email != null && !email.isEmpty() &&
                        telefone != null && !telefone.isEmpty() &&
                        rua != null && !rua.isEmpty() &&
                        bairro != null && !bairro.isEmpty() &&
                        cidade != null && !cidade.isEmpty() &&
                        estado != null && !estado.isEmpty() &&
                        cep != null && !cep.isEmpty();

                Log.d("FornecedorAlterarFragment","B: "+b);
                if(b == true){
                    Fornecedor fornecedor = new Fornecedor(nomeFantasia,razaoSocial,cnpj,email,telefone);
                    Gson gson = new Gson();
                    String json = gson.toJson(fornecedor);
                    Log.d("FornecedorNovoFragment","Fornecedor: "+json);

                    Call<Fornecedor> call = apiService.atualizarFornecedor(cnpj,fornecedor);
                    call.enqueue(new Callback<Fornecedor>() {
                        @Override
                        public void onResponse(Call<Fornecedor> call, Response<Fornecedor> response) {
                            if (response.isSuccessful()) {
                                Log.d("FornecedorAlterarFragment", "Fornecedor Alterado com sucesso: " + response.body());

                                FornecedorEndereco fornecedorEndereco  = new FornecedorEndereco(cnpj,rua,bairro,cidade,estado,cep);

                                String json2 = gson.toJson(fornecedorEndereco);
                                Log.d("FornecedorNovoFragment","Endereco: "+json2);

                                Call <FornecedorEndereco> call2 = apiService.atualizarFornecedorEndereco(cnpj,fornecedorEndereco);
                                call2.enqueue(new Callback<FornecedorEndereco>() {
                                    @Override
                                    public void onResponse(Call<FornecedorEndereco> call, Response<FornecedorEndereco> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("FornecedorAlterarFragment", "Endereco alterado com sucesso: " + response.body());

                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("Cadastro de fornecedor")
                                                    .setMessage("Fornecedor alterado com sucesso!")
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
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SecundaryActivity.class);
                intent.putExtra("fragment", "Fornecedor");
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
package com.example.farmtech_mobile.ui.clienteNovo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Cliente;
import com.example.farmtech_mobile.data.model.ClienteEndereco;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteNovoFragment extends Fragment {

    public ClienteNovoFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cliente_novo, container, false);
    }
    private EditText txtNome, txtCpf, txtEmail, txtTelefone;
    private Button btnCadastrar;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referenciar o Spinner de Anos no layout
        Spinner slcAno = view.findViewById(R.id.slcAno);


        List<String> anos = new ArrayList<>();
        for (int i = 1900; i <= 2024; i++) {
            anos.add(String.valueOf(i));
        }

        // Criar o adaptador para o Spinner
        ArrayAdapter<String> adapterAnos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, anos);
        adapterAnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Conectar o adaptador ao Spinner
        slcAno.setAdapter(adapterAnos);

        EditText txtNome = view.findViewById(R.id.txtNome);
        EditText txtCpf = view.findViewById(R.id.txtCpf);
        EditText txtEmail = view.findViewById(R.id.txtEmail);
        EditText txtTelefone = view.findViewById(R.id.txtTelefone);
        Spinner  slcDia = view.findViewById(R.id.slcDia);
        Spinner  slcMes = view.findViewById(R.id.slcMes);
        Spinner  slcGenero = view.findViewById(R.id.slcGenero);
        EditText txtRua = view.findViewById(R.id.txtRua);
        EditText txtBairro = view.findViewById(R.id.txtBairro);
        EditText txtCidade = view.findViewById(R.id.txtCidade);
        Spinner slcEstado = view.findViewById(R.id.slcEstado);
        EditText txtCep = view.findViewById(R.id.txtCep);


        btnCadastrar = view.findViewById(R.id.btnConfirma);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Ação de clique do botão
                ApiService apiService = RetrofitClient.getApiService();

                // Coletando os valores preenchidos nos campos

                String nome = txtNome.getText().toString();
                String email = txtEmail.getText().toString();
                String telefone = txtTelefone.getText().toString();
                String cpf = txtCpf.getText().toString();

                    Map<String,String> meses = new HashMap();
                    meses.put("Janeiro","01");
                    meses.put("Fevereiro","02");
                    meses.put("Março","03");
                    meses.put("Abril","04");
                    meses.put("Maio","05");
                    meses.put("Junho","06");
                    meses.put("Julho","07");
                    meses.put("Agosto","08");
                    meses.put("Setembro","09");
                    meses.put("Outubro","10");
                    meses.put("Novembro","11");
                    meses.put("Dezembro","12");

                String dia = slcDia.getSelectedItem().toString();
                String mes = slcMes.getSelectedItem().toString();
                String ano = slcAno.getSelectedItem().toString();
                mes = meses.get(mes);
                String dataNasc = ano+"-"+mes+"-"+dia;
                Log.d("ClienteNovoFragment","Data nasc: "+dataNasc);
                String gen = slcGenero.getSelectedItem().toString();
                char genero;
                switch (gen){
                    case "Masculino":
                        genero = 'M';
                        break;
                    case "Feminino":
                        genero = 'F';
                        break;
                    case "Outro":
                        genero = 'O';
                        break;
                    default:
                        genero='N';
                        break;
                }
                Log.d("ClienteNovoFragment","Genero: "+genero);

                String rua = txtRua.getText().toString();
                String bairro = txtBairro.getText().toString();
                String cidade = txtCidade.getText().toString();
                String estado = slcEstado.getSelectedItem().toString();
                String cep= txtCep.getText().toString();

                boolean b = cpf != null && !cpf.isEmpty() &&
                        nome != null && !nome.isEmpty() &&
                        email != null && !email.isEmpty() &&
                        telefone != null && !telefone.isEmpty() &&
                        dia != null && !dia.isEmpty() &&
                        mes != null && !mes.isEmpty() &&
                        ano != null && !ano.isEmpty() &&
                        genero != 'N' &&
                        rua != null && !rua.isEmpty() &&
                        bairro != null && !bairro.isEmpty() &&
                        cidade != null && !cidade.isEmpty() &&
                        estado != null && !estado.isEmpty() &&
                        cep != null && !cep.isEmpty();
                Log.d("ClienteNovoFragment","B: "+b);
                if(b == true){
                    Cliente cliente = new Cliente(nome,cpf,email,telefone,dataNasc,genero);
                    Gson gson = new Gson();
                    String json = gson.toJson(cliente);
                     Log.d("ClienteNovoFragment","Cliente: "+json);
                    Call<Cliente> call = apiService.criarCliente(cliente);
                    call.enqueue(new Callback<Cliente>() {
                        @Override
                        public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                            if (response.isSuccessful()) {
                                Log.d("ClienteNovoFragment", "Cliente criado com sucesso: " + response.body());

                                ClienteEndereco clienteEndereco  = new ClienteEndereco(cpf,rua,bairro,cidade,estado,cep);

                                String json2 = gson.toJson(clienteEndereco);
                                Log.d("ClienteNovoFragment","Endereco: "+json2);

                                Call <ClienteEndereco> call2 = apiService.criarClienteEndereco(clienteEndereco);
                                call2.enqueue(new Callback<ClienteEndereco>() {
                                    @Override
                                    public void onResponse(Call<ClienteEndereco> call, Response<ClienteEndereco> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("ClienteNovoFragment", "Endereco criado com sucesso: " + response.body());

                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("Cadastro de cliente")
                                                    .setMessage("Cliente cadastrado com sucesso!")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Ação ao clicar no botão "OK"
                                                            view.invalidate();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        } else {
                                            Log.e("ClienteNovoFragment", "Falha na criação do endereco: " + response.message());
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<ClienteEndereco> call, Throwable t) {
                                        Log.e("ClienteNovoFragment", "Erro: " + t.getMessage());
                                    }
                                });

                            } else {
                                Log.e("ClienteNovoFragment", "Falha na criação do cliente: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Cliente> call, Throwable t) {
                            Log.e("ClienteNovoFragment", "Erro: " + t.getMessage());
                        }
                    });

                }else{
                    Log.d("ClienteNovoFragment","Campos vazios");
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de cliente")
                            .setMessage("Existem campos vazios ou incorretos, favor corrigir!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Ação ao clicar no botão "OK"

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                //Log.d("ClienteNovoFragment","Cliente: "+json);

            }
        });


    }

}
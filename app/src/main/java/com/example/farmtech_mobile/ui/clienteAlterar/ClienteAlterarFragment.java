package com.example.farmtech_mobile.ui.clienteAlterar;

import static android.content.Intent.getIntent;

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
import com.example.farmtech_mobile.data.model.Cliente;
import com.example.farmtech_mobile.data.model.ClienteEndereco;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClienteAlterarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClienteAlterarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClienteAlterarFragment() {
        // Required empty public constructor
    }
    ApiService apiService = RetrofitClient.getApiService();

    public static ClienteAlterarFragment newInstance(String param1, String param2) {
        ClienteAlterarFragment fragment = new ClienteAlterarFragment();
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
                intent.putExtra("fragment", "Cliente");
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

        View view = inflater.inflate(R.layout.fragment_cliente_alterar, container, false);

        return inflater.inflate(R.layout.fragment_cliente_alterar, container, false);

    }
    private EditText txtNome, txtCpf, txtEmail, txtTelefone;
    private Button btnCadastrar;
    private Button btnCancelar;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner slcAno = view.findViewById(R.id.slcAno);

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


        Intent intent = getActivity().getIntent();
        String cpf = intent.getStringExtra("cpf");
        Log.d("ClienteAlterar", "CPF recebido: "+cpf);

        final Cliente[] clienteArr = new Cliente[1];
        final ClienteEndereco[] clienteEnderecoArr = new ClienteEndereco[1];

        Call<Cliente> clienteCall = apiService.getCliente(cpf);
        clienteCall.enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if(response.isSuccessful()){
                    clienteArr[0] = response.body();
                    Log.d("ClienteAlterar", "Cliente recebido: "+ clienteArr[0]);

                    Call<ClienteEndereco> enderecoCall = apiService.getClienteEndereco(cpf);
                    enderecoCall.enqueue(new Callback<ClienteEndereco>() {
                        @Override
                        public void onResponse(Call<ClienteEndereco> call, Response<ClienteEndereco> response) {
                            if(response.isSuccessful()){
                                clienteEnderecoArr[0] = response.body();
                                Log.d("ClienteAlterar", "Endereco recebido: "+ clienteEnderecoArr[0]);

                                Cliente cliente = clienteArr[0];
                                ClienteEndereco clienteEndereco = clienteEnderecoArr[0];

                                String json = new Gson().toJson(cliente);
                                Log.d("ClienteAlterar", "Cliente: "+json);

                                String dataNasc = cliente.getDataNasc();
                                String ano = dataNasc.substring(0,4);
                                String mes = dataNasc.substring(5,7);
                                String dia = dataNasc.substring(8,10);
                                Log.d("ClienteAlterar", "Ano"+ano+" Mes"+mes+" Dia"+dia);

                                List<String> anos = new ArrayList<>();
                                for (int i = 1900; i <= 2024; i++) {
                                    anos.add(String.valueOf(i));
                                }
                                String[] diasArray = getResources().getStringArray(R.array.dias_spinner);
                                List<String> dias = new ArrayList<>(Arrays.asList(diasArray));

                                String[] mesesArray = getResources().getStringArray(R.array.meses_spinner);
                                List<String> mesesList = new ArrayList<>(Arrays.asList(mesesArray));

                                String[] estadosArray = getResources().getStringArray(R.array.estados_spinner);
                                List<String> estadosList = new ArrayList<>(Arrays.asList(estadosArray));

                                // Criar o adaptador para o Spinner
                                ArrayAdapter<String> adapterAnos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, anos);
                                ArrayAdapter<String> adapterMeses = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mesesList);
                                ArrayAdapter<String> adapterDias = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dias);
                                ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, estadosList);
                                adapterAnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // Conectar o adaptador ao Spinner
                                slcAno.setAdapter(adapterAnos);
                                slcMes.setAdapter(adapterMeses);
                                slcDia.setAdapter(adapterDias);
                                slcEstado.setAdapter(adapterEstados);

                                Map<String,String> meses = new HashMap();
                                meses.put("01","Janeiro");
                                meses.put("02","Fevereiro");
                                meses.put("03","Março");
                                meses.put("04","Abril");
                                meses.put("05","Maio");
                                meses.put("06","Junho");
                                meses.put("07","Julho");
                                meses.put("08","Agosto");
                                meses.put("09","Setembro");
                                meses.put("10","Outubro");
                                meses.put("11","Novembro");
                                meses.put("12","Dezembro");

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

                                txtNome.setText(cliente.getNome());
                                txtCpf.setText(cliente.getCpf());
                                txtCpf.setEnabled(false);
                                txtEmail.setText(cliente.getEmail());
                                txtTelefone.setText(cliente.getTelefone());
                                slcDia.setSelection(adapterDias.getPosition(dia));

                                String mesExtenso = meses.get(mes);
                                slcMes.setSelection(adapterMeses.getPosition(mesExtenso));
                                slcAno.setSelection(adapterAnos.getPosition(ano));

                                if(cliente.getGenero() == 'M'){
                                    slcGenero.setSelection(1);
                                }else if(cliente.getGenero() == 'F'){
                                    slcGenero.setSelection(2);
                                }else {
                                    slcGenero.setSelection(3);
                                }
                                txtRua.setText(clienteEndereco.getRua());
                                txtBairro.setText(clienteEndereco.getBairro());
                                txtCidade.setText(clienteEndereco.getCidade());
                                slcEstado.setSelection(adapterEstados.getPosition(clienteEndereco.getEstado()));
                                txtCep.setText(clienteEndereco.getCep());

                                }else{
                                Log.d("ClienteAlterar", "Endereco Call não retornou");
                            }
                        }
                        @Override
                        public void onFailure(Call<ClienteEndereco> call, Throwable t) {
                            Log.d("ClienteAlterar", "Erro na call de endereco cliente");
                        }
                    });
                }else{
                    Log.d("ClienteAlterar", "Cliente Call não retornou");
                }
            }
            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Log.d("ClienteAlterar", "Erro na call de cliente");
            }
        });


        btnCadastrar = view.findViewById(R.id.btnConfirma);
        btnCancelar = view.findViewById(R.id.btnCancelar);

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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    LocalDate ld = LocalDate.parse(dataNasc, formatter);
                    Log.d("ClienteNovoFragment", "DATA: "+ ld);
                    if(ld.toString().equals(dataNasc)){
                        Log.d("ClienteNovoFragment","Data valida");
                    }else{
                        Log.d("ClienteNovoFragment","Data invalida");
                        new AlertDialog.Builder(getContext())
                                .setTitle("Cadastro de cliente")
                                .setMessage("Data de nascimento invalida!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } catch (DateTimeParseException e) {

                }
                Log.d("ClienteAlterarFragment","Data nasc: "+dataNasc);
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
                Log.d("ClienteAlterarFragment","Genero: "+genero);

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

                Log.d("ClienteAlterarFragment","B: "+b);
                if(b == true){
                    Cliente cliente = new Cliente(nome,cpf,email,telefone,dataNasc,genero);
                    Gson gson = new Gson();
                    String json = gson.toJson(cliente);
                    Log.d("ClienteNovoFragment","Cliente: "+json);

                    Call<Void> call = apiService.atualizarCliente(cpf,cliente);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("ClienteAlterarFragment","Response: "+ response.code() + response.errorBody());
                            if (response.isSuccessful()) {
                                Log.d("ClienteAlterarFragment", "Cliente Alterado com sucesso: " + response.body());

                                ClienteEndereco clienteEndereco  = new ClienteEndereco(cpf,rua,bairro,cidade,estado,cep);

                                String json2 = gson.toJson(clienteEndereco);
                                Log.d("ClienteNovoFragment","Endereco: "+json2);

                                Call <ClienteEndereco> call2 = apiService.atualizarClienteEndereco(cpf,clienteEndereco);
                                call2.enqueue(new Callback<ClienteEndereco>() {
                                    @Override
                                    public void onResponse(Call<ClienteEndereco> call, Response<ClienteEndereco> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("ClienteAlterarFragment", "Endereco alterado com sucesso: " + response.body());

                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("Cadastro de cliente")
                                                    .setMessage("Cliente alterado com sucesso!")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(getActivity(), SecundaryActivity.class);
                                                            intent.putExtra("fragment", "Cliente");
                                                            startActivity(intent);
                                                            getActivity().finish();
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
                        public void onFailure(Call<Void> call, Throwable t) {
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
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SecundaryActivity.class);
                intent.putExtra("fragment", "Cliente");
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
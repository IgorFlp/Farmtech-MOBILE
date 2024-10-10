package com.example.farmtech_mobile.ui.Cliente;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.farmtech_mobile.SecundaryActivity;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Cliente;
import com.example.farmtech_mobile.data.model.ClienteEndereco;
import com.example.farmtech_mobile.data.model.Fornecedor;
import com.example.farmtech_mobile.data.model.FornecedorEndereco;
import com.example.farmtech_mobile.databinding.FragmentClienteBinding;
import com.example.farmtech_mobile.databinding.FragmentLoginBinding;
import com.example.farmtech_mobile.ui.login.LoginViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClienteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClienteFragment extends Fragment {

    private FragmentClienteBinding binding;
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClienteBinding.inflate(inflater, container, false);
        return binding.getRoot();
        // Inflate the layout for this fragment

    }
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button btnNovoCliente = binding.btnNovoCliente;
        final Button btnAlterarCliente = binding.btnAlterarCliente;
        LinearLayout clienteLista = binding.clientesLista;

        ApiService apiService = RetrofitClient.getApiService();


        Call<List<Cliente>> call = apiService.getClientes();
        call.enqueue(new Callback<List<Cliente>>() {
                         @Override
                         public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                             List<Cliente> clientes = response.body();
                             if (response.isSuccessful()) {
                                 Gson gson = new Gson();
                                 String json = gson.toJson(clientes);
                                 Log.d("getClientes", "Clientes consultados com sucesso"+json);

                                 Call <List<ClienteEndereco>> call2 = apiService.getClientesEnderecos();
                                 call2.enqueue(new Callback<List<ClienteEndereco>>() {
                                     @Override
                                     public void onResponse(Call<List<ClienteEndereco>> call, Response<List<ClienteEndereco>> response) {
                                         List<ClienteEndereco> enderecos = response.body();
                                         if (response.isSuccessful()) {
                                             Gson gson = new Gson();
                                             String json = gson.toJson(enderecos);
                                             Log.d("getEnderecos", "Endereços de clientes consultados com sucesso"+json);
                                             int i = 0;
                                             Context contextLista = clienteLista.getContext();
                                             for(Cliente cliente: clientes){


                                                LinearLayout clienteContainer = new LinearLayout(contextLista);
                                                clienteContainer.setLayoutParams(new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                ));
                                                clienteContainer.setOrientation(LinearLayout.VERTICAL);
                                                clienteContainer.setTag("clienteContainer");
                                                clienteContainer.setId(View.generateViewId());
                                                int containerId = clienteContainer.getId();

                                                Context contextContainer = clienteContainer.getContext();

                                                 LinearLayout clienteRow = new LinearLayout(contextContainer);
                                                 clienteRow.setLayoutParams(new LinearLayout.LayoutParams(
                                                         LinearLayout.LayoutParams.MATCH_PARENT,
                                                         LinearLayout.LayoutParams.WRAP_CONTENT

                                                 ));
                                                 clienteRow.setOrientation(LinearLayout.HORIZONTAL);
                                                 clienteRow.setGravity(Gravity.CENTER_VERTICAL);
                                                 clienteRow.setTag("clienteRow");
                                                 clienteRow.setId(View.generateViewId());
                                                 int rowId = clienteRow.getId();
                                                 Context contextRow = clienteRow.getContext();

                                                 CheckBox cbCliente = new CheckBox(contextRow);
                                                 cbCliente.setLayoutParams(new LinearLayout.LayoutParams(
                                                         LinearLayout.LayoutParams.WRAP_CONTENT,
                                                         LinearLayout.LayoutParams.WRAP_CONTENT));
                                                 cbCliente.setTag("cbCliente");
                                                 cbCliente.setId(View.generateViewId());


                                                 TextView lblCliente = new TextView(contextRow);
                                                 LinearLayout.LayoutParams  lblClienteParams = new LinearLayout.LayoutParams(
                                                         dpToPx(257), // largura em pixels
                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
                                                 lblClienteParams.setMargins(20, 0, 0, 0);
                                                 lblCliente.setLayoutParams(lblClienteParams);
                                                 lblCliente.setText(cliente.getNome());
                                                 lblCliente.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                                 lblCliente.setTextColor(Color.parseColor("#242424"));
                                                 lblCliente.setId(View.generateViewId());
                                                 lblCliente .setTag("clienteLabel");

                                                 ImageView dropdownButton = new ImageView(contextRow);
                                                 LinearLayout.LayoutParams dropdownLayoutParams = new LinearLayout.LayoutParams(
                                                         dpToPx(48), // largura em pixels
                                                         dpToPx(48));
                                                 dropdownLayoutParams.setMargins(dpToPx(40), 0, 0, 0);
                                                 dropdownButton.setLayoutParams(dropdownLayoutParams);
                                                 dropdownButton.setImageResource(R.mipmap.dropdown_foreground);
                                                 dropdownButton.setTag("dropdownButton");
                                                 dropdownButton.setId(View.generateViewId());

                                                 clienteRow.addView(cbCliente);
                                                 clienteRow.addView(lblCliente);
                                                 clienteRow.addView(dropdownButton);

                                                 LinearLayout clienteData = new LinearLayout(contextContainer);
                                                 clienteData.setLayoutParams(new LinearLayout.LayoutParams(
                                                         LinearLayout.LayoutParams.MATCH_PARENT,
                                                         LinearLayout.LayoutParams.WRAP_CONTENT));
                                                 clienteData.setOrientation(LinearLayout.VERTICAL);
                                                 clienteData.setTag("clienteData");
                                                 clienteData.setVisibility(View.GONE);
                                                 Context contextData = clienteData.getContext();

                                                 // Crie o TextView para os dados do cliente
                                                 TextView dadosCliente = new TextView(contextData);
                                                 dadosCliente.setLayoutParams(new LinearLayout.LayoutParams(
                                                         LinearLayout.LayoutParams.WRAP_CONTENT,
                                                         LinearLayout.LayoutParams.WRAP_CONTENT));
                                                 String clienteJson = gson.toJson(cliente);
                                                 dadosCliente.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                                 dadosCliente.setTag(cliente.getCpf());
                                                 dadosCliente.setText("Nome: "+cliente.getNome()+"\nCPF: "+cliente.getCpf()+"\nTelefone: "+cliente.getTelefone()+"\nEmail: "+cliente.getEmail()+"\nNascimento: "+cliente.getDataNasc()+"\nGenero: "+cliente.getGenero()+"\n");
                                                 clienteData.addView(dadosCliente);

                                                 TextView dadosEndereco = new TextView(contextData);
                                                 dadosEndereco.setLayoutParams(new LinearLayout.LayoutParams(
                                                         LinearLayout.LayoutParams.WRAP_CONTENT,
                                                         LinearLayout.LayoutParams.WRAP_CONTENT));
                                                 String enderecoJson = gson.toJson(enderecos.get(i));
                                                 dadosEndereco.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                                 dadosEndereco.setText("Rua: "+enderecos.get(i).getRua()+"\nBairro: "+enderecos.get(i).getBairro()+"\nCidade: "+enderecos.get(i).getCidade()+"\nEstado: "+enderecos.get(i).getEstado()+"\nCEP: "+enderecos.get(i).getCep()+"\n");
                                                 clienteData.addView(dadosEndereco);


                                                clienteContainer.addView(clienteRow);
                                                clienteContainer.addView(clienteData);

                                                clienteLista.addView(clienteContainer);

                                                i++;
                                             }
                                             criaListeners();
                                         }
                                     }
                                     @Override
                                     public void onFailure(Call<List<ClienteEndereco>> call, Throwable t) {
                                         Log.e("getClienteEndereco", "Erro: " + t.getMessage());
                                     }
                                 });
                             }
                         }
                        @Override
                        public void onFailure(Call<List<Cliente>> call, Throwable t) {
                            Log.e("getCliente", "Erro: " + t.getMessage());
                        }
                     });


        btnNovoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment", "ClienteNovo");
                startActivity(intent);
                getActivity().finish();
            }
        });
        Button btnDeleteCliente = binding.btnDeleteCliente;
        btnDeleteCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;

                for (int i = 0; i < clienteLista.getChildCount(); i++) {
                    View container = clienteLista.getChildAt(i);
                    View row = container.findViewWithTag("clienteRow");
                    CheckBox cb = row.findViewWithTag("cbCliente");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout clienteContainer = (LinearLayout) selecionado.getParent().getParent();
                    LinearLayout clienteData = clienteContainer.findViewWithTag("clienteData");
                    String cpf = (String) clienteData.getChildAt(0).getTag();
                    Log.d("Delete", "CPF Selecionado: " + cpf);

                    Call<ClienteEndereco> deleteEndereco = apiService.deleteClienteEndereco(cpf);
                    deleteEndereco.enqueue(new Callback<ClienteEndereco>() {
                        @Override
                        public void onResponse(Call<ClienteEndereco> delete, Response<ClienteEndereco> response) {
                            if (response.isSuccessful()) {
                                Log.d("EnderecoDelete", "Endereco deletado com sucesso ");
                                Call<Cliente> deleteCliente = apiService.deleteCliente(cpf);
                                deleteCliente.enqueue(new Callback<Cliente>() {
                                    @Override
                                    public void onResponse(Call<Cliente> delete, Response<Cliente> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("ClienteDelete", "Cliente deletado com sucesso ");
                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("Cadastro de cliente")
                                                    .setMessage("Cliente deletado com sucesso!")
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                                            navController.navigate(R.id.nav_Cliente);
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Cliente> delete, Throwable t) {
                                        Log.e("ClienteDelete, ", "Erro: " + t.getMessage());
                                    };
                                });
                            }
                        }
                        @Override
                        public void onFailure(Call<ClienteEndereco> delete, Throwable t) {
                            Log.e("EnderecoDelete", "Erro: " + t.getMessage());
                        };;
                });
                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de cliente")
                            .setMessage("Selecione uma checkbox!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                    navController.navigate(R.id.nav_Cliente);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                }
            });

        btnAlterarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;
                for (int i = 0; i < clienteLista.getChildCount(); i++) {
                    View container = clienteLista.getChildAt(i);
                    View row = container.findViewWithTag("clienteRow");
                    CheckBox cb = row.findViewWithTag("cbCliente");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout clienteContainer = (LinearLayout) selecionado.getParent().getParent();
                    LinearLayout clienteData = clienteContainer.findViewWithTag("clienteData");
                    String cpf = (String) clienteData.getChildAt(0).getTag();
                    Log.d("Alterar", "CPF Selecionado: " + cpf);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("fragment", "ClienteAlterar");
                    intent.putExtra("cpf", cpf);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de cliente")
                            .setMessage("Selecione uma checkbox!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                    navController.navigate(R.id.nav_Cliente);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
    }
    private void criaListeners() {
        LinearLayout clienteLista = binding.clientesLista;
        View.OnClickListener dropdownListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout clienteRow = (LinearLayout) v.getParent();
                LinearLayout container = (LinearLayout) clienteRow.getParent();
                ImageView dropdownButton = container.findViewWithTag("dropdownButton");
                String tag = "clienteData";
                LinearLayout clienteData = container.findViewWithTag(tag);

                if (clienteData.getVisibility() == View.GONE) {
                    clienteData.setVisibility(View.VISIBLE);
                    dropdownButton.setImageResource(R.mipmap.dropup_foreground);
                } else {
                    clienteData.setVisibility(View.GONE);
                    dropdownButton.setImageResource(R.mipmap.dropdown_foreground);
                }
                int clienteId = container.getId();
            }
        };
        List<CheckBox> checkBoxes = new ArrayList<>();
        LinearLayout clienteRow = binding.clientesLista.findViewWithTag("clienteRow");
        for (int i = 0; i < clienteLista.getChildCount(); i++) {
            View container = clienteLista.getChildAt(i);
            View row = container.findViewWithTag("clienteRow");
            CheckBox cb = row.findViewWithTag("cbCliente");
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


        for (int i = 0; i < clienteLista.getChildCount(); i++) {

            String tag = "dropdownButton";

            ImageView dropdownButton = clienteLista.getChildAt(i).findViewWithTag(tag);

            dropdownButton.setOnClickListener(dropdownListener);
        }

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
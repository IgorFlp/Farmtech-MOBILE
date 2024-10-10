package com.example.farmtech_mobile.ui.usuarios;

import androidx.lifecycle.ViewModelProvider;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.farmtech_mobile.MainActivity;
import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Usuario;
import com.example.farmtech_mobile.databinding.FragmentUsuarioBinding;
import com.example.farmtech_mobile.ui.fornecedor.FornecedorFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioFragment extends Fragment {

    private UsuarioViewModel mViewModel;
    private FragmentUsuarioBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUsuarioBinding.inflate(inflater, container, false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_usuario, container, false);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button btnNovoUsuario = binding.btnNovoUsuario;
        final Button btnAlterarUsuario = binding.btnAlterarUsuario;

        LinearLayout campos = binding.camposCadastroLogin;
        campos.setVisibility(View.GONE);
        TextView txtLoginUsuario = binding.txtLoginUsuario;
        TextView txtSenhaUsuario = binding.txtSenhaUsuario;
        TextView txtNomeUsuario = binding.txtNomeUsuario;
        Spinner slcCargo = binding.slcCargo;
        Button btnSalvar = binding.btnSalvar;
        Button btnEsconder = binding.btnEsconder;

        LinearLayout usuarioLista = binding.usuariosLista;

        ApiService apiService = RetrofitClient.getApiService();


        Call<List<Usuario>> call = apiService.getUsuarios();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> usuarios = response.body();
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(usuarios);
                    Log.d("getUsuarios", "Usuarios consultados com sucesso"+json);

                    Context contextLista = usuarioLista.getContext();
                    for(Usuario usuario: usuarios){
                        LinearLayout usuarioContainer = new LinearLayout(contextLista);
                        usuarioContainer.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        usuarioContainer.setOrientation(LinearLayout.VERTICAL);
                        usuarioContainer.setTag("usuarioContainer");
                        usuarioContainer.setId(View.generateViewId());
                        int containerId = usuarioContainer.getId();

                        Context contextContainer = usuarioContainer.getContext();

                        LinearLayout usuarioRow = new LinearLayout(contextContainer);
                        usuarioRow.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT

                        ));
                        usuarioRow.setOrientation(LinearLayout.HORIZONTAL);
                        usuarioRow.setGravity(Gravity.CENTER_VERTICAL);
                        usuarioRow.setTag("usuarioRow");
                        usuarioRow.setId(View.generateViewId());
                        int rowId = usuarioRow.getId();
                        Context contextRow = usuarioRow.getContext();

                        CheckBox cbUsuario = new CheckBox(contextRow);
                        cbUsuario.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        cbUsuario.setTag("cbUsuario");
                        cbUsuario.setId(View.generateViewId());


                        TextView lblUsuario = new TextView(contextRow);
                        LinearLayout.LayoutParams  lblUsuarioParams = new LinearLayout.LayoutParams(
                                dpToPx(257), // largura em pixels
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        lblUsuarioParams.setMargins(20, 0, 0, 0);
                        lblUsuario.setLayoutParams(lblUsuarioParams);
                        lblUsuario.setText(usuario.getLogin());
                        lblUsuario.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        lblUsuario.setTextColor(Color.parseColor("#242424"));
                        lblUsuario.setId(View.generateViewId());
                        lblUsuario .setTag("usuarioLabel");

                        ImageView dropdownButton = new ImageView(contextRow);
                        LinearLayout.LayoutParams dropdownLayoutParams = new LinearLayout.LayoutParams(
                                dpToPx(48), // largura em pixels
                                dpToPx(48));
                        dropdownLayoutParams.setMargins(dpToPx(40), 0, 0, 0);
                        dropdownButton.setLayoutParams(dropdownLayoutParams);
                        dropdownButton.setImageResource(R.mipmap.dropdown_foreground);
                        dropdownButton.setTag("dropdownButton");
                        dropdownButton.setId(View.generateViewId());

                        usuarioRow.addView(cbUsuario);
                        usuarioRow.addView(lblUsuario);
                        usuarioRow.addView(dropdownButton);

                        LinearLayout usuarioData = new LinearLayout(contextContainer);
                        usuarioData.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        usuarioData.setOrientation(LinearLayout.VERTICAL);
                                   usuarioData.setTag("usuarioData");
                                   usuarioData.setVisibility(View.GONE);
                                   Context contextData = usuarioData.getContext();

                                    // Crie o TextView para os dados do usuario
                        TextView dadosUsuario = new TextView(contextData);
                        dadosUsuario.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        String usuarioJson = gson.toJson(usuario);
                        dadosUsuario.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        dadosUsuario.setTag(usuario.getId());
                        dadosUsuario.setText("Usuario: "+usuario.getLogin() +"\nSenha: "+usuario.getSenha()+"\nNome: "+usuario.getNome()+"\nCargo: "+usuario.getCargo()+"\n");
                        usuarioData.addView(dadosUsuario);


                        usuarioContainer.addView(usuarioRow);
                        usuarioContainer.addView(usuarioData);

                        usuarioLista.addView(usuarioContainer);

                    }
                    criaListeners();
                }
            };
            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.e("getUsuario", "Erro: " + t.getMessage());
            }
        });
        String[] modo = new String[1];
        btnNovoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout campos = binding.camposCadastroLogin;
                modo[0] = "Novo";
                campos.setVisibility(View.VISIBLE);
                txtLoginUsuario.setText("");
                txtSenhaUsuario.setText("");
                txtNomeUsuario.setText("");
                slcCargo.setSelection(0);
            }
        });
        Button btnDeleteUsuario = binding.btnDeleteUsuario;
        btnDeleteUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;

                for (int i = 0; i < usuarioLista.getChildCount(); i++) {
                    View container = usuarioLista.getChildAt(i);
                    View row = container.findViewWithTag("usuarioRow");
                    CheckBox cb = row.findViewWithTag("cbUsuario");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout usuarioContainer = (LinearLayout) selecionado.getParent().getParent();
                    LinearLayout usuarioData = usuarioContainer.findViewWithTag("usuarioData");
                    Integer id = (Integer) usuarioData.getChildAt(0).getTag();
                    Log.d("Delete", "ID Selecionado: " + id);

                    Call<Usuario> deleteUsuario = apiService.deleteUsuario(id);
                    deleteUsuario.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> delete, Response<Usuario> response) {
                            if (response.isSuccessful()) {
                                Log.d("UsuarioDelete", "Usuario deletado com sucesso ");
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Cadastro de usuario")
                                        .setMessage("Usuario deletado com sucesso!")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                                navController.navigate(R.id.nav_usuario);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Usuario> delete, Throwable t) {
                            Log.e("UsuarioDelete, ", "Erro: " + t.getMessage());
                        };
                    });


                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de usuario")
                            .setMessage("Selecione uma checkbox!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                    navController.navigate(R.id.nav_usuario);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        btnAlterarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;
                for (int i = 0; i < usuarioLista.getChildCount(); i++) {
                    View container = usuarioLista.getChildAt(i);
                    View row = container.findViewWithTag("usuarioRow");
                    CheckBox cb = row.findViewWithTag("cbUsuario");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout usuarioContainer = (LinearLayout) selecionado.getParent().getParent();
                    LinearLayout usuarioData = usuarioContainer.findViewWithTag("usuarioData");
                    Integer id= (Integer) usuarioData.getChildAt(0).getTag();
                    Log.d("Alterar", "ID Selecionado: " + id);

                    LinearLayout campos = binding.camposCadastroLogin;
                    modo[0] = "Alterar";
                    campos.setVisibility(View.VISIBLE);
                    Call<Usuario> usuarioCall = apiService.getUsuario(id);
                    usuarioCall.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                if(response.isSuccessful()){
                                    Usuario usuario = response.body();
                                    txtLoginUsuario.setText(usuario.getLogin());
                                    txtLoginUsuario.setTag(usuario.getId());
                                    txtSenhaUsuario.setText(usuario.getSenha());
                                    txtNomeUsuario.setText(usuario.getNome());

                                    String[] cargosArray = getResources().getStringArray(R.array.cargo_spinner);
                                    List<String> cargosList = new ArrayList<>(Arrays.asList(cargosArray));
                                    ArrayAdapter<String> adapterCargo = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cargosList);

                                    slcCargo.setSelection(adapterCargo.getPosition(usuario.getCargo()));
                                }
                        }
                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Log.d("UsuarioFragment", "Erro: "+t.getMessage());
                        }
                    });

                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de usuario")
                            .setMessage("Selecione uma checkbox!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                    navController.navigate(R.id.nav_usuario);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modo[0].equals("Novo")){
                    Log.d("UsuarioFragment", "MODO NOVO");
                    String login = txtLoginUsuario.getText().toString();
                    String senha = txtSenhaUsuario.getText().toString();
                    String nome = txtNomeUsuario.getText().toString();
                    String cargo = slcCargo.getSelectedItem().toString();

                    Usuario usuario = new Usuario(0,login,senha,cargo,nome);
                    Call<Usuario> novoUsuario = apiService.criarUsuario(usuario);
                    novoUsuario.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if(response.isSuccessful()){
                                Log.d("UsuarioFragment", "Usuario criado com sucesso");
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Cadastro de usuario")
                                        .setMessage("Usuario criado com sucesso!")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                                navController.navigate(R.id.nav_usuario);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Log.d("UsuarioFragment", "Erro: "+t.getMessage());
                        }
                    });
                } else if (modo[0].equals("Alterar")) {
                    Log.d("UsuarioFragment", "MODO ALTERAR");
                    String login = txtLoginUsuario.getText().toString();
                    String senha = txtSenhaUsuario.getText().toString();
                    String nome =  txtNomeUsuario.getText().toString();
                    String cargo = slcCargo.getSelectedItem().toString();
                    Integer id = (Integer) txtLoginUsuario.getTag();

                    Usuario usuario = new Usuario(id,login,senha,cargo,nome);
                    Call<Usuario> alterarUsuario = apiService.atualizarUsuario(id,usuario);
                    alterarUsuario.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if(response.isSuccessful()){
                                Log.d("UsuarioFragment", "Usuario alterado com sucesso");
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Cadastro de usuario")
                                        .setMessage("Usuario alterado com sucesso!")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                                navController.navigate(R.id.nav_usuario);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Log.d("UsuarioFragment", "Erro: "+t.getMessage());
                        }
                    });
                }
            }
        });
        btnEsconder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               LinearLayout campos = binding.camposCadastroLogin;
               txtLoginUsuario.setText("");
               txtSenhaUsuario.setText("");
               txtNomeUsuario.setText("");
               slcCargo.setSelection(0);
               campos.setVisibility(View.GONE);
           }
        });
    };
    private void criaListeners() {
        LinearLayout usuarioLista = binding.usuariosLista;
        View.OnClickListener dropdownListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout usuarioRow = (LinearLayout) v.getParent();
                LinearLayout container = (LinearLayout) usuarioRow.getParent();
                ImageView dropdownButton = container.findViewWithTag("dropdownButton");
                String tag = "usuarioData";
                LinearLayout usuarioData = container.findViewWithTag(tag);

                if (usuarioData.getVisibility() == View.GONE) {
                    usuarioData.setVisibility(View.VISIBLE);
                    dropdownButton.setImageResource(R.mipmap.dropup_foreground);
                } else {
                    usuarioData.setVisibility(View.GONE);
                    dropdownButton.setImageResource(R.mipmap.dropdown_foreground);
                }
                int usuarioId = container.getId();
            }
        };
        List<CheckBox> checkBoxes = new ArrayList<>();
        LinearLayout usuarioRow = binding.usuariosLista.findViewWithTag("usuarioRow");
        for (int i = 0; i < usuarioLista.getChildCount(); i++) {
            View container = usuarioLista.getChildAt(i);
            View row = container.findViewWithTag("usuarioRow");
            CheckBox cb = row.findViewWithTag("cbUsuario");
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


        for (int i = 0; i < usuarioLista.getChildCount(); i++) {

            String tag = "dropdownButton";

            ImageView dropdownButton = usuarioLista.getChildAt(i).findViewWithTag(tag);

            dropdownButton.setOnClickListener(dropdownListener);
        }

    }
    public UsuarioFragment() {
        // Required empty public constructor
    }

    public static UsuarioFragment newInstance() {
        UsuarioFragment fragment = new UsuarioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
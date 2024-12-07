package com.example.farmtech_mobile.ui.produto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Estoque;
import com.example.farmtech_mobile.data.model.Produto;
import com.example.farmtech_mobile.databinding.FragmentProdutoBinding;
import com.example.farmtech_mobile.ui.produto.ProdutoFragment;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProdutoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProdutoFragment extends Fragment {     
    
    private FragmentProdutoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProdutoBinding.inflate(inflater, container, false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_produto, container, false);
    }
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Button btnNovoProduto = binding.btnNovoProduto;
        final Button btnAlterarProduto = binding.btnAlterarProduto;

        LinearLayout campos = binding.camposCadastroProduto;
        campos.setVisibility(View.GONE);

        EditText txtNomeProduto = binding.txtNomeProduto;
        EditText txtPreco = binding.txtPreco;
        Spinner slcUnidade = binding.slcUnidade;

        Button btnSalvar = binding.btnSalvar;
        Button btnEsconder = binding.btnEsconder;

        LinearLayout produtoLista = binding.produtosLista;

        ApiService apiService = RetrofitClient.getApiService();


        Call<List<Produto>> call = apiService.getProdutos();
        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                List<Produto> produtos = response.body();
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(produtos);
                    Log.d("getProdutos", "Produtos consultados com sucesso"+json);

                    Context contextLista = produtoLista.getContext();
                    for(Produto produto: produtos){
                        LinearLayout produtoContainer = new LinearLayout(contextLista);
                        produtoContainer.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        ));
                        produtoContainer.setOrientation(LinearLayout.VERTICAL);
                        produtoContainer.setTag("produtoContainer");
                        produtoContainer.setId(View.generateViewId());
                        int containerId = produtoContainer.getId();

                        Context contextContainer = produtoContainer.getContext();

                        LinearLayout produtoRow = new LinearLayout(contextContainer);
                        produtoRow.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT

                        ));
                        produtoRow.setOrientation(LinearLayout.HORIZONTAL);
                        produtoRow.setGravity(Gravity.CENTER_VERTICAL);
                        produtoRow.setTag("produtoRow");
                        produtoRow.setId(View.generateViewId());
                        int rowId = produtoRow.getId();
                        Context contextRow = produtoRow.getContext();

                        CheckBox cbProduto = new CheckBox(contextRow);
                        cbProduto.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        cbProduto.setTag("cbProduto");
                        cbProduto.setId(View.generateViewId());


                        TextView lblProduto = new TextView(contextRow);
                        LinearLayout.LayoutParams  lblProdutoParams = new LinearLayout.LayoutParams(
                                dpToPx(257), // largura em pixels
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        lblProdutoParams.setMargins(20, 0, 0, 0);
                        lblProduto.setLayoutParams(lblProdutoParams);
                        lblProduto.setText(produto.getNome());
                        lblProduto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        lblProduto.setTextColor(Color.parseColor("#242424"));
                        lblProduto.setId(View.generateViewId());
                        lblProduto .setTag("produtoLabel");

                        ImageView dropdownButton = new ImageView(contextRow);
                        LinearLayout.LayoutParams dropdownLayoutParams = new LinearLayout.LayoutParams(
                                dpToPx(48), // largura em pixels
                                dpToPx(48));
                        dropdownLayoutParams.setMargins(dpToPx(40), 0, 0, 0);
                        dropdownButton.setLayoutParams(dropdownLayoutParams);
                        dropdownButton.setImageResource(R.mipmap.dropdown_foreground);
                        dropdownButton.setTag("dropdownButton");
                        dropdownButton.setId(View.generateViewId());

                        produtoRow.addView(cbProduto);
                        produtoRow.addView(lblProduto);
                        produtoRow.addView(dropdownButton);

                        LinearLayout produtoData = new LinearLayout(contextContainer);
                        produtoData.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        produtoData.setOrientation(LinearLayout.VERTICAL);
                        produtoData.setTag("produtoData");
                        produtoData.setVisibility(View.GONE);
                        Context contextData = produtoData.getContext();

                        // Crie o TextView para os dados do produto
                        TextView dadosProduto = new TextView(contextData);
                        dadosProduto.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        String produtoJson = gson.toJson(produto);
                        dadosProduto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        dadosProduto.setTag(produto.getId());
                        dadosProduto.setText("Nome: "+produto.getNome()   +"\nPreço: "+produto.getPrecoUn()+"\nUnidade: "+produto.getUnMedida()+"\n");
                        produtoData.addView(dadosProduto);


                        produtoContainer.addView(produtoRow);
                        produtoContainer.addView(produtoData);

                        produtoLista.addView(produtoContainer);

                    }
                    criaListeners();
                }
            };
            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                Log.e("getProduto", "Erro: " + t.getMessage());
            }
        });
        String[] modo = new String[1];
        btnNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout campos = binding.camposCadastroProduto;
                modo[0] = "Novo";
                campos.setVisibility(View.VISIBLE);
                txtNomeProduto.setText("");
                txtPreco.setText("");
                slcUnidade.setSelection(0);
            }
        });

        Button btnDeleteProduto = binding.btnDeleteProduto;
        btnDeleteProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;

                for (int i = 0; i < produtoLista.getChildCount(); i++) {
                    View container = produtoLista.getChildAt(i);
                    View row = container.findViewWithTag("produtoRow");
                    CheckBox cb = row.findViewWithTag("cbProduto");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout produtoContainer = (LinearLayout) selecionado.getParent().getParent();
                    LinearLayout produtoData = produtoContainer.findViewWithTag("produtoData");
                    Integer id = (Integer) produtoData.getChildAt(0).getTag();
                    Log.d("Delete", "ID Selecionado: " + id);
                    Call <Estoque> deleteEstoque = apiService.deleteEstoque(id);
                    deleteEstoque.enqueue(new Callback<Estoque>() {
                        @Override
                        public void onResponse(Call<Estoque> call, Response<Estoque> response) {
                            Call<Produto> deleteProduto = apiService.deleteProduto(id);
                            deleteProduto.enqueue(new Callback<Produto>() {
                                @Override
                                public void onResponse(Call<Produto> delete, Response<Produto> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("ProdutoDelete", "Produto deletado com sucesso ");
                                        new AlertDialog.Builder(getContext())
                                                .setTitle("Cadastro de produto")
                                                .setMessage("Produto deletado com sucesso!")
                                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                                        navController.navigate(R.id.nav_produto);
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Produto> delete, Throwable t) {
                                    Log.e("ProdutoDelete, ", "Erro: " + t.getMessage());
                                };
                            });
                        }
                        @Override
                        public void
                        onFailure(Call<Estoque> call, Throwable t) {

                        }
                    });
                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de produto")
                            .setMessage("Selecione uma checkbox!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                    navController.navigate(R.id.nav_produto);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        btnAlterarProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;
                for (int i = 0; i < produtoLista.getChildCount(); i++) {
                    View container = produtoLista.getChildAt(i);
                    View row = container.findViewWithTag("produtoRow");
                    CheckBox cb = row.findViewWithTag("cbProduto");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout produtoContainer = (LinearLayout) selecionado.getParent().getParent();
                    LinearLayout produtoData = produtoContainer.findViewWithTag("produtoData");
                    Integer id= (Integer) produtoData.getChildAt(0).getTag();
                    Log.d("Alterar", "ID Selecionado: " + id);

                    LinearLayout campos = binding.camposCadastroProduto;
                    modo[0] = "Alterar";
                    campos.setVisibility(View.VISIBLE);
                    Call<Produto> produtoCall = apiService.getProduto(id);
                    produtoCall.enqueue(new Callback<Produto>() {
                        @Override
                        public void onResponse(Call<Produto> call, Response<Produto> response) {
                            if(response.isSuccessful()){
                                Produto produto = response.body();
                                txtNomeProduto.setText(produto.getNome());
                                txtNomeProduto.setTag(produto.getId());
                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                String valorFormatado = decimalFormat.format(produto.getPrecoUn());
                                txtPreco.setText(valorFormatado);


                                String[] unidadeArray = getResources().getStringArray(R.array.unidade_spinner);
                                List<String>  unidadeList = new ArrayList<>(Arrays.asList(unidadeArray));
                                ArrayAdapter<String> adapterUnidade = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, unidadeList);

                                slcUnidade.setSelection(adapterUnidade.getPosition(produto.getUnMedida()));
                            }
                        }
                        @Override
                        public void onFailure(Call<Produto> call, Throwable t) {
                            Log.d("ProdutoFragment", "Erro: "+t.getMessage());
                        }
                    });

                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de produto")
                            .setMessage("Selecione uma checkbox!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                    navController.navigate(R.id.nav_produto);
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
                boolean b = txtNomeProduto.getText() != null && !txtNomeProduto.getText().toString().isEmpty() &&
                        txtPreco.getText() != null && !txtPreco.getText().toString().isEmpty() &&
                        slcUnidade.getSelectedItem() != null && !slcUnidade.getSelectedItem().toString().isEmpty();

                if(b == true) {
                    if (modo[0].equals("Novo")) {
                        Log.d("ProdutoFragment", "MODO NOVO");
                        String nome = txtNomeProduto.getText().toString();


                        double precoUn = Double.parseDouble(txtPreco.getText().toString().replace(",", "."));

                        String unidade = slcUnidade.getSelectedItem().toString();

                        Produto produto = new Produto(nome, unidade, precoUn);

                        Call<Produto> novoProduto = apiService.criarProduto(produto);
                        novoProduto.enqueue(new Callback<Produto>() {
                            @Override
                            public void onResponse(Call<Produto> call, Response<Produto> response) {
                                if (response.isSuccessful()) {
                                    Produto produto = response.body();
                                    String json = new Gson().toJson(produto);
                                    Log.d("Response", "Response Produto: " + json);

                                /*
                                Estoque estoque = new Estoque(produto.getId(),100);
                                Log.d("ProdutoFragment", "Produto criado com sucesso");
                                String json2 = new Gson().toJson(estoque);
                                Log.d("ProdutoFragment", "Estoque: "+json2);
                                Call<Estoque> novoEstoque = apiService.criarEstoque(estoque);
                                novoEstoque.enqueue(new Callback<Estoque>() {
                                    @Override
                                    public void onResponse(Call<Estoque> call, Response<Estoque> response) {
                                        if(response.isSuccessful()){*/
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Cadastro de produto")
                                            .setMessage("Produto criado com sucesso!")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_secundary);
                                                    navController.navigate(R.id.nav_produto);
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                                    /*}
                                    @Override
                                    public void onFailure(Call<Estoque> call, Throwable t) {
                                        Log.d("ProdutoFragment", "Erro Estoque: "+t.getMessage());
                                    }
                                });
                                }*/
                            }

                            @Override
                            public void onFailure(Call<Produto> call, Throwable t) {
                                Log.d("ProdutoFragment", "Erro: " + t.getMessage());
                            }
                        });
                    } else if (modo[0].equals("Alterar")) {
                        Log.d("ProdutoFragment", "MODO ALTERAR");
                        String nome = txtNomeProduto.getText().toString();
                        String preco = txtPreco.getText().toString().replaceAll(",", ".");

                        String unidade = slcUnidade.getSelectedItem().toString();
                        Integer id = (Integer) txtNomeProduto.getTag();

                        Produto produto = new Produto(nome, unidade, Double.parseDouble(preco));
                        produto.setId(id);
                        Log.d("ProdutoFragment", "Produto: "+produto);
                        Call<Produto> alterarProduto = apiService.atualizarProduto(id, produto);
                        alterarProduto.enqueue(new Callback<Produto>() {
                            @Override
                            public void onResponse(Call<Produto> call, Response<Produto> response) {
                                if (response.isSuccessful()) {
                                    Log.d("ProdutoFragment", "Produto alterado com sucesso");
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Cadastro de produto")
                                            .setMessage("Produto alterado com sucesso!")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_secundary);
                                                    navController.navigate(R.id.nav_produto);
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Produto> call, Throwable t) {
                                Log.d("ProdutoFragment", "Erro: " + t.getMessage());
                            }
                        });
                    }
                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Cadastro de produto")
                            .setMessage("Preencha todos os campos!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        btnEsconder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout campos = binding.camposCadastroProduto;
                txtNomeProduto.setText("");
                txtPreco.setText("");
                slcUnidade.setSelection(0);
                campos.setVisibility(View.GONE);
            }
        });
    };
    private void criaListeners() {
        LinearLayout produtoLista = binding.produtosLista;
        View.OnClickListener dropdownListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout produtoRow = (LinearLayout) v.getParent();
                LinearLayout container = (LinearLayout) produtoRow.getParent();
                ImageView dropdownButton = container.findViewWithTag("dropdownButton");
                String tag = "produtoData";
                LinearLayout produtoData = container.findViewWithTag(tag);

                if (produtoData.getVisibility() == View.GONE) {
                    produtoData.setVisibility(View.VISIBLE);
                    dropdownButton.setImageResource(R.mipmap.dropup_foreground);
                } else {
                    produtoData.setVisibility(View.GONE);
                    dropdownButton.setImageResource(R.mipmap.dropdown_foreground);
                }
                int produtoId = container.getId();
            }
        };
        List<CheckBox> checkBoxes = new ArrayList<>();
        LinearLayout produtoRow = binding.produtosLista.findViewWithTag("produtoRow");
        for (int i = 0; i < produtoLista.getChildCount(); i++) {
            View container = produtoLista.getChildAt(i);
            View row = container.findViewWithTag("produtoRow");
            CheckBox cb = row.findViewWithTag("cbProduto");
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


        for (int i = 0; i < produtoLista.getChildCount(); i++) {

            String tag = "dropdownButton";

            ImageView dropdownButton = produtoLista.getChildAt(i).findViewWithTag(tag);

            dropdownButton.setOnClickListener(dropdownListener);
        }

    }
    public ProdutoFragment() {
        // Required empty public constructor
    }

    public static ProdutoFragment newInstance() {
        ProdutoFragment fragment = new ProdutoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
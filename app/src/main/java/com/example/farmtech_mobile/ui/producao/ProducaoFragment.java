package com.example.farmtech_mobile.ui.producao;

import androidx.lifecycle.ViewModelProvider;

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

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.farmtech_mobile.data.model.Producao;
import com.example.farmtech_mobile.data.model.Produto;
import com.example.farmtech_mobile.data.model.SpinnerItem;
import com.example.farmtech_mobile.databinding.FragmentProducaoBinding;
import com.example.farmtech_mobile.ui.producao.ProducaoFragment;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducaoFragment extends Fragment {

    private ProducaoViewModel mViewModel;
    private FragmentProducaoBinding binding;
    private ApiService apiService = RetrofitClient.getApiService();

    public static ProducaoFragment newInstance() {
        return new ProducaoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProducaoBinding.inflate(inflater, container, false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_producao, container, false);
    }
    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Button btnNovoProduto = binding.btnNovoProduto;
        LinearLayout campos = binding.camposAdicionarProduto;
        campos.setVisibility(View.GONE);

        EditText txtQuant = binding.txtQuant;
        Spinner slcProduto = binding.slcProduto;
        criaProdutos(slcProduto, view);

        Button btnSalvar = binding.btnSalvar;
        Button btnEsconder = binding.btnEsconder;
        Button btnConfirma = binding.btnConfirma;
        Button btnCancelar = binding.btnCancelar;

        LinearLayout producaoLista = binding.producaoLista;

        //ApiService apiService = RetrofitClient.getApiService();

        btnNovoProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout campos = binding.camposAdicionarProduto;
                campos.setVisibility(View.VISIBLE);
                txtQuant.setText("");
                //slcProduto.setSelection(0);
                slcProduto.setEnabled(true);
            }
        });

        Button btnDeleteProduto = binding.btnDeleteProduto;
        btnDeleteProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox selecionado = null;

                for (int i = 0; i < producaoLista.getChildCount(); i++) {
                    View container = producaoLista.getChildAt(i);
                    View row = container.findViewWithTag("produtoRow");
                    CheckBox cb = row.findViewWithTag("cbProduto");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout producaoContainer = (LinearLayout) selecionado.getParent().getParent();
                    TextView lblProduto = producaoContainer.findViewWithTag("lblProduto");
                    Integer id = (Integer) lblProduto.getId();
                    Log.d("Delete", "ID Selecionado: " + id);

                    LinearLayout container = (LinearLayout) selecionado.getParent().getParent();
                    producaoLista.removeView(container);

                }
            }
        });
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerItem item = (SpinnerItem) slcProduto.getSelectedItem();


                Context contextLista = producaoLista.getContext();
                LinearLayout producaoContainer = new LinearLayout(contextLista);
                producaoContainer.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                producaoContainer.setOrientation(LinearLayout.VERTICAL);
                producaoContainer.setTag("produtoContainer");
                producaoContainer.setId(View.generateViewId());
                int containerId = producaoContainer.getId();

                Context contextContainer = producaoContainer.getContext();
                LinearLayout producaoRow = new LinearLayout(contextContainer);
                producaoRow.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT

                ));
                producaoRow.setOrientation(LinearLayout.HORIZONTAL);
                producaoRow.setGravity(Gravity.CENTER_VERTICAL);
                producaoRow.setTag("produtoRow");
                producaoRow.setId(View.generateViewId());
                int rowId = producaoRow.getId();
                Context contextRow = producaoRow.getContext();

                CheckBox cbProducao = new CheckBox(contextRow);
                cbProducao.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                cbProducao.setTag("cbProduto");
                cbProducao.setId(View.generateViewId());

                TextView lblProduto = new TextView(contextRow);
                LinearLayout.LayoutParams  lblProdutoParams = new LinearLayout.LayoutParams(
                        dpToPx(210), // largura em pixels
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lblProdutoParams.setMargins(20, 0, 0, 0);
                lblProduto.setLayoutParams(lblProdutoParams);
                lblProduto.setText(item.getText());
                lblProduto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                lblProduto.setTextColor(Color.parseColor("#242424"));
                lblProduto.setId(Integer.parseInt(item.getArg1()));
                lblProduto.setTag("lblProduto");

                TextView lblQuant = new TextView(contextRow);
                LinearLayout.LayoutParams  lblQuantParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lblQuantParams.setMargins(40, 0, 0, 0);
                lblQuant.setLayoutParams(lblQuantParams);
                String quant = txtQuant.getText()+item.getArg2();
                lblQuant.setText(quant);
                lblQuant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                lblQuant.setTextColor(Color.parseColor("#242424"));
                lblQuant.setId(View.generateViewId());
                lblQuant.setTag("produtoQuantLabel");

                producaoRow.addView(cbProducao);
                producaoRow.addView(lblProduto);
                producaoRow.addView(lblQuant);

                producaoContainer.addView(producaoRow);

                producaoLista.addView(producaoContainer);
            }});
        btnEsconder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout campos = binding.camposAdicionarProduto;
                txtQuant.setText("");
                slcProduto.setSelection(0);
                campos.setVisibility(View.GONE);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_secundary);
                navController.navigate(R.id.nav_home);
            }
        });
        btnConfirma.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LinearLayout listaProdutos = binding.producaoLista;
                List<Estoque> estoques = new ArrayList<>();
                for (int i = 0; i < listaProdutos.getChildCount(); i++) {
                    LinearLayout container = (LinearLayout) listaProdutos.getChildAt(i);
                    LinearLayout row = container.findViewWithTag("produtoRow");
                    TextView lblProduto = (TextView) row.getChildAt(1);
                    TextView lblQuant = (TextView) row.getChildAt(2);

                    Integer idProduto = lblProduto.getId();
                    String quantString = lblQuant.getText().toString().replaceAll("[^\\d.]", "");
                    double quantProd = Double.parseDouble(quantString);
                    Estoque estoque = new Estoque(idProduto,quantProd);
                    Log.d("ProducaoFragment", "ProdutoId e Quant: "+ estoque.getPdtId() +" - "+ estoque.getQuantidade());
                    estoques.add(estoque);
                }
                Calendar calendar = Calendar.getInstance();
                Date dataAtual = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dataFormatada = sdf.format(dataAtual);
                Producao producao = new Producao(dataAtual,estoques);

                //Invocar criar produção com a dataFormadata e ID gerado automaticamente
                Call<Producao> criarProducao = apiService.criarProducao(producao);
                criarProducao.enqueue(new Callback<Producao>() {
                    @Override
                    public void onResponse(Call<Producao> call, Response<Producao> response) {
                        if (response.isSuccessful()){
                            Log.d("ProducaoFragment", "Producao criada com sucesso"+response.body());
                        }
                        //Invocar criar produção_produtos com pdt_id e quant da lista estoques
                        // e pdc_id(gerado pela call anterior)
                        //Invocar SubtrairEstoque com pdt_id e quant da lista estoques.
                    }
                    @Override
                    public void onFailure(Call<Producao> call, Throwable t) {
                        Log.d("ProducaoFragment", "Erro producao call: "+t.getMessage());
                    }
                });
            }
        });
        slcProduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem selectedItem = (SpinnerItem) parent.getSelectedItem();
                String selectedText = selectedItem.getText();
                String selectedId = selectedItem.getArg1();
                String selectedUnMedida = selectedItem.getArg2();


                // Mostra o item selecionado em um log ou onde for necessário
                Log.d("SpinnerSelection", "Texto: " + selectedText + ", Id: " + selectedId+", UnMedida: " + selectedUnMedida);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ação se nada for selecionado
            }
        });

    };
    private void criaProdutos(Spinner slcProduto, View v) {
        Spinner spinner = v.findViewById(R.id.slcProduto);
        Context context = spinner.getContext();

        List<SpinnerItem> spinnerItems = new ArrayList<>();

        Call<List<Produto>> call = apiService.getProdutos();
        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if(response.isSuccessful()){
                        List<Produto> produtos = response.body();
                        for (Produto produto : produtos) {
                            spinnerItems.add(new SpinnerItem(produto.getNome(), String.valueOf(produto.getId()),produto.getUnMedida()));
                        }

                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerItems);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {

            }
        });
    }

    private void criaListeners() {
        LinearLayout producaoLista = binding.producaoLista;

        List<CheckBox> checkBoxes = new ArrayList<>();
        LinearLayout producaoRow = binding.producaoLista.findViewWithTag("producaoRow");
        for (int i = 0; i < producaoLista.getChildCount(); i++) {
            View container = producaoLista.getChildAt(i);
            View row = container.findViewWithTag("producaoRow");
            CheckBox cb = row.findViewWithTag("cbProducao");
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
    }
    public ProducaoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
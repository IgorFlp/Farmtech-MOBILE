package com.example.farmtech_mobile.ui.vender;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.SecundaryActivity;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Cliente;
import com.example.farmtech_mobile.data.model.Cupom;
import com.example.farmtech_mobile.data.model.Estoque;
import com.example.farmtech_mobile.data.model.LoggedInUser;
import com.example.farmtech_mobile.data.model.Venda;
import com.example.farmtech_mobile.data.model.VendaProdutos;
import com.example.farmtech_mobile.data.model.Produto;
import com.example.farmtech_mobile.data.model.SpinnerItem;
import com.example.farmtech_mobile.databinding.FragmentVenderBinding;
import com.example.farmtech_mobile.databinding.FragmentVenderBinding;
import com.example.farmtech_mobile.ui.login.LoginViewModel;
import com.example.farmtech_mobile.ui.vender.VenderFragment;
import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class VenderFragment extends Fragment {

    private FragmentVenderBinding binding;
    final private ApiService apiService = RetrofitClient.getApiService();
    private LoginViewModel loginViewModel;

    public static VenderFragment newInstance() {
        return new VenderFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VenderViewModel venderViewModel =
                new ViewModelProvider(this).get(VenderViewModel.class);
        binding = FragmentVenderBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getActivity();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String loggedUserName = sharedPreferences.getString("loggedUserName", "Usuário não encontrado");
        String loggedUserId = sharedPreferences.getString("loggedUserId", "Usuário não encontrado");

        int[] userid = new int[1];
        userid[0] = Integer.parseInt(loggedUserId);

        Log.d("VenderFragment", "Usuario id e nome"+loggedUserId+" - "+loggedUserName);


        List<Estoque> estoques = new ArrayList<>();
        List<BigDecimal> totaisProdutos = new ArrayList<>();


        BigDecimal[] subtotal = {BigDecimal.ZERO};
        BigDecimal[] frete = {BigDecimal.ZERO};
        BigDecimal[] desconto = {BigDecimal.ZERO};
        BigDecimal[] total = {BigDecimal.ZERO};

        final Button btnNovoProduto = binding.btnNovoProduto;
        LinearLayout campos = binding.camposAdicionarProduto;
        campos.setVisibility(View.GONE);

        EditText txtQuant = binding.txtQuant;
        EditText txtCupom = binding.txtCupom;



        TextView lblSubtotal = binding.lblSubtotal;
        TextView lblFrete = binding.lblFrete;
        TextView lblDesconto = binding.lblDesconto;
        TextView lblTotal = binding.lblTotal;
        TextView lblUsuario = binding.lblUsuario;

        Spinner slcProduto = binding.slcProduto;
        Spinner slcCliente = binding.slcCliente;
        Spinner slcEntrega = binding.slcEntrega;
        Spinner slcMtdPagto = binding.slcMtdPagto;

        final List<Produto> produtos = criaProdutos(slcProduto, view);
        criaClientes(slcCliente, view);

        Button btnSalvar = binding.btnSalvar;
        Button btnEsconder = binding.btnEsconder;
        Button btnConfirma = binding.btnConfirma;
        Button btnCancelar = binding.btnCancelar;
        Button btnBuscarCupom = binding.btnBuscarCupom;

        LinearLayout vendaLista = binding.vendaLista;

        lblUsuario.setText(loggedUserName);
        lblUsuario.setTag(loggedUserId);
        Log.d("VenderFragment", "Usuario loggedin id e nome"+loggedUserId+" - "+loggedUserName);
        Log.d("VenderFragment", "Usuario label id e nome"+lblUsuario.getTag().toString()+" - "+lblUsuario.getText().toString());

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

                for (int i = 0; i < vendaLista.getChildCount(); i++) {
                    View container = vendaLista.getChildAt(i);
                    View row = container.findViewWithTag("vendaRow");
                    CheckBox cb = row.findViewWithTag("cbProduto");
                    if (cb.isChecked()) {
                        selecionado = cb;
                    }
                }
                if(selecionado != null){
                    LinearLayout vendaContainer = (LinearLayout) selecionado.getParent().getParent();
                    TextView lblProduto = vendaContainer.findViewWithTag("lblProduto");
                    Integer id = (Integer) lblProduto.getId();
                    Log.d("Delete", "ID Selecionado: " + id);

                    LinearLayout container = (LinearLayout) selecionado.getParent().getParent();
                    vendaLista.removeView(container);
                    calcSubTotal();

                }else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Vendas")
                            .setMessage("Selecione um produto para deletar!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        Produto[] produtoSelecionado = new Produto[1];
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinnerItem item = (SpinnerItem) slcProduto.getSelectedItem();
                if(txtQuant.getText().toString().isEmpty()){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Vendas")
                            .setMessage("Preencha a quantidade do produto!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else if(((SpinnerItem) slcProduto.getSelectedItem()).getArg1().equals("0")){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Vendas")
                            .setMessage("Selecione um produto valido!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    for (Produto produto : produtos) {
                        if (produto.getId() == Integer.parseInt(item.getArg1())) {
                            produtoSelecionado[0] = produto;
                        }
                    }
                    ;


                    Context contextLista = vendaLista.getContext();
                    LinearLayout vendaContainer = new LinearLayout(contextLista);
                    vendaContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    vendaContainer.setOrientation(LinearLayout.VERTICAL);
                    vendaContainer.setTag("produtoContainer");
                    vendaContainer.setId(View.generateViewId());
                    int containerId = vendaContainer.getId();
                    Context contextContainer = vendaContainer.getContext();

                    LinearLayout vendaRow = new LinearLayout(contextContainer);
                    vendaRow.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT

                    ));

                    vendaRow.setOrientation(LinearLayout.HORIZONTAL);
                    vendaRow.setGravity(Gravity.CENTER_VERTICAL);
                    vendaRow.setTag("vendaRow");
                    vendaRow.setId(View.generateViewId());
                    int rowId = vendaRow.getId();
                    Context contextRow = vendaRow.getContext();

                    CheckBox cbVenda = new CheckBox(contextRow);
                    cbVenda.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    cbVenda.setTag("cbProduto");
                    cbVenda.setId(View.generateViewId());

                    TextView lblProduto = new TextView(contextRow);
                    LinearLayout.LayoutParams lblProdutoParams = new LinearLayout.LayoutParams(
                            dpToPx(170), // largura em pixels
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lblProdutoParams.setMargins(20, 0, 0, 0);
                    lblProduto.setLayoutParams(lblProdutoParams);
                    lblProduto.setText(item.getText());
                    lblProduto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    lblProduto.setTextColor(Color.parseColor("#242424"));
                    lblProduto.setId(Integer.parseInt(item.getArg1()));
                    lblProduto.setTag("lblProduto");

                    TextView lblQuant = new TextView(contextRow);
                    LinearLayout.LayoutParams lblQuantParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lblQuantParams.setMargins(dpToPx(5), 0, 0, 0);
                    lblQuant.setLayoutParams(lblQuantParams);
                    String quant = txtQuant.getText() + item.getArg2();
                    lblQuant.setText(quant);
                    lblQuant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    lblQuant.setTextColor(Color.parseColor("#242424"));
                    lblQuant.setId(View.generateViewId());
                    lblQuant.setTag("produtoQuantLabel");

                    TextView lblPrecoUn = new TextView(contextRow);
                    LinearLayout.LayoutParams lblPrecoUnParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lblPrecoUnParams.setMargins(dpToPx(13), 0, 0, 0);
                    lblPrecoUn.setLayoutParams(lblPrecoUnParams);
                    String precoUn = Double.toString(produtoSelecionado[0].getPrecoUn());
                    lblPrecoUn.setText(String.format("%.2f", produtoSelecionado[0].getPrecoUn()));
                    lblPrecoUn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    lblPrecoUn.setTextColor(Color.parseColor("#242424"));
                    lblPrecoUn.setId(View.generateViewId());
                    lblPrecoUn.setTag("produtoPrecoUnLabel");

                    TextView lblTotalProd = new TextView(contextRow);
                    LinearLayout.LayoutParams lblTotalProdParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    lblTotalProdParams.setMargins(dpToPx(20), 0, 0, 0);
                    lblTotalProd.setLayoutParams(lblTotalProdParams);

                    double preco = produtoSelecionado[0].getPrecoUn();
                    String precoString = String.valueOf(preco).replace(",", ".");
                    double totalProd = Double.parseDouble(precoString) * Double.parseDouble(txtQuant.getText().toString());

                    lblTotalProd.setText(String.format("%.2f", totalProd));
                    lblTotalProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    lblTotalProd.setTextColor(Color.parseColor("#242424"));
                    lblTotalProd.setId(View.generateViewId());
                    lblTotalProd.setTag("produtoTotalLabel");

                    vendaRow.addView(cbVenda);
                    vendaRow.addView(lblProduto);
                    vendaRow.addView(lblQuant);
                    vendaRow.addView(lblPrecoUn);
                    vendaRow.addView(lblTotalProd);

                    //vendaContainer.addView(legenda);
                    vendaContainer.addView(vendaRow);

                    vendaLista.addView(vendaContainer);
                    calcSubTotal();
                }
            }});
        btnBuscarCupom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarCupom(txtCupom.getText().toString());
            }
        });
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
                if(slcCliente.getSelectedItem().toString().equals("Selecione um cliente")){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Vendas")
                            .setMessage("Selecione um cliente valido!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else if(binding.vendaLista.getChildCount() == 0){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Vendas")
                            .setMessage("Lista vazia, adicione um produto!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                LinearLayout listaVendas = binding.vendaLista;
                for(int i = 0; i < listaVendas.getChildCount(); i++){
                    LinearLayout container = (LinearLayout) listaVendas.getChildAt(i);
                    LinearLayout row = container.findViewWithTag("vendaRow");
                    TextView lblProduto = (TextView) row.findViewWithTag("lblProduto");
                    TextView lblQuant = (TextView) row.findViewWithTag("produtoQuantLabel");
                    TextView lblTotal = (TextView) row.findViewWithTag("produtoTotalLabel");

                    int id = lblProduto.getId();
                    BigDecimal quant = BigDecimal.valueOf(Double.parseDouble(lblQuant.getText().toString().replaceAll("[^\\d.]", "")));
                    Log.d("VendaFragment", "Quant"+quant);
                    Estoque estoque = new Estoque(id,quant);
                    estoques.add(estoque);
                }
                Gson gson = new Gson();
                String json = gson.toJson(estoques);
                Log.d("VendaFragment", "Estoques "+json);

                Spinner slcCliente = binding.slcCliente;
                SpinnerItem selectedItem = (SpinnerItem) slcCliente.getSelectedItem();
                String cpf = selectedItem.getArg1();
                Log.d("VenderFragment", "CPF do cliente selecionado: " + cpf);

                Calendar calendar = Calendar.getInstance();
                Date dataAtual = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dataFormatada = dateFormat.format(dataAtual);
                Log.d("VenderFragment", "Data atual: " + dataFormatada);

                //lblProduto tag: lblProduto text: nome do produto id: Id do produto
                //lblQuant tag: produtoQuantLabel text: quantidade do produto + UnMedida
                //lblTotalProd tag: produtoTotalLabel  text: total do produto 0.00
                Log.d("VenderFragment", "Usuario id arr: "+userid[0]);
                int userId = userid[0];
                Log.d("VenderFragment", "Usuario id: "+userId);
                Venda venda = new Venda(
                        BigDecimal.valueOf(Double.parseDouble(lblSubtotal.getText().toString().replaceAll("[^\\d.]", ""))),
                        BigDecimal.valueOf(Double.parseDouble(lblFrete.getText().toString().replaceAll("[^\\d.]", ""))),
                        BigDecimal.valueOf(Double.parseDouble(lblDesconto.getText().toString().replaceAll("[^\\d.]", ""))),
                        BigDecimal.valueOf(Double.parseDouble(lblTotal.getText().toString().replaceAll("[^\\d.]", ""))),
                        txtCupom.getText().toString().toUpperCase(),
                        slcMtdPagto.getSelectedItem().toString(),
                        cpf,dataFormatada,
                        slcEntrega.getSelectedItem().toString(),
                        userid[0]
                        );
                Log.d("VenderFragment", "Venda objeto: "+ new Gson().toJson(venda));
                Call<Venda> criarVenda = apiService.criarVenda(venda);
                criarVenda.enqueue(new Callback<Venda>() {
                    @Override
                    public void onResponse(Call<Venda> call, Response<Venda> response) {
                        if (response.isSuccessful()) {
                            Venda venda = response.body();
                            Log.d("VenderFragment", "Venda response: " + new Gson().toJson(venda));
                            for(Estoque estoque : estoques){
                                VendaProdutos vendaProduto = new VendaProdutos(venda.getId(),estoque.getPdtId(),estoque.getQuant());
                                Call<VendaProdutos> criarVendaProduto = apiService.criarVendaProduto(vendaProduto);
                                Log.d("VenderFragment", "VendaProduto objeto: "+ new Gson().toJson(vendaProduto));
                                criarVendaProduto.enqueue(new Callback<VendaProdutos>() {
                                    @Override
                                    public void onResponse(Call<VendaProdutos> call, Response<VendaProdutos> response) {
                                        if (response.isSuccessful()) {
                                            VendaProdutos vendaProdutos = response.body();
                                            String json2 = new Gson().toJson(vendaProdutos);
                                            Log.d("VenderFragment", "vendaProdutos: "+ json2);

                                            Call<Void> subtrairEstoque = apiService.subtrairEstoque(estoque.getPdtId(),estoque.getQuant());
                                            subtrairEstoque.enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    if(response.isSuccessful()){
                                                        Log.d("ProducaoFragment", "Estoque atualizado com sucesso");
                                                    }else{
                                                        try {
                                                            // Log do código de erro, headers e o corpo do erro
                                                            Log.d("ProducaoFragment", "Estoque não atualizado. Código: " + response.code() +
                                                                    " /// Headers: " + response.headers() +
                                                                    " /// Erro: " + response.errorBody().string());
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    Log.d("ProducaoFragment", "Erro call: "+t.getMessage());
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<VendaProdutos> call, Throwable t) {
                                        Log.d("VenderFragment", "onFailure Erro: " + t.getMessage());
                                    }
                                });
                            }
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Cadastro de vendas")
                                    .setMessage("Venda registrada com sucesso!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            NavController navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment_content_secundary);
                                            navController.navigate(R.id.nav_vender  );
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    }
                    @Override
                    public void onFailure(Call<Venda> call, Throwable t) {
                        Log.d("VenderFragment", "onFailure Erro: "+t.getMessage());
                    }
                });
                }

        }});
        slcEntrega.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) slcEntrega.getSelectedItem();
                //String selectedText = selectedItem.getText().toString();
                if(selectedItem.equals("Entrega")){
                    binding.lblFrete.setText("R$ 25.00");
                    calcSubTotal();
                }else{
                    binding.lblFrete.setText("R$ 00.00");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
    public void buscarCupom(String txtCupom){

        Call<Cupom> call = apiService.buscarCupom(txtCupom);
        LinearLayout cupomContainer = binding.cupomContainer;
        call.enqueue(new Callback<Cupom>() {
            @Override
            public void onResponse(Call<Cupom> call, Response<Cupom> response) {
                if(response.isSuccessful()){
                    Log.d("VenderFragment", "Response");
                    Cupom cupom = response.body();
                    String json = new Gson().toJson(cupom);
                    Log.d("VenderFragment", "Cupom "+json);
                    Calendar calendar = Calendar.getInstance();
                    Date dataAtual = calendar.getTime();
                    int res = dataAtual.compareTo(cupom.getDtValid());
                    Log.d("VenderFragment", "Cupom encontrado");
                    if(res < 0 ){
                        Log.d("VenderFragment", "Cupom valido");
                        cupomContainer.setBackgroundResource(R.drawable.borda_sucesso_background);
                        binding.lblDesconto.setText(String.format("%.2f",cupom.getValor()).replace(",","."));
                        calcTotal();

                    }else{
                        Log.d("VenderFragment", "Cupom invalido");
                        cupomContainer.setBackgroundResource(R.drawable.borda_erro_background);

                    }

                }else{
                    Log.d("VenderFragment", "Cupom não encontrado");
                    cupomContainer.setBackgroundResource(R.drawable.borda_erro_background);
                }
            }
            @Override
            public void onFailure(Call<Cupom> call, Throwable t) {
                Log.d("VenderFragment", "Erro na importação de cupons"+ t.getMessage());
                cupomContainer.setBackgroundResource(R.drawable.borda_erro_background);
            }
        });
        ;
    }
    private void calcSubTotal(){
        List<BigDecimal> totaisProdutos = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        LinearLayout listaVendas = binding.vendaLista;
        TextView lblSubtotal = binding.lblSubtotal;
        for(int i = 0; i < listaVendas.getChildCount(); i++){
            LinearLayout container = (LinearLayout) listaVendas.getChildAt(i);
            LinearLayout row = container.findViewWithTag("vendaRow");
            TextView lblTotal = (TextView) row.findViewWithTag("produtoTotalLabel");

        BigDecimal totalProd = BigDecimal.valueOf(Double.parseDouble(lblTotal.getText().toString().replace(",",".")));
        Log.d("VendaFragment", "Total do produto: "+totalProd);
        totaisProdutos.add(totalProd);
        subtotal = subtotal.add(totalProd);
        Log.d("VendaFragment", "Subtotal: "+String.format("%.2f", subtotal));
        }
        lblSubtotal.setText(String.format("R$ %.2f", subtotal).replace(",","."));
        calcTotal();
    }
    private void calcTotal(){

        BigDecimal subtotal = BigDecimal.valueOf(Double.parseDouble(binding.lblSubtotal.getText().toString().replaceAll("[^\\d.,]", "").replace(",",".")));
        BigDecimal frete = BigDecimal.valueOf(Double.parseDouble(binding.lblFrete.getText().toString().replaceAll("[^\\d.,]", "").replace(",",".")));
        BigDecimal desconto = BigDecimal.valueOf(Double.parseDouble(binding.lblDesconto.getText().toString().replaceAll("[^\\d.,]", "").replace(",",".")));
        BigDecimal total = subtotal.add(frete).subtract(desconto);
        binding.lblTotal.setText(String.format("R$ %.2f", total).replace(",","."));
         }

    private List<Produto> criaProdutos(Spinner slcProduto, View v) {
        List<SpinnerItem> spinnerProdutos = new ArrayList<>();
        Spinner spinner = v.findViewById(R.id.slcProduto);
        Context context = spinner.getContext();

        List<Produto> produtosList = new ArrayList<>();

        Call<List<Produto>> call = apiService.getProdutos();
        call.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                List<Produto> produtos = new ArrayList<>();
                if(response.isSuccessful()){
                    produtos = response.body();
                    spinnerProdutos.add(new SpinnerItem("Selecione um produto","0",""));

                    for (Produto produto : produtos) {
                        spinnerProdutos.add(new SpinnerItem(produto.getNome(), String.valueOf(produto.getId()),produto.getUnMedida()));
                        produtosList.add(produto);
                    }

                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerProdutos);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {

            }
        });
        return produtosList;
    }
    private void criaClientes(Spinner slcCliente, View v) {

        Spinner spinner = v.findViewById(R.id.slcCliente);
        Context context = spinner.getContext();
        List<SpinnerItem> spinnerClientes = new ArrayList<>();

        Call<List<Cliente>> call = apiService.getClientes();
        call.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if(response.isSuccessful()){
                    List<Cliente> clientes = response.body();
                    spinnerClientes.add(new SpinnerItem("Selecione um cliente","0",""));
                    for (Cliente cliente : clientes) {
                        spinnerClientes.add(new SpinnerItem(cliente.getNome(), cliente.getCpf(), cliente.getTelefone()));

                    }

                    ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinnerClientes);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    spinner.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Log.d("VenderFragment", "Erro na importação de clientes");
            }
        });
    }

    private void criaListeners() {
        LinearLayout vendaLista = binding.vendaLista;

        List<CheckBox> checkBoxes = new ArrayList<>();
        LinearLayout vendaRow = binding.vendaLista.findViewWithTag("vendaRow");
        for (int i = 0; i < vendaLista.getChildCount(); i++) {
            View container = vendaLista.getChildAt(i);
            View row = container.findViewWithTag("vendaRow");
            CheckBox cb = row.findViewWithTag("cbVenda");
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
    public VenderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
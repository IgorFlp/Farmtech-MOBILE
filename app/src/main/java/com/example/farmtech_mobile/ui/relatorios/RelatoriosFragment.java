package com.example.farmtech_mobile.ui.relatorios;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.api.ApiService;
import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.model.Cliente;
import com.example.farmtech_mobile.data.model.ClienteEndereco;
import com.example.farmtech_mobile.data.model.Estoque;
import com.example.farmtech_mobile.data.model.Fornecedor;
import com.example.farmtech_mobile.data.model.Producao;
import com.example.farmtech_mobile.data.model.ProducaoProdutos;
import com.example.farmtech_mobile.data.model.Produto;
import com.example.farmtech_mobile.data.model.Usuario;
import com.example.farmtech_mobile.data.model.Venda;
import com.example.farmtech_mobile.data.model.VendaProdutos;
import com.example.farmtech_mobile.databinding.FragmentRelatoriosBinding;
import com.example.farmtech_mobile.databinding.FragmentVenderBinding;
import com.example.farmtech_mobile.ui.vender.VenderViewModel;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RelatoriosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RelatoriosFragment extends Fragment {

    private FragmentRelatoriosBinding binding;
    private TextView lblTitulo;
    private Spinner slcRelatorio;
    private EditText txtDataInicial;
    private EditText txtDataFinal;
    private Button btnExpandir;
    private Button btnPesquisar;
    private LinearLayout buscaContainer;
    private TableLayout tabelaRelatorio;
    private ApiService apiService = RetrofitClient.getApiService();
    private Gson gson = new Gson();

    public RelatoriosFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RelatoriosFragment newInstance() {
        return new RelatoriosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        VenderViewModel venderViewModel =
                new ViewModelProvider(this).get(VenderViewModel.class);
        binding = FragmentRelatoriosBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slcRelatorio = binding.slcRelatorio;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this.getContext(), R.array.relatorios_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slcRelatorio.setAdapter(adapter);

        txtDataInicial = binding.txtDataInicial;
        txtDataFinal = binding.txtDataFinal;

        btnExpandir = binding.btnExpandir;
        btnPesquisar = binding.btnPesquisar;
        buscaContainer = binding.buscaContainer;

        tabelaRelatorio = binding.tabelaRelatorio;
        lblTitulo = binding.lblTitulo;


        buscaContainer.setVisibility(View.VISIBLE);

        // LISTENERS

        slcRelatorio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = slcRelatorio.getSelectedItem().toString();
                if (!item.equals("Vendas") && !item.equals("Produção")) {
                    txtDataInicial.setEnabled(false);
                    LinearLayout container = (LinearLayout) txtDataInicial.getParent();
                    container.setVisibility(View.GONE);

                    txtDataFinal.setEnabled(false);
                    container = (LinearLayout) txtDataFinal.getParent();
                    container.setVisibility(View.GONE);

                } else {
                    txtDataInicial.setEnabled(false);
                    LinearLayout container = (LinearLayout) txtDataInicial.getParent();
                    container.setVisibility(View.GONE);

                    txtDataFinal.setEnabled(false);
                    container = (LinearLayout) txtDataFinal.getParent();
                    container.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtDataFinal.isEnabled()) {
                    String dataInicial = txtDataInicial.getText().toString();
                    String dataFinal = txtDataFinal.getText().toString();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date inicio = sdf.parse(dataInicial);
                        Date fim = sdf.parse(dataFinal);

                        // Comparando as datas
                        if (inicio.compareTo(fim) > 0) {
                            System.out.println(dataInicial + " é posterior a " + dataFinal + "Levantar janela de aviso");
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Relatórios")
                                    .setMessage("A data inicial é superior a final!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            System.out.println(dataInicial + " é anterior ou igual " + dataFinal + "Podemos seguir");
                            Log.d("RelatoriosFragment", "Pesquisar");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                String tipoRelatorio = slcRelatorio.getSelectedItem().toString();
                switch (tipoRelatorio) {
                    case "Vendas":
                        relatorioVendas();
                        break;
                    case "Estoque":
                        relatorioEstoque();
                        break;
                    case "Produção":
                        relatorioProducao();
                        break;
                    case "Clientes":
                        relatorioClientes();
                        break;
                    case "Fornecedores":
                        relatorioFornecedores();
                        break;
                    case "Produtos":
                        relatorioProdutos();
                        break;
                    case "Usuarios":
                        relatorioUsuarios();
                        break;
                    default:
                        break;
                }
            }
        });
        btnExpandir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buscaContainer.getVisibility() == View.VISIBLE) {
                    buscaContainer.setVisibility(View.GONE);
                    Drawable dw = getResources().getDrawable(R.mipmap.dropdown_foreground, null);
                    btnExpandir.setForeground(dw);
                } else {
                    Drawable dw = getResources().getDrawable(R.mipmap.dropup_foreground, null);
                    btnExpandir.setForeground(dw);
                    buscaContainer.setVisibility(View.VISIBLE);
                }
            }
        });
        txtDataInicial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //formatarData(txtDataInicial.getText().toString());
                if (keyCode == KeyEvent.KEYCODE_8) {
                    return true;
                } else {
                    txtDataInicial.setText(formatarData(txtDataInicial.getText().toString()));
                    txtDataInicial.setSelection(txtDataInicial.getText().length());
                    return false;
                }


            }
        });
        txtDataFinal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //formatarData(txtDataInicial.getText().toString());
                if (keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                } else {
                    txtDataFinal.setText(formatarData(txtDataFinal.getText().toString()));
                    txtDataFinal.setSelection(txtDataFinal.getText().length());
                    return false;
                }
            }
        });
    }

    private Date stringToDate(String formato, String data) {
        SimpleDateFormat pattern = new SimpleDateFormat(formato);
        try {
            Date d = pattern.parse(data);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private String formatarData(String texto) {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //String dataFormatada = dateFormat.format(texto);
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (i == 2 && c != '/' || i == 5 && c != '/') {
                texto = texto.substring(0, i) + "/" + texto.substring(i);
            }
        }
        return texto;
    }

    private void relatorioVendas() {
        lblTitulo.setText("Relatório de Vendas");
        List<Usuario> usuarios = new ArrayList<>();
        List<Cliente> clientes = new ArrayList<>();
        List<Venda> vendas = new ArrayList<>();
        List<Produto> produtos = new ArrayList<>();
        List<VendaProdutos> vendaProdutos = new ArrayList<>();
        // Fazer call e alimentar as listas Vendas, VendasProdutos,Produtos, Clientes, Vendas

        CountDownLatch latch = new CountDownLatch(5);

        Call<List<Usuario>> callUsuario = apiService.getUsuarios();
        callUsuario.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.isSuccessful()){
                    List<Usuario> u = response.body();
                    usuarios.addAll(u);
                    Log.d("RelatoriosFragment", "Usuarios: "+ gson.toJson(usuarios));
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                latch.countDown();
            }
        });
        Call<List<Cliente>> callCliente = apiService.getClientes();
        callCliente.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if(response.isSuccessful()){
                    List<Cliente> u = response.body();
                    clientes.addAll(u);
                    Log.d("RelatoriosFragment", "Clientes: "+ gson.toJson(clientes));
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                latch.countDown();
            }
        });
        Call<List<Venda>> callVenda = apiService.getVendas();
        callVenda.enqueue(new Callback<List<Venda>>() {
            @Override
            public void onResponse(Call<List<Venda>> call, Response<List<Venda>> response) {
                if(response.isSuccessful()){
                    List<Venda> u = response.body();
                    vendas.addAll(u);
                    Log.d("RelatoriosFragment", "Vendas: "+ gson.toJson(vendas));
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Venda>> call, Throwable t) {
                latch.countDown();
            }
        });
        Call<List<VendaProdutos>> callVendaProdutos = apiService.getVendaProdutos();
        callVendaProdutos.enqueue(new Callback<List<VendaProdutos>>() {
            @Override
            public void onResponse(Call<List<VendaProdutos>> call, Response<List<VendaProdutos>> response) {
                if(response.isSuccessful()){
                    List<VendaProdutos> u = response.body();
                    vendaProdutos.addAll(u);
                    Log.d("RelatoriosFragment", "VendaProdutoss: "+ gson.toJson(vendaProdutos));
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<VendaProdutos>> call, Throwable t) {
                latch.countDown();
            }
        });
        Call<List<Produto>> callProdutos = apiService.getProdutos();
        callProdutos.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if(response.isSuccessful()){
                    List<Produto> u = response.body();
                    produtos.addAll(u);
                    Log.d("RelatoriosFragment", "Produtoss: "+ gson.toJson(produtos));
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {
                latch.countDown();
            }
        });

        new Thread(() -> {
            try {
                latch.await(); // Aguardar até que o contador atinja zero
                Log.d("RelatoriosFragment", "Todas as listas foram preenchidas, continuando o código...");

                    //Criar Linear layout com  id e data do produto e dropdown pra expandir a TableView
                    getActivity().runOnUiThread(() -> {
                        if(usuarios.isEmpty() || clientes.isEmpty() || vendas.isEmpty() || produtos.isEmpty() || vendaProdutos.isEmpty()){

                            relatorioVendas();
                        }else {
                            LinearLayout layoutRelatorio = binding.layoutRelatorio;
                            layoutRelatorio.removeAllViews();
                            for (Venda venda : vendas) {
                                //Criar Linear layout com  id e data do produto e dropdown pra expandir a TableView
                                LinearLayout vendaHeader1 = new LinearLayout(layoutRelatorio.getContext());
                                LinearLayout.LayoutParams vendaHeader1Param = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                                vendaHeader1.setOrientation(LinearLayout.HORIZONTAL);
                                int cor = ContextCompat.getColor(getContext(), R.color.verde_forte_claro);
                                vendaHeader1.setTag("vendaHeader1");
                                vendaHeader1.setBackgroundColor(cor);
                                vendaHeader1.setLayoutParams(vendaHeader1Param);


                                TextView vendaID = new TextView(vendaHeader1.getContext());
                                LinearLayout.LayoutParams vendaIDParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                vendaIDParam.leftMargin = 20;
                                vendaIDParam.gravity = Gravity.START;
                                vendaID.setLayoutParams(vendaIDParam);
                                vendaID.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                vendaID.setTextColor(Color.parseColor("#242424"));
                                vendaID.setText("Venda ID: " + String.valueOf(venda.getId()));
                                vendaHeader1.addView(vendaID);

                                TextView colDataVend = new TextView(vendaHeader1.getContext());
                                LinearLayout.LayoutParams colDataVendParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                colDataVendParam.leftMargin = dpToPx(40);
                                colDataVendParam.gravity = Gravity.START;
                                colDataVend.setLayoutParams(colDataVendParam);
                                colDataVend.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                colDataVend.setTextColor(Color.parseColor("#242424"));
                                colDataVend.setText("Data da venda: " + venda.getDtVenda());
                                vendaHeader1.addView(colDataVend);

                                TextView vendedor = new TextView(vendaHeader1.getContext());
                                LinearLayout.LayoutParams vendedorParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                vendedorParam.leftMargin = dpToPx(40);

                                vendedorParam.gravity = Gravity.START;
                                vendedor.setLayoutParams(vendedorParam);
                                vendedor.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                vendedor.setTextColor(Color.parseColor("#242424"));
                                for (Usuario usuario : usuarios) {
                                    if (usuario.getId() == venda.getUsr_id()) {
                                        vendedor.setText("Vendedor: " + usuario.getNome());
                                    }
                                }
                                vendaHeader1.addView(vendedor);

                                LinearLayout vendaHeader2 = new LinearLayout(layoutRelatorio.getContext());
                                LinearLayout.LayoutParams vendaHeader2Param = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                                vendaHeader2.setOrientation(LinearLayout.HORIZONTAL);
                                cor = ContextCompat.getColor(getContext(), R.color.verde_fundo);
                                vendaHeader2.setTag("vendaHeader2");
                                vendaHeader2.setBackgroundColor(cor);
                                vendaHeader2.setLayoutParams(vendaHeader2Param);


                                TextView clienteNome = new TextView(vendaHeader2.getContext());
                                LinearLayout.LayoutParams clienteNomeParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                clienteNomeParam.leftMargin = dpToPx(10);
                                clienteNomeParam.gravity = Gravity.START;
                                clienteNome.setLayoutParams(clienteNomeParam);
                                clienteNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                clienteNome.setTextColor(Color.parseColor("#242424"));

                                TextView clienteCpf = new TextView(vendaHeader2.getContext());
                                LinearLayout.LayoutParams clienteCpfParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                clienteCpfParam.leftMargin = dpToPx(20);
                                clienteCpfParam.gravity = Gravity.START;
                                clienteCpf.setLayoutParams(clienteCpfParam);
                                clienteCpf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                clienteCpf.setTextColor(Color.parseColor("#242424"));

                                for (Cliente cliente : clientes) {
                                    if (cliente.getCpf().equals(venda.getCl_cpf())) {
                                        clienteNome.setText("Cliente:  " + cliente.getNome());
                                        clienteCpf.setText("CPF:  " + venda.getCl_cpf());
                                        Log.d("RelatorioFragment", "Cliente nome e cpf " + cliente.getNome() + venda.getCl_cpf());
                                        vendaHeader2.addView(clienteNome);
                                        vendaHeader2.addView(clienteCpf);
                                    }
                                }


                                LinearLayout vendaHeader3 = new LinearLayout(layoutRelatorio.getContext());
                                LinearLayout.LayoutParams vendaHeader3Param = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                                vendaHeader3.setOrientation(LinearLayout.HORIZONTAL);
                                cor = ContextCompat.getColor(getContext(), R.color.verde_fundo);
                                vendaHeader3.setTag("vendaHeader3");
                                vendaHeader3.setBackgroundColor(cor);
                                vendaHeader3.setLayoutParams(vendaHeader3Param);


                                TextView mtdPagto = new TextView(vendaHeader3.getContext());
                                LinearLayout.LayoutParams mtdPagtoParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                mtdPagtoParam.leftMargin = dpToPx(10);
                                mtdPagtoParam.gravity = Gravity.START;
                                mtdPagto.setLayoutParams(mtdPagtoParam);
                                mtdPagto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                mtdPagto.setTextColor(Color.parseColor("#242424"));
                                mtdPagto.setText("Pagamento: " + venda.getMtdPagto());

                                TextView cupom = new TextView(vendaHeader3.getContext());
                                LinearLayout.LayoutParams cupomParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                cupomParam.leftMargin = dpToPx(50);
                                cupomParam.gravity = Gravity.START;
                                cupom.setLayoutParams(cupomParam);
                                cupom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                cupom.setTextColor(Color.parseColor("#242424"));
                                cupom.setText("Cupom: " + venda.getCupom());

                                TextView entrega = new TextView(vendaHeader3.getContext());
                                LinearLayout.LayoutParams entregaParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                entregaParam.leftMargin = dpToPx(30);
                                entregaParam.gravity = Gravity.START;
                                entrega.setLayoutParams(entregaParam);
                                entrega.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                entrega.setTextColor(Color.parseColor("#242424"));
                                entrega.setText("Entrega: " + venda.getEntrega());

                                vendaHeader3.addView(mtdPagto);
                                vendaHeader3.addView(entrega);
                                vendaHeader3.addView(cupom);


                                LinearLayout vendaHeader4 = new LinearLayout(layoutRelatorio.getContext());
                                LinearLayout.LayoutParams vendaHeader4Param = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );

                                vendaHeader4.setOrientation(LinearLayout.HORIZONTAL);
                                cor = ContextCompat.getColor(getContext(), R.color.verde_fundo);
                                vendaHeader4.setTag("vendaHeader4");
                                vendaHeader4.setBackgroundColor(cor);
                                vendaHeader4.setLayoutParams(vendaHeader4Param);


                                TextView subtotal = new TextView(vendaHeader4.getContext());
                                LinearLayout.LayoutParams subtotalParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                subtotalParam.leftMargin = dpToPx(10);
                                subtotalParam.bottomMargin = dpToPx(20);
                                subtotalParam.gravity = Gravity.START;
                                subtotal.setLayoutParams(subtotalParam);
                                subtotal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                subtotal.setTextColor(Color.parseColor("#242424"));
                                subtotal.setText("Subtotal: " + venda.getSubtotal());

                                TextView frete = new TextView(vendaHeader4.getContext());
                                LinearLayout.LayoutParams freteParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                freteParam.leftMargin = dpToPx(50);
                                freteParam.gravity = Gravity.START;
                                frete.setLayoutParams(freteParam);
                                frete.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                frete.setTextColor(Color.parseColor("#242424"));
                                frete.setText("Frete: " + venda.getFrete());

                                TextView desconto = new TextView(vendaHeader4.getContext());
                                LinearLayout.LayoutParams descontoParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                descontoParam.leftMargin = dpToPx(30);
                                descontoParam.gravity = Gravity.START;
                                desconto.setLayoutParams(descontoParam);
                                desconto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                desconto.setTextColor(Color.parseColor("#242424"));
                                desconto.setText("Desconto: " + venda.getDesconto());

                                TextView total = new TextView(vendaHeader4.getContext());
                                LinearLayout.LayoutParams totalParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                totalParam.leftMargin = dpToPx(20);
                                totalParam.gravity = Gravity.START;
                                total.setLayoutParams(totalParam);
                                total.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                total.setTextColor(Color.parseColor("#242424"));
                                total.setText("Total: " + venda.getTotal());


                                vendaHeader4.addView(subtotal);
                                vendaHeader4.addView(frete);
                                vendaHeader4.addView(desconto);
                                vendaHeader4.addView(total);

                                LinearLayout bordaHeader = new LinearLayout(layoutRelatorio.getContext());
                                LinearLayout.LayoutParams bordaHeaderParam = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        dpToPx(10)
                                );
                                cor = ContextCompat.getColor(getContext(), R.color.verde_fundo);
                                bordaHeader.setBackgroundColor(cor);

                                LinearLayout bordaHeader2 = new LinearLayout(layoutRelatorio.getContext());
                                LinearLayout.LayoutParams bordaHeader2Param = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        dpToPx(5)
                                );
                                cor = ContextCompat.getColor(getContext(), R.color.verde_fundo);
                                bordaHeader2.setBackgroundColor(cor);

                                LinearLayout produtosHeader = new LinearLayout(layoutRelatorio.getContext());
                                LinearLayout.LayoutParams produtosHeaderParam = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                produtosHeader.setGravity(Gravity.CENTER_HORIZONTAL);
                                cor = ContextCompat.getColor(getContext(), R.color.verde_header);
                                produtosHeader.setBackgroundColor(cor);

                                TextView produtosText = new TextView(produtosHeader.getContext());
                                LinearLayout.LayoutParams produtosTextParam = new LinearLayout.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                produtosText.setGravity(Gravity.CENTER_HORIZONTAL);
                                produtosText.setLayoutParams(totalParam);
                                produtosText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                produtosText.setTextColor(Color.parseColor("#242424"));
                                produtosText.setText("Produtos");
                                produtosHeader.addView(produtosText);


                                layoutRelatorio.addView(vendaHeader1);
                                layoutRelatorio.addView(bordaHeader);
                                layoutRelatorio.addView(vendaHeader2);
                                layoutRelatorio.addView(vendaHeader3);
                                layoutRelatorio.addView(vendaHeader4);
                                layoutRelatorio.addView(bordaHeader2);
                                layoutRelatorio.addView(produtosHeader);


                                //Criar TableLayout nova
                                TableLayout tabelaRelatorio = new TableLayout(layoutRelatorio.getContext());
                                TableLayout.LayoutParams tabelaRelatorioParam = new TableLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                tabelaRelatorio.setLayoutParams(tabelaRelatorioParam);
                                tabelaRelatorio.setTag("tabelaRelatorio");
                                //layoutRelatorio.addView(tabelaRelatorio);
                                Context contextTabela = tabelaRelatorio.getContext();

                                TableRow rowHeader = new TableRow(contextTabela);
                                Context contextRowHeader = rowHeader.getContext();
                                TableRow.LayoutParams rowHeaderParam = new TableRow.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );

                                rowHeader.setOrientation(LinearLayout.HORIZONTAL);
                                cor = ContextCompat.getColor(getContext(), R.color.verde_fundo);
                                rowHeader.setTag("vendaProdutoHeaderRow");
                                rowHeader.setBackgroundColor(cor);
                                rowHeader.setLayoutParams(rowHeaderParam);


                                TextView produtoIdHeader = new TextView(rowHeader.getContext());
                                TableRow.LayoutParams produtoIdHeaderParam = new TableRow.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                produtoIdHeaderParam.weight = 1f;
                                produtoIdHeaderParam.leftMargin = dpToPx(10);
                                produtoIdHeaderParam.gravity = Gravity.START;
                                produtoIdHeader.setLayoutParams(produtoIdHeaderParam);
                                produtoIdHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                produtoIdHeader.setTextColor(Color.parseColor("#242424"));
                                produtoIdHeader.setText("ID");

                                rowHeader.addView(produtoIdHeader);

                                TextView produtoNomeHeader = new TextView(rowHeader.getContext());
                                TableRow.LayoutParams produtoNomeHeaderParam = new TableRow.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                produtoNomeHeaderParam.weight = 3f;
                                produtoNomeHeaderParam.leftMargin = dpToPx(20);
                                produtoNomeHeaderParam.gravity = Gravity.START;
                                produtoNomeHeader.setLayoutParams(produtoNomeHeaderParam);
                                produtoNomeHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                produtoNomeHeader.setTextColor(Color.parseColor("#242424"));
                                produtoNomeHeader.setText("Nome");
                                rowHeader.addView(produtoNomeHeader);

                                TextView unMedidaHeader = new TextView(rowHeader.getContext());
                                TableRow.LayoutParams unMedidaHeaderParam = new TableRow.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                unMedidaHeaderParam.weight = 1f;
                                unMedidaHeaderParam.leftMargin = dpToPx(20);
                                unMedidaHeaderParam.gravity = Gravity.START;
                                unMedidaHeader.setLayoutParams(unMedidaHeaderParam);
                                unMedidaHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                unMedidaHeader.setTextColor(Color.parseColor("#242424"));
                                unMedidaHeader.setText("Medida");
                                rowHeader.addView(unMedidaHeader);

                                TextView quantidadeHeader = new TextView(rowHeader.getContext());
                                TableRow.LayoutParams quantidadeHeaderParam = new TableRow.LayoutParams(
                                        TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                );
                                quantidadeHeaderParam.weight = 1f;
                                quantidadeHeaderParam.leftMargin = dpToPx(20);
                                quantidadeHeaderParam.gravity = Gravity.START;
                                quantidadeHeader.setLayoutParams(quantidadeHeaderParam);
                                quantidadeHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                quantidadeHeader.setTextColor(Color.parseColor("#242424"));
                                quantidadeHeader.setText("Quant");
                                rowHeader.addView(quantidadeHeader);

                                tabelaRelatorio.addView(rowHeader);

                                for (VendaProdutos vendaProduto : vendaProdutos) {
                                    if (vendaProduto.getVen_id() == venda.getId()) {
                                        TableRow row = new TableRow(contextTabela);
                                        Context contextRow = row.getContext();
                                        TableRow.LayoutParams rowParam = new TableRow.LayoutParams(
                                                TableRow.LayoutParams.MATCH_PARENT,
                                                TableRow.LayoutParams.WRAP_CONTENT
                                        );

                                        row.setOrientation(LinearLayout.HORIZONTAL);
                                        cor = ContextCompat.getColor(getContext(), R.color.verde_fundo);
                                        row.setTag("vendaProdutoRow");
                                        row.setBackgroundColor(cor);
                                        row.setLayoutParams(rowParam);


                                        TextView produtoId = new TextView(row.getContext());
                                        TableRow.LayoutParams produtoIdParam = new TableRow.LayoutParams(
                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                TableRow.LayoutParams.WRAP_CONTENT
                                        );
                                        produtoIdParam.weight = 1f;
                                        produtoIdParam.leftMargin = dpToPx(10);
                                        produtoIdParam.gravity = Gravity.START;
                                        produtoId.setLayoutParams(produtoIdParam);
                                        produtoId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                        produtoId.setTextColor(Color.parseColor("#242424"));
                                        produtoId.setText(String.valueOf(vendaProduto.getPdt_id()));

                                        row.addView(produtoId);

                                        TextView produtoNome = new TextView(row.getContext());
                                        TableRow.LayoutParams produtoNomeParam = new TableRow.LayoutParams(
                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                TableRow.LayoutParams.WRAP_CONTENT
                                        );
                                        produtoNomeParam.weight = 3f;
                                        produtoNomeParam.leftMargin = dpToPx(20);
                                        produtoNomeParam.gravity = Gravity.START;
                                        produtoNome.setLayoutParams(produtoNomeParam);
                                        produtoNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                        produtoNome.setTextColor(Color.parseColor("#242424"));

                                        TextView unMedida = new TextView(row.getContext());
                                        TableRow.LayoutParams unMedidaParam = new TableRow.LayoutParams(
                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                TableRow.LayoutParams.WRAP_CONTENT
                                        );
                                        unMedidaParam.weight = 1f;
                                        unMedidaParam.leftMargin = dpToPx(20);
                                        unMedidaParam.gravity = Gravity.START;
                                        unMedida.setLayoutParams(unMedidaParam);
                                        unMedida.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                        unMedida.setTextColor(Color.parseColor("#242424"));


                                        for (Produto produto : produtos) {
                                            if (produto.getId() == vendaProduto.getPdt_id()) {
                                                produtoNome.setText(produto.getNome());
                                                unMedida.setText(produto.getUnMedida());
                                                row.addView(produtoNome);
                                                row.addView(unMedida);
                                            }
                                        }

                                        TextView quantidade = new TextView(row.getContext());
                                        TableRow.LayoutParams quantidadeParam = new TableRow.LayoutParams(
                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                TableRow.LayoutParams.WRAP_CONTENT
                                        );
                                        quantidadeParam.weight = 1f;
                                        quantidadeParam.leftMargin = dpToPx(20);
                                        quantidadeParam.gravity = Gravity.START;
                                        quantidade.setLayoutParams(quantidadeParam);
                                        quantidade.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                        quantidade.setTextColor(Color.parseColor("#242424"));
                                        quantidade.setText(String.valueOf(vendaProduto.getQuant()));

                                        row.addView(quantidade);

                                        tabelaRelatorio.addView(row);
                                    }
                                }
                                LinearLayout bordaHeader3 = new LinearLayout(layoutRelatorio.getContext());
                                LinearLayout.LayoutParams bordaHeader3Param = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT
                                );
                                cor = ContextCompat.getColor(getContext(), R.color.verde_fundo);
                                bordaHeader3.setBackgroundColor(cor);
                                TextView txtBorda = new TextView(bordaHeader3.getContext());
                                LinearLayout.LayoutParams txtBordaParam = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                txtBordaParam.weight = 1f;
                                txtBordaParam.leftMargin = dpToPx(20);
                                txtBordaParam.gravity = Gravity.START;
                                txtBorda.setLayoutParams(txtBordaParam);
                                txtBorda.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                                txtBorda.setTextColor(cor);
                                txtBorda.setText("aaaaa");
                                bordaHeader3.addView(txtBorda);

                                layoutRelatorio.addView(tabelaRelatorio);
                                layoutRelatorio.addView(bordaHeader3);
                            }
                        }
                    });


            } catch (InterruptedException e) {
                Log.e("RelatoriosFragment", "Thread de verificação interrompida: " + e.getMessage());
            }
        }).start();


    }

    private void relatorioProducao() {
        lblTitulo.setText("Relatório de produções");
        LinearLayout layoutRelatorio = binding.layoutRelatorio;
        layoutRelatorio.removeAllViews();

        Call<List<Producao>> callProducao = apiService.getProducoes();
        callProducao.enqueue(new Callback<List<Producao>>() {
            @Override
            public void onResponse(Call<List<Producao>> call, Response<List<Producao>> response) {
                if (response.isSuccessful()) {
                    List<Producao> producoes = response.body();

                    Gson gson = new Gson();
                    Log.d("RelatoriosFragment", "producoes: " + gson.toJson(producoes));
                    for (Producao producao : producoes) {
                        //Criar Linear layout com  id e data do produto e dropdown pra expandir a TableView
                        LinearLayout producaoHeader = new LinearLayout(layoutRelatorio.getContext());
                        LinearLayout.LayoutParams producaoHeaderParam = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        producaoHeaderParam.topMargin = dpToPx(10);
                        producaoHeader.setOrientation(LinearLayout.HORIZONTAL);
                        int cor = ContextCompat.getColor(getContext(), R.color.verde_forte_claro);
                        producaoHeader.setTag("producaoHeader");
                        producaoHeader.setBackgroundColor(cor);
                        producaoHeader.setLayoutParams(producaoHeaderParam);


                        TextView colProdutoId = new TextView(producaoHeader.getContext());
                        LinearLayout.LayoutParams colProdutoIdParam = new LinearLayout.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colProdutoIdParam.leftMargin = 20;
                        colProdutoIdParam.gravity = Gravity.START;
                        colProdutoId.setLayoutParams(colProdutoIdParam);
                        colProdutoId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colProdutoId.setTextColor(Color.parseColor("#242424"));
                        colProdutoId.setText("Producao ID: " + String.valueOf(producao.getId()));
                        producaoHeader.addView(colProdutoId);

                        TextView colDataProd = new TextView(producaoHeader.getContext());
                        LinearLayout.LayoutParams colDataProdParam = new LinearLayout.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colDataProdParam.leftMargin = 20;
                        colDataProdParam.gravity = Gravity.START;
                        colDataProd.setLayoutParams(colDataProdParam);
                        colDataProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colDataProd.setTextColor(Color.parseColor("#242424"));
                        colDataProd.setText("Data da Produção");
                        producaoHeader.addView(colDataProd);

                        colDataProd = new TextView(producaoHeader.getContext());
                        colDataProdParam = new LinearLayout.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colDataProdParam.leftMargin = 20;
                        colDataProdParam.gravity = Gravity.START;
                        colDataProd.setLayoutParams(colDataProdParam);
                        colDataProd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colDataProd.setTextColor(Color.parseColor("#242424"));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        colDataProd.setText(sdf.format(producao.getDataProd()));
                        producaoHeader.addView(colDataProd);

                        layoutRelatorio.addView(producaoHeader);
                        //Criar TableView nova
                        TableLayout tabelaRelatorio = new TableLayout(layoutRelatorio.getContext());
                        LinearLayout.LayoutParams tabelaRelatorioParam = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        tabelaRelatorio.setLayoutParams(tabelaRelatorioParam);
                        tabelaRelatorio.setTag("tabelaRelatorio");
                        layoutRelatorio.addView(tabelaRelatorio);
                        Context contextTabela = tabelaRelatorio.getContext();

                        TableRow row = new TableRow(contextTabela);
                        Context contextRow = row.getContext();

                        TextView colId = new TextView(contextRow);
                        TableRow.LayoutParams colIdParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colIdParam.weight = 0.5f;
                        colIdParam.leftMargin = dpToPx(10);
                        colIdParam.gravity = Gravity.START;
                        colId.setLayoutParams(colIdParam);
                        colId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                        colId.setTextColor(Color.parseColor("#242424"));
                        colId.setText("ID");

                        TextView colNome = new TextView(contextRow);
                        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colNomeParam.weight = 6f;
                        colNomeParam.leftMargin = 20;
                        colNomeParam.gravity = Gravity.START;
                        colNome.setLayoutParams(colNomeParam);
                        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colNome.setTextColor(Color.parseColor("#242424"));
                        colNome.setText("Nome");

                        TextView colUnidade = new TextView(contextRow);
                        TableRow.LayoutParams colUnidadeParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colUnidadeParam.weight = 1f;
                        colUnidadeParam.leftMargin = 20;
                        colUnidadeParam.gravity = Gravity.START;
                        colUnidade.setLayoutParams(colUnidadeParam);
                        colUnidade.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colUnidade.setTextColor(Color.parseColor("#242424"));
                        colUnidade.setText("Unidade");

                        TextView colQuant = new TextView(contextRow);
                        TableRow.LayoutParams colQuantParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colQuantParam.weight = 1f;
                        colQuantParam.leftMargin = 20;
                        colQuantParam.gravity = Gravity.START;
                        colQuant.setLayoutParams(colQuantParam);
                        colQuant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colQuant.setTextColor(Color.parseColor("#242424"));
                        colQuant.setText("Quantidade");

                        row.addView(colId);
                        row.addView(colNome);
                        row.addView(colUnidade);
                        row.addView(colQuant);

                        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
                        tabelaRelatorio.addView(row);


                        // GET PRODUTOS
                        Call<List<ProducaoProdutos>> callProdutos = apiService.getProducaoProdutos();
                        callProdutos.enqueue(new Callback<List<ProducaoProdutos>>() {
                            @Override
                            public void onResponse(Call<List<ProducaoProdutos>> call, Response<List<ProducaoProdutos>> response) {
                                if (response.isSuccessful()) {
                                    List<ProducaoProdutos> producaoProdutos = response.body();
                                    for (ProducaoProdutos producaoProduto : producaoProdutos) {
                                        // Para cada Produto criar uma Table row com dados
                                        TableRow row = new TableRow(contextTabela);
                                        Context contextRow = row.getContext();
                                        if(producaoProduto.getPdc_id() == producao.getId()) {
                                            Call<Produto> callProduto = apiService.getProduto(producaoProduto.getPdt_id());
                                            callProduto.enqueue(new Callback<Produto>() {
                                                @Override
                                                public void onResponse(Call<Produto> call, Response<Produto> response) {
                                                    Log.d("RelatorioFragment", "REsponse estoque" + response.body());
                                                    if (response.isSuccessful()) {
                                                        Produto produto = response.body();

                                                        TextView colId = new TextView(contextRow);
                                                        TableRow.LayoutParams colIdParam = new TableRow.LayoutParams(
                                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                                TableRow.LayoutParams.WRAP_CONTENT
                                                        );
                                                        colIdParam.weight = 0.5f;
                                                        colIdParam.leftMargin = dpToPx(10);
                                                        colIdParam.gravity = Gravity.START;
                                                        colId.setLayoutParams(colIdParam);
                                                        colId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                                        colId.setTextColor(Color.parseColor("#242424"));
                                                        colId.setText(String.valueOf(producaoProduto.getPdt_id()));
                                                        row.addView(colId);

                                                        Log.d("RelatorioFragment", "Produto nome e id" + produto.getId() + produto.getNome());
                                                        TextView colNome = new TextView(contextRow);
                                                        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                                TableRow.LayoutParams.WRAP_CONTENT
                                                        );
                                                        colNomeParam.weight = 6f;
                                                        colNomeParam.leftMargin = 20;
                                                        colNomeParam.gravity = Gravity.START;
                                                        colNome.setLayoutParams(colNomeParam);
                                                        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                                        colNome.setTextColor(Color.parseColor("#242424"));
                                                        colNome.setText(produto.getNome());
                                                        row.addView(colNome);


                                                        TextView colUnidade = new TextView(contextRow);
                                                        TableRow.LayoutParams colUnidadeParam = new TableRow.LayoutParams(
                                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                                TableRow.LayoutParams.WRAP_CONTENT
                                                        );
                                                        colUnidadeParam.weight = 1f;
                                                        colUnidadeParam.leftMargin = 20;
                                                        colUnidadeParam.gravity = Gravity.START;
                                                        colUnidade.setLayoutParams(colUnidadeParam);
                                                        colUnidade.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                                        colUnidade.setTextColor(Color.parseColor("#242424"));
                                                        colUnidade.setText(produto.getUnMedida());
                                                        row.addView(colUnidade);

                                                        TextView colQuant = new TextView(contextRow);
                                                        TableRow.LayoutParams colQuantParam = new TableRow.LayoutParams(
                                                                TableRow.LayoutParams.WRAP_CONTENT,
                                                                TableRow.LayoutParams.WRAP_CONTENT
                                                        );
                                                        colQuantParam.weight = 1f;
                                                        colQuantParam.leftMargin = 20;
                                                        colQuantParam.gravity = Gravity.START;
                                                        colQuant.setLayoutParams(colQuantParam);
                                                        colQuant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                                        colQuant.setTextColor(Color.parseColor("#242424"));
                                                        colQuant.setText(producaoProduto.getQuant().toString());
                                                        row.addView(colQuant);

                                                        Log.d("RelatorioFragment", "Produto id" + producaoProduto.getPdt_id());

                                                        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
                                                        tabelaRelatorio.addView(row);

                                                    } else {

                                                        Log.d("RelatorioFragment", "Erro " + response.errorBody());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Produto> call, Throwable t) {

                                                    Log.d("RelatorioFragment", "Erro " + t.getMessage());
                                                }
                                            });
                                        }
                                    }
                                }

                            }
                            @Override
                            public void onFailure(Call<List<ProducaoProdutos>> call, Throwable t) {

                            }
                        });

                        // Criar Cabeçalho com weights fixos

                        // GET VENDAS e CLIENTES

                        // Para cada Venda criar uma Table row com dados
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Producao>> call, Throwable t) {
            }
        });

    }

    private void relatorioProdutos() {
        lblTitulo.setText("Relatório de Produtos");

        Context contextTabela = tabelaRelatorio.getContext();
        tabelaRelatorio.removeAllViews();

        // Criar Cabeçalho com weights fixos
        TableRow row = new TableRow(contextTabela);
        Context contextRow = row.getContext();

        TextView colId = new TextView(contextRow);
        TableRow.LayoutParams colIdParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colIdParam.weight = 0.5f;
        colIdParam.gravity = Gravity.START;
        colId.setLayoutParams(colIdParam);
        colId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colId.setTextColor(Color.parseColor("#242424"));
        colId.setText("ID");

        TextView colNome = new TextView(contextRow);
        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colNomeParam.weight = 6f;
        colNomeParam.leftMargin = 20;
        colNomeParam.gravity = Gravity.START;
        colNome.setLayoutParams(colNomeParam);
        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colNome.setTextColor(Color.parseColor("#242424"));
        colNome.setText("Nome");

        TextView colUnidade = new TextView(contextRow);
        TableRow.LayoutParams colUnidadeParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colUnidadeParam.weight = 1f;
        colUnidadeParam.leftMargin = 20;
        colUnidadeParam.gravity = Gravity.START;
        colUnidade.setLayoutParams(colUnidadeParam);
        colUnidade.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colUnidade.setTextColor(Color.parseColor("#242424"));
        colUnidade.setText("Unidade");

        TextView colPreco = new TextView(contextRow);
        TableRow.LayoutParams colPrecoParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colPrecoParam.weight = 1f;
        colPrecoParam.leftMargin = 20;
        colPrecoParam.gravity = Gravity.START;
        colPreco.setLayoutParams(colPrecoParam);
        colPreco.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colPreco.setTextColor(Color.parseColor("#242424"));
        colPreco.setText("Preco Un");

        row.addView(colId);
        row.addView(colNome);
        row.addView(colUnidade);
        row.addView(colPreco);

        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
        tabelaRelatorio.addView(row);

        // GET PRODUTOS
        Call<List<Produto>> callProdutos = apiService.getProdutos();
        callProdutos.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful()) {
                    List<Produto> produtos = response.body();
                    for (Produto produto : produtos) {
                        // Para cada Produto criar uma Table row com dados
                        TableRow row = new TableRow(contextTabela);
                        Context contextRow = row.getContext();

                        TextView colId = new TextView(contextRow);
                        TableRow.LayoutParams colIdParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colIdParam.weight = 0.5f;
                        colIdParam.gravity = Gravity.START;
                        colId.setLayoutParams(colIdParam);
                        colId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colId.setTextColor(Color.parseColor("#242424"));
                        colId.setText(String.valueOf(produto.getId()));

                        TextView colNome = new TextView(contextRow);
                        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colNomeParam.weight = 6f;
                        colNomeParam.leftMargin = 20;
                        colNomeParam.gravity = Gravity.START;
                        colNome.setLayoutParams(colNomeParam);
                        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colNome.setTextColor(Color.parseColor("#242424"));
                        colNome.setText(produto.getNome());

                        TextView colUnidade = new TextView(contextRow);
                        TableRow.LayoutParams colUnidadeParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colUnidadeParam.weight = 1f;
                        colUnidadeParam.leftMargin = 20;
                        colUnidadeParam.gravity = Gravity.START;
                        colUnidade.setLayoutParams(colUnidadeParam);
                        colUnidade.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colUnidade.setTextColor(Color.parseColor("#242424"));
                        colUnidade.setText(produto.getUnMedida());

                        TextView colPreco = new TextView(contextRow);
                        TableRow.LayoutParams colPrecoParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colPrecoParam.weight = 1f;
                        colPrecoParam.leftMargin = 20;
                        colPrecoParam.gravity = Gravity.START;
                        colPreco.setLayoutParams(colPrecoParam);
                        colPreco.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colPreco.setTextColor(Color.parseColor("#242424"));
                        colPreco.setText(String.valueOf(produto.getPrecoUn()));

                        row.addView(colId);
                        row.addView(colNome);
                        row.addView(colUnidade);
                        row.addView(colPreco);

                        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
                        tabelaRelatorio.addView(row);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {

            }
        });
    }

    private void relatorioEstoque() {
        lblTitulo.setText("Relatório de estoque");

        Context contextTabela = tabelaRelatorio.getContext();
        tabelaRelatorio.removeAllViews();

        // Criar Cabeçalho com weights fixos
        TableRow row = new TableRow(contextTabela);
        Context contextRow = row.getContext();

        TextView colId = new TextView(contextRow);
        TableRow.LayoutParams colIdParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colIdParam.weight = 0.5f;
        colIdParam.gravity = Gravity.START;
        colId.setLayoutParams(colIdParam);
        colId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colId.setTextColor(Color.parseColor("#242424"));
        colId.setText("ID");

        TextView colNome = new TextView(contextRow);
        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colNomeParam.weight = 6f;
        colNomeParam.leftMargin = 20;
        colNomeParam.gravity = Gravity.START;
        colNome.setLayoutParams(colNomeParam);
        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colNome.setTextColor(Color.parseColor("#242424"));
        colNome.setText("Nome");

        TextView colUnidade = new TextView(contextRow);
        TableRow.LayoutParams colUnidadeParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colUnidadeParam.weight = 1f;
        colUnidadeParam.leftMargin = 20;
        colUnidadeParam.gravity = Gravity.START;
        colUnidade.setLayoutParams(colUnidadeParam);
        colUnidade.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colUnidade.setTextColor(Color.parseColor("#242424"));
        colUnidade.setText("Unidade");

        TextView colQuant = new TextView(contextRow);
        TableRow.LayoutParams colQuantParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colQuantParam.weight = 1f;
        colQuantParam.leftMargin = 20;
        colQuantParam.gravity = Gravity.START;
        colQuant.setLayoutParams(colQuantParam);
        colQuant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colQuant.setTextColor(Color.parseColor("#242424"));
        colQuant.setText("Quantidade");

        row.addView(colId);
        row.addView(colNome);
        row.addView(colUnidade);
        row.addView(colQuant);

        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
        tabelaRelatorio.addView(row);

        // GET PRODUTOS
        Call<List<Produto>> callProdutos = apiService.getProdutos();
        callProdutos.enqueue(new Callback<List<Produto>>() {
            @Override
            public void onResponse(Call<List<Produto>> call, Response<List<Produto>> response) {
                if (response.isSuccessful()) {
                    List<Produto> produtos = response.body();
                    for (Produto produto : produtos) {
                        // Para cada Produto criar uma Table row com dados
                        TableRow row = new TableRow(contextTabela);
                        Context contextRow = row.getContext();

                        TextView colId = new TextView(contextRow);
                        TableRow.LayoutParams colIdParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colIdParam.weight = 0.5f;
                        colIdParam.gravity = Gravity.START;
                        colId.setLayoutParams(colIdParam);
                        colId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colId.setTextColor(Color.parseColor("#242424"));
                        colId.setText(String.valueOf(produto.getId()));

                        TextView colNome = new TextView(contextRow);
                        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colNomeParam.weight = 6f;
                        colNomeParam.leftMargin = 20;
                        colNomeParam.gravity = Gravity.START;
                        colNome.setLayoutParams(colNomeParam);
                        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colNome.setTextColor(Color.parseColor("#242424"));
                        colNome.setText(produto.getNome());

                        TextView colUnidade = new TextView(contextRow);
                        TableRow.LayoutParams colUnidadeParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colUnidadeParam.weight = 1f;
                        colUnidadeParam.leftMargin = 20;
                        colUnidadeParam.gravity = Gravity.START;
                        colUnidade.setLayoutParams(colUnidadeParam);
                        colUnidade.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colUnidade.setTextColor(Color.parseColor("#242424"));
                        colUnidade.setText(produto.getUnMedida());

                        TextView colQuant = new TextView(contextRow);
                        TableRow.LayoutParams colQuantParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colQuantParam.weight = 1f;
                        colQuantParam.leftMargin = 20;
                        colQuantParam.gravity = Gravity.START;
                        colQuant.setLayoutParams(colQuantParam);
                        colQuant.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colQuant.setTextColor(Color.parseColor("#242424"));

                        Log.d("RelatorioFragment", "Produto id" + produto.getId());

                        Call<Estoque> callEstoque = apiService.getEstoque(produto.getId());
                        callEstoque.enqueue(new Callback<Estoque>() {
                            @Override
                            public void onResponse(Call<Estoque> call, Response<Estoque> response) {
                                Log.d("RelatorioFragment", "REsponse estoque" + response.body());
                                if (response.isSuccessful()) {
                                    Estoque estoque = response.body();
                                    Log.d("RelatorioFragment", "Estoque id e quant" + estoque.getPdtId() + estoque.getQuant().toString());
                                    colQuant.setText(String.valueOf(estoque.getQuant()).replace(",", "."));
                                } else {
                                    colQuant.setText(String.valueOf(00.00));
                                    Log.d("RelatorioFragment", "Erro " + response.errorBody());
                                }
                            }

                            @Override
                            public void onFailure(Call<Estoque> call, Throwable t) {
                                colQuant.setText(String.valueOf(00.00));
                                Log.d("RelatorioFragment", "Erro " + t.getMessage());
                            }
                        });

                        row.addView(colId);
                        row.addView(colNome);
                        row.addView(colUnidade);
                        row.addView(colQuant);

                        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
                        tabelaRelatorio.addView(row);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Produto>> call, Throwable t) {

            }
        });
    }

    public void relatorioUsuarios() {
        lblTitulo.setText("Relatório de usuarios");

        Context contextTabela = tabelaRelatorio.getContext();
        tabelaRelatorio.removeAllViews();

        // Criar Cabeçalho com weights fixos
        TableRow row = new TableRow(contextTabela);
        Context contextRow = row.getContext();

        TextView colId = new TextView(contextRow);
        TableRow.LayoutParams colIdParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colIdParam.weight = 0.5f;
        colIdParam.gravity = Gravity.START;
        colId.setLayoutParams(colIdParam);
        colId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colId.setTextColor(Color.parseColor("#242424"));
        colId.setText("ID");

        TextView colNome = new TextView(contextRow);
        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colNomeParam.weight = 4f;
        colNomeParam.leftMargin = 20;
        colNomeParam.gravity = Gravity.START;
        colNome.setLayoutParams(colNomeParam);
        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colNome.setTextColor(Color.parseColor("#242424"));
        colNome.setText("Nome");

        TextView colLogin = new TextView(contextRow);
        TableRow.LayoutParams colLoginParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colLoginParam.weight = 4f;
        colLoginParam.leftMargin = 20;
        colLoginParam.gravity = Gravity.START;
        colLogin.setLayoutParams(colLoginParam);
        colLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colLogin.setTextColor(Color.parseColor("#242424"));
        colLogin.setText("Login");

        TextView colCargo = new TextView(contextRow);
        TableRow.LayoutParams colCargoParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colCargoParam.weight = 1f;
        colCargoParam.leftMargin = 20;
        colCargoParam.gravity = Gravity.START;
        colCargo.setLayoutParams(colCargoParam);
        colCargo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        colCargo.setTextColor(Color.parseColor("#242424"));
        colCargo.setText("Cargo");

        row.addView(colId);
        row.addView(colNome);
        row.addView(colLogin);
        row.addView(colCargo);

        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
        tabelaRelatorio.addView(row);

        // GET PRODUTOS
        Call<List<Usuario>> callUsuarios = apiService.getUsuarios();
        callUsuarios.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    List<Usuario> usuarios = response.body();
                    for (Usuario usuario : usuarios) {
                        // Para cada Produto criar uma Table row com dados
                        TableRow row = new TableRow(contextTabela);
                        Context contextRow = row.getContext();

                        TextView colId = new TextView(contextRow);
                        TableRow.LayoutParams colIdParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colIdParam.weight = 0.5f;
                        colIdParam.gravity = Gravity.START;
                        colId.setLayoutParams(colIdParam);
                        colId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colId.setTextColor(Color.parseColor("#242424"));
                        colId.setText(String.valueOf(usuario.getId()));

                        TextView colNome = new TextView(contextRow);
                        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colNomeParam.weight = 4f;
                        colNomeParam.leftMargin = 20;
                        colNomeParam.gravity = Gravity.START;
                        colNome.setLayoutParams(colNomeParam);
                        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colNome.setTextColor(Color.parseColor("#242424"));
                        colNome.setText(usuario.getNome());

                        TextView colLogin = new TextView(contextRow);
                        TableRow.LayoutParams colLoginParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colLoginParam.weight = 4f;
                        colLoginParam.leftMargin = 20;
                        colLoginParam.gravity = Gravity.START;
                        colLogin.setLayoutParams(colLoginParam);
                        colLogin.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colLogin.setTextColor(Color.parseColor("#242424"));
                        colLogin.setText(usuario.getLogin());

                        TextView colCargo = new TextView(contextRow);
                        TableRow.LayoutParams colCargoParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colCargoParam.weight = 1f;
                        colCargoParam.leftMargin = 20;
                        colCargoParam.gravity = Gravity.START;
                        colCargo.setLayoutParams(colCargoParam);
                        colCargo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        colCargo.setTextColor(Color.parseColor("#242424"));
                        colCargo.setText(String.valueOf(usuario.getCargo()));

                        row.addView(colId);
                        row.addView(colNome);
                        row.addView(colLogin);
                        row.addView(colCargo);

                        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
                        tabelaRelatorio.addView(row);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });
    }

    public void relatorioClientes() {
        lblTitulo.setText("Relatório de clientes");

        Context contextTabela = tabelaRelatorio.getContext();
        tabelaRelatorio.removeAllViews();

        // Criar Cabeçalho com weights fixos
        TableRow row = new TableRow(contextTabela);
        Context contextRow = row.getContext();

        TextView colCpf = new TextView(contextRow);
        TableRow.LayoutParams colCpfParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colCpfParam.weight = 2f;
        colCpfParam.gravity = Gravity.START;
        colCpf.setLayoutParams(colCpfParam);
        colCpf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colCpf.setTextColor(Color.parseColor("#242424"));
        colCpf.setText("CPF");

        TextView colNome = new TextView(contextRow);
        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colNomeParam.weight = 3f;
        colNomeParam.leftMargin = 20;
        colNomeParam.gravity = Gravity.START;
        colNome.setLayoutParams(colNomeParam);
        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colNome.setTextColor(Color.parseColor("#242424"));
        colNome.setText("Nome");

        TextView colTelefone = new TextView(contextRow);
        TableRow.LayoutParams colTelefoneParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colTelefoneParam.weight = 2f;
        colTelefoneParam.leftMargin = 20;
        colTelefoneParam.gravity = Gravity.START;
        colTelefone.setLayoutParams(colTelefoneParam);
        colTelefone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colTelefone.setTextColor(Color.parseColor("#242424"));
        colTelefone.setText("Telefone");

        TextView colEmail = new TextView(contextRow);
        TableRow.LayoutParams colEmailParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colEmailParam.weight = 4f;
        colEmailParam.leftMargin = 20;
        colEmailParam.gravity = Gravity.START;
        colEmail.setLayoutParams(colEmailParam);
        colEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colEmail.setTextColor(Color.parseColor("#242424"));
        colEmail.setText("Email");

        TextView colDtNasc = new TextView(contextRow);
        TableRow.LayoutParams colDtNascParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colDtNascParam.weight = 2f;
        colDtNascParam.leftMargin = 20;
        colDtNascParam.gravity = Gravity.START;
        colDtNasc.setLayoutParams(colDtNascParam);
        colDtNasc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colDtNasc.setTextColor(Color.parseColor("#242424"));
        colDtNasc.setText("Nascimento");

        TextView colGenero = new TextView(contextRow);
        TableRow.LayoutParams colGeneroParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        colGeneroParam.weight = 2f;
        colGeneroParam.leftMargin = 20;
        colGeneroParam.gravity = Gravity.START;
        colGenero.setLayoutParams(colGeneroParam);
        colGenero.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colGenero.setTextColor(Color.parseColor("#242424"));
        colGenero.setText("Genero");

        row.addView(colCpf);
        row.addView(colNome);
        row.addView(colTelefone);
        row.addView(colEmail);
        row.addView(colDtNasc);
        row.addView(colGenero);

        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
        tabelaRelatorio.addView(row);

        // GET Cliente

        Call<List<Cliente>> callClientes = apiService.getClientes();
        callClientes.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful()) {
                    List<Cliente> clientes = response.body();
                    for (Cliente cliente : clientes) {
                        // Para cada Produto criar uma Table row com dados
                        TableRow row = new TableRow(contextTabela);
                        Context contextRow = row.getContext();

                        TextView colCpf = new TextView(contextRow);
                        TableRow.LayoutParams colCpfParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colCpfParam.weight = 2f;
                        colCpfParam.gravity = Gravity.START;
                        colCpf.setLayoutParams(colCpfParam);
                        colCpf.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colCpf.setTextColor(Color.parseColor("#242424"));
                        colCpf.setText(cliente.getCpf());

                        TextView colNome = new TextView(contextRow);
                        TableRow.LayoutParams colNomeParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colNomeParam.weight = 3f;
                        colNomeParam.leftMargin = 20;
                        colNomeParam.gravity = Gravity.START;
                        colNome.setLayoutParams(colNomeParam);
                        colNome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colNome.setTextColor(Color.parseColor("#242424"));
                        colNome.setText(cliente.getNome());

                        TextView colTelefone = new TextView(contextRow);
                        TableRow.LayoutParams colTelefoneParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colTelefoneParam.weight = 2f;
                        colTelefoneParam.leftMargin = 20;
                        colTelefoneParam.gravity = Gravity.START;
                        colTelefone.setLayoutParams(colTelefoneParam);
                        colTelefone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colTelefone.setTextColor(Color.parseColor("#242424"));
                        colTelefone.setText(cliente.getTelefone());

                        TextView colEmail = new TextView(contextRow);
                        TableRow.LayoutParams colEmailParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colEmailParam.weight = 4f;
                        colEmailParam.leftMargin = 20;
                        colEmailParam.gravity = Gravity.START;
                        colEmail.setLayoutParams(colEmailParam);
                        colEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colEmail.setTextColor(Color.parseColor("#242424"));
                        colEmail.setText(cliente.getEmail());

                        TextView colDtNasc = new TextView(contextRow);
                        TableRow.LayoutParams colDtNascParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colDtNascParam.weight = 2f;
                        colDtNascParam.leftMargin = 20;
                        colDtNascParam.gravity = Gravity.START;
                        colDtNasc.setLayoutParams(colDtNascParam);
                        colDtNasc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colDtNasc.setTextColor(Color.parseColor("#242424"));
                        colDtNasc.setText(cliente.getDataNasc().replaceAll("-", "/"));

                        TextView colGenero = new TextView(contextRow);
                        TableRow.LayoutParams colGeneroParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        colGeneroParam.weight = 2f;
                        colGeneroParam.leftMargin = 20;
                        colGeneroParam.gravity = Gravity.START;
                        colGenero.setLayoutParams(colGeneroParam);
                        colGenero.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colGenero.setTextColor(Color.parseColor("#242424"));
                        if (cliente.getGenero() == 'M') {
                            colGenero.setText("Masculino");
                        } else if (cliente.getGenero() == 'F') {
                            colGenero.setText("Feminino");
                        } else {
                            colGenero.setText("Outro");
                        }

                        row.addView(colCpf);
                        row.addView(colNome);
                        row.addView(colTelefone);
                        row.addView(colEmail);
                        row.addView(colDtNasc);
                        row.addView(colGenero);

                        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
                        tabelaRelatorio.addView(row);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {

            }
        });
    }

    public void relatorioFornecedores() {
        lblTitulo.setText("Relatório de fornecedores");

        Context contextTabela = tabelaRelatorio.getContext();
        tabelaRelatorio.removeAllViews();

        // Criar Cabeçalho com weights fixos
        TableRow row = new TableRow(contextTabela);
        Context contextRow = row.getContext();

        TextView colCnpj = new TextView(contextRow);
        TableRow.LayoutParams colCnpjParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        //colCnpjParam.weight = 2.5f;
        colCnpjParam.gravity = Gravity.START;
        colCnpj.setLayoutParams(colCnpjParam);
        colCnpj.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colCnpj.setTextColor(Color.parseColor("#242424"));
        colCnpj.setText("CNPJ");

        TextView colNomeFantasia = new TextView(contextRow);
        TableRow.LayoutParams colNomeFantasiaParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        //colNomeFantasiaParam.weight = 3f;
        colNomeFantasiaParam.leftMargin = 20;
        colNomeFantasiaParam.gravity = Gravity.START;
        colNomeFantasia.setLayoutParams(colNomeFantasiaParam);
        colNomeFantasia.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colNomeFantasia.setTextColor(Color.parseColor("#242424"));
        colNomeFantasia.setText("Nome");

        TextView colRazaoSocial = new TextView(contextRow);
        TableRow.LayoutParams colRazaoSocialParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        //colRazaoSocialParam.weight = 3f;
        colRazaoSocialParam.leftMargin = 20;
        colRazaoSocialParam.gravity = Gravity.START;
        colRazaoSocial.setLayoutParams(colRazaoSocialParam);
        colRazaoSocial.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colRazaoSocial.setTextColor(Color.parseColor("#242424"));
        colRazaoSocial.setText("Razão social");

        TextView colTelefone = new TextView(contextRow);
        TableRow.LayoutParams colTelefoneParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        //colTelefoneParam.weight = 2f;
        colTelefoneParam.leftMargin = 20;
        colTelefoneParam.gravity = Gravity.START;
        colTelefone.setLayoutParams(colTelefoneParam);
        colTelefone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colTelefone.setTextColor(Color.parseColor("#242424"));
        colTelefone.setText("Telefone");

        TextView colEmail = new TextView(contextRow);
        TableRow.LayoutParams colEmailParam = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        //colEmailParam.weight = 4f;
        colEmailParam.leftMargin = 20;
        colEmailParam.gravity = Gravity.START;
        colEmail.setLayoutParams(colEmailParam);
        colEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        colEmail.setTextColor(Color.parseColor("#242424"));
        colEmail.setText("Email");


        row.addView(colCnpj);
        row.addView(colNomeFantasia);
        row.addView(colRazaoSocial);
        row.addView(colTelefone);
        row.addView(colEmail);


        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
        tabelaRelatorio.addView(row);

        // GET Fornecedores

        Call<List<Fornecedor>> callFornecedores = apiService.getFornecedores();
        callFornecedores.enqueue(new Callback<List<Fornecedor>>() {
            @Override
            public void onResponse(Call<List<Fornecedor>> call, Response<List<Fornecedor>> response) {
                if (response.isSuccessful()) {
                    List<Fornecedor> fornecedores = response.body();
                    for (Fornecedor fornecedor : fornecedores) {
                        // Para cada Produto criar uma Table row com dados
                        TableRow row = new TableRow(contextTabela);
                        Context contextRow = row.getContext();

                        TextView colCnpj = new TextView(contextRow);
                        TableRow.LayoutParams colCnpjParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        //colCnpjParam.weight = 2.5f;
                        colCnpjParam.gravity = Gravity.START;
                        colCnpj.setLayoutParams(colCnpjParam);
                        colCnpj.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colCnpj.setTextColor(Color.parseColor("#242424"));
                        colCnpj.setText(fornecedor.getCnpj());

                        TextView colNomeFantasia = new TextView(contextRow);
                        TableRow.LayoutParams colNomeFantasiaParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        //colNomeFantasiaParam.weight = 3f;
                        colNomeFantasiaParam.leftMargin = 20;
                        colNomeFantasiaParam.gravity = Gravity.START;
                        colNomeFantasia.setLayoutParams(colNomeFantasiaParam);
                        colNomeFantasia.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colNomeFantasia.setTextColor(Color.parseColor("#242424"));
                        colNomeFantasia.setText(fornecedor.getNomeFantasia());

                        TextView colTelefone = new TextView(contextRow);
                        TableRow.LayoutParams colTelefoneParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        // colTelefoneParam.weight = 2f;
                        colTelefoneParam.leftMargin = 20;
                        colTelefoneParam.gravity = Gravity.START;
                        colTelefone.setLayoutParams(colTelefoneParam);
                        colTelefone.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colTelefone.setTextColor(Color.parseColor("#242424"));
                        colTelefone.setText(fornecedor.getTelefone());

                        TextView colEmail = new TextView(contextRow);
                        TableRow.LayoutParams colEmailParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        //colEmailParam.weight = 5f;
                        colEmailParam.leftMargin = 20;
                        colEmailParam.gravity = Gravity.START;
                        colEmail.setLayoutParams(colEmailParam);
                        colEmail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colEmail.setTextColor(Color.parseColor("#242424"));
                        colEmail.setText(fornecedor.getEmail());

                        TextView colRazaoSocial = new TextView(contextRow);
                        TableRow.LayoutParams colRazaoSocialParam = new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                        );
                        //colRazaoSocialParam.weight = 3f;
                        colRazaoSocialParam.leftMargin = 20;
                        colRazaoSocialParam.gravity = Gravity.START;
                        colRazaoSocial.setLayoutParams(colRazaoSocialParam);
                        colRazaoSocial.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        colRazaoSocial.setTextColor(Color.parseColor("#242424"));
                        colRazaoSocial.setText(fornecedor.getRazaoSocial());

                        row.addView(colCnpj);
                        row.addView(colNomeFantasia);
                        row.addView(colRazaoSocial);
                        row.addView(colTelefone);
                        row.addView(colEmail);


                        tabelaRelatorio.setBackgroundColor(requireActivity().getColor(R.color.verde_fundo));
                        tabelaRelatorio.addView(row);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Fornecedor>> call, Throwable t) {

            }
        });
    }


}







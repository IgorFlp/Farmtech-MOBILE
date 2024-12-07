package com.example.farmtech_mobile.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.example.farmtech_mobile.data.model.Cliente;
import com.example.farmtech_mobile.data.model.ClienteEndereco;
import com.example.farmtech_mobile.data.model.Cupom;
import com.example.farmtech_mobile.data.model.Estoque;
import com.example.farmtech_mobile.data.model.Fornecedor;
import com.example.farmtech_mobile.data.model.FornecedorEndereco;
import com.example.farmtech_mobile.data.model.Producao;
import com.example.farmtech_mobile.data.model.ProducaoProdutos;
import com.example.farmtech_mobile.data.model.Produto;
import com.example.farmtech_mobile.data.model.Usuario;
import com.example.farmtech_mobile.data.model.Venda;
import com.example.farmtech_mobile.data.model.VendaProdutos;


public interface ApiService {

    //CLIENTES
    @GET("Clientes")
    Call<List<Cliente>> getClientes();

    @GET("Clientes/{cpf}")
    Call<Cliente> getCliente(@Path("cpf") String cpf);

    @POST("Clientes")
    Call<Cliente> criarCliente(@Body Cliente cliente);

    @PUT("Clientes/{cpf}")
    Call<Void> atualizarCliente(@Path("cpf") String cpf,@Body Cliente cliente );

    @DELETE("Clientes/{cpf}")
    Call<Cliente> deleteCliente(@Path("cpf") String cpf);

    //CLIENTE ENDERECOS

    @GET("ClientesEnderecos")
    Call<List<ClienteEndereco>> getClientesEnderecos();

    @GET("ClientesEnderecos/{cl_cpf}")
    Call<ClienteEndereco> getClienteEndereco(@Path("cl_cpf") String cl_cpf);

    @POST("ClientesEnderecos")
    Call<ClienteEndereco> criarClienteEndereco(@Body ClienteEndereco clienteEndereco);
    @PUT("ClientesEnderecos/{cl_cpf}")
    Call<ClienteEndereco> atualizarClienteEndereco(@Path("cl_cpf") String cl_cpf,@Body ClienteEndereco clienteEndereco);

    @DELETE("ClientesEnderecos/{cl_cpf}")
    Call<ClienteEndereco> deleteClienteEndereco(@Path("cl_cpf") String cl_cpf);

    //FORNECEDORES

    @GET("Fornecedores")
    Call<List<Fornecedor>> getFornecedores();

    @GET("Fornecedores/{cnpj}")
    Call<Fornecedor> getFornecedor(@Path("cnpj") String cnpj);

    @POST("Fornecedores")
    Call<Fornecedor> criarFornecedor(@Body Fornecedor fornecedor);

    @PUT("Fornecedores/{cnpj}")
    Call<Fornecedor> atualizarFornecedor(@Path("cnpj") String cnpj,@Body Fornecedor fornecedor );

    @DELETE("Fornecedores/{cnpj}")
    Call<Fornecedor> deleteFornecedor(@Path("cnpj") String cnpj);

    //FORNECEDORES ENDERECOS

    @GET("FornecedoresEnderecos")
    Call<List<FornecedorEndereco>> getFornecedoresEnderecos();

    @GET("FornecedoresEnderecos/{frn_cnpj}")
    Call<FornecedorEndereco> getFornecedorEndereco(@Path("frn_cnpj") String frn_cnpj);

    @POST("FornecedoresEnderecos")
    Call<FornecedorEndereco> criarFornecedorEndereco(@Body FornecedorEndereco fornecedorEndereco);

    @PUT("FornecedoresEnderecos/{frn_cnpj}")
    Call<FornecedorEndereco> atualizarFornecedorEndereco(@Path("frn_cnpj") String frn_cnpj,@Body FornecedorEndereco fornecedorEndereco);

    @DELETE("FornecedoresEnderecos/{frn_cnpj}")
    Call<FornecedorEndereco> deleteFornecedorEndereco(@Path("frn_cnpj") String frn_cnpj);




    //USUARIO
    @GET("Usuario/logar")
    Call<Usuario> logar(@Query("login") String login, @Query("senha") String senha);

    @GET("Usuario")
    Call<List<Usuario>> getUsuarios();

    @GET("Usuario/{id}")
    Call<Usuario> getUsuario(@Path("id") Integer id);

    @POST("Usuario")
    Call<Usuario> criarUsuario(@Body Usuario usuario);

    @PUT("Usuario/{id}")
    Call<Usuario> atualizarUsuario(@Path("id") Integer id,@Body Usuario usuario);

    @DELETE("Usuario/{id}")
    Call<Usuario> deleteUsuario(@Path("id") Integer id);

    //PRODUTO
    @GET("Produto")
    Call<List<Produto>> getProdutos();

    @GET("Produto/{id}")
    Call<Produto> getProduto(@Path("id") Integer id);

    @POST("Produto")
    Call<Produto> criarProduto(@Body Produto produto);

    @PUT("Produto/{id}")
    Call<Produto> atualizarProduto(@Path("id") Integer id,@Body Produto produto);

    @DELETE("Produto/{id}")
    Call<Produto> deleteProduto(@Path("id") Integer id);

    //ESTOQUE
    @GET("Estoque")
    Call<List<Estoque>> getEstoques();

    @GET("Estoque/{id}")
    Call<Estoque> getEstoque(@Path("id") int id);

    @POST("Estoque")
    Call<Estoque> criarEstoque(@Body Estoque estoque);

    @PUT("Estoque/{id}")
    Call<Estoque> atualizarEstoque(@Path("id") Integer id,@Body Estoque estoque);

    @DELETE("Estoque/{id}")
    Call<Estoque> deleteEstoque(@Path("id") Integer id);

    @PUT("Estoque/Adicionar/{id}")
    Call<Void> adicionarEstoque(@Path("id") int id, @Query("quantidade") BigDecimal quantidade);

    @PUT("Estoque/Subtrair/{id}")
    Call<Void> subtrairEstoque(@Path("id") int id, @Query("quantidade") BigDecimal quantidade);

    //PRODUÇÃO

    @GET("Producao")
    Call<List<Producao>> getProducoes();
    /*
    @GET("Producao/{id}")
    Call<Producao> getProducao(@Path("id") Integer id);
    */
    @POST("Producao")
    Call<Producao> criarProducao(@Body Producao producao);
    /*
    @PUT("Produto/{id}")
    Call<Producao> atualizarProducao(@Path("id") Integer id,@Body Producao producao);

    @DELETE("Producao/{id}")
    Call<Producao> deleteProducao(@Path("id") Integer id);
    */
    //PRODUCAO PRODUTOS
    @GET("ProducaoProdutos")
    Call<List<ProducaoProdutos>> getProducaoProdutos();
    @POST("ProducaoProdutos")
    Call<ProducaoProdutos> criarProducaoProdutos(@Body ProducaoProdutos producaoProdutos);

    //CUPOM
    @GET("Cupom/{nome}")
    Call<Cupom> buscarCupom(@Path("nome") String nome);

    //VENDA
    @GET("Venda")
    Call<List<Venda>> getVendas();
    @POST("Venda")
    Call<Venda> criarVenda(@Body Venda venda);


    //VENDAPRODUTOS
    @GET("VendaProdutos")
    Call<List<VendaProdutos>> getVendaProdutos();
    @POST("VendaProdutos")
    Call<VendaProdutos> criarVendaProduto(@Body VendaProdutos vendaProdutos);
}

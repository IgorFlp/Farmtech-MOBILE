package com.example.farmtech_mobile.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;
import com.example.farmtech_mobile.data.model.Cliente;
import com.example.farmtech_mobile.data.model.ClienteEndereco;
import com.example.farmtech_mobile.data.model.Fornecedor;
import com.example.farmtech_mobile.data.model.FornecedorEndereco;
import com.example.farmtech_mobile.data.model.Usuario;


public interface ApiService {

    @GET("Clientes")
    Call<List<Cliente>> getClientes();

    @POST("Clientes")
    Call<Cliente> criarCliente(@Body Cliente cliente);

    @GET("ClientesEnderecos")
    Call<List<ClienteEndereco>> getClientesEnderecos();

    @POST("ClientesEnderecos")
    Call<ClienteEndereco> criarClienteEndereco(@Body ClienteEndereco clienteEndereco);

    @GET("Fornecedores")
    Call<List<Fornecedor>> getFornecedores();

    @POST("Fornecedores")
    Call<Fornecedor> criarFornecedor(@Body Fornecedor fornecedor);

    @GET("FornecedoresEnderecos")
    Call<List<FornecedorEndereco>> getFornecedoresEnderecos();

    @POST("FornecedoresEnderecos")
    Call<FornecedorEndereco> criarFornecedorEndereco(@Body FornecedorEndereco fornecedorEndereco);

    @GET("Usuario/logar")
    Call<Usuario> logar(@Query("login") String login, @Query("senha") String senha);

}

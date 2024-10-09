package com.example.farmtech_mobile.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    //CLIENTES
    @GET("Clientes")
    Call<List<Cliente>> getClientes();

    @GET("Clientes/{cpf}")
    Call<Cliente> getCliente(@Path("cpf") String cpf);

    @POST("Clientes")
    Call<Cliente> criarCliente(@Body Cliente cliente);

    @PUT("Clientes/{cpf}")
    Call<Cliente> atualizarCliente(@Path("cpf") String cpf,@Body Cliente cliente );

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

}

package com.example.farmtech_mobile.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;
import com.example.farmtech_mobile.data.model.Cliente;
import com.example.farmtech_mobile.data.model.ClienteEndereco;


public interface ApiService {

    @GET("Clientes")
    Call<List<Cliente>> getClientes();


    @POST("Clientes")
    Call<Cliente> criarCliente(@Body Cliente cliente);

    @GET("ClientesEnderecos")
    Call<List<ClienteEndereco>> getClientesEnderecos();


    @POST("ClientesEnderecos")
    Call<ClienteEndereco> criarClienteEndereco(@Body ClienteEndereco clienteEndereco);

}

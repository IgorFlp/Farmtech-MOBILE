package com.example.farmtech_mobile.ui.login;

import static java.security.AccessController.getContext;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;

import com.example.farmtech_mobile.api.RetrofitClient;
import com.example.farmtech_mobile.data.LoginRepository;
import com.example.farmtech_mobile.data.Result;
import com.example.farmtech_mobile.data.model.ClienteEndereco;
import com.example.farmtech_mobile.data.model.LoggedInUser;
import com.example.farmtech_mobile.R;
import com.example.farmtech_mobile.data.model.Usuario;
import com.example.farmtech_mobile.api.ApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    private MutableLiveData<LoggedInUser> loggedInUser = new MutableLiveData<>();
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    // Método para obter os dados do usuário logado
    public LiveData<LoggedInUser> getLoggedInUser() {
        return loggedInUser;
    }


    public void login(String username, String password) {
        // can be launched in a separate asynchronous job

        ApiService apiService = RetrofitClient.getApiService();
        Usuario usuario = new Usuario(0,username,password,null,null);
        Call<Usuario> call = apiService.logar(username,password);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    Log.d("LoginFragment", "Login efetuado com sucesso: " + response.body());
                    Usuario usuario = response.body();
                    LoggedInUser  loggedInUser = new LoggedInUser(String.valueOf(usuario.getId()), usuario.getNome());

                    Result result = new Result.Success(usuario);
                    if (result instanceof Result.Success) {
                        loginResult.setValue(new LoginResult(new LoggedInUserView(loggedInUser.getUserId(),loggedInUser.getDisplayName())));
                    }
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                    Log.e("UsuarioFragment", "Falha no login do endereco: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("UsuarioFragment", "Erro: " + t.getMessage());
            }
        });



    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
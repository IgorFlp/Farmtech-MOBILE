package com.example.farmtech_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;


import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.farmtech_mobile.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializando o ViewBinding antes de configurar os elementos
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.Main_toolbar);
        setSupportActionBar(toolbar);

        // Configurar NavController e AppBarConfiguration
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        // Sincronizar a Toolbar com o NavController
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Atualizar o título da Toolbar com base no Fragment atual
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (getSupportActionBar() != null) {
                // Pega o label definido no arquivo de navegação
                getSupportActionBar().setTitle(destination.getLabel());
            }
        });

        // Habilitar o botão "up"
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Definir o clique do botão "up"
        toolbar.setNavigationOnClickListener(v -> {
            NavController navControllerMain = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

                int idMain = navControllerMain.getCurrentDestination().getId();
            if (idMain == R.id.loginFragment) {
                finish();

            }else if (idMain == R.id.nav_ClienteNovo) {
                Intent intent = new Intent(MainActivity.this, SecundaryActivity.class);
                intent.putExtra("fragment", "Cliente");
                startActivity(intent);
                MainActivity.this.finish();
            }else if (idMain == R.id.nav_FornecedorNovo) {
                Intent intent = new Intent(MainActivity.this, SecundaryActivity.class);
                intent.putExtra("fragment", "Fornecedor");
                startActivity(intent);
                MainActivity.this.finish();
            }
            Log.d("MainActivity", "UP FUNCIONOU");
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("fragment")) {
            String fragment = intent.getStringExtra("fragment");
            if("ClienteNovo".equals(fragment)){
                NavController navControllerMain = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navControllerMain.navigate(R.id.nav_ClienteNovo);
            }
            if("FornecedorNovo".equals(fragment)){
                NavController navControllerMain = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navControllerMain.navigate(R.id.nav_FornecedorNovo);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
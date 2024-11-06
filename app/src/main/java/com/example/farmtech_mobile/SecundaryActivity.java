package com.example.farmtech_mobile;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.farmtech_mobile.databinding.ActivityMainBinding;
import com.example.farmtech_mobile.databinding.ActivitySecundaryBinding;
import com.example.farmtech_mobile.ui.relatorios.RelatoriosFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


public class SecundaryActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ActivitySecundaryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_secundary);

        binding = ActivitySecundaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("fragment")) {
            String fragment = intent.getStringExtra("fragment");
            if("Cliente".equals(fragment)){
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_secundary);
                navController.navigate(R.id.nav_Cliente);
            }else if("Fornecedor".equals(fragment)){
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_secundary);
                navController.navigate(R.id.nav_fornecedor);
            }
        }

        // Configurando a toolbar com o ViewBinding
        setSupportActionBar(binding.appBarSecundary.toolbar);

        // Referência para o DrawerLayout via binding
        drawerLayout = binding.drawerLayout;

        // Configurando o ActionBarDrawerToggle para abrir e fechar o drawer
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        // Adicionando o listener para o Drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Habilitar o botão de navegação (ícone dos três traços)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurando o NavigationView e o NavController
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_Cliente,R.id.nav_fornecedor,R.id.nav_usuario,R.id.nav_produto,R.id.nav_vender, R.id.nav_producao,R.id.nav_relatorios)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_secundary);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                // Verifica o ID do fragmento atual
                if (destination.getId() == R.id.nav_relatorios) {
                    // Define a orientação como landscape para o fragmento de relatórios
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    // Define a orientação como portrait para os outros fragmentos
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_secundary);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Ativar o clique no ícone de três traços para abrir o drawer
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
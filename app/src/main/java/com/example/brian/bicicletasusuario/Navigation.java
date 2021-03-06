package com.example.brian.bicicletasusuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaUsuario;
import com.example.brian.bicicletasusuario.utils.Util;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Abrir un fragment por defecto
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.pes_vermapa));
        ButterKnife.bind(this);

		ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);

		SharedPreferences pref = this.getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);
		String email=pref.getString("email", null);

		Call<RespuestaUsuario> call = api.getPerfil(email);
		call.enqueue(new Callback<RespuestaUsuario>() {
			@Override
			public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
				if (response.body().getCodigo().equals("1")) {


					View headerView = navigationView.getHeaderView(0);
					TextView navUsername = (TextView) headerView.findViewById(R.id.tvNombreUsuario);
					navUsername.setText(response.body().getUsuario().getNombre());
					TextView navUsername2 = (TextView) headerView.findViewById(R.id.textView);
					navUsername2.setText(response.body().getUsuario().getEmail());
				}
			}

			@Override
			public void onFailure(Call<RespuestaUsuario> call, Throwable t) {

			}
		});


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (drawer.isDrawerOpen(Gravity.LEFT))
                // No le den bola, anda
                this.finishAffinity();
            else
                drawer.openDrawer(Gravity.LEFT);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean fragmentTransaction = false;
        Fragment fragment = null;

        switch (id) {
            case R.id.pes_perfil:
                fragment = new Perfil();
                fragmentTransaction = true;
                break;
            case R.id.pes_alquiactual:
                fragment = new AlquilarBici();
                fragmentTransaction = true;
                break;
            case R.id.pes_vermapa:
                fragment = new VerMapa();
                fragmentTransaction = true;
                break;
            case R.id.pes_repincidencia:
                fragment = new ReporteIncidencia();
                fragmentTransaction = true;
                break;
            case R.id.pes_cerrarsesion:
                SharedPreferences sp = getSharedPreferences("usuario", MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit();
                et.putString("email", null);
                et.putString("password", null);
                et.commit();
                Intent intent = new Intent(Navigation.this, IniciarSesion.class);
                startActivity(intent);
                break;
            case R.id.pes_historial:
                fragment = new HistoricoAlquileres();
                fragmentTransaction = true;
                break;

        }

        if (fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenido_navigation, fragment)
                    .commit();
            item.setChecked(true);
            getSupportActionBar().setTitle("  " + item.getTitle());
            Drawable icon = item.getIcon();
            icon.mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setIcon(icon);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

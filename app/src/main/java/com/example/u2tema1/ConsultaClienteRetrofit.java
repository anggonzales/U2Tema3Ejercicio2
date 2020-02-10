package com.example.u2tema1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ConsultaClienteRetrofit extends AppCompatActivity {
    Retrofit retrofit;
    servicesRetrofit miserviceretrofit;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MiNuevoAdaptador adaptador;
    private ArrayList<ClienteRetrofit> misdatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        misdatos = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);

        final String url = "https://notogaea-decoration.000webhostapp.com/";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        miserviceretrofit = retrofit.create(servicesRetrofit.class);

        Call<List<ClienteRetrofit>> call = miserviceretrofit.getUsersGet();
//Apartir de aqui la forma cambia de la manera sincrona a la asincrona
//basicamente mandamos a llamar el metodo enqueue, y le pasamos como parametro el call back
//Recuerda que el IDE es para ayudarte asi que lo creara automaticamente al escribir "new"
        call.enqueue(new Callback<List<ClienteRetrofit>>() {
            //Metodo que se ejecutara cuando no hay problemas y obtenemos respuesta del server
            @Override
            public void onResponse(Call<List<ClienteRetrofit>> call, Response<List<ClienteRetrofit>> response) {
//Exactamente igual a la manera sincrona,la respuesta esta en el body
                for (ClienteRetrofit res : response.body()) {

                    misdatos.add(new ClienteRetrofit(res.getcodigo(), res.getNombre(), res.getApellido()));
                    adaptador = new MiNuevoAdaptador(ConsultaClienteRetrofit.this , misdatos);
                    recyclerView.setAdapter(adaptador);
                    layoutManager = new LinearLayoutManager(ConsultaClienteRetrofit.this);
                    recyclerView.setLayoutManager(layoutManager);
                    Log.e("Usuario: ", res.getNombre() + " " + res.getApellido());

                }
            }

            //Metodo que se ejecutara cuando ocurrio algun problema
            @Override
            public void onFailure(Call<List<ClienteRetrofit>> call, Throwable t) {
                Log.e("onFailure", t.toString());// mostrmos el error
            }

        });

        //listar()
    }

    private ArrayList<ClienteRetrofit> ListaClientes() {
        final ArrayList<ClienteRetrofit> Clientes = new ArrayList<>();
        Call<List<ClienteRetrofit>> call = miserviceretrofit.getUsersGet();
        call.enqueue(new Callback<List<ClienteRetrofit>>() {
            @Override
            public void onResponse(Call<List<ClienteRetrofit>> call, Response<List<ClienteRetrofit>> response) {
                Log.e("mirespuesta: ", response.toString());
                for (ClienteRetrofit res : response.body()) {
                    Clientes.add(new ClienteRetrofit(res.getcodigo(), res.getNombre(), res.getApellido()));
                    Log.e("mirespuesta: ", "id= " + res.getcodigo() + " prod= " + res.getNombre() + " precio " + res.getApellido());
                }
            }

            @Override
            public void onFailure(Call<List<ClienteRetrofit>> call, Throwable t) {
                Log.e("onFailure", t.toString());// mostrmos el error
            }
        });
        return Clientes;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_insertar:
                startActivity(new Intent(this, InsertarCliente.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

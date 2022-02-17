package it.uniba.pioneers.testtool.VisualizzaVisite;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.uniba.pioneers.data.Visita;
import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.data.users.Visitatore;
import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo_visualizza.GrafoVisualizzaFragment;

public class VisiteCreateUtente extends AppCompatActivity {

    public static Visita visitaSelezionata;
    public static List<Visita> listaVisite;

    //Metodo necessario per inserire le visite corrette all'interno della list view
    //Caso particolare è quello della guida che può vedere le visite da fare o passate
    private void addItemToLista(Visita vis){

        if(MainActivity.tipoUtente.equals("guida")){
            Date tempDate = new Date(vis.getData() * 1000);
            //Guida vuole le visite effettuate
            if(MainActivity.flagVisiteGuida == 0 && tempDate.before(new Date()) ){
                listaVisite.add(vis);
                return;
            //Guida vuole le visite che deve ancora effettuare
            } else if (MainActivity.flagVisiteGuida == 1 && tempDate.after(new Date()) ){
                listaVisite.add(vis);
                return;
            } else {
                return;
            }
        }

        listaVisite.add(vis);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitaSelezionata = null;
        listaVisite = null;
        setContentView(R.layout.activity_visite_create_utente);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startFrag();
    }

    //Metodo necessario per caricare il fragment corretto in base all'utente
    private void startFrag(){
        if(MainActivity.tipoUtente.equals("curatore")){
            startFragCuratore();
        } else if(MainActivity.tipoUtente.equals("visitatore")){
            startFragVisitatore();
        } else if(MainActivity.tipoUtente.equals("guida")){
            startFragGuida();
        }
    }
    //Metodo necessario per caricare le visite della guida
    private void startFragGuida(){
        System.out.println(MainActivity.guida.getId());
        try{
            Guida.getAllVisiteGuida(this, MainActivity.guida,
                    response -> {
                        try {

                            if(response.getBoolean("status")){
                                //Visita e ListaVisite necessarie per popolare la ListView
                                Visita v;
                                listaVisite = new ArrayList<>();
                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrVisiteGuida");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject visita = arrayData.getJSONObject(i);
                                    v = new Visita();
                                    try {
                                        v.setDataFromJSON(visita);
                                        addItemToLista(v);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                startFragListaVisite();

                            }else{
                                System.out.println(R.string.impossibile_procedere);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //Metodo necessario per caricare le visite del curatore
    private void startFragCuratore(){
        try{
            CuratoreMuseale.getAllVisiteSingolo(this, MainActivity.curatore,
                    response -> {
                        try {
                            if(response.getBoolean("status")){
                                //Visita e ListaVisite necessarie per popolare la ListView
                                Visita v;
                                listaVisite = new ArrayList<>();
                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrVisite");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject visita = arrayData.getJSONObject(i);
                                    v = new Visita();
                                    try {
                                        v.setDataFromJSON(visita);
                                        addItemToLista(v);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                startFragListaVisite();

                            }else{
                                System.out.println(R.string.impossibile_procedere);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //Metodo necessario per caricare le visite del visitatore corrette in base al caso
    private void startFragVisitatore(){
        //I valori del flag servono per capire che visite vuole il visitatore:
        //2 = ricerca visite in base al luogo
        //1 = visite predef
        //0 = sue visite
        if(MainActivity.flagVisite == 1){
            visitePredefinite();
        } else if(MainActivity.flagVisite == 0){
            visiteVisitatore();
        } else if(MainActivity.flagVisite == 2){
            tutteVisiteVisitatore();
        }
    }

    //Metodo necessario per caricare tutte le visite create dai curatori nel caso in cui
    //il visitatore clicca su visite predefinite
    private void visitePredefinite(){
        try{
            Visitatore.getAllVisitePredefinite(this, MainActivity.visitatore,
                    response -> {
                        try {
                            if(response.getBoolean("status")){
                                //Visita e ListaVisite necessarie per popolare la ListView
                                Visita v;
                                listaVisite = new ArrayList<>();
                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrVisitePredef");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject visita = arrayData.getJSONObject(i);
                                    v = new Visita();
                                    try {
                                        v.setDataFromJSON(visita);
                                        addItemToLista(v);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                startFragListaVisite();

                            }else{
                                System.out.println(R.string.impossibile_procedere);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //Metodo necessario per caricare tutte le visite create dal visitatore attuale nel caso in cui
    //il visitatore clicca su le tue visite
    private void visiteVisitatore(){
        try{
            Visitatore.getAllVisiteSingolo(this, MainActivity.visitatore,
                    response -> {
                        try {
                            if(response.getBoolean("status")){
                                //Visita e ListaVisite necessarie per popolare la ListView
                                Visita v;
                                listaVisite = new ArrayList<>();
                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrVisite");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject visita = arrayData.getJSONObject(i);
                                    v = new Visita();
                                    try {
                                        v.setDataFromJSON(visita);
                                        addItemToLista(v);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                startFragListaVisite();

                            }else{
                                System.out.println(R.string.impossibile_procedere);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //Metodo necessario per caricare tutte le visite in base al luogo cercato dal visitatore nel
    //caso in cui il visitatore effettua una ricerca dalla search bar
    private void tutteVisiteVisitatore(){
        try{
            Visitatore.getAllVisiteByLuogo(this, MainActivity.visitatore, MainActivity.luogoToSearch,
                    response -> {
                        try {

                            if(response.getBoolean("status")){
                                //Visita e ListaVisite necessarie per popolare la ListView
                                Visita v;
                                listaVisite = new ArrayList<>();
                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrVisiteByLuogo");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject visita = arrayData.getJSONObject(i);
                                    v = new Visita();
                                    try {
                                        v.setDataFromJSON(visita);
                                        addItemToLista(v);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                startFragListaVisite();

                            }else{
                                System.out.println(R.string.impossibile_procedere);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //Metodo necessario per caricare il fragment con la list view piena di visite
    private void startFragListaVisite(){
        //Carico il fragment per mostrare la lista delle visite
        FragmentVisiteCreateUtente flv = new FragmentVisiteCreateUtente();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.frame_visite_create, flv)
                .addToBackStack(null)
                .commit();
    }

    //Metodo necessario per eliminare una visita
    public void eliminaVisitaSingola(View view) {
        if(visitaSelezionata.getId() != 0){
            listaVisite.remove(visitaSelezionata);
            FragmentVisiteCreateUtente.arrayAdapter.notifyDataSetChanged();
            visitaSelezionata.deleteDataDb(this);
            visitaSelezionata = null;
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(visitaSelezionata == null){
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(visitaSelezionata == null){
            finish();
        } else {
            super.onBackPressed();
        }
        return true;
    }

    //Metodo necessario per caricare il grafo per visualizzare una visita
    public void avviaGrafoVisualizza(View view) {
        GrafoVisualizzaFragment grafoVisualizzaFragment = new GrafoVisualizzaFragment(VisiteCreateUtente.visitaSelezionata);
        androidx.fragment.app.FragmentManager supportFragmentManager;

        findViewById(R.id.scroll_singola_visita).setVisibility(View.GONE);

        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.frameVIsualizzaGrafo, grafoVisualizzaFragment)
                .commit();
    }
}
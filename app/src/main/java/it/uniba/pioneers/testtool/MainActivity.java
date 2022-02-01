package it.uniba.pioneers.testtool;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Response;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import it.uniba.pioneers.data.Area;
import it.uniba.pioneers.data.Opera;
import it.uniba.pioneers.data.Zona;
import it.uniba.pioneers.data.users.Visitatore;
import it.uniba.pioneers.testtool.databinding.ActivityMainBinding;
import it.uniba.pioneers.testtool.home.CaptureAct;
import it.uniba.pioneers.testtool.home.FragmentHomeCuratore;
import it.uniba.pioneers.testtool.ui.FragmentListaAree;


public class MainActivity extends AppCompatActivity {
    public static int MY_PERMISSIONS_REQUEST_CAMERA=100;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    public DialogNodeInfo dialogOperaInfo = new DialogNodeInfo();
    public FragmentHomeCuratore frag;
    public static Opera opera;
    public static Zona zona;
    public static ArrayList<Area> areeZona;
    public int tipoUtente=1;
    public int idUtente;

    //AGGIUNTO DA IVAN
    public static Visitatore visitatore = new Visitatore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolBarHome);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle); //aggiungo un listner al toggle
        toggle.syncState(); //Ruota il toggle quando viene cliccato

        //AGGIUNTO DA IVAN
        visitatore.setId(2);
        visitatore.readDataDb(MainActivity.this);

        /*** INIZIO TRANSAZIONE ***/
        //// if per tipo di utente e fragment da committare
        frag = new FragmentHomeCuratore();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_list, frag)
                .commit();

        /*** FINE TRANSAZIONE ***/
    }

    //se clicco il bottone back e sta aperto il drawer
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            //il drawer non è aperto
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void goEditorActivity(View view){
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    public void scannerQr(View view) {
       scanCode();
    }
    private void scanCode(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permessi Camera")
                        .setMessage("Consentire all'app l'accesso alla camera per scansionare i QR delle opere, negando l'accesso non ci si potrà interagire")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }else{
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CaptureAct.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scanning code...");
            integrator.initiateScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                // Se la richiesta è stata cancellata, l'array result è vuoto
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    IntentIntegrator integrator = new IntentIntegrator(this);
                    integrator.setCaptureActivity(CaptureAct.class);
                    integrator.setOrientationLocked(false);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scanning code...");
                    integrator.initiateScan();
                }
            }
            return;
        }
    }

    public void gestisciMuseo(View view) {
        Area.areeZona(this, 10 ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    areeZona = new ArrayList<Area>();

                    if (status) {
                        //JSONObject resultData = response.getJSONObject("data");
                        JSONArray resultAree = response.getJSONArray("data");

                        for(int i =0; i< resultAree.length(); i++) {
                            Area tmp = new Area();
                            tmp.setDataFromJSON(resultAree.getJSONObject(i));
                            areeZona.add(tmp);
                        }
                        FragmentListaAree fls = new FragmentListaAree();
                        androidx.fragment.app.FragmentManager supportFragmentManager;
                        supportFragmentManager = getSupportFragmentManager();
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container_list, fls)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getApplicationContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class Inserisci extends Thread{
        private final int id;
        public Inserisci(int id){
            this.id = id;
        }
        public void run(){
            opera = new Opera();
            opera.setId(this.id);
            opera.readDataDb(MainActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null  ){
                if(result.getContents() != null) {
                    Inserisci i = new Inserisci(Integer.parseInt(result.getContents()));
                    i.setPriority(4);
                    i.start();
                    SystemClock.sleep(1000);


                    Intent informazioniOpera = new Intent(MainActivity.this, InfoOpera.class);
                    informazioniOpera.putExtra("tipoUtente", tipoUtente);  // curatore = 1, visitatore = 2, guida = 3
                    startActivity(informazioniOpera);
                }
                else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Nessun risultato")
                            .setPositiveButton(android.R.string.yes,null)
                            .show();
                }
            }
            else{
                super.onActivityResult(requestCode, resultCode, data);
            }
        }catch(Exception e){

        }
    }

    //AGGIUNTO DA IVAN
    public void goToPersonalArea(MenuItem item) {
        Intent intent = new Intent(this, AreaPersonaleVisitatore.class);
        startActivity(intent);
    }

}
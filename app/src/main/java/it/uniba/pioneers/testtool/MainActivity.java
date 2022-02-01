package it.uniba.pioneers.testtool;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import it.uniba.pioneers.data.Opera;
import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.testtool.databinding.ActivityMainBinding;
import it.uniba.pioneers.testtool.home.CaptureAct;
import it.uniba.pioneers.testtool.home.FragmentHomeCuratore;
import it.uniba.pioneers.testtool.home.ui.curatore.IlTuoMuseoFragment;


public class MainActivity extends AppCompatActivity {
    public static int MY_PERMISSIONS_REQUEST_CAMERA=100;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    public DialogNodeInfo dialogOperaInfo = new DialogNodeInfo();
    public FragmentHomeCuratore f;
    public IlTuoMuseoFragment tm;
    public static Opera opera = new Opera();
    public static CuratoreMuseale c = new CuratoreMuseale();

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

        c.setId(6);
        c.readDataDb(MainActivity.this);

        /*** INIZIO TRANSAZIONE ***/
        //// if per tipo di utente e fragment da committare
        f = new FragmentHomeCuratore();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container_list, f)
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
        int permessoCamera = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        if(permessoCamera == PackageManager.PERMISSION_GRANTED){
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setCaptureActivity(CaptureAct.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Scanning code...");
            integrator.initiateScan();
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)){
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
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                // Se la richiesta è stata cancellata, l'array result è vuoto
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanCode();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Allerta permesso")
                            .setMessage("Il permesso di accesso alla camere è fondamentale per poter interagire con le opere")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                opera.setId(Integer.parseInt(result.getContents()));
                opera.readDataDb(MainActivity.this);
                if(opera.getTitolo().equals("")){
                    Toast.makeText(MainActivity.this, "Nessun opera trovata, prova a rieseguire la scansione" ,Toast.LENGTH_LONG).show();
                }else{
                    Intent informazioniOpera = new Intent(this, InfoOpera.class);

                    startActivity(informazioniOpera);


                    //infoOpera.show(this.getSupportFragmentManager(), "informazione oprera");
                }
            }
            else {
                Toast.makeText(this, "No results", Toast.LENGTH_LONG).show();
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void GestisciMuseo (View v){
        Button btnIlTuoMuseo = (Button) v.findViewById(R.id.btn_gestisci_museo);

        btnIlTuoMuseo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                tm = new IlTuoMuseoFragment();
                androidx.fragment.app.FragmentManager supportFragmentManager;
                supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_list, tm)
                        .commit();

            }
        });


    }
}
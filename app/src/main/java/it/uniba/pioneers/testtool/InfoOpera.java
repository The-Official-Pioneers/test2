package it.uniba.pioneers.testtool;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class InfoOpera extends AppCompatActivity {

    private TextView titolo;
    private TextView descrizione;
    private ImageView img;

    private EditText editableTitolo;
    private EditText editableDescrizione;

    private static FragmentSingolaOpera fso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opera);
        MainActivity.qr=true;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fso = new FragmentSingolaOpera();    // carica il fragment FragmentSingolaOpera(usato anche dalla MainActivity)
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_list, fso)
                .commit();
    }

    private boolean checkExit() {
        boolean c1 = String.valueOf(fso.editableTitolo.getText()).equals(MainActivity.operaSelezionata.getTitolo());
        boolean c2 = String.valueOf(fso.editableDescrizione.getText()).equals(MainActivity.operaSelezionata.getDescrizione());
        boolean c3 = MainActivity.fotoModificata;
        return (!c1 || !c2 || c3);
    }

    @Override
    public void onBackPressed() {     // controllo uscita senza salvare da tasto indietro
        if (MainActivity.tipoUtente.equals("curatore")) {
            if (checkExit()) {
                new AlertDialog.Builder(this)
                        .setTitle("Uscire?")
                        .setMessage("Uscire senza salvare le modifiche?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                InfoOpera.super.onBackPressed();
                                MainActivity.fotoModificata = false;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.action_save_opera) {
            modificaAggiungiOpera(null);
        } else if (MainActivity.tipoUtente.equals("curatore")) {  // controllo uscita senza salvare da barra
            if (checkExit()) {
                new AlertDialog.Builder(this)
                        .setTitle("Uscire?")
                        .setMessage("Uscire senza salvare le modifiche?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.fotoModificata = false;
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_delete_opera).setVisible(false);
        menu.findItem(R.id.action_save_opera).setVisible(false);
        menu.findItem(R.id.action_delete_area).setVisible(false);
        menu.findItem(R.id.action_save_area).setVisible(false);

        if(MainActivity.operaSelezionata != null && MainActivity.tipoUtente.equals("curatore")){
            menu.findItem(R.id.action_delete_opera).setVisible(true);
            menu.findItem(R.id.action_save_opera).setVisible(true);
        }
        return true;
    }

    public void modificaAggiungiOpera(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Confermi")
                .setMessage("Confermare la modifica dell'opera?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {    // se utente conferma modifiche
                        String titolo = (String) fso.editableTitolo.getText().toString();
                        String descrizione = (String) fso.editableDescrizione.getText().toString();
                        Bitmap image = ((BitmapDrawable) fso.img.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] b = baos.toByteArray();
                        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

                        System.out.print(encImage);
                        System.out.print(MainActivity.operaSelezionata.getFoto());
                        if (titolo.equals(MainActivity.operaSelezionata.getTitolo()) && descrizione.equals(MainActivity.operaSelezionata.getDescrizione()) && !MainActivity.fotoModificata) {
                            new AlertDialog.Builder(InfoOpera.this)
                                    .setMessage("Modifica almeno un campo per salvare")
                                    .setPositiveButton(android.R.string.yes, null)
                                    .show();

                        } else {
                            MainActivity.operaSelezionata.setTitolo(titolo);
                            MainActivity.operaSelezionata.setDescrizione(descrizione);
                            MainActivity.operaSelezionata.setFoto(encImage);
                            MainActivity.operaSelezionata.updateDataDb(InfoOpera.this);
                            new AlertDialog.Builder(InfoOpera.this)
                                    .setMessage("Modifica effettuata")
                                    .setPositiveButton(android.R.string.yes, null)
                                    .show();
                            MainActivity.fotoModificata = false;
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void modificaFoto(View view) {  // controllo permessi accesso alla galleria
        if (ContextCompat.checkSelfPermission(InfoOpera.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(InfoOpera.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permesso di accesso alla galleria")
                        .setMessage("Consentire all'app l'accesso alla galleria per poter scegliere l'immagine o la foto da usare per l'opera")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(InfoOpera.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PICK_FROM_GALLERY);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                ActivityCompat.requestPermissions(InfoOpera.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.PICK_FROM_GALLERY);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);   // selezione foto da galleria
            intent.setType("image/*");
            startActivityForResult(intent, MainActivity.PICK_FROM_GALLERY);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MainActivity.PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, MainActivity.PICK_FROM_GALLERY);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Permesso negato")
                            .setMessage("Permesso di accesso alla galleria non concesso")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
        }
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
           if (requestCode == MainActivity.PICK_FROM_GALLERY && resultCode == RESULT_OK) {
                Uri targetUri = data.getData();
                Bitmap bitmap;
                ImageView oldPropic = (ImageView) fso.img;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    oldPropic.setImageBitmap(bitmap);

                    /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] b = baos.toByteArray();
                    String encImage = Base64.encodeToString(b, Base64.DEFAULT);
                    MainActivity.operaSelezionata.setFoto(encImage);*/
                   // Toast.makeText(getApplicationContext(), encImage, Toast.LENGTH_SHORT).show();

                    Snackbar.make(getWindow().getDecorView().getRootView(), "Foto impostata con successo!",
                            Snackbar.LENGTH_LONG).show();
                    MainActivity.fotoModificata = true;

                } catch (FileNotFoundException e) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Impossibile procedere",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.qr=false;
        MainActivity.operaSelezionata=null;
    }
}
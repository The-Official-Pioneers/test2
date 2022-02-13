package it.uniba.pioneers.testtool;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.data.users.Visitatore;

public class AreaPersonale extends AppCompatActivity {

    private static FragmentAreaPersonaleVisitatore fragAreaVisit;
    private static FragmentAreaPersonaleGuida fragAreaGuida;
    private static FragmentAreaPersonaleCuratore fragAreaCurat;

    private static final int PICK_FROM_GALLERY = 1;

    private String selectedSpecializ = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_personale);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startFrag();
    }

    private void startFrag(){
        switch(MainActivity.tipoUtente){
            case "visitatore":
                startFragVisitatore();
                break;
            case "guida":
                startFragGuida();
                break;
            case "curatore":
                startFragCuratore();
                break;
        }
    }

    private void startFragVisitatore(){
        fragAreaVisit = new FragmentAreaPersonaleVisitatore();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_areap, fragAreaVisit)
                .commit();
    }

    private void startFragGuida(){
        fragAreaGuida = new FragmentAreaPersonaleGuida();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_areap, fragAreaGuida)
                .commit();

    }

    private void startFragCuratore(){
        fragAreaCurat = new FragmentAreaPersonaleCuratore();
        androidx.fragment.app.FragmentManager supportFragmentManager;
        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.frame_areap, fragAreaCurat)
                .commit();
    }

    public void editProfile(View view){

        EditText nome = (EditText) findViewById(R.id.txt_nome_opera);
        EditText cognome = (EditText) findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) findViewById(R.id.txt_datan);
        EditText email = (EditText) findViewById(R.id.txt_email);

        if(MainActivity.tipoUtente.equals("visitatore")){

            if(checkForChangesVisitatore(nome,cognome,datanascita,email)){

                String newNome = nome.getText().toString();
                String newCognome = cognome.getText().toString();
                String newDatan = datanascita.getText().toString();
                String newEmail = email.getText().toString();

                try{
                    MainActivity.visitatore.setNome(newNome);
                    MainActivity.visitatore.setCognome(newCognome);
                    MainActivity.visitatore.setEmail(newEmail);
                    if(validateDate(newDatan)){
                        MainActivity.visitatore.setDataNascita( Visitatore.output.parse(newDatan) );
                    }
                    MainActivity.visitatore.updateDataDb(view.getContext());
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.aggiornamento_profilo_successo,
                            Snackbar.LENGTH_LONG).show();
                } catch(ParseException e){
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.impossibile_procedere,
                            Snackbar.LENGTH_LONG).show();
                }

            }

        } else if(MainActivity.tipoUtente.equals("guida")){

            Spinner spinner = (Spinner) this.findViewById(R.id.spinner_specializzazione);
            selectedSpecializ = spinner.getSelectedItem().toString();

            if(checkForChangesGuida(nome,cognome,datanascita,email,selectedSpecializ)){

                String newNome = nome.getText().toString();
                String newCognome = cognome.getText().toString();
                String newDatan = datanascita.getText().toString();
                String newEmail = email.getText().toString();
                String newSpecializzazione = selectedSpecializ;

                try{
                    MainActivity.guida.setNome(newNome);
                    MainActivity.guida.setCognome(newCognome);
                    MainActivity.guida.setEmail(newEmail);
                    MainActivity.guida.setSpecializzazione(newSpecializzazione);
                    if(validateDate(newDatan)){
                        MainActivity.guida.setDataNascita( Guida.output.parse(newDatan) );
                    }
                    MainActivity.guida.updateDataDb(view.getContext());
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.aggiornamento_profilo_successo,
                            Snackbar.LENGTH_LONG).show();

                } catch(ParseException e){
                    Snackbar.make(view, R.string.impossibile_procedere,
                            Snackbar.LENGTH_LONG).show();
                }

            }

        } else if(MainActivity.tipoUtente.equals("curatore")){

            EditText zona = (EditText) findViewById(R.id.txt_zona);

            if(checkForChangesCuratore(nome,cognome,datanascita,email,zona)){

                String newNome = nome.getText().toString();
                String newCognome = cognome.getText().toString();
                String newDatan = datanascita.getText().toString();
                String newEmail = email.getText().toString();
                Long newZona = Long.parseLong(zona.getText().toString());

                try{
                    MainActivity.curatore.setNome(newNome);
                    MainActivity.curatore.setCognome(newCognome);
                    MainActivity.curatore.setEmail(newEmail);
                    MainActivity.curatore.setZona(newZona);
                    if(validateDate(newDatan)){
                        MainActivity.curatore.setDataNascita( CuratoreMuseale.output.parse(newDatan) );
                    }
                    MainActivity.curatore.updateDataDb(view.getContext());
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.aggiornamento_profilo_successo,
                            Snackbar.LENGTH_LONG).show();
                } catch(ParseException e){
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.impossibile_procedere,
                            Snackbar.LENGTH_LONG).show();
                }

            }
        }


    }

    private boolean checkForChangesVisitatore(EditText nomeToCheck, EditText cognomeToCheck,
                                              EditText datanToCheck, EditText emailToCheck){
        if( !(nomeToCheck.getText().toString().equals(MainActivity.visitatore.getNome())) ||
                !(cognomeToCheck.getText().toString().equals(MainActivity.visitatore.getCognome())) ||
                !(datanToCheck.getText().toString().equals(MainActivity.visitatore.getShorterDataNascita())) ||
                !(emailToCheck.getText().toString().equals(MainActivity.visitatore.getEmail())) ){
            return true;
        }
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.no_modifiche, Snackbar.LENGTH_LONG).show();
        return false;
    }

    private boolean checkForChangesGuida(EditText nomeToCheck, EditText cognomeToCheck, EditText datanToCheck,
                                    EditText emailToCheck, String specialToCheck){
        if( !(nomeToCheck.getText().toString().equals(MainActivity.guida.getNome())) ||
                !(cognomeToCheck.getText().toString().equals(MainActivity.guida.getCognome())) ||
                !(datanToCheck.getText().toString().equals(MainActivity.guida.getShorterDataNascita())) ||
                !(emailToCheck.getText().toString().equals(MainActivity.guida.getEmail())) ||
                !(specialToCheck.equals(MainActivity.guida.getSpecializzazione()))){
            return true;
        }
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.no_modifiche, Snackbar.LENGTH_LONG).show();
        return false;
    }

    private boolean checkForChangesCuratore(EditText nomeToCheck, EditText cognomeToCheck, EditText datanToCheck,
                                    EditText emailToCheck, EditText zonaToCheck){

        if( !(nomeToCheck.getText().toString().equals(MainActivity.curatore.getNome())) ||
                !(cognomeToCheck.getText().toString().equals(MainActivity.curatore.getCognome())) ||
                !(datanToCheck.getText().toString().equals(MainActivity.curatore.getShorterDataNascita())) ||
                !(emailToCheck.getText().toString().equals(MainActivity.curatore.getEmail())) ||
                !(Long.parseLong(zonaToCheck.getText().toString()) == (MainActivity.curatore.getZona()))){
            return true;
        }
        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.no_modifiche, Snackbar.LENGTH_LONG).show();
        return false;
    }

    private boolean validateDate(String dateToValid){
        //output is SimpleDateFormat of this type ("dd/MM/yyyy")
        try {
            if(countSlashes(dateToValid)){
                Guida.output.parse(dateToValid);
            } else {
                return false;
            }
        } catch (ParseException e){

            return false;
        }
        return true;
    }

    private boolean countSlashes(String s){
        int slashes = 0;
        for(int i = 0; i < s.length()-1; i++) {
            if (s.charAt(i) == '/') {
                slashes++;
            }
        }
        return (slashes == 2);
    }

    public void editPassword(View view) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
        dialogBuilder.setTitle(R.string.modifica_password);
        dialogBuilder.setMessage(R.string.inserisci_nuova_password);

        //Definisco il layout per l'edittext per la nuova password
        LinearLayout lp = new LinearLayout(this);

        lp.setLayoutParams( new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        );

        //Definisco l'edittext per la nuova password
        final EditText passwordInput = new EditText(view.getContext());

        passwordInput.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        passwordInput.setHint(R.string.inserisci_nuova_password);
        passwordInput.setTextSize(TypedValue.COMPLEX_UNIT_SP ,14);
        passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        passwordInput.setTransformationMethod(new PasswordTransformationMethod());

        //Definisco il bottone per mostrare/nascondere password
        final ToggleButton tb = new ToggleButton(this);
        tb.setChecked(false);
        tb.setTextOff(getResources().getString(R.string.mostra_pass));
        tb.setTextOn(getResources().getString(R.string.nascondi_pass));
        tb.setText(R.string.mostra_pass);
        tb.setTextSize(TypedValue.COMPLEX_UNIT_SP ,12);
        tb.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 3));

        tb.setOnClickListener(a -> {
            if( !(tb.isChecked()) ){
                passwordInput.setTransformationMethod(new PasswordTransformationMethod());
            } else {
                passwordInput.setTransformationMethod(null);
            }
        });

        lp.setOrientation(LinearLayout.HORIZONTAL);
        lp.addView(passwordInput);
        lp.addView(tb);
        dialogBuilder.setView(lp);

        dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                String newPassword = passwordInput.getText().toString();

                try {
                    String passToSave = digest(newPassword);

                    switch (MainActivity.tipoUtente){
                        case "visitatore":
                            updatePasswordVisitatore(passToSave);
                            break;
                        case "guida":
                            updatePasswordGuida(passToSave);
                            break;
                        case "curatore":
                            updatePasswordCuratore(passToSave);
                            break;
                    }

                    chiudiTastiera(passwordInput);

                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.pass_aggiornata_successo,
                            Snackbar.LENGTH_LONG).show();

                } catch (NoSuchAlgorithmException e) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.impossibile_procedere,
                            Snackbar.LENGTH_LONG).show();
                }

            }
        });

        dialogBuilder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                chiudiTastiera(passwordInput);
            }
        });

        dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        dialogBuilder.show();

    }

    private void chiudiTastiera(EditText textToClose){
        //Necessario per chiudere la tastiera dopo aver premuto OK
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(textToClose.getWindowToken(), 0);
    }

    //Metodi necessari per l'aggiornamento della password nel metodo editPassword
    private void updatePasswordVisitatore(String pass){
        MainActivity.visitatore.setPassword(pass);
        MainActivity.visitatore.updateDataDb(AreaPersonale.fragAreaVisit.getContext());
    }
    private void updatePasswordGuida(String pass){
        MainActivity.guida.setPassword(pass);
        MainActivity.guida.updateDataDb(AreaPersonale.fragAreaGuida.getContext());
    }
    private void updatePasswordCuratore(String pass){
        MainActivity.curatore.setPassword(pass);
        MainActivity.curatore.updateDataDb(AreaPersonale.fragAreaCurat.getContext());
    }
    //=============================================================================

    public String digest(String value) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        byte[] hash = digester.digest(value.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void changePropic(View view) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY
                );
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        } catch (Exception e) {
            Snackbar.make(getWindow().getDecorView().getRootView(), R.string.impossibile_procedere,
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.permessi_galleria)
                            .setMessage(R.string.consentire_permessi_galleria)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(AreaPersonale.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            Bitmap bitmap;
            ImageView oldPropic = (ImageView) findViewById(R.id.img_propic);
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                oldPropic.setImageBitmap(bitmap);
                oldPropic.setScaleType(ImageView.ScaleType.CENTER_CROP);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
                byte[] b = baos.toByteArray();
                String encImage = Base64.encodeToString(b, Base64.DEFAULT);

                switch (MainActivity.tipoUtente){
                    case "visitatore":
                        updatePropicVisitatore(encImage);
                        break;
                    case "guida":
                        updatePropicGuida(encImage);
                        break;
                    case "curatore":
                        updatePropicCuratore(encImage);
                        break;
                }



                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.foto_aggiornata_successo,
                        Snackbar.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.impossibile_procedere,
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    //Metodi necessari per l'aggiornamento della propic nel metodo changePropic
    private void updatePropicVisitatore(String newImage){
        MainActivity.visitatore.setPropic(newImage);
        MainActivity.visitatore.updateDataDb(this);
    }
    private void updatePropicGuida(String newImage){
        MainActivity.guida.setPropic(newImage);
        MainActivity.guida.updateDataDb(this);
    }
    private void updatePropicCuratore(String newImage){
        MainActivity.curatore.setPropic(newImage);
        MainActivity.curatore.updateDataDb(this);
    }
    //=============================================================================

    private boolean checkFieldsBeforeLeaving(){
        String textNome = ((EditText) findViewById(R.id.txt_nome_opera)).getText().toString();
        String textCognome = ((EditText) findViewById(R.id.txt_cognome)).getText().toString();
        String textDatan = ((EditText) findViewById(R.id.txt_datan)).getText().toString();
        String textEmail = ((EditText) findViewById(R.id.txt_email)).getText().toString();

        boolean check1 = false;
        boolean check2 = false;
        boolean check3 = false;
        boolean check4 = false;

        if(MainActivity.tipoUtente.equals("visitatore")){

            check1 = textNome.equals(MainActivity.visitatore.getNome());
            check2 = textCognome.equals(MainActivity.visitatore.getCognome());
            check3 = textDatan.equals(MainActivity.visitatore.getShorterDataNascita());
            check4 = textEmail.equals(MainActivity.visitatore.getEmail());

            return (check1 && check2 && check3 && check4);

        } else if(MainActivity.tipoUtente.equals("guida")){

            check1 = textNome.equals(MainActivity.guida.getNome());
            check2 = textCognome.equals(MainActivity.guida.getCognome());
            check3 = textDatan.equals(MainActivity.guida.getShorterDataNascita());
            check4 = textEmail.equals(MainActivity.guida.getEmail());

            Spinner spinner = (Spinner) this.findViewById(R.id.spinner_specializzazione);
            selectedSpecializ = spinner.getSelectedItem().toString();

            boolean check5 = selectedSpecializ.equals(MainActivity.guida.getSpecializzazione());

            return (check1 && check2 && check3 && check4 && check5);

        } else if(MainActivity.tipoUtente.equals("curatore")){

            check1 = textNome.equals(MainActivity.curatore.getNome());
            check2 = textCognome.equals(MainActivity.curatore.getCognome());
            check3 = textDatan.equals(MainActivity.curatore.getShorterDataNascita());
            check4 = textEmail.equals(MainActivity.curatore.getEmail());

            Long longZona = Long.parseLong(((EditText) findViewById(R.id.txt_zona)).getText().toString());
            boolean check5 = longZona == MainActivity.curatore.getZona();

            return (check1 && check2 && check3 && check4 && check5);
        }
        return false;
    }

    @Override
    //Controllo l'uscita senza salvare da barra
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!checkFieldsBeforeLeaving()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.uscire)
                    .setMessage(R.string.uscire_no_salvare)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
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
        }else{
            return false;
        }
        return true;
    }

    @Override
    //Controllo l'uscita senza salvare da tasto indietro (tasto sulla barra inferiore del telefono)
    public void onBackPressed(){
        if(!checkFieldsBeforeLeaving()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.uscire)
                    .setMessage(R.string.uscire_no_salvare)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
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
            super.onBackPressed();
        }
    }

    public String getSelectedSpecializzazione() {
        return selectedSpecializ;
    }

}
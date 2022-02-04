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

public class AreaPersonaleCuratore extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_area_personale_curatore);

        ImageView propic = (ImageView) findViewById(R.id.img_propic);

        EditText nome = (EditText) findViewById(R.id.txt_nome);
        EditText cognome = (EditText) findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) findViewById(R.id.txt_datan);
        EditText email = (EditText) findViewById(R.id.txt_email);
        EditText zona = (EditText) findViewById(R.id.txt_zona);

        byte[] bytes = Base64.decode(MainActivity.curatore.getPropic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        propic.setImageBitmap(decodedByte);
        propic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        nome.setText(MainActivity.curatore.getNome());
        cognome.setText(MainActivity.curatore.getCognome());
        email.setText(MainActivity.curatore.getEmail());
        datanascita.setText(MainActivity.curatore.getShorterDataNascita());
        zona.setText( String.valueOf(MainActivity.curatore.getZona()) );

        System.out.println("Zona: " + Long.parseLong(zona.getText().toString()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void editProfile(View view){

        EditText nome = (EditText) findViewById(R.id.txt_nome);
        EditText cognome = (EditText) findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) findViewById(R.id.txt_datan);
        EditText email = (EditText) findViewById(R.id.txt_email);
        EditText zona = (EditText) findViewById(R.id.txt_zona);

        if(checkForChanges(nome,cognome,datanascita,email,zona)){

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
                Snackbar.make(getWindow().getDecorView().getRootView(), "Profilo aggiornato con successo!",
                        Snackbar.LENGTH_LONG).show();
            } catch(ParseException e){
                Snackbar.make(getWindow().getDecorView().getRootView(), "Impossibile procedere",
                        Snackbar.LENGTH_LONG).show();
            }

        }

    }

    private boolean checkForChanges(EditText nomeToCheck, EditText cognomeToCheck, EditText datanToCheck,
                                    EditText emailToCheck, EditText zonaToCheck){
        if( !(nomeToCheck.getText().toString().equals(MainActivity.curatore.getNome())) ||
                !(cognomeToCheck.getText().toString().equals(MainActivity.curatore.getCognome())) ||
                !(datanToCheck.getText().toString().equals(MainActivity.curatore.getShorterDataNascita())) ||
                !(emailToCheck.getText().toString().equals(MainActivity.curatore.getEmail())) ||
                !(zonaToCheck.getText().toString().equals(MainActivity.curatore.getEmail()))){
            return true;
        }

        Snackbar.make(getWindow().getDecorView().getRootView(), "Nessuna modifica effettuata", Snackbar.LENGTH_LONG).show();
        return false;
    }

    private boolean validateDate(String dateToValid){
        //output is SimpleDateFormat of this type ("dd/MM/yyyy")
        try {
            if(countSlashes(dateToValid)){
                CuratoreMuseale.output.parse(dateToValid);
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
        dialogBuilder.setTitle("Modifica Password");
        dialogBuilder.setMessage("Inserisci la nuova password");

        LinearLayout lp = new LinearLayout(this);

        lp.setLayoutParams( new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        );

        final EditText passwordInput = new EditText(view.getContext());

        passwordInput.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        passwordInput.setHint("Inserisci nuova password");
        passwordInput.setTextSize(TypedValue.COMPLEX_UNIT_SP ,14);
        passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        passwordInput.setTransformationMethod(new PasswordTransformationMethod());

        final ToggleButton tb = new ToggleButton(this);
        tb.setChecked(false);
        tb.setTextOff("Mostra");
        tb.setTextOn("Nascondi");
        tb.setText("Mostra");
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
                    MainActivity.curatore.setPassword(passToSave);
                    MainActivity.curatore.updateDataDb(view.getContext());

                    //Necessario per chiudere la tastiera dopo aver premuto OK
                    InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(passwordInput.getWindowToken(), 0);

                    Snackbar.make(getWindow().getDecorView().getRootView(), "Password aggiornata con successo!",
                            Snackbar.LENGTH_LONG).show();

                } catch (NoSuchAlgorithmException e) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Impossibile procedere",
                            Snackbar.LENGTH_LONG).show();
                }

            }
        });

        dialogBuilder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        dialogBuilder.show();

    }

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
            e.printStackTrace();
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
                            .setTitle("Permessi Galleria")
                            .setMessage("Consentire all'app l'accesso alla galleria, negando l'accesso non si potrà caricare una nuova foto.")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(AreaPersonaleCuratore.this,
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

                MainActivity.curatore.setPropic(encImage);
                MainActivity.curatore.updateDataDb(this);

                Snackbar.make(getWindow().getDecorView().getRootView(), "Foto caricata con successo!",
                        Snackbar.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Impossibile procedere",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkFieldsBeforeLeaving(){
        String textNome = ((EditText) findViewById(R.id.txt_nome)).getText().toString();
        String textCognome = ((EditText) findViewById(R.id.txt_cognome)).getText().toString();
        String textDatan = ((EditText) findViewById(R.id.txt_datan)).getText().toString();
        String textEmail = ((EditText) findViewById(R.id.txt_email)).getText().toString();
        Long longZona = Long.parseLong(((EditText) findViewById(R.id.txt_zona)).getText().toString());

        boolean check1 = textNome.equals(MainActivity.curatore.getNome());
        boolean check2 = textCognome.equals(MainActivity.curatore.getCognome());
        boolean check3 = textDatan.equals(MainActivity.curatore.getShorterDataNascita());
        boolean check4 = textEmail.equals(MainActivity.curatore.getEmail());
        boolean check5 = longZona == MainActivity.curatore.getZona();

        if(check1 && check2 && check3 && check4 && check5){
            return true;
        } else {
            return false;
        }
    }

    @Override
    // controllo uscita senza salvare da barra
    public boolean onOptionsItemSelected(MenuItem item) {

        if(!checkFieldsBeforeLeaving()) {
            new AlertDialog.Builder(this)
                    .setTitle("Uscire?")
                    .setMessage("Uscire senza salvare le modifiche?")
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
    // controllo uscita senza salvare da tasto indietro (tasto sulla barra inferiore del telefono)
    public void onBackPressed(){

        if(!checkFieldsBeforeLeaving()) {
            new AlertDialog.Builder(this)
                    .setTitle("Uscire?")
                    .setMessage("Uscire senza salvare le modifiche?")
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

}
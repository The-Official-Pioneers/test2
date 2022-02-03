package it.uniba.pioneers.testtool;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import it.uniba.pioneers.data.users.Visitatore;

public class AreaPersonaleVisitatore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_area_personale_visitatore);

        ImageView propic = (ImageView) findViewById(R.id.img_propic);

        EditText nome = (EditText) findViewById(R.id.txt_nome);
        EditText cognome = (EditText) findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) findViewById(R.id.txt_datan);
        EditText email = (EditText) findViewById(R.id.txt_email);

        System.out.println("NOME: " + MainActivity.visitatore.getNome());
        System.out.println("COGNOME: " + MainActivity.visitatore.getCognome());
        System.out.println("EMAIL: " + MainActivity.visitatore.getEmail());
        System.out.println("DATA NASCITA: " + MainActivity.visitatore.getShorterDataNascita());

        byte[] bytes = Base64.decode(MainActivity.visitatore.getPropic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        propic.setImageBitmap(decodedByte);
        propic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        nome.setText(MainActivity.visitatore.getNome());
        cognome.setText(MainActivity.visitatore.getCognome());
        email.setText(MainActivity.visitatore.getEmail());
        datanascita.setText(MainActivity.visitatore.getShorterDataNascita());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void editProfile(View view){

        EditText nome = (EditText) findViewById(R.id.txt_nome);
        EditText cognome = (EditText) findViewById(R.id.txt_cognome);
        EditText datanascita = (EditText) findViewById(R.id.txt_datan);
        EditText email = (EditText) findViewById(R.id.txt_email);

        if(checkForChanges(nome,cognome,datanascita,email)){

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
                Snackbar.make(getWindow().getDecorView().getRootView(), "Profilo aggiornato con successo!",
                        Snackbar.LENGTH_LONG).show();
            } catch(ParseException e){
                Snackbar.make(getWindow().getDecorView().getRootView(), "Impossibile procedere",
                        Snackbar.LENGTH_LONG).show();
            }

        }

    }

    private boolean checkForChanges(EditText nomeToCheck, EditText cognomeToCheck, EditText datanToCheck, EditText emailToCheck){

        if( !(nomeToCheck.getText().toString().equals(MainActivity.visitatore.getNome())) ||
                !(cognomeToCheck.getText().toString().equals(MainActivity.visitatore.getCognome())) ||
                !(datanToCheck.getText().toString().equals(MainActivity.visitatore.getShorterDataNascita())) ||
                !(emailToCheck.getText().toString().equals(MainActivity.visitatore.getEmail()))){
            return true;
        }

        Snackbar.make(getWindow().getDecorView().getRootView(), "Nessuna modifica effettuata", Snackbar.LENGTH_LONG).show();
        return false;
    }

    private boolean validateDate(String dateToValid){

        //output is SimpleDateFormat of this type ("dd/MM/yyyy")
        try {
            if(countSlashes(dateToValid)){
                Visitatore.output.parse(dateToValid);
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
                    MainActivity.visitatore.setPassword(passToSave);
                    MainActivity.visitatore.updateDataDb(view.getContext());
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

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

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

                MainActivity.visitatore.setPropic(encImage);
                MainActivity.visitatore.updateDataDb(this);

                Snackbar.make(getWindow().getDecorView().getRootView(), "Foto caricata con successo!",
                        Snackbar.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                Snackbar.make(getWindow().getDecorView().getRootView(), "Impossibile procedere",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {         // controllo uscita senza salvare da barra

        String textNome = ((EditText) findViewById(R.id.txt_nome)).getText().toString();
        String textCognome = ((EditText) findViewById(R.id.txt_cognome)).getText().toString();
        String textDatan = ((EditText) findViewById(R.id.txt_datan)).getText().toString();
        String textEmail = ((EditText) findViewById(R.id.txt_email)).getText().toString();

        boolean check1 = textNome.equals(MainActivity.visitatore.getNome());
        boolean check2 = textCognome.equals(MainActivity.visitatore.getCognome());
        boolean check3 = textDatan.equals(MainActivity.visitatore.getDataNascita());
        boolean check4 = textEmail.equals(MainActivity.visitatore.getEmail());


        if(!check1 || !check2 ||!check3 ||!check4) {
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
    public void onBackPressed(){     // controllo uscita senza salvare da tasto indietro

        String textNome = ((EditText) findViewById(R.id.txt_nome)).getText().toString();
        String textCognome = ((EditText) findViewById(R.id.txt_cognome)).getText().toString();
        String textDatan = ((EditText) findViewById(R.id.txt_datan)).getText().toString();
        String textEmail = ((EditText) findViewById(R.id.txt_email)).getText().toString();

        boolean check1 = textNome.equals(MainActivity.visitatore.getNome());
        boolean check2 = textCognome.equals(MainActivity.visitatore.getCognome());
        boolean check3 = textDatan.equals(MainActivity.visitatore.getDataNascita());
        boolean check4 = textEmail.equals(MainActivity.visitatore.getEmail());


        if(!check1 || !check2 ||!check3 ||!check4) {
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
            super.onBackPressed();
        }

        super.onBackPressed();
    }

}

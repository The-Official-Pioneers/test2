package it.uniba.pioneers.testtool;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
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

        byte[] bytes = Base64.decode(MainActivity.visitatore.getPropic(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        propic.setImageBitmap(decodedByte);

        nome.setText(MainActivity.visitatore.getNome());
        cognome.setText(MainActivity.visitatore.getCognome());
        email.setText(MainActivity.visitatore.getEmail());
        datanascita.setText(MainActivity.visitatore.getShorterDataNascita());

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

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Snackbar.make(getWindow().getDecorView().getRootView(), "Nessuna foto scelta", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Bitmap newProfilePic = extras.getParcelable("data");

                ByteArrayOutputStream baos = new  ByteArrayOutputStream();
                newProfilePic.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String propicString = Base64.encodeToString(b, Base64.DEFAULT);

                ImageView oldPropic = (ImageView) findViewById(R.id.img_propic);
                oldPropic.setImageBitmap(newProfilePic);

                System.out.println("AWE E' CAMBIATA LA FOTO SIUM");
                //MainActivity.visitatore.setPropic(propicString);

            }
        }
    }

}
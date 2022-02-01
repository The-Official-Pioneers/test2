package it.uniba.pioneers.testtool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoOpera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_opera);
        TextView titolo = (TextView) findViewById(R.id.txt_titolo);
        TextView descrizione = (TextView) findViewById(R.id.txt_descrizione);
        TextView altezza = (TextView) findViewById(R.id.txt_altezza);
        ImageView img = (ImageView) findViewById(R.id.img_foto);

        byte[] bytes = Base64.decode(MainActivity.opera.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(decodedByte);

        titolo.setText(MainActivity.opera.getTitolo());
        descrizione.setText(MainActivity.opera.getDescrizione());
        altezza.setText(String.valueOf(MainActivity.opera.getAltezza())+" metri");
    }
}
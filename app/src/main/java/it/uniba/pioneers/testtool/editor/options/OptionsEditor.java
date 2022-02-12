package it.uniba.pioneers.testtool.editor.options;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.sql.Time;
import java.time.Instant;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo_modifica.GrafoFragment;

public class OptionsEditor extends LinearLayout {

    ImageButton dowloadVisita = null;
    ImageButton shareVisita = null;
    ImageButton deleteVisita = null;

    public File writeFileOnInternalStorage(String sFileName, String sBody){
        File dir = new File(getContext().getFilesDir(),"visite");
        if(!dir.exists()){
            dir.mkdir();
        }
        File gpxfile = new File(dir, sFileName);

        try {
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

            return  gpxfile;
        } catch (Exception e){
            e.printStackTrace();
            return gpxfile;
        }
    }

    public void initButtonsListener(){
        dowloadVisita.setOnClickListener(view -> {

        });

        shareVisita.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            try {
                JSONObject exportedJson = GrafoFragment.grafoFragment.graph.exportToJson();
                int idVisita = exportedJson.getJSONObject("visita").getJSONObject("data").getInt("id");
                String exportedString = exportedJson.toString(4);
                StringBuilder filename = new StringBuilder();
                generateFilename(idVisita, filename);

                File expFile = writeFileOnInternalStorage(filename.toString(), exportedString);

                Uri contentUri = getUriForFile(getContext(), "it.uniba.pioneers.testtool", expFile);

                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_STREAM, contentUri);

                Intent chooserIntent = Intent.createChooser(i, "CONDIVIDI LA TUA VISITA!");

                GrafoFragment.grafoFragment.getActivity().startActivity(chooserIntent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        deleteVisita.setOnClickListener(view -> {

        });
    }

    private void generateFilename(int idVisita, StringBuilder filename) {
        filename.append("visita");
        filename.append(idVisita);
        filename.append("_");
        filename.append(Time.from(Instant.now()).getTime());
        filename.append(".json");
    }

    public void init(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.widget_options_grafo, this);

        dowloadVisita = findViewById(R.id.dowloadVisita);
        shareVisita = findViewById(R.id.shareVisita);
        deleteVisita = findViewById(R.id.deleteVisita);

        initButtonsListener();

    }

    public OptionsEditor(Context context) {
        super(context);
        init(context);
    }

    public OptionsEditor(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OptionsEditor(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
}

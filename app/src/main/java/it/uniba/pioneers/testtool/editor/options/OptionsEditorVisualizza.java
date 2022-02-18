package it.uniba.pioneers.testtool.editor.options;

import static androidx.core.content.FileProvider.getUriForFile;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo_visualizza.GrafoVisualizzaFragment;

public class OptionsEditorVisualizza extends OptionsEditor {


    public OptionsEditorVisualizza(Context context) {
        super(context);
    }

    public OptionsEditorVisualizza(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionsEditorVisualizza(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initButtonsListener() {
        dowloadVisita.setOnClickListener(view -> {

        });

        shareVisita.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            try {
                JSONObject exportedJson = GrafoVisualizzaFragment.grafoVisualizzaFragment.graph.exportToJson();
                int idVisita = exportedJson.getJSONObject("visita").getJSONObject("data").getInt("id");
                String exportedString = exportedJson.toString(4);
                StringBuilder filename = new StringBuilder();
                generateFilename(idVisita, filename);

                File expFile = writeFileOnInternalStorage(filename.toString(), exportedString);

                Uri contentUri = getUriForFile(getContext(), "it.uniba.pioneers.testtool", expFile);

                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_STREAM, contentUri);
                Intent chooserIntent = Intent.createChooser(i, getResources().getString(R.string.condividi_visita));

                if(i.resolveActivity(getContext().getPackageManager()) != null){
                    GrafoVisualizzaFragment.grafoVisualizzaFragment.getActivity().startActivity(chooserIntent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        deleteVisita.setOnClickListener(view -> {

        });
    }
}

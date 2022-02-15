package it.uniba.pioneers.testtool.editor.node.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;

import java.sql.Date;
import java.time.Instant;

import it.uniba.pioneers.data.server.Server;
import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.testtool.editor.node.enums.NodeType;
import it.uniba.pioneers.testtool.editor.node.GraphNodeVisualizza;

public class NodeVisualizzaDialog{

    public static LinearLayout getRow(Context context, String key, String value){
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView keyLabel = new TextView(context);
        keyLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        keyLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        keyLabel.setPadding(20, 20, 20, 20);
        keyLabel.setText(key);

        TextView valueLabel = new TextView(context);
        valueLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        valueLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        valueLabel.setPadding(20, 20, 20, 20);
        valueLabel.setTypeface(null, Typeface.BOLD);
        valueLabel.setText(value);

        layout.addView(keyLabel);
        layout.addView(valueLabel);

        return layout;
    }

    protected static AlertDialog getDialog(Context context, GraphNodeVisualizza nodeObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        try {
            int id = nodeObject.data.getInt("id");
            setDialogTitle(nodeObject, builder, id);

            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                dialogInterface.cancel();
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AlertDialog tmpDialog = builder.create();
        loadDataLayout(context, nodeObject, tmpDialog);
        return tmpDialog;
    }

    public static void setDialogTitle(GraphNodeVisualizza nodeObject, AlertDialog.Builder builder, int id) {
        if(nodeObject.type == NodeType.VISITA){
            builder.setTitle("VISITA #"+ id);
        }else if(nodeObject.type == NodeType.ZONA){
            builder.setTitle("ZONA #"+ id);
        }else if(nodeObject.type == NodeType.AREA){
            builder.setTitle("AREA #"+ id);
        }else if(nodeObject.type == NodeType.OPERA){
            builder.setTitle("OPERA #"+ id);
        }
    }

    private static void loadDataLayout(Context context, GraphNodeVisualizza nodeObject, AlertDialog tmpDialog) {
        ConstraintLayout alertLayout = new ConstraintLayout(context);

        ScrollView scrollView = new ScrollView(context);

        LinearLayout ln = new LinearLayout(context);
        ln.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(ln);
        alertLayout.addView(scrollView);
        try {
            if(nodeObject.type == NodeType.VISITA){
                String luogo = nodeObject.data.getString("luogo");
                ln.addView(getRow(context, "LUOGO", luogo));

                String dateTmp = Date.from(Instant.ofEpochSecond(Long.parseLong(nodeObject.data.getString("data")))).toString();
                ln.addView(getRow(context, "DATA CREAZIONE", dateTmp));

            }else if(nodeObject.type == NodeType.ZONA){
                String denominazione = nodeObject.data.getString("denominazione");
                ln.addView(getRow(context, "DENOMINAZIONE", denominazione));

                String descrizione = nodeObject.data.getString("descrizione");
                ln.addView(getRow(context, "DESCRIZIONE", descrizione));

                String tipo = nodeObject.data.getString("tipo");
                ln.addView(getRow(context, "TIPO", tipo));

                String luogo = nodeObject.data.getString("luogo");
                ln.addView(getRow(context, "LUOGO", luogo));

            }else if(nodeObject.type == NodeType.AREA){
                String nome = nodeObject.data.getString("nome");
                ln.addView(getRow(context, "NOME", nome));

            }else if(nodeObject.type == NodeType.OPERA){
                loadImage(context, nodeObject, ln); //CARICAMENTO DINAMICO IMMAGINE TRAMITE STREAM DEL WEBSERVER

                String titolo = nodeObject.data.getString("titolo");
                ln.addView(getRow(context, "TITOLO", titolo));

                String descrizione = nodeObject.data.getString("descrizione");
                ln.addView(getRow(context, "DESCRIZIONE", descrizione));

                String altezza = nodeObject.data.getString("altezza");
                ln.addView(getRow(context, "ALTEZZA", altezza));

                String larghezza = nodeObject.data.getString("larghezza");
                ln.addView(getRow(context, "LARGHEZZA", larghezza));

                String profondita = nodeObject.data.getString("profondita");
                ln.addView(getRow(context, "PROFONDITA", profondita));

            }
            tmpDialog.setView(alertLayout);
            //Toast.makeText(context, nodeObject.data.toString(4), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void loadImage(Context context, GraphNodeVisualizza nodeObject, LinearLayout ln) throws JSONException {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 2));
        layout.setBackgroundColor(Color.WHITE);

        WebView webView = new WebView(layout.getContext());
        webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1000));
        webView.setBackgroundColor(Color.WHITE);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE); //CARICAMENTO DELL'IMMAGINE DALLA CACHE SE POSSIBILE
        webView.getSettings().setJavaScriptEnabled(false);
        layout.addView(webView);
        ln.addView(layout);

        webView.loadUrl(Server.getUrl()+"/opera/image/"+ nodeObject.data.getInt(DbContract.OperaEntry.COLUMN_ID));
    }
}

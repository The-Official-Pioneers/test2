package it.uniba.pioneers.testtool.editor.grafo_modifica;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import it.uniba.pioneers.data.Visita;

public class DisplayGrafoModifica extends ConstraintLayout {
    public GrafoModifica graph = null;

    DisplayGrafoModifica self = null;
    Context context = null;
    public static Visita visita = null;

    public void init(){
        self = this;
        graph = new GrafoModifica(context, visita);
        graph.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //AGGIUNTA DEL GRAFO AL DISPLAYGRAFO
        addView(graph);
    }


    public DisplayGrafoModifica(@NonNull Context context, Visita visita) {
        super(context);
        this.visita = visita;
        this.context = context;
        init();
    }
}
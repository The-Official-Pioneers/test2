package it.uniba.pioneers.testtool.editor.grafo_visualizza;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import it.uniba.pioneers.data.Visita;

public class DisplayGrafoVisualizza extends ConstraintLayout {
    public GrafoVisualizza graph = null;

    DisplayGrafoVisualizza self = null;
    Context context = null;
    public static Visita visita = null;

    public void init(){
        self = this;
        graph = new GrafoVisualizza(context);
        graph.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //AGGIUNTA DEL GRAFO AL DISPLAYGRAFO
        addView(graph);
    }


    public DisplayGrafoVisualizza(@NonNull Context context, Visita visita) {
        super(context);
        this.visita = visita;
        this.context = context;
        init();
    }
}

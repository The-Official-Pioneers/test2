package it.uniba.pioneers.testtool.editor.grafo_visualizza;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DisplayGrafoVisualizza extends ConstraintLayout {
    public GrafoVisualizza graph = null;

    DisplayGrafoVisualizza self = null;
    Context context = null;

    public void init(){
        self = this;
        graph = new GrafoVisualizza(context);
        graph.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(graph); //AGGIUNTA DEL GRAFO AL DISPLAYGRAFO
    }


    public DisplayGrafoVisualizza(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }
}

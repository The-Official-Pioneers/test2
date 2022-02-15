package it.uniba.pioneers.testtool.editor.grafo_modifica;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;

public class DisplayGrafoModifica extends ConstraintLayout {
    public GrafoModifica graph = null;

    ListaNodi listaNodi = null;
    DisplayGrafoModifica self = null;
    Context context = null;

    public void init(){
        self = this;
        graph = new GrafoModifica(context);
        graph.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(graph); //AGGIUNTA DEL GRAFO AL DISPLAYGRAFO
    }


    public DisplayGrafoModifica(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }
}

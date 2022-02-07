package it.uniba.pioneers.testtool.editor.grafo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.node.ListNode;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;

public class DisplayGrafo extends ConstraintLayout {
    public Grafo graph = null;

    ListaNodi listaNodi = null;
    DisplayGrafo self = null;
    Context context = null;

    public void init(){
        self = this;
        graph = new Grafo(context);
        graph.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(this.graph); //AGGIUNTA DEL GRAFO AL DISPLAYGRAFO
    }


    public DisplayGrafo(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public DisplayGrafo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DisplayGrafo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DisplayGrafo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }
}

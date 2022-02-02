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
        //LayoutInflater layoutInflater = LayoutInflater.from(context);
        //layoutInflater.inflate(R.layout.widget_display_grafo, this);
        graph = new Grafo(context);

        this.addView(this.graph); //AGGIUNTA DEL GRAFO AL DISPLAYGRAFO
        Log.v("ROOTVIEW", String.valueOf(getRootView().findViewById(R.id.listaNodi)));
        self = this;
        //setOnDragListener(new MyOnDragListener()); // SET LISTENER ONDRAG
        this.graph.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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

    private class MyOnDragListener implements OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    listaNodi = getRootView().findViewById(R.id.listaNodi);

                    if(listaNodi != null){
                        Log.v("ROOTVIEW-SERIO", String.valueOf(event.getLocalState()));
                        ListNode tmp = ((ListNode) event.getLocalState()); // CE BISOGNO DI CONVERSIONE DA DATANODE A GRAPHDATANODE
                        tmp.setVisibility(GONE);

                        listaNodi.linearLayout.removeView(tmp);
                        //graph.addSuccessorToNode(graph.visitaIniziale, new GraphNode(context, graph, NodeType.ZONA));
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }
}

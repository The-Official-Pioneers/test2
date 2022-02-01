package it.uniba.pioneers.testtool.editor.grafo.node;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.common.graph.MutableGraph;

import org.json.JSONException;
import org.json.JSONObject;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.Grafo;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;
import it.uniba.pioneers.widget.NodeType;

public class GraphNode extends Node {
    Grafo graphParent = null;
    MutableGraph<GraphNode> graph = null;
    GraphNode self = null;

    public boolean inizializated = false;
    public void setInizializated(boolean flag){
        this.inizializated = flag;
    }

    public void init(Context context) throws JSONException {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sample_node, this);

        data = new JSONObject();
        setCircle(false);
        this.setOnDragListener(new MyDragListener());

        // Defines the one method for the interface, which is called when the View is long-clicked
        //this.setOnTouchListener(new MyTouchListener());

    }

    private class MyDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if(self.type == NodeType.VISITA){
                        graphParent.addSuccessorToNode(self, new GraphNode(graphParent.getContext(), graphParent, NodeType.ZONA));

                    }else if(self.type == NodeType.ZONA){
                        graphParent.addSuccessorToNode(self, new GraphNode(graphParent.getContext(), graphParent, NodeType.AREA));

                    }else if(self.type == NodeType.AREA){
                        graphParent.addSuccessorToNode(self, new GraphNode(graphParent.getContext(), graphParent, NodeType.OPERA));

                    }else if(self.type == NodeType.OPERA){

                    }

                    break;
                default:
                    break;
            }
            return true;
        }
    }


    public GraphNode(@NonNull Context context) {
        super(context);
    }

    public void addSuccessor(GraphNode dataNodeEnd){
        this.graph.addNode(dataNodeEnd);
        this.graph.putEdge(this, dataNodeEnd);
    }


    public GraphNode(@NonNull Context context, Grafo graphParent, NodeType tipo) {
        super(context);
        try {
            type = tipo;
            graph = graphParent.graph;
            self = this;
            this.graphParent = graphParent;

            init(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

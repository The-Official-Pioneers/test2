package it.uniba.pioneers.testtool.editor.grafo.draw;

import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.node.GraphNode;
import it.uniba.pioneers.testtool.editor.grafo.node.NodeType;

public class Line {

    float startX, startY, stopX, stopY;
    public GraphNode startNode;
    public GraphNode endNode;

    private float fromDpToPx(int dip){
        Resources r = startNode.getResources();
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
    }

    public Line(GraphNode startNode, GraphNode endNode) {

        this.startNode = startNode;
        this.endNode = endNode;


        View vStart = startNode.findViewById(R.id.vistaProva);
        Log.v("START-NODE", String.valueOf(vStart.getWidth()));

        this.startX = startNode.getX() + ((float)startNode.size/2);
        this.startY = startNode.getY() + (float)startNode.size;



        View vEnd = endNode.findViewById(R.id.vistaProva);
        Log.v("START-NODE-END", String.valueOf(vEnd.getWidth()));

        //IL NODO END HA PROP SETTATE
        if(endNode.type == NodeType.ZONA){
            this.stopX = endNode.getX() + ((float)startNode.size/2);
            this.stopY = endNode.getY();
        }else{
            this.stopX = endNode.getX() + ((float)startNode.size/2);
            this.stopY = endNode.getY();
        }

    }

}

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

        startX = startNode.getX() + ((float)startNode.size/2);
        startY = startNode.getY() + (float)startNode.size;

        stopX = endNode.getX() + ((float)endNode.size/2);
        stopY = endNode.getY() + ((float)endNode.size/100)*4;

    }

}

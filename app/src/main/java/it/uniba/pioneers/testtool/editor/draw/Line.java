package it.uniba.pioneers.testtool.editor.draw;
import android.util.Log;

import it.uniba.pioneers.testtool.editor.node.GraphNodeModifica;
import it.uniba.pioneers.testtool.editor.node.GraphNodeVisualizza;

public class Line {
    float startX, startY, stopX, stopY;
    public GraphNodeModifica startNode;
    public GraphNodeModifica endNode;

    public GraphNodeVisualizza startNodeView;
    public GraphNodeVisualizza endNodeView;

    public Line(GraphNodeModifica startNode, GraphNodeModifica endNode) {
        this.startNode = startNode;
        this.endNode = endNode;

        startX = (startNode).getX() + ((float)(startNode).size/2);
        startY = (startNode).getY() + (float)(startNode).size;

        Log.v("Start-x", String.valueOf(startX));
        Log.v("Start-y", String.valueOf(startY));
        Log.v("Start-node-size", String.valueOf((float)(startNode).size));
        Log.v("Start-half-node", String.valueOf(((float)(startNode).size/2)));

        stopX = (endNode).getX() + ((float)(endNode).size/2);
        stopY = (endNode).getY() + ((float)(endNode).size/100)*4;
    }

    public Line(GraphNodeVisualizza startNode, GraphNodeVisualizza endNode) {
        this.startNodeView = startNode;
        this.endNodeView = endNode;

        startX = (startNodeView).getX() + ((float)(startNodeView).size/2);
        startY = (startNodeView).getY() + (float)(startNodeView).size;

        stopX = (endNodeView).getX() + ((float)(endNodeView).size/2);
        stopY = (endNodeView).getY() + ((float)(endNodeView).size/100)*4;
    }
}

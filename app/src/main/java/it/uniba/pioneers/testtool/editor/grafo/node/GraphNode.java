package it.uniba.pioneers.testtool.editor.grafo.node;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.common.graph.MutableGraph;

import org.json.JSONObject;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.Grafo;
import it.uniba.pioneers.testtool.editor.grafo.draw.Line;
import it.uniba.pioneers.testtool.editor.grafo.node.dialogs.NodeDialog;

public class GraphNode extends Node {
    Grafo graphParent = null;
    public NodeType type;
    public MutableGraph<GraphNode> graph = null;
    GraphNode self = null;
    public JSONObject data = null;

    public int size = 0;

    public boolean inizializated = false;

    public void setInizializated(boolean flag){
        this.inizializated = flag;
    }

    public void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sample_node, this);

        setOnDragListener(new MyDragListener());
    }

    public void deleteNode(){
        this.hide();
        this.hideAllChild();
        if(type == NodeType.ZONA){  
            graphParent.drawView.linesZona.removeIf(line -> line.endNode.equals(this));
        }else if(type == NodeType.AREA){
            graphParent.drawView.linesArea.removeIf(line -> line.endNode.equals(this));
        }else if(type == NodeType.OPERA){
            graphParent.drawView.linesOpera.removeIf(line -> line.endNode.equals(this));
        }
        graphParent.graph.removeNode(this);
    }

    @NonNull
    private Set<GraphNode> getPredecessors(Grafo graphParent, GraphNode self) {
        return graphParent.graph.predecessors(self);
    }

    private class MyDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DROP:
                    JSONObject data = ((ListNode)event.getLocalState()).data; //CONVERSIONE DI TIPO DA ListNode -> GraphNode
                    if(self.type == NodeType.VISITA){
                        graphParent.addSuccessorToNode(self, new GraphNode(graphParent.getContext(), graphParent, NodeType.ZONA, data));
                        Log.v("DROP", "VISITA");

                    }else if(self.type == NodeType.ZONA){
                        graphParent.addSuccessorToNode(self, new GraphNode(graphParent.getContext(), graphParent, NodeType.AREA, data));
                        Log.v("DROP", "ZONA");

                    }else if(self.type == NodeType.AREA){
                        ListNode listNode = ((ListNode)event.getLocalState());
                        Log.v("DROP", "AREA");
                        if(listNode.type == NodeType.OPERA){
                            Log.v("DROP", "AREA/OPERA");
                            GraphNode graphNode = new GraphNode(graphParent.getContext(), graphParent, listNode.type, listNode.data);
                            self.hideAllChild();
                            //self.addSuccessor(graphNode);

                            //self.drawAllChild();
                            //listNode.setVisibility(GONE);
                        }else{
                            listNode.reset();
                            listNode.setVisibility(VISIBLE);
                        }


                    }else if(self.type == NodeType.OPERA){

                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    public void addSuccessor(GraphNode dataNodeEnd){
        this.graph.addNode(dataNodeEnd);
        this.graph.putEdge(this, dataNodeEnd);
    }

    private void setFields(Grafo graphParent, NodeType type) {
        this.graph = graphParent.graph;
        this.self = this;
        this.type = type;
        this.graphParent = graphParent;
    }

    private void setFields(Grafo graphParent, NodeType type, JSONObject data) {
        setFields(graphParent, type);
        this.data = data;
    }

    public GraphNode(@NonNull Context context, Grafo graphParent, NodeType type, JSONObject data) {
        super(context);
        setFields(graphParent, type, data);
        init(context);

        setOnClickListener(graphParent, type);
        setOnLongClickListener(context);

    }



    private void setOnClickListener(Grafo graphParent, NodeType type) {
        if(type != NodeType.VISITA && type != NodeType.OPERA){
            setOnClickListener(view -> {//THIS (View)
                resetLines();
                inizializated = true;

                Log.v("ckck", String.valueOf(clicked));
                if(clicked){
                    clicked = false;
                    setCircle(false);
                    hideAllChild();
                    for(GraphNode node : getSuccessors(graphParent, (GraphNode) view)){
                        node.clicked = false;
                        node.setCircle(false);
                        node.hideAllChild();
                    }
                }else{
                    if(type == NodeType.VISITA){
                        graphParent.drawView.resetDrawView(graphParent, 1);
                    }else if(type == NodeType.ZONA){
                        graphParent.drawView.resetDrawView(graphParent, 2);
                    }else if(type == NodeType.AREA){
                        graphParent.drawView.resetDrawView(graphParent, 3);
                    }else if(type == NodeType.OPERA){
                    }

                    hideAllChild();
                    drawAllChild();
                    clicked = true;
                    setCircle(true);

                    for(GraphNode node : getSuccessors(graphParent, self)){
                        if(node != self){
                            node.clicked = false;
                            node.setCircle(false);
                            node.hideAllChild();
                        }
                    }
                }
            });
        }
    }

    @NonNull
    private Set<GraphNode> getSuccessors(Grafo graphParent, GraphNode view) {
        return graphParent.graph.successors((GraphNode) getPredecessors(graphParent, view).toArray()[0]);
    }

    public void drawAllChild(){
        int numSuccessors = graphParent.graph.successors(this).size();

        if(numSuccessors <= 3){
            size = 200;
        }else if(numSuccessors < 6){
            size = 150;
        }else if(numSuccessors < 9){
            size = 120;
        }else {
            size = 90;
        }

        draw();
        AtomicInteger count = new AtomicInteger(1);

        for(GraphNode nodeChild : graphParent.graph.successors(this)){
            if(type == NodeType.VISITA){
                nodeChild.setY(graphParent.r2);
                nodeChild.setX(count.getAndIncrement() * graphParent.calcX(numSuccessors));

                graphParent.drawView.linesZona.add(graphParent.buildLineGraph(this, nodeChild));

                nodeChild.draw();
            }else if(type == NodeType.ZONA){
                nodeChild.setY(graphParent.r3);
                nodeChild.setX(count.getAndIncrement() * graphParent.calcX(numSuccessors));

                graphParent.drawView.linesArea.add(graphParent.buildLineGraph(this, nodeChild));

                nodeChild.draw();
            }else if(type == NodeType.AREA){
                nodeChild.setY(graphParent.r4);
                nodeChild.setX(count.getAndIncrement() * graphParent.calcX(numSuccessors));

                graphParent.drawView.linesOpera.add(graphParent.buildLineGraph(this, nodeChild));

                nodeChild.draw();
            }else if(type == NodeType.OPERA){
                nodeChild.draw();
            }

            nodeChild.findViewById(R.id.vistaProva)
                    .setLayoutParams(new LinearLayout.LayoutParams(size, size));
        }
    }


    public void draw(){
        boolean flag = true;

        for(int k = 0; k < graphParent.getChildCount(); ++k){
            if(graphParent.getChildAt(k).equals(this)){
                flag = false;
                break;
            }
        }

        if(flag){
            graphParent.addView(this);
            setInizializated(true);
            clicked = false;
        }
        resetLines();

        if(type != NodeType.VISITA && type != NodeType.OPERA){
            setCircle(false);
        }else{
            setCircle(true);
        }

        setVisibility(VISIBLE);

    }

    public void hide(){
        resetLines();
        setCircle(false);
        clicked = false;
        setVisibility(INVISIBLE);
    }

    public void hideAllChild(){
        for(GraphNode nodeChild : graphParent.graph.successors(this)){
            if(type == NodeType.VISITA){
                graphParent.drawView.linesZona.removeIf(line -> line.startNode.equals(this));
                nodeChild.hideAllChild();
            }else if(type == NodeType.ZONA){
                graphParent.drawView.linesArea.removeIf(line -> line.startNode.equals(this));
                nodeChild.hideAllChild();
            }else if(type == NodeType.AREA){
                graphParent.drawView.linesOpera.removeIf(line -> line.startNode.equals(this));
                nodeChild.hideAllChild();
            }
            nodeChild.hide();
        }
    }

    private void resetLines() {
        if(type == NodeType.VISITA){
            graphParent.drawView.resetDrawView(graphParent, 0);
        }else if(type == NodeType.ZONA){
            graphParent.drawView.resetDrawView(graphParent, 1);
        }else if(type == NodeType.AREA){
            graphParent.drawView.resetDrawView(graphParent, 2);
        }else if(type == NodeType.OPERA){
            graphParent.drawView.resetDrawView(graphParent, 3);
        }
    }
}

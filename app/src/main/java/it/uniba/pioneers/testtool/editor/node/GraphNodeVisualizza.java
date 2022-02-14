package it.uniba.pioneers.testtool.editor.node;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.graph.MutableGraph;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo_visualizza.GrafoVisualizza;
import it.uniba.pioneers.testtool.editor.node.dialogs.NodeDialog;

public class GraphNodeVisualizza extends Node {
    public GrafoVisualizza graphParent = null;
    public MutableGraph<GraphNodeVisualizza> graph = null;
    GraphNodeVisualizza self = null;

    public int size = 0;


    public GraphNodeVisualizza(@NonNull Context context) {
        super(context);
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
            graphParent.drawView.linesZona.removeIf(line -> line.endNodeView.equals(this));
        }else if(type == NodeType.AREA){
            graphParent.drawView.linesArea.removeIf(line -> line.endNodeView.equals(this));
        }else if(type == NodeType.OPERA){
            graphParent.drawView.linesOpera.removeIf(line -> line.endNodeView.equals(this));
        }
        graphParent.graph.removeNode(this);
    }

    @NonNull
    private Set<GraphNodeVisualizza> getPredecessors(GrafoVisualizza graphParent, GraphNodeVisualizza self) {
        return graphParent.graph.predecessors(self);
    }

    public void addSuccessor(GraphNodeVisualizza dataNodeEnd){
        this.graph.addNode(dataNodeEnd);
        this.graph.putEdge(this, dataNodeEnd);
    }

    private void setFields(GrafoVisualizza graphParent, NodeType type) {
        this.graph = graphParent.graph;
        this.self = this;
        this.type = type;
        this.graphParent = graphParent;
    }

    private void setFields(GrafoVisualizza graphParent, NodeType type, JSONObject data) {
        this.data = data;
        setFields(graphParent, type);
    }

    private class MyDragListener implements OnDragListener {
        private boolean checkRelation(ListNode listNode){
            boolean state = false;

            try {
                state =  (
                        (self.type == NodeType.VISITA && listNode.type == NodeType.ZONA)
                                || (self.type == NodeType.ZONA && listNode.type == NodeType.AREA
                                && self.data.getInt(DbContract.ZonaEntry.COLUMN_ID) == listNode.data.getInt(DbContract.AreaEntry.COLUMN_ZONA))
                                || (self.type == NodeType.AREA && listNode.type == NodeType.OPERA
                                && self.data.getInt(DbContract.AreaEntry.COLUMN_ID) == listNode.data.getInt(DbContract.OperaEntry.COLUMN_AREA))
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return state;
        }

        private boolean checkIfPresent(ListNode listNode){
            boolean state = true;

            for(GraphNodeVisualizza child : graphParent.graph.successors(self)){
                try {
                    if(child.data.getInt("id") == listNode.data.getInt("id")){
                        state = false;
                        break;
                    }
                } catch (JSONException e) {
                    state = false;
                    break;
                }
            }

            if(!state){
                Snackbar.make(self.getRootView(), "È già presente questo elemento nella visita", BaseTransientBottomBar.LENGTH_SHORT).show();
            }

            return state;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            ListNode listNode = ((ListNode)event.getLocalState());
            switch (action) {
                case DragEvent.ACTION_DROP:
                    if(checkRelation(listNode) && checkIfPresent(listNode)){
                        GraphNodeVisualizza graphNode = new GraphNodeVisualizza(graphParent.getContext(), graphParent, listNode.type, listNode.data);
                        self.hide();
                        self.hideAllNodeAtSameLevel();
                        self.hideAllChild();

                        self.draw();

                        self.addSuccessor(graphNode);
                        self.drawAllChild();
                        listNode.setVisibility(GONE);

                        self.setCircle(true);
                        self.clicked = true;
                        self.pick();
                    }else{
                        listNode.reset();
                        listNode.setVisibility(VISIBLE);
                    }
                    break;
                case DragEvent.ACTION_DRAG_STARTED:
                    if(checkRelation(listNode) && checkIfPresent(listNode)){
                        self.hideAllNodeAtSameLevel();
                        self.hideAllChild();
                        self.drawAllChild();
                        self.setCircle(true);
                        self.pick();
                    }
                default:
                    break;
            }
            return true;
        }
    }

    protected void setOnLongClickListener(@NonNull Context context) {
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                GraphNodeVisualizza node = ((GraphNodeVisualizza)view);
                System.out.println(node.data.toString());
                AlertDialog dialog1 = NodeDialog.NodeDialog(context, node);
                dialog1.show();
                return true;
            }
        });
    }

    private void setOnClickListener(GrafoVisualizza graphParent, NodeType type) {
        if(type != NodeType.VISITA && type != NodeType.OPERA){
            setOnClickListener(view -> {//THIS (View)
                setNodeSize(graphParent, type, (GraphNodeVisualizza) view);
            });
        }

        if(type != NodeType.OPERA){
            setOnClickListener(view -> {
                setNodeSize(graphParent, type, (GraphNodeVisualizza) view);
            });
        }
    }


    private void setNodeSize(GrafoVisualizza graphParent, NodeType type, GraphNodeVisualizza view) {
        inizializated = true;

        if(clicked){
            clicked = false;
            setCircle(false);
            hideAllChild();
            hideAllNodeAtSameLevel();
        }else{
            if(type == NodeType.VISITA){
                graphParent.drawView.resetDrawView(graphParent, 1);
            }else if(type == NodeType.ZONA){
                graphParent.drawView.resetDrawView(graphParent, 2);
            }else if(type == NodeType.AREA){
                graphParent.drawView.resetDrawView(graphParent, 3);
            }

            hideAllChild();
            drawAllChild();

            clicked = true;
            setCircle(true);

            hideAllNodeAtSameLevel();
            pick();
        }
    }

    public void pick() {
        setColor(Color.RED);
        if(type != NodeType.VISITA){
            ((GraphNodeVisualizza) graphParent.graph.predecessors(self).toArray()[0]).setColor(Color.YELLOW);
        }
    }

    public GraphNodeVisualizza(@NonNull Context context, GrafoVisualizza graphParent, NodeType type, JSONObject data) {
        super(context);
        setFields(graphParent, type, data);
        init(context);

        setOnClickListener(graphParent, type);
        setOnLongClickListener(context);
    }

    private void hideAllNodeAtSameLevel() {
        if(type != NodeType.VISITA){
            for(GraphNodeVisualizza node : getSuccessors(graphParent, self)){
                if(node != self){
                    node.clicked = false;
                    node.setCircle(false);
                    node.hideAllChild();
                }
            }
        }
    }

    @NonNull
    private Set<GraphNodeVisualizza> getSuccessors(GrafoVisualizza graphParent, GraphNodeVisualizza view) {
        return graphParent.graph.successors((GraphNodeVisualizza) getPredecessors(graphParent, view).toArray()[0]);
    }

    public void draw(){
        checkIfAlreadyInit();

        if(type != NodeType.VISITA && type != NodeType.OPERA){
            setCircle(false);
        }else{
            setCircle(true);
        }

        findViewById(R.id.vistaProva)
                .setLayoutParams(new LinearLayout.LayoutParams(size, size));

        setVisibility(VISIBLE);
    }

    public void drawAllChild(){
        int numSuccessors = graphParent.graph.successors(this).size();

        draw();
        AtomicInteger count = new AtomicInteger(1);

        for(GraphNodeVisualizza nodeChild : graphParent.graph.successors(this)){
            setNodeSize(numSuccessors, nodeChild);
            float tmpX = getTmpX(numSuccessors, count, nodeChild);

            if(type == NodeType.VISITA){
                nodeChild.setY(graphParent.r2);
                nodeChild.setX(tmpX);

                graphParent.drawView.linesZona.add(graphParent.buildLineGraph(this, nodeChild));

                nodeChild.draw();
            }else if(type == NodeType.ZONA){
                nodeChild.setY(graphParent.r3);
                nodeChild.setX(tmpX);

                graphParent.drawView.linesArea.add(graphParent.buildLineGraph(this, nodeChild));

                nodeChild.draw();
            }else if(type == NodeType.AREA){
                nodeChild.setY(graphParent.r4);
                nodeChild.setX(tmpX);

                graphParent.drawView.linesOpera.add(graphParent.buildLineGraph(this, nodeChild));

                nodeChild.draw();
            }else if(type == NodeType.OPERA){
                nodeChild.draw();
            }
        }
    }

    private void setNodeSize(int numSuccessors, GraphNodeVisualizza nodeChild) {

        switch (numSuccessors){
            case 1:
            case 2:
            case 3:
                nodeChild.size = 170;
                break;
            case 4:
            case 5:
            case 6:
                nodeChild.size = 150;
                break;
            case 7:
                nodeChild.size = 130;
                break;
            case 8:
            case 9:
            case 10:
                nodeChild.size = 100;
                break;
            case 11:
            case 12:
            case 13:
                nodeChild.size = 80;
                break;
            default:
                nodeChild.size = 60;
                break;
        }
    }

    private float getTmpX(int numSuccessors, AtomicInteger count, GraphNodeVisualizza nodeChild) {
        //FORMULA DI CENTRATURA DEL NODO IN BASE AL NUMERO DI NODI DI QUEL LIVELLO
        return (count.getAndIncrement() * (((float)graphParent.size.x / (numSuccessors + 1)))) - (float)nodeChild.size/2;
    }




    private void checkIfAlreadyInit() {
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
    }

    public void hide(){
        resetLines();
        setCircle(false);
        clicked = false;
        setVisibility(INVISIBLE);
    }

    public void hideAllChild(){
        for(GraphNodeVisualizza nodeChild : graphParent.graph.successors(this)){
            if(type == NodeType.VISITA){
                graphParent.drawView.linesZona.removeIf(line -> line.startNodeView.equals(this));
                nodeChild.hideAllChild();
            }else if(type == NodeType.ZONA){
                graphParent.drawView.linesArea.removeIf(line -> line.startNodeView.equals(this));
                nodeChild.hideAllChild();
            }else if(type == NodeType.AREA){
                graphParent.drawView.linesOpera.removeIf(line -> line.startNodeView.equals(this));
                nodeChild.hideAllChild();
            }
            nodeChild.hide();
        }
    }

    private void resetLines() {
        if(type == NodeType.VISITA){
        }else if(type == NodeType.ZONA){
            graphParent.drawView.resetDrawView(graphParent, 1);
        }else if(type == NodeType.AREA){
            graphParent.drawView.resetDrawView(graphParent, 2);
        }else if(type == NodeType.OPERA){
            graphParent.drawView.resetDrawView(graphParent, 3);
        }
    }
}

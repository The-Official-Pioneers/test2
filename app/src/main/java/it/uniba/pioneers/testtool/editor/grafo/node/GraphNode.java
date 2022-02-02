package it.uniba.pioneers.testtool.editor.grafo.node;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.graph.MutableGraph;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.EditorActivity;
import it.uniba.pioneers.testtool.editor.grafo.Grafo;

public class GraphNode extends Node {
    Grafo graphParent = null;
    private NodeType type;
    public MutableGraph<GraphNode> graph = null;
    GraphNode self = null;
    JSONObject data = null;

    public boolean inizializated = false;

    public void setInizializated(boolean flag){
        this.inizializated = flag;
    }

    public void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.sample_node, this);

        setOnDragListener(new MyDragListener());
    }

    public void removeAllChild(){
        for(GraphNode node : getSuccessors(graphParent, self)){
            graph.removeNode(node);
        }

        if(type == NodeType.OPERA) {
            return;
        }
        removeAllChild();
    }

    private class MyDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    JSONObject data = ((ListNode)event.getLocalState()).data; //CONVERSIONE DI TIPO DA ListNode -> GraphNode
                    if(self.type == NodeType.VISITA){
                        graphParent.addSuccessorToNode(self, new GraphNode(graphParent.getContext(), graphParent, NodeType.ZONA, data));

                    }else if(self.type == NodeType.ZONA){
                        graphParent.addSuccessorToNode(self, new GraphNode(graphParent.getContext(), graphParent, NodeType.AREA, data));

                    }else if(self.type == NodeType.AREA){
                        graphParent.addSuccessorToNode(self, new GraphNode(graphParent.getContext(), graphParent, NodeType.OPERA, data));

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
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                try {
                    GraphNode node = ((GraphNode)view);

                    int id = node.data.getInt("id");

                    if(self.type == NodeType.VISITA){
                        builder.setMessage(String.valueOf(id))
                                .setTitle("VISITA #"+id);
                    }else if(self.type == NodeType.ZONA){
                        builder.setMessage(String.valueOf(id))
                                .setTitle("ZONA #"+id);
                    }else if(self.type == NodeType.AREA){
                        builder.setMessage(String.valueOf(id))
                                .setTitle("AREA #"+id);
                    }else if(self.type == NodeType.OPERA){
                        builder.setMessage(String.valueOf(id))
                                .setTitle("OPERA #"+id);
                    }


                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
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
        return graphParent.graph.successors((GraphNode) graphParent.graph.predecessors(view).toArray()[0]);
    }

    public void drawAllChild(){
        draw();
        for(GraphNode nodeChild : graphParent.graph.successors(this)){

            if(type == NodeType.VISITA){
                graphParent.drawView.linesZona.add(graphParent.buildLineGraph(this, nodeChild));
                nodeChild.draw();
            }else if(type == NodeType.ZONA){
                graphParent.drawView.linesArea.add(graphParent.buildLineGraph(this, nodeChild));
                nodeChild.draw();
            }else if(type == NodeType.AREA){
                graphParent.drawView.linesOpera.add(graphParent.buildLineGraph(this, nodeChild));
                nodeChild.draw();
            }else if(type == NodeType.OPERA){
                nodeChild.draw();
            }

        }
    }


    public void draw(){
        if(!inizializated){
            graphParent.addView(this);
            setInizializated(true);
            resetLines();
            clicked = false;
        }

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
                graphParent.drawView.resetDrawView(graphParent, 1);
                nodeChild.hideAllChild();
            }else if(type == NodeType.ZONA){
                graphParent.drawView.resetDrawView(graphParent, 2);
                nodeChild.hideAllChild();
            }else if(type == NodeType.AREA){
                graphParent.drawView.resetDrawView(graphParent, 3);
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

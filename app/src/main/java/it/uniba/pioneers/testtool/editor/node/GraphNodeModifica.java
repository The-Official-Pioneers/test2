package it.uniba.pioneers.testtool.editor.node;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.graph.MutableGraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import it.uniba.pioneers.data.Area;
import it.uniba.pioneers.data.Visita;
import it.uniba.pioneers.data.Zona;
import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo_modifica.GrafoModifica;
import it.uniba.pioneers.testtool.editor.grafo_modifica.GrafoModificaFragment;
import it.uniba.pioneers.testtool.editor.node.dialogs.NodeDialog;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;
import it.uniba.pioneers.testtool.editor.node.enums.NodeType;

public class GraphNodeModifica extends Node {
    public GrafoModifica graphParent = null;
    public MutableGraph<GraphNodeModifica> graph = null;
    GraphNodeModifica self = null;

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
    private Set<GraphNodeModifica> getPredecessors(GrafoModifica graphParent, GraphNodeModifica self) {
        return graphParent.graph.predecessors(self);
    }

    public void addSuccessor(GraphNodeModifica dataNodeEnd){
        this.graph.addNode(dataNodeEnd);
        this.graph.putEdge(this, dataNodeEnd);
    }

    private void setFields(GrafoModifica graphParent, NodeType type) {
        this.graph = graphParent.graph;
        this.self = this;
        this.type = type;
        this.graphParent = graphParent;
    }

    private void setFields(GrafoModifica graphParent, NodeType type, JSONObject data) {
        this.data = data;
        setFields(graphParent, type);
    }

    private class MyDragListener implements OnDragListener {
        private boolean checkRelation(ListNodeModifica listNodeModifica){
            boolean state = false;

            try {
                state =  (
                        (self.type == NodeType.VISITA && listNodeModifica.type == NodeType.ZONA)
                        || (self.type == NodeType.ZONA && listNodeModifica.type == NodeType.AREA
                            && self.data.getInt(DbContract.ZonaEntry.COLUMN_ID) == listNodeModifica.data.getInt(DbContract.AreaEntry.COLUMN_ZONA))
                        || (self.type == NodeType.AREA && listNodeModifica.type == NodeType.OPERA
                            && self.data.getInt(DbContract.AreaEntry.COLUMN_ID) == listNodeModifica.data.getInt(DbContract.OperaEntry.COLUMN_AREA))
                );

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return state;
        }

        private boolean checkIfPresent(ListNodeModifica listNodeModifica){
            boolean state = true;

            for(GraphNodeModifica child : graphParent.graph.successors(self)){
                try {
                    if(child.data.getInt("id") == listNodeModifica.data.getInt("id")){
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
            ListNodeModifica listNodeModifica = ((ListNodeModifica)event.getLocalState());
            switch (action) {
                case DragEvent.ACTION_DROP:
                    if(checkRelation(listNodeModifica) && checkIfPresent(listNodeModifica)){
                        GraphNodeModifica graphNodeModifica = new GraphNodeModifica(graphParent.getContext(), graphParent, listNodeModifica.type, listNodeModifica.data);
                        self.hide();
                        self.hideAllNodeAtSameLevel();
                        self.hideAllChild();

                        self.draw();

                        self.addSuccessor(graphNodeModifica);
                        self.drawAllChild();
                        listNodeModifica.setVisibility(GONE);

                        self.setCircle(true);
                        self.clicked = true;
                        self.pick();

                        //AGGIORNAMENTO DEL DB

                        try{
                            if(listNodeModifica.type == NodeType.ZONA && self.type == NodeType.VISITA){
                                Visita.addZona(self.getContext(), self.data.getInt("id"), listNodeModifica.data.getInt("id"),
                                        response -> {
                                            try {
                                                if(response.getBoolean("status")){
                                                    Toast.makeText(self.getContext(), "Zona aggiunta con successo alla visita #"+self.data.getInt("id"), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        },
                                        error -> {

                                        });
                            }else if(listNodeModifica.type == NodeType.AREA && self.type == NodeType.ZONA){

                            }else if(listNodeModifica.type == NodeType.OPERA && self.type == NodeType.AREA){

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }


                    }else{
                        listNodeModifica.reset();
                        listNodeModifica.setVisibility(VISIBLE);
                    }
                    break;
                case DragEvent.ACTION_DRAG_STARTED:
                    if(checkRelation(listNodeModifica) && checkIfPresent(listNodeModifica)){
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
                GraphNodeModifica node = ((GraphNodeModifica)view);
                System.out.println(node.data.toString());
                AlertDialog dialog1 = NodeDialog.NodeDialog(context, node);
                dialog1.show();
                return true;
            }
        });
    }

    private void setOnClickListener(GrafoModifica graphParent, NodeType type) {
        if(type != NodeType.VISITA && type != NodeType.OPERA){
            setOnClickListener(view -> {//THIS (View)
                setNodeSize(graphParent, type, (GraphNodeModifica) view);
            });
        }

        if(type != NodeType.OPERA){
            setOnClickListener(view -> {
                setNodeSize(graphParent, type, (GraphNodeModifica) view);

                initListNode();
            });
        }
    }

    public void initListNode() {
        if(self.type == NodeType.VISITA){
            initListNodeVisita();
        }else if(type == NodeType.ZONA){
            initListNodeZona();
        }else if(type == NodeType.AREA){
            initListNodeArea();
        }
    }

    private void initListNodeArea() {
        Area tmpArea = new Area();

        try{
            tmpArea.setId(data.getInt(DbContract.AreaEntry.COLUMN_ID));

            Area.getAllPossibleChild(getContext(), tmpArea,
                    response -> {
                        try {
                            if(response.getBoolean("status")){
                                ListaNodi listaNodi = new ListaNodi(GrafoModificaFragment.listaNodiLinearLayout.getContext(), NodeType.AREA, self);

                                JSONArray arrayData = response.getJSONArray("data");

                                resetListaNodi(GrafoModificaFragment.listaNodiLinearLayout);

                                GrafoModificaFragment.listaNodiLinearLayout.addView(listaNodi);

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject child = arrayData.getJSONObject(i);
                                    listaNodi.addNode(new ListNodeModifica(GrafoModificaFragment.listaNodiLinearLayout.getContext(), listaNodi, child, NodeType.OPERA));
                                }
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void initListNodeZona() {
        Zona tmpZona = new Zona();

        try{
            tmpZona.setId(data.getInt(DbContract.ZonaEntry.COLUMN_ID));

            Zona.getAllPossibleChild(getContext(), tmpZona,
                    response -> {
                        try {
                            if(response.getBoolean("status")){
                                ListaNodi listaNodi = new ListaNodi( GrafoModificaFragment.listaNodiLinearLayout.getContext(), NodeType.ZONA, self);

                                JSONArray arrayData = response.getJSONArray("data");

                                resetListaNodi(GrafoModificaFragment.listaNodiLinearLayout);

                                GrafoModificaFragment.listaNodiLinearLayout.addView(listaNodi);

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject child = arrayData.getJSONObject(i);
                                    listaNodi.addNode(new ListNodeModifica(GrafoModificaFragment.listaNodiLinearLayout.getContext(), listaNodi, child, NodeType.AREA));
                                }
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void initListNodeVisita() {
        Visita visita = new Visita();
        visita.setId(3);
        visita.setLuogo("Roma");

        Visita.getAllPossibleChild(getContext(), visita,
                response -> {
                    try {
                        if(response.getBoolean("status")){
                            ListaNodi listaNodi = new ListaNodi( GrafoModificaFragment.listaNodiLinearLayout.getContext(), NodeType.ZONA, self);

                            JSONArray arrayData = response.getJSONArray("data");

                            resetListaNodi(GrafoModificaFragment.listaNodiLinearLayout);

                            GrafoModificaFragment.listaNodiLinearLayout.addView(listaNodi);

                            for(int i = 0; i < arrayData.length(); ++i){
                                JSONObject child = arrayData.getJSONObject(i);
                                listaNodi.addNode(new ListNodeModifica(GrafoModificaFragment.listaNodiLinearLayout.getContext(),
                                        listaNodi, child, NodeType.ZONA));
                            }
                        }else{

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                });
    }

    private void setNodeSize(GrafoModifica graphParent, NodeType type, GraphNodeModifica view) {
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
            ((GraphNodeModifica) graphParent.graph.predecessors(self).toArray()[0]).setColor(Color.YELLOW);
        }
    }

    private void resetListaNodi(LinearLayout listaNodiLinearLayout) {
        for (int i = 0; i < listaNodiLinearLayout.getChildCount(); ++i) {
            GrafoModificaFragment.listaNodiLinearLayout.removeView(GrafoModificaFragment.listaNodiLinearLayout.getChildAt(i));
        }
    }


    public GraphNodeModifica(@NonNull Context context, GrafoModifica graphParent, NodeType type, JSONObject data) {
        super(context);
        setFields(graphParent, type, data);
        init(context);

        setOnClickListener(graphParent, type);
        setOnLongClickListener(context);
    }

    private void hideAllNodeAtSameLevel() {
        if(type != NodeType.VISITA){
            for(GraphNodeModifica node : getSuccessors(graphParent, self)){
                if(node != self){
                    node.clicked = false;
                    node.setCircle(false);
                    node.hideAllChild();
                }
            }
        }
    }

    @NonNull
    private Set<GraphNodeModifica> getSuccessors(GrafoModifica graphParent, GraphNodeModifica view) {
        return graphParent.graph.successors((GraphNodeModifica) getPredecessors(graphParent, view).toArray()[0]);
    }

    public void drawAllChild(){
        int numSuccessors = graphParent.graph.successors(this).size();

        draw();
        AtomicInteger count = new AtomicInteger(1);

        for(GraphNodeModifica nodeChild : graphParent.graph.successors(this)){
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

    private void setNodeSize(int numSuccessors, GraphNodeModifica nodeChild) {

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

    private float getTmpX(int numSuccessors, AtomicInteger count, GraphNodeModifica nodeChild) {
        //FORMULA DI CENTRATURA DEL NODO IN BASE AL NUMERO DI NODI DI QUEL LIVELLO
        return (count.getAndIncrement() * (((float)graphParent.size.x / (numSuccessors + 1)))) - (float)nodeChild.size/2;
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
        for(GraphNodeModifica nodeChild : graphParent.graph.successors(this)){
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
        }else if(type == NodeType.ZONA){
            graphParent.drawView.resetDrawView(graphParent, 1);
        }else if(type == NodeType.AREA){
            graphParent.drawView.resetDrawView(graphParent, 2);
        }else if(type == NodeType.OPERA){
            graphParent.drawView.resetDrawView(graphParent, 3);
        }
    }
}

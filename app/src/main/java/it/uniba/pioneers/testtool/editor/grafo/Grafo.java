package it.uniba.pioneers.testtool.editor.grafo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import it.uniba.pioneers.data.Visita;
import it.uniba.pioneers.data.Zona;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.node.GraphNode;
import it.uniba.pioneers.testtool.editor.grafo.node.Node;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;
import it.uniba.pioneers.testtool.editor.grafo.draw.DrawView;
import it.uniba.pioneers.testtool.editor.grafo.draw.Line;
import it.uniba.pioneers.testtool.editor.grafo.node.NodeType;

public class Grafo extends ConstraintLayout {
    public MutableGraph<GraphNode> graph = GraphBuilder.directed().build();

    WindowManager wm = null;
    Display display = null;
    Point size = new Point();

    public DrawView drawView = null;

    GraphNode actualVisita = null;
    GraphNode actualZona = null;
    GraphNode actualArea = null;
    GraphNode actualOpera = null;

    public GraphNode visita = null;

    ListaNodi listaNodi = null;

    Context context = null;

    private float fromDpToPx(int dip){
        Resources r = getResources();
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
    }

    private float powOfTwo(int n){
        return 2 << (n - 1);
    }

    public void initDrawAttribute(Context context){
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        display.getSize(size);
        drawView = new DrawView(context);
    }

    public void init(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.widget_grafo, this);

        this.context = context;
        initDrawAttribute(context);

        Visita tmpVisita = new Visita();
        tmpVisita.setId(3);

        Grafo self = this;
        Visita.getGraphData(context, tmpVisita, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("status") == true){
                                Log.v("VISITE", response.getJSONObject("data").toString(4));
                                JSONObject data = response.getJSONObject("data");

                                visita = new GraphNode(context, self,NodeType.VISITA, data.getJSONObject("visita"));
                                JSONArray arrZona = data.getJSONArray("arrZona");
                                JSONArray arrArea = data.getJSONArray("arrArea");
                                JSONArray arrOpera = data.getJSONArray("arrOpera");

                                for(int i = 0; i < arrZona.length(); ++i){
                                    JSONObject zonaJSON = arrZona.getJSONObject(i);
                                    GraphNode zona = new GraphNode(context, self, NodeType.ZONA, zonaJSON);
                                    visita.addSuccessor(zona);
                                    for(int j = 0; j < arrArea.length(); ++j){
                                        JSONObject areaJSON = arrArea.getJSONObject(j);
                                        if(areaJSON.getInt("zona") == zonaJSON.getInt("id")){
                                            GraphNode area = new GraphNode(context, self, NodeType.AREA, areaJSON);
                                            zona.addSuccessor(area);

                                            for(int k = 0; k < arrOpera.length(); ++k){
                                                JSONObject operaJSON = arrOpera.getJSONObject(k);
                                                if(operaJSON.getInt("area") == areaJSON.getInt("id")){
                                                    GraphNode opera = new GraphNode(context, self, NodeType.OPERA, operaJSON);
                                                    area.addSuccessor(opera);
                                                }
                                            }
                                        }
                                    }
                                }
                                addStartNode(visita);
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

/*        visita = new GraphNode(context, this, NodeType.VISITA); //CREAZIONE VISITA
        //AGGIUNTA DI TUTTE LE ZONE
        GraphNode zona = new GraphNode(context, this, NodeType.ZONA);
        visita.addSuccessor(zona);

        GraphNode zona1 = new GraphNode(context, this, NodeType.ZONA);
        visita.addSuccessor(zona1);

        GraphNode zona2 = new GraphNode(context, this, NodeType.ZONA);
        visita.addSuccessor(zona2);

        //AGGIUNTA DI DELLE AREE
        GraphNode area = new GraphNode(context, this, NodeType.AREA);
        zona.addSuccessor(area);

        GraphNode area2 = new GraphNode(context, this, NodeType.AREA);
        zona1.addSuccessor(area2);

        GraphNode area3 = new GraphNode(context, this, NodeType.AREA);
        zona1.addSuccessor(area3);

        //AGIUNTA DELLE OPERE
        GraphNode opera = new GraphNode(context, this, NodeType.OPERA);
        area.addSuccessor(opera);

        GraphNode opera2 = new GraphNode(context, this, NodeType.OPERA);
        area.addSuccessor(opera2);

        GraphNode opera3 = new GraphNode(context, this, NodeType.OPERA);
        area2.addSuccessor(opera3);

        addStartNode(visita);
  */
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public void addStartNode(GraphNode dataNode){
        graph.addNode(dataNode);
        this.post(new GraphViewer(this, context, visita));
    }

    public void addSuccessorToNode(GraphNode parentDataNode, GraphNode childDataNode){
        //parentDataNode.addSuccessor(childDataNode);
        //postInvalidate();
        //this.post(new MyRunnableRefresh2(this, context, visitaIniziale));
    }

    public Grafo(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Grafo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Grafo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public Grafo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public Line buildLineGraph(GraphNode start, GraphNode stop){
        return new Line(
                (start.getX() + fromDpToPx(24)),
                (start.getY() + fromDpToPx(47)),
                (stop.getX() + fromDpToPx(24)),
                stop.getY()
        );
    }

    private class GraphViewer implements Runnable{
        protected final Grafo self;
        protected final Context context;
        float r1, r2, r3, r4;
        int height, weight;

        GraphNode visita = null;

        public GraphViewer(Grafo self, Context context, GraphNode startDataNode) {
            this.visita = startDataNode;
            this.self = self;
            this.context = context;
        }

        protected void initRow(){
            height = self.getHeight();
            weight = self.getWidth();

            r1 = 0;
            r2 = (float) (height)/4;
            r3 = r2*2;
            r4 = r2*3;
        }

        protected float calcX(int n){
            Log.v("size", String.valueOf(size.x));
            Log.v("sizen", String.valueOf(size.x/n));
            float x = ((float) size.x/n) - fromDpToPx(48);
            Log.v("X-VALUE", String.valueOf(x));
            return n > 1 ?  x : ((float) size.x/powOfTwo(1)) - fromDpToPx(48);
        }


        @Override
        public void run(){
            initRow();
            visita.setY(r1);
            visita.setX(calcX(1));
            visita.draw();
            //visita.removeAllChild();

            loadNode();


            //POSIZIONAMENTO DI TUTTI I NODI
            setNodePositions();
            actualVisita = visita;
        }

        private void loadNode() {
            for(GraphNode nodeZona : self.graph.successors(visita)){
                visita.addSuccessor(nodeZona);
                for(GraphNode nodeArea : self.graph.successors(nodeZona)){
                    nodeZona.addSuccessor(nodeArea);
                    for(GraphNode nodeOpera : self.graph.successors(nodeArea)){
                        nodeArea.addSuccessor(nodeOpera);
                    }
                }
            }
        }

        private void setNodePositions() {
            AtomicInteger cntZona = new AtomicInteger(1);
            int numZona = graph.successors(visita).size();

            for(GraphNode zonaNode : graph.successors(visita)){
                zonaNode.setX(cntZona.getAndIncrement() * calcX(numZona));
                zonaNode.setY(r2);

                AtomicInteger cntArea = new AtomicInteger(1);
                int numArea = graph.successors(zonaNode).size();

                for(GraphNode areaNode : graph.successors(zonaNode)){
                    areaNode.setX(cntArea.getAndIncrement() * calcX(numArea));
                    areaNode.setY(r3);

                    AtomicInteger cntOpera = new AtomicInteger(1);
                    int numOpera = graph.successors(areaNode).size();

                    for(GraphNode operaNode : graph.successors(areaNode)){
                        operaNode.setX(cntOpera.getAndIncrement() * calcX(numOpera));
                        operaNode.setY(r4);
                    }
                }
            }
            visita.drawAllChild();
        }
    }

    /*
    private class MyRunnable implements Runnable {
        protected final Grafo self;
        protected final Context context;
        float r1, r2, r3, r4;
        int height, weight;

        public MyRunnable(Grafo self, Context context) {
            this.self = self;
            this.context = context;
        }

        protected void initRow(){
            height = self.getHeight();
            weight = self.getWidth();

            r1 = 0;
            r2 = (float) (height)/4;
            r3 = r2*2;
            r4 = r2*3;
        }

        protected float calcX(int n){
            Log.v("size", String.valueOf(size.x));
            Log.v("sizen", String.valueOf(size.x/n));
            float x = ((float) size.x/n) - fromDpToPx(48);
            Log.v("X-VALUE", String.valueOf(x));
            return n > 1 ?  x : ((float) size.x/powOfTwo(1)) - fromDpToPx(48);
        }

        protected void refreshDrawView(){
            if(drawView != null)
                drawView.invalidate();
        }

        protected Line buildLine(GraphNode start, GraphNode stop){
            return new Line(
                    (start.getX() + fromDpToPx(24)),
                    (start.getY() + fromDpToPx(47)),
                    (stop.getX() + fromDpToPx(24)),
                    stop.getY()
            );
        }

        //NODE VISITA
        protected void nodeVisitaInitListeners(GraphNode dataNodeVisita){
            AtomicInteger contatoreZone = new AtomicInteger(1);
            drawView.resetDrawView(self, 0);

            for(GraphNode dataNodeZonaReal : graph.successors(dataNodeVisita)){

                int size = graph.successors(dataNodeZonaReal).size();

                dataNodeZonaReal.setX(contatoreZone.get() * calcX(graph.successors(dataNodeVisita).size()));
                dataNodeZonaReal.setY(r2);

                drawView.linesZona.add(buildLine(dataNodeVisita, dataNodeZonaReal));

                nodeZonaSetOnClickListener(dataNodeZonaReal, size);

                contatoreZone.incrementAndGet();

                if(!dataNodeZonaReal.inizializated){
                    removeView(dataNodeZonaReal);
                    addView(dataNodeZonaReal);
                }
            }
        }

        //NODE AREA
        protected void nodeAreaSetOnClickListener(GraphNode dataNodeArea) {
            int size = graph.successors(dataNodeArea).size();

            dataNodeArea.setOnClickListener(view1 -> {

                if (size > 0) {
                    graph.predecessors(dataNodeArea).forEach(zona -> {
                        graph.successors(zona).forEach(area -> {
                            if (area.inizializated && !area.equals(dataNodeArea)) {
                                area.setClicked(false);
                                area.setCircle(false);

                                graph.successors(area).forEach(opera -> {
                                    if(opera.inizializated)
                                        opera.setVisibility(INVISIBLE);
                                });
                            }
                        });
                    });

                    if (dataNodeArea.clicked) {
                        self.drawView.resetDrawView(self, 1);
                        graph.predecessors(dataNodeArea).forEach(zona -> {
                            graph.successors(zona).forEach(area ->{
                                drawView.linesOpera.add(buildLine(zona, area));
                            });
                        });

                        graph.successors(dataNodeArea).forEach(opera -> {
                            opera.setVisibility(INVISIBLE);
                            opera.setCircle(true);
                        });
                        dataNodeArea.setClicked(false);
                        dataNodeArea.setCircle(false);
                    } else {
                        self.drawView.resetDrawView(self, 1);

                        graph.predecessors(dataNodeArea).forEach(zona -> {
                            graph.successors(zona).forEach(area ->{
                                drawView.linesOpera.add(buildLine(zona, area));
                            });
                        });

                        if(dataNodeArea.inizializated){
                            for(GraphNode Opera : graph.successors(dataNodeArea)){
                                Opera.setVisibility(VISIBLE);
                                Opera.setCircle(true);

                                drawView.linesOpera.add(buildLine(dataNodeArea, Opera));
                            }
                        }else{
                            AtomicInteger contatoreOpere = new AtomicInteger(1);
                            dataNodeArea.setCircle(true);
                            graph.successors(dataNodeArea).forEach(nodeOpera -> {
                                nodeOpera.setX(contatoreOpere.get() * calcX(graph.successors(dataNodeArea).size()));
                                nodeOpera.setY(r4);

                                drawView.linesOpera.add(buildLine(dataNodeArea, nodeOpera));

                                nodeOpera.setOnLongClickListener(view2 -> {
                                    EditorActivity ea = ((EditorActivity)getActivity());

                                    ea.d.show(ea.getSupportFragmentManager(), "MyDialogFragment");

                                    return true;

                                });

                                addView(nodeOpera);
                                nodeOpera.setInizializated(true);
                                nodeOpera.setCircle(true);

                                contatoreOpere.incrementAndGet();
                            });

                            dataNodeArea.setInizializated(true);

                        }
                        refreshDrawView();
                        dataNodeArea.setCircle(true);
                        dataNodeArea.setClicked(true);
                    }
                } else {
                    Snackbar.make(self, "Non esistono Opere associate", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            });
        }

        //NODE ZONA
        protected void nodeZonaSetOnClickListener(GraphNode dataNodeZonaReal, int size) {
            dataNodeZonaReal.setOnClickListener(view -> {

                if(size > 0){

                    graph.predecessors(dataNodeZonaReal).forEach(nodeVisita -> {
                        graph.successors(nodeVisita).forEach(zona -> {
                            if(zona.inizializated && !zona.equals(dataNodeZonaReal)){   // il problema potrebbe essere qui, eh infatti
                                zona.setClicked(false);
                                zona.setCircle(false);

                                graph.successors(zona).forEach(area -> {
                                    area.setVisibility(INVISIBLE);
                                    graph.successors(area).forEach(opera -> {
                                        if(opera.inizializated)
                                            opera.setVisibility(INVISIBLE);
                                    });
                                });
                            }
                        });
                    });

                    if(dataNodeZonaReal.clicked){
                        nodeZonaOnClickIfNotInizialized(dataNodeZonaReal);
                    }else{
                        nodeZonaOnClickIfNotClicked(dataNodeZonaReal);
                    }

                }else{
                    Snackbar.make(self, "Non esistono Aree associate", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            });
        }

        protected void nodeZonaOnClickIfNotClicked(GraphNode dataNodeZonaReal) {
            self.drawView.resetDrawView(self, 1);
            dataNodeZonaReal.setCircle(true);

            if(dataNodeZonaReal.inizializated){
                nodeZonaOnClickIfInizialized(dataNodeZonaReal);
            }else{
                AtomicInteger contatoreAree = new AtomicInteger(1);
                dataNodeZonaReal.setCircle(true);

                graph.successors(dataNodeZonaReal).forEach(nodeArea -> {
                    nodeArea.setX(contatoreAree.get() *calcX(graph.successors(dataNodeZonaReal).size()));
                    nodeArea.setY(r3);

                    drawView.linesArea.add(buildLine(dataNodeZonaReal, nodeArea));
                    refreshDrawView();

                    nodeAreaSetOnClickListener(nodeArea);

                    addView(nodeArea);
                    contatoreAree.incrementAndGet();
                });

                dataNodeZonaReal.setInizializated(true);

            }
            dataNodeZonaReal.setClicked(true);
        }

        protected void nodeZonaOnClickIfNotInizialized(GraphNode dataNodeZonaReal) {
            self.drawView.resetDrawView(self, 1); //RESET DELLE LINEE

            graph.successors(dataNodeZonaReal).forEach(area -> {
                graph.successors(area).forEach(opere ->{
                    if(!area.clicked) {
                        opere.setClicked(false);
                        opere.setCircle(false);
                    }
                    opere.setVisibility(INVISIBLE);
                });
                if(!area.clicked) {
                    area.setClicked(false);
                    area.setCircle(false);
                }
                area.setVisibility(INVISIBLE);

            });
            dataNodeZonaReal.setClicked(false);
            dataNodeZonaReal.setCircle(false);
        }

        protected void nodeZonaOnClickIfInizialized(GraphNode dataNodeZonaReal) {
            graph.successors(dataNodeZonaReal).forEach(area -> {
                area.setVisibility(VISIBLE);

                drawView.linesArea.add(buildLine(dataNodeZonaReal, area));
                if(area.clicked){
                    graph.successors(area).forEach(opera ->{
                        opera.setVisibility(VISIBLE);
                        drawView.linesArea.add(buildLine(area, opera));
                    });
                }
            });

            refreshDrawView();
        }

        public void run(){
            initRow();

            GraphNode visita = new GraphNode(context, self, NodeType.VISITA);
            addView(visita);
            visita.setY(r1);
            visita.setX(calcX(1));

            ///////////////////////////
            GraphNode museo = new GraphNode(context, self, NodeType.ZONA);
            GraphNode museo2 = new GraphNode(context, self, NodeType.ZONA);
            GraphNode museo3 = new GraphNode(context, self, NodeType.ZONA);
            GraphNode museo4 = new GraphNode(context, self, NodeType.ZONA);
            GraphNode museo5 = new GraphNode(context, self, NodeType.ZONA);

            visita.addSuccessor(museo);
            visita.addSuccessor(museo2);
            visita.addSuccessor(museo3);
            visita.addSuccessor(museo4);
            visita.addSuccessor(museo5);


            ///////////////////////////
            GraphNode stanza1 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza2 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza3 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza4 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza5 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza6 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza7 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza8 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza9 = new GraphNode(context, self, NodeType.AREA);
            GraphNode stanza10 = new GraphNode(context, self, NodeType.AREA);
            //Node stanza11 = new Node(context, self, NodeType.AREA);
            //Node stanza12 = new Node(context, self, NodeType.AREA);
            //Node stanza13 = new Node(context, self, NodeType.AREA);
            //Node stanza14 = new Node(context, self, NodeType.AREA);



            museo2.addSuccessor(stanza1);
            museo2.addSuccessor(stanza2);
            museo2.addSuccessor(stanza3);
            museo.addSuccessor(stanza4);
            museo.addSuccessor(stanza5);
            museo.addSuccessor(stanza6);
            museo.addSuccessor(stanza7);
            museo.addSuccessor(stanza8);
            museo.addSuccessor(stanza9);
            museo.addSuccessor(stanza10);
            //museo.addSuccessor(stanza11);
            //museo.addSuccessor(stanza12);
            //museo.addSuccessor(stanza13);
            //museo.addSuccessor(stanza14);
            ///////////////////////////
            GraphNode opera1 = new GraphNode(context, self, NodeType.OPERA);
            GraphNode opera2 = new GraphNode(context, self, NodeType.OPERA);
            GraphNode opera3 = new GraphNode(context, self, NodeType.OPERA);
            GraphNode opera4 = new GraphNode(context, self, NodeType.OPERA);
            GraphNode opera5 = new GraphNode(context, self, NodeType.OPERA);
            GraphNode opera6 = new GraphNode(context, self, NodeType.OPERA);


            stanza2.addSuccessor(opera1);
            stanza2.addSuccessor(opera2);
            stanza1.addSuccessor(opera3);
            stanza1.addSuccessor(opera4);
            stanza1.addSuccessor(opera5);
            stanza1.addSuccessor(opera6);


            ///////////////////////////
            nodeVisitaInitListeners(visita);
            visita.setCircle(true);
            actualVisita = visita;
            actualOpera = (GraphNode) graph.successors(stanza1).toArray()[0];

        }
    }

     */

}

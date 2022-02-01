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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;

import java.util.concurrent.atomic.AtomicInteger;

import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.EditorActivity;
import it.uniba.pioneers.testtool.editor.grafo.node.GraphNode;
import it.uniba.pioneers.testtool.editor.listaNodi.ListaNodi;
import it.uniba.pioneers.testtool.editor.grafo.draw.DrawView;
import it.uniba.pioneers.testtool.editor.grafo.draw.Line;
import it.uniba.pioneers.widget.NodeType;

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

    public GraphNode visitaIniziale = null;

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

        visitaIniziale = new GraphNode(context, this, NodeType.VISITA);
        addStartNode(visitaIniziale);
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

    MyRunnableRefresh rr = null;
    public void addStartNode(GraphNode dataNode){
        graph.addNode(dataNode);

        rr = new MyRunnableRefresh(this, context, visitaIniziale);
        this.post(rr);
    }

    public void addSuccessorToNode(GraphNode parentDataNode, GraphNode childDataNode){
        parentDataNode.addSuccessor(childDataNode);
        this.post(new MyRunnableRefresh2(this, context, visitaIniziale));
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

    private class GraphViewer implements Runnable{
        protected final Grafo graphObject;
        protected final Context context;
        float r1, r2, r3, r4;
        int height, weight;


        private void initRow(){
            height = graphObject.getHeight();
            weight = graphObject.getWidth();

            r1 = 0;
            r2 = (float) (height)/4;
            r3 = r2*2;
            r4 = r2*3;
        }

        private GraphViewer(Grafo graphObject) {
            context = graphObject.context;
            this.graphObject = graphObject;
            initRow();
        }

        @Override
        public void run() {

        }
    }

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
            float x = ((float) size.x/n);
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

    private class MyRunnableRefresh extends MyRunnable{
        GraphNode visita = null;

        public MyRunnableRefresh(Grafo self, Context context, GraphNode startDataNode) {
            super(self, context);
            this.visita = startDataNode;
        }

        @Override
        public void run(){
            initRow();

            addView(visita);
            visita.setY(r1);
            visita.setX(calcX(1));
            visita.setCircle(true);

            ///////////////////////////
            nodeVisitaInitListeners(visita);

            actualVisita = visita;
        }

    }

    private class MyRunnableRefresh2 extends MyRunnable{
        GraphNode visita = null;

        public MyRunnableRefresh2(Grafo self, Context context, GraphNode startDataNode) {
            super(self, context);
            this.visita = startDataNode;
        }

        @Override
        public void run(){
            initRow();
            visita.setY(r1);
            visita.setX(calcX(1));
            visita.setCircle(true);

            ///////////////////////////
            nodeVisitaInitListeners(visita);

            actualVisita = visita;
        }

    }
}

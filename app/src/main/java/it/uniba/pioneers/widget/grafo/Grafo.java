package it.uniba.pioneers.widget.grafo;

import android.content.Context;
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
import it.uniba.pioneers.widget.Node;
import it.uniba.pioneers.widget.NodeType;

public class Grafo extends ConstraintLayout {
    MutableGraph<Node> graph = GraphBuilder.directed().build();

    WindowManager wm = null;
    Display display = null;
    Point size = new Point();

    public DrawView drawView = null;

    Node actualVisita = null;
    Node actualZona = null;
    Node actualArea = null;
    Node actualOpera = null;

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

        initDrawAttribute(context);
        initGrafo(context);

    }

    public void initGrafo(Context context){
        Grafo self = this;
        this.post(new Runnable(){
            float r1, r2, r3, r4;
            int height, weight;

            private void initRow(){
                height = self.getHeight();
                weight = self.getWidth();

                r1 = 0;
                r2 = (float) (height)/4;
                r3 = r2*2;
                r4 = r2*3;
            }

            private float calcX(int n){
                return n > 1 ? ((float) size.x/(n)) - fromDpToPx(24) : ((float) size.x/powOfTwo(1)) - fromDpToPx(24);
            }

            private void refreshDrawView(){
                if(drawView != null)
                    drawView.invalidate();
            }

            private Line buildLine(Node start, Node stop){
                return new Line(
                        (start.getX() + fromDpToPx(24)),
                        (start.getY() + fromDpToPx(47)),
                        (stop.getX() + fromDpToPx(24)),
                        stop.getY()
                );
            }



            //NODE VISITA
            private void nodeVisitaInitListeners(Node nodeVisita){
                AtomicInteger contatoreZone = new AtomicInteger(1);
                drawView.resetDrawView(self, 0);

                for(Node nodeZonaReal : graph.successors(nodeVisita)){

                    int size = graph.successors(nodeZonaReal).size();

                    nodeZonaReal.setX(contatoreZone.get() * calcX(graph.successors(nodeVisita).size()));
                    nodeZonaReal.setY(r2);

                    drawView.linesZona.add(buildLine(nodeVisita, nodeZonaReal));

                    nodeZonaSetOnClickListener(nodeZonaReal, size);

                    contatoreZone.incrementAndGet();

                    if(!nodeZonaReal.inizializated){
                        addView(nodeZonaReal);
                    }
                }
            }



            //NODE AREA
            private void nodeAreaSetOnClickListener(Node nodeArea) {
                int size = graph.successors(nodeArea).size();

                nodeArea.setOnClickListener(view1 -> {

                    if (size > 0) {
                        self.drawView.resetDrawView(self, 2);

                        graph.predecessors(nodeArea).forEach(zona -> {
                            graph.successors(zona).forEach(area -> {
                                if (area.clicked && !area.equals(nodeArea)) {
                                    area.setClicked(false);
                                    area.setCircle(false);
                                    graph.successors(area).forEach(opera -> {
                                        opera.setClicked(false);
                                        opera.setVisibility(INVISIBLE);
                                    });
                                }
                            });
                        });

                        if (nodeArea.clicked) {
                            self.drawView.resetDrawView(self, 2);
                            graph.predecessors(nodeArea).forEach(zona -> {
                                graph.successors(zona).forEach(area ->{
                                    drawView.linesOpera.add(buildLine(zona, area));
                                });
                            });

                            graph.successors(nodeArea).forEach(figloNodoArea -> {
                                figloNodoArea.setVisibility(INVISIBLE);
                            });
                            nodeArea.setClicked(false);
                            nodeArea.setCircle(false);
                        } else {
                            self.drawView.resetDrawView(self, 1);

                            graph.predecessors(nodeArea).forEach(zona -> {
                                graph.successors(zona).forEach(area ->{
                                    drawView.linesOpera.add(buildLine(zona, area));
                                });
                            });

                            if(nodeArea.inizializated){
                                for(Node figloNodoArea : graph.successors(nodeArea)){
                                    figloNodoArea.setVisibility(VISIBLE);

                                    drawView.linesOpera.add(buildLine(nodeArea, figloNodoArea));
                                }
                            }else{
                                AtomicInteger contatoreOpere = new AtomicInteger(1);
                                nodeArea.setCircle(true);
                                graph.successors(nodeArea).forEach(nodeOpera -> {
                                    nodeOpera.setX(contatoreOpere.get() * calcX(graph.successors(nodeArea).size()));
                                    nodeOpera.setY(r4);

                                    drawView.linesOpera.add(buildLine(nodeArea, nodeOpera));

                                    nodeOpera.setOnClickListener(view2 -> {
                                        nodeOpera.setCircle(true);
                                    });

                                    addView(nodeOpera);
                                    nodeOpera.setInizializated(true);
                                    contatoreOpere.incrementAndGet();
                                });

                                nodeArea.setInizializated(true);

                            }
                            refreshDrawView();
                            nodeArea.setCircle(true);
                            nodeArea.setClicked(true);
                        }
                    } else {
                        Snackbar.make(self, "Non esistono Opere associate", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });
            }


            //NODE ZONA
            private void nodeZonaSetOnClickListener(Node nodeZonaReal, int size) {
                nodeZonaReal.setOnClickListener(view -> {

                    if(size > 0){
                        graph.predecessors(nodeZonaReal).forEach(nodeVisita -> {
                            graph.successors(nodeVisita).forEach(zona -> {
                                if(zona.inizializated){
                                    zona.setClicked(false);
                                    zona.setCircle(false);

                                    graph.successors(zona).forEach(area -> {
                                        if(!area.clicked){
                                            area.setClicked(false);
                                        }
                                        area.setVisibility(INVISIBLE);

                                        graph.successors(area).forEach(opera -> {
                                            if(!area.clicked){
                                                opera.setClicked(false);
                                            }
                                            opera.setVisibility(INVISIBLE);
                                        });
                                    });
                                }

                            });
                        });

                        if(nodeZonaReal.clicked){
                            nodeZonaOnClickIfNotInizialized(nodeZonaReal);
                        }else{
                            nodeZonaOnClickIfNotClicked(nodeZonaReal);
                        }

                    }else{
                        Snackbar.make(self, "Non esistono Aree associate", BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                });
            }

            private void nodeZonaOnClickIfNotClicked(Node nodeZonaReal) {
                self.drawView.resetDrawView(self, 1);
                nodeZonaReal.setCircle(true);

                if(nodeZonaReal.inizializated){
                    nodeZonaOnClickIfInizialized(nodeZonaReal);
                }else{
                    AtomicInteger contatoreAree = new AtomicInteger(1);
                    nodeZonaReal.setCircle(true);

                    graph.successors(nodeZonaReal).forEach(nodeArea -> {
                        nodeArea.setX(contatoreAree.get() *calcX(graph.successors(nodeZonaReal).size()));
                        nodeArea.setY(r3);

                        drawView.linesArea.add(buildLine(nodeZonaReal, nodeArea));
                        refreshDrawView();

                        nodeAreaSetOnClickListener(nodeArea);

                        addView(nodeArea);
                        contatoreAree.incrementAndGet();
                    });

                    nodeZonaReal.setInizializated(true);

                }
                nodeZonaReal.setClicked(true);
            }

            private void nodeZonaOnClickIfNotInizialized(Node nodeZonaReal) {
                self.drawView.resetDrawView(self, 1); //RESET DELLE LINEE

                graph.successors(nodeZonaReal).forEach(area -> {
                    area.setVisibility(INVISIBLE);

                    graph.successors(area).forEach(figlioNodoArea ->{
                        figlioNodoArea.setVisibility(INVISIBLE);
                        figlioNodoArea.setClicked(false);
                        figlioNodoArea.setCircle(false);
                    });
                });
                nodeZonaReal.setClicked(false);
                nodeZonaReal.setCircle(false);
            }

            private void nodeZonaOnClickIfInizialized(Node nodeZonaReal) {
                graph.successors(nodeZonaReal).forEach(area -> {
                    area.setVisibility(VISIBLE);

                    drawView.linesArea.add(buildLine(nodeZonaReal, area));
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

                Node visita = new Node(context, graph, NodeType.VISITA);
                addView(visita);
                visita.setY(r1);
                visita.setX(calcX(1));

                ///////////////////////////
                Node museo = new Node(context, graph, NodeType.ZONA);
                Node museo2 = new Node(context, graph, NodeType.ZONA);
                Node museo3 = new Node(context, graph, NodeType.ZONA);
                Node museo4 = new Node(context, graph, NodeType.ZONA);
                Node museo5 = new Node(context, graph, NodeType.ZONA);

                visita.addSuccessor(museo);
                visita.addSuccessor(museo2);
                visita.addSuccessor(museo3);
                visita.addSuccessor(museo4);
                visita.addSuccessor(museo5);


                ///////////////////////////
                Node stanza1 = new Node(context, graph, NodeType.AREA);
                Node stanza2 = new Node(context, graph, NodeType.AREA);
                Node stanza3 = new Node(context, graph, NodeType.AREA);
                Node stanza4 = new Node(context, graph, NodeType.AREA);
                Node stanza5 = new Node(context, graph, NodeType.AREA);
                Node stanza6 = new Node(context, graph, NodeType.AREA);
                Node stanza7 = new Node(context, graph, NodeType.AREA);
                Node stanza8 = new Node(context, graph, NodeType.AREA);


                museo2.addSuccessor(stanza1);
                museo2.addSuccessor(stanza2);
                museo2.addSuccessor(stanza3);
                museo.addSuccessor(stanza4);
                museo.addSuccessor(stanza5);
                museo.addSuccessor(stanza6);
                museo.addSuccessor(stanza7);
                museo.addSuccessor(stanza8);

                ///////////////////////////
                Node opera1 = new Node(context, graph, NodeType.OPERA);
                Node opera2 = new Node(context, graph, NodeType.OPERA);
                Node opera3 = new Node(context, graph, NodeType.OPERA);
                Node opera4 = new Node(context, graph, NodeType.OPERA);
                Node opera5 = new Node(context, graph, NodeType.OPERA);
                Node opera6 = new Node(context, graph, NodeType.OPERA);


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
                actualOpera = (Node) graph.successors(stanza1).toArray()[0];

            }
        });

    }

    public void prova(Context context){


        Grafo tmp = this;
        this.post(new Runnable(){
            public void setXY(Node node, float row){
                node.setX(((float) size.x/2) - fromDpToPx(24));
                node.setY(row);
            }

            public void run(){
                tmp.addView(drawView);


                int height = tmp.getHeight();
                int weight = tmp.getWidth();

                Log.d("altezza", String.valueOf(height));

                float r1 = 0;
                float r2 = (float) (height)/4;
                float r3 = r2*2;
                float r4 = r2*3;

                Node visita = new Node(context);
                tmp.addView(visita);
                setXY(visita, r1);


                Node museo1 = new Node(context);
                tmp.addView(museo1);
                graph.addNode(museo1);
                setXY(museo1, r2);
                //drawView.lines.add(new Line(visita.getX() + fromDpToPx(24), 0 + fromDpToPx(47), museo1.getX() + fromDpToPx(24), museo1.getY()));


                Node museo2 = new Node(context);

                tmp.addView(museo2);

                museo2.setX(((float) size.x/2) - fromDpToPx(24));
                museo2.setY(r3);

                //drawView.lines.add(new Line(museo1.getX() + fromDpToPx(24), museo1.getY() + fromDpToPx(47), museo2.getX() + fromDpToPx(24), museo2.getY()));


                Node stanza1_1 = new Node(context);
                tmp.addView(stanza1_1);

                stanza1_1.setX(((float) size.x/2) - fromDpToPx(24));
                stanza1_1.setY(r4);

                //drawView.lines.add(new Line(museo2.getX() + fromDpToPx(24), museo2.getY() + fromDpToPx(47), stanza1_1.getX() + fromDpToPx(24), stanza1_1.getY()));


                graph.addNode(museo2);

                Node stanza1_2 = new Node(context);
                graph.addNode(stanza1_2);

                Node stanza2_1 = new Node(context);
                graph.addNode(stanza2_1);

                Node stanza2_2 = new Node(context);
                graph.addNode(stanza2_2);

            }
        });

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
}

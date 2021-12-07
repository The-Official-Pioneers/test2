package it.uniba.pioneers.widget.grafo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

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
                if(n % 2 == 0)
                    return n > 1 ? ((float) size.x/(n - 1)) - fromDpToPx(24) : ((float) size.x/powOfTwo(1)) - fromDpToPx(24);
                else
                    return n > 1 ? ((float) size.x/n) - fromDpToPx(24) : ((float) size.x/powOfTwo(1)) - fromDpToPx(24);
            }

            private void resetDrawView(){
                if(drawView != null)
                    drawView.invalidate();
            }

            private void drawLine(Node nodeVisita){
                AtomicInteger contatoreZone = new AtomicInteger(1);
                drawView.resetDrawView(self, 0);

                for(Object nodeZona : graph.successors(nodeVisita).toArray()){
                    Node nodeZonaReal = (Node)nodeZona;

                    nodeZonaReal.setX(contatoreZone.get() * calcX(graph.successors(nodeVisita).size()));
                    nodeZonaReal.setY(r2);

                    drawView.linesZona.add(new Line(
                            (nodeVisita.getX() + fromDpToPx(24)),
                            nodeVisita.getY() + fromDpToPx(47),
                            (nodeZonaReal.getX() + fromDpToPx(24)),
                            nodeZonaReal.getY())
                    );

                    nodeZonaReal.setOnClickListener(view -> {
                        if(nodeZonaReal.clicked){

                            graph.successors(nodeZonaReal).forEach(figloNodoZona -> {
                                figloNodoZona.setVisibility(INVISIBLE);

                                graph.successors(figloNodoZona).forEach(figlioNodoArea ->{
                                    figlioNodoArea.setVisibility(INVISIBLE);
                                });
                            });
                            nodeZonaReal.setClicked(false);
                            nodeZonaReal.setCircle(false);

                        }else{

                            if(nodeZonaReal.inizializated){
                                graph.successors(nodeZonaReal).forEach(figloNodoZona -> {
                                    figloNodoZona.setVisibility(VISIBLE);

                                    graph.successors(figloNodoZona).forEach(figlioNodoArea ->{
                                        figlioNodoArea.setVisibility(VISIBLE);
                                    });
                                });
                            }else{
                                AtomicInteger contatoreAree = new AtomicInteger(1);
                                nodeZonaReal.setCircle(true);
                                //resetDrawView();
                                graph.successors(nodeZonaReal).forEach(nodeArea -> {
                                    nodeArea.setX(contatoreAree.get() *calcX(graph.successors(nodeZonaReal).size()));
                                    nodeArea.setY(r3);

                                    drawView.linesArea.add(new Line(
                                            (nodeZonaReal.getX() + fromDpToPx(24)),
                                            nodeZonaReal.getY() + fromDpToPx(47),
                                            (nodeArea.getX() + fromDpToPx(24)),
                                            nodeArea.getY())
                                    );

                                    resetDrawView();

                                    nodeArea.setOnClickListener(view1 -> {
                                        AtomicInteger contatoreOpere = new AtomicInteger(1);
                                        nodeArea.setCircle(true);
                                        graph.successors(nodeArea).forEach(nodeOpera -> {
                                            nodeOpera.setX(contatoreOpere.get() *calcX(graph.successors(nodeArea).size()));
                                            nodeOpera.setY(r4);


                                            drawView.linesOpera.add(new Line(
                                                    (nodeArea.getX() + fromDpToPx(24)),
                                                    nodeArea.getY() + fromDpToPx(47),
                                                    (nodeOpera.getX() + fromDpToPx(24)),
                                                    nodeOpera.getY())
                                            );
                                            resetDrawView();


                                            nodeOpera.setOnClickListener(view2 -> {
                                                nodeOpera.setCircle(true);
                                            });
                                            addView(nodeOpera);
                                            nodeOpera.setInizializated(true);
                                            contatoreOpere.incrementAndGet();
                                        });
                                    });
                                    addView(nodeArea);
                                    nodeArea.setInizializated(true);
                                    contatoreAree.incrementAndGet();
                                });

                                nodeZonaReal.setInizializated(true);

                            }

                            nodeZonaReal.setClicked(true);

                        }
                    });

                    contatoreZone.incrementAndGet();
                    if(!nodeZonaReal.inizializated){
                        addView(nodeZonaReal);
                    }
                }

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


                stanza1.addSuccessor(opera1);
                stanza1.addSuccessor(opera2);
                stanza1.addSuccessor(opera3);
                stanza1.addSuccessor(opera4);
                stanza1.addSuccessor(opera5);
                stanza1.addSuccessor(opera6);

                ///////////////////////////
                drawLine(visita);
                visita.setCircle(true);
                actualVisita = visita;

                /*drawLine(museo, r3, museo.getY());
                museo.setCircle(true);
                actualZona = museo;

                drawLine(stanza1, r4, stanza1.getY());
                stanza1.setCircle(true);
                actualArea = stanza1;*/

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

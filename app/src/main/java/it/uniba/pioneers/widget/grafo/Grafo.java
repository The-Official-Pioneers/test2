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

    DrawView drawView = null;

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

        Grafo tmp = this;
        this.post(new Runnable(){
            float r1, r2, r3, r4;
            int height, weight;

            private void initRow(){
                height = tmp.getHeight();
                weight = tmp.getWidth();

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

            private void drawLine(Node parent, float row, float parentY){
                AtomicInteger k = new AtomicInteger(1);
                graph.successors(parent)
                        .forEach(node -> {
                            node.setX(k.get() *calcX(graph.successors(parent).size()));
                            node.setY(row);
                            drawView.lines.add(new Line(
                                    (parent.getX() + fromDpToPx(24)),
                                     parentY + fromDpToPx(47),
                                    (node.getX() + fromDpToPx(24)),
                                    node.getY())
                            );
                            addView(node);

                            k.incrementAndGet();
                        });
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


                museo.addSuccessor(stanza1);
                museo.addSuccessor(stanza2);
                museo.addSuccessor(stanza3);
                museo.addSuccessor(stanza4);
                museo.addSuccessor(stanza5);

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
                drawLine(visita, r2, 0);
                visita.setCircle(true);

                drawLine(museo, r3, museo.getY());
                museo.setCircle(true);

                drawLine(stanza1, r4, stanza1.getY());
                stanza1.setCircle(true);

                tmp.addView(drawView);

            }
        });

    }

    public void prova(Context context){


        Grafo tmp = this;
        this.post(new Runnable(){
            public void run(){
                int height = tmp.getHeight();
                int weight = tmp.getWidth();

                Log.d("altezza", String.valueOf(height));

                float r1 = 0;
                float r2 = (float) (height)/4;
                float r3 = r2*2;
                float r4 = r2*3;

                Node visita = new Node(context);
                tmp.addView(visita);
                visita.setX(((float) size.x/2) - fromDpToPx(24));
                visita.setY(r1);


                Node museo1 = new Node(context);
                tmp.addView(museo1);
                graph.addNode(museo1);

                museo1.setX(((float) size.x/2) - fromDpToPx(24));
                museo1.setY(r2);

                tmp.addView(drawView);
                drawView.lines.add(new Line(visita.getX() + fromDpToPx(24), 0 + fromDpToPx(47), museo1.getX() + fromDpToPx(24), museo1.getY()));


                Node museo2 = new Node(context);

                tmp.addView(museo2);

                museo2.setX(((float) size.x/2) - fromDpToPx(24));
                museo2.setY(r3);

                drawView.lines.add(new Line(museo1.getX() + fromDpToPx(24), museo1.getY() + fromDpToPx(47), museo2.getX() + fromDpToPx(24), museo2.getY()));


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

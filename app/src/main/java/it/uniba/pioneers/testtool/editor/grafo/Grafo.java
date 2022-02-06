package it.uniba.pioneers.testtool.editor.grafo;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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

    public GraphNode visita = null;

    ListaNodi listaNodi = null;

    Context context = null;

    public float fromDpToPx(int dip) {
        Resources r = getResources();
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
    }

    private float powOfTwo(int n) {
        return 2 << (n - 1);
    }

    public void initDrawAttribute(Context context) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        display.getSize(size);
        drawView = new DrawView(context);
    }

    public void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.widget_grafo, this);

        this.context = context;
        initDrawAttribute(context);

        Visita tmpVisita = new Visita();
        tmpVisita.setId(3);

        Grafo self = this;
        Visita.getGraphData(context, tmpVisita,
                response -> {
                    try {
                        if (response.getBoolean("status") == true) {
                            Log.v("VISITE", response.getJSONObject("data").toString(4));
                            JSONObject data = response.getJSONObject("data");

                            visita = new GraphNode(context, self, NodeType.VISITA, data.getJSONObject("visita"));
                            JSONArray arrZona = data.getJSONArray("arrZona");
                            JSONArray arrArea = data.getJSONArray("arrArea");
                            JSONArray arrOpera = data.getJSONArray("arrOpera");

                            for (int i = 0; i < arrZona.length(); ++i) {
                                JSONObject zonaJSON = arrZona.getJSONObject(i);
                                GraphNode zona = new GraphNode(context, self, NodeType.ZONA, zonaJSON);
                                visita.addSuccessor(zona);
                                for (int j = 0; j < arrArea.length(); ++j) {
                                    JSONObject areaJSON = arrArea.getJSONObject(j);
                                    if (areaJSON.getInt("zona") == zonaJSON.getInt("id")) {
                                        GraphNode area = new GraphNode(context, self, NodeType.AREA, areaJSON);
                                        zona.addSuccessor(area);

                                        for (int k = 0; k < arrOpera.length(); ++k) {
                                            JSONObject operaJSON = arrOpera.getJSONObject(k);
                                            if (operaJSON.getInt("area") == areaJSON.getInt("id")) {
                                                GraphNode opera = new GraphNode(context, self, NodeType.OPERA, operaJSON);
                                                area.addSuccessor(opera);
                                            }
                                        }
                                    }
                                }
                            }
                            addStartNode(visita);
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Snackbar.make(
                        self.getRootView(),
                        "Non è stato possibile comunicare con il server",
                        BaseTransientBottomBar.LENGTH_LONG
                ).show()
        );

    }

    public void addStartNode(GraphNode dataNode) {
        graph.addNode(dataNode);
        this.post(new GraphViewer(this, context, visita));
    }

    public void addSuccessorToNode(GraphNode parentDataNode, GraphNode childDataNode) {
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

    public Line buildLineGraph(GraphNode start, GraphNode stop) {
        return new Line(start, stop);
    }

    public void setNodePositions() {
        AtomicInteger cntZona = new AtomicInteger(1);
        int numZona = graph.successors(visita).size();

    }

    public void initRow() {
        height = getHeight();
        weight = getWidth();

        r1 = 0;
        r2 = (float) (height) / 4;
        r3 = r2 * 2;
        r4 = r2 * 3;
    }

    public float r1;
    public float r2;
    public float r3;
    public float r4;
    int height, weight;

    public float calcX(int n) {
        Log.v("size", String.valueOf(size.x));
        Log.v("sizen", String.valueOf(size.x / n));
        float x = ((float) size.x / n) - fromDpToPx(20);
        Log.v("X-VALUE", String.valueOf(x));
        return n > 1 ? x : ((float) size.x / powOfTwo(1)) - fromDpToPx(24);
    }

    private class GraphViewer implements Runnable {
        protected final Grafo self;
        protected final Context context;

        GraphNode visita = null;

        public GraphViewer(Grafo self, Context context, GraphNode startDataNode) {
            this.visita = startDataNode;
            this.self = self;
            this.context = context;
        }

        @Override
        public void run() {
            initRow();
            visita.setY(r1);
            visita.setX(calcX(1));
            visita.size = 200;

            loadNode();
            visita.drawAllChild();
            actualVisita = visita;
        }

        private void loadNode() {
            for (GraphNode nodeZona : self.graph.successors(visita)) {
                visita.addSuccessor(nodeZona);
                for (GraphNode nodeArea : self.graph.successors(nodeZona)) {
                    nodeZona.addSuccessor(nodeArea);
                    for (GraphNode nodeOpera : self.graph.successors(nodeArea)) {
                        nodeArea.addSuccessor(nodeOpera);
                    }
                }
            }
        }
    }
}
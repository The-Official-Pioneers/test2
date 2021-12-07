package it.uniba.pioneers.widget.grafo;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class DrawView extends View{
    Paint paint = new Paint();
    ArrayList<Line> linesZona = new ArrayList<Line>();
    ArrayList<Line> linesArea = new ArrayList<Line>();
    ArrayList<Line> linesOpera = new ArrayList<Line>();

    public DrawView(Context context) {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void updateDrawLines(ArrayList<Line> linesZona, ArrayList<Line> linesArea, ArrayList<Line> linesOpera) {
        this.linesZona = linesZona;
        this.linesArea = linesArea;
        this.linesOpera = linesOpera;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Line l : linesZona) {
            canvas.drawLine(l.startX, l.startY, l.stopX, l.stopY, paint);
        }

        for (Line l : linesArea) {
            canvas.drawLine(l.startX, l.startY, l.stopX, l.stopY, paint);
        }

        for (Line l : linesOpera) {
            canvas.drawLine(l.startX, l.startY, l.stopX, l.stopY, paint);
        }
    }


    public void resetDrawView(Grafo graphWidget, int level){
        if(graphWidget.drawView != null)
            graphWidget.removeView(graphWidget.drawView);


        switch (level){
            case 0:
                updateDrawLines(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                break;
            case 1:
                updateDrawLines(this.linesZona, new ArrayList<>(), new ArrayList<>());
                break;
            case 2:
                updateDrawLines(this.linesZona, this.linesArea, new ArrayList<>());
                break;
            case 3:
                updateDrawLines(this.linesZona, this.linesArea, this.linesOpera);
                break;
        }

        graphWidget.addView(this);

    }

}
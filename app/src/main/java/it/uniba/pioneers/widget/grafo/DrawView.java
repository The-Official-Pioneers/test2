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
    ArrayList<Line> lines = new ArrayList<Line>();
    public DrawView(Context context) {
        super(context);

        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Line l : lines) {
            canvas.drawLine(l.startX, l.startY, l.stopX, l.stopY, paint);
        }
    }


}
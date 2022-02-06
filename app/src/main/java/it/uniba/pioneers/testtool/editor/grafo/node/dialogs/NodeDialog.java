package it.uniba.pioneers.testtool.editor.grafo.node.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.icu.util.Measure;
import android.icu.util.MeasureUnit;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.sql.Date;
import java.time.Instant;

import it.uniba.pioneers.testtool.DialogNodeInfo;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.editor.grafo.draw.Line;
import it.uniba.pioneers.testtool.editor.grafo.node.GraphNode;
import it.uniba.pioneers.testtool.editor.grafo.node.NodeType;

public class NodeDialog extends NodeListDialog{
    public static AlertDialog NodeDialog(Context context, GraphNode nodeObject) {
        AlertDialog tmpDialog = getDialog(context, nodeObject);
        return tmpDialog;
    }
}

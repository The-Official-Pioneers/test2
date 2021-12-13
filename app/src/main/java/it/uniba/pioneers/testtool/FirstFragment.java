package it.uniba.pioneers.testtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.testtool.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject tmp = new JSONObject();

                    tmp.put("id", 1);
                    tmp.put("nome", "Gino");
                    tmp.put("cognome", "Pino");
                    //tmp.put("data_nascita", CuratoreMuseale.dtStart);
                    tmp.put("email", "dadasdasd@asdasdd.com");
                    tmp.put("password", "dsadadasdasdsdfsdfdsffd");
                    tmp.put("propic", "/bin/usr");
                    tmp.put("zona", 1);

                    CuratoreMuseale c = new CuratoreMuseale(tmp);

                    MutableValueGraph<String, Integer> graph = ValueGraphBuilder.directed().build();

                    graph.addNode("MUSEO 1");
                    graph.addNode("OPERA 1");
                    graph.addNode("OPERA 2");
                    graph.addNode("OPERA 3");

                    graph.addNode("MUSEO 2");
                    graph.addNode("OPERA 4");
                    graph.addNode("OPERA 5");
                    graph.addNode("OPERA 6");

                    graph.putEdgeValue("MUSEO 1", "OPERA 1", 3);
                    graph.putEdgeValue("MUSEO 1", "OPERA 2", 3);
                    graph.putEdgeValue("MUSEO 1", "OPERA 3", 3);

                    graph.putEdgeValue("MUSEO 1", "MUSEO 2", 3);

                    graph.putEdgeValue("MUSEO 2", "OPERA 4", 3);
                    graph.putEdgeValue("MUSEO 2", "OPERA 5", 3);
                    graph.putEdgeValue("MUSEO 2", "OPERA 6", 3);


                    Toast.makeText(view.getContext(), (CharSequence) graph.toString(), Toast.LENGTH_LONG).show();

                }catch (JSONException | ParseException e){
                    Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_LONG).show();

                }


            }
        });

        binding.buttonDragAndDrop.setOnClickListener(view1 -> {
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}


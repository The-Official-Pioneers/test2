package it.uniba.pioneers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.uniba.pioneers.data.Zona;
import it.uniba.pioneers.testtool.R;

public class WidgetRegister extends LinearLayout {

    public void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        layoutInflater.inflate(R.layout.widget_register, this);
    }

    public WidgetRegister(@NonNull Context context) {
        super(context);
        init();
        setSpecialization(context);
        setTipoZona(context);
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
        setLuoghiVisite(context);
    }

    private void setTipoZona(Context context) {
        Spinner spinner = (Spinner) this.findViewById(R.id.tipoZona);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.tipo_zona_curatore,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setSpecialization(Context context) {
        Spinner spinner = (Spinner) this.findViewById(R.id.specializzazione);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.specialization_guida,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    //Gestione Spinner con Luoghi presenti nel DB
    public void setLuoghiVisite(Context context){
        Spinner spinner = (Spinner) this.findViewById(R.id.cittaZona);
        try{
            Zona.getAllLuoghi(context,
                    response -> {
                        try {
                            if(response.getBoolean("status")){

                                List<String> listaLuoghi = new ArrayList<>();
                                JSONObject tmpObj = response.getJSONObject("data");
                                JSONArray arrayData = tmpObj.getJSONArray("arrLuoghi");

                                for(int i = 0; i < arrayData.length(); ++i){
                                    JSONObject luogo = arrayData.getJSONObject(i);
                                    listaLuoghi.add(luogo.getString("nome"));
                                }

                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context,
                                        android.R.layout.simple_spinner_item, listaLuoghi);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(spinnerAdapter);

                            }else{
                                Toast.makeText(context, R.string.impossibile_procedere, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WidgetRegister(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
        setSpecialization(context);
        setTipoZona(context);
        setLuoghiVisite(context);
    }

    public WidgetRegister(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
        setSpecialization(context);
        setTipoZona(context);
        setLuoghiVisite(context);
    }

    public WidgetRegister(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        setDayDate(context);
        setMounthDate(context);
        setYearsDate(context);
        setSpecialization(context);
        setTipoZona(context);
        setLuoghiVisite(context);
    }

    public void setDayDate(Context context){
        Spinner spinner = (Spinner) this.findViewById(R.id.day_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.days_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setMounthDate(Context context){
        Spinner spinner = (Spinner) this.findViewById(R.id.mounth_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.mounth_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setYearsDate(Context context){
        Spinner spinner = (Spinner) this.findViewById(R.id.years_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.years_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

}
package it.uniba.pioneers.testtool.home.ui;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import it.uniba.pioneers.data.Zona;
import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.data.users.Visitatore;
import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.home.HomeActivity;
import it.uniba.pioneers.widget.WidgetRegister;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    //FOR PHOTO REQUEST
    private static final int RESULT_LOAD_IMG = 1;
    private static final int PICK_FROM_GALLERY = 1;
    boolean c1;
    boolean c2;
    boolean c3;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WidgetRegister ln;
    private String userType = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setAnimationToButton(view);
        ln = this.getView().findViewById(R.id.curatoreForm);
        ln.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setAnimationToButton(View view){
        Animation fadeIn = new AlphaAnimation(0, 1); //@Param: fromAlpha e toAlpha => valore alfa iniziale per l'animazione, dove 1.0 significa completamente opaco e 0.0 significa completamente trasparente.
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        Button buttonCuratore = (Button)getView().findViewById(R.id.curatore);
        buttonCuratore.setAnimation(animation);
        Button buttonVisitatore = (Button)getView().findViewById(R.id.visitatore);
        buttonVisitatore.setAnimation(animation);
        Button buttonGuida = (Button)getView().findViewById(R.id.guida);
        buttonGuida.setAnimation(animation);
    }

    public void callBackHandler(View view, String string){
        switch (string) {
            case "curatore":
                userType = "curatore";
                setWidegetRegister(view);
                break;
            case "visitatore":
                userType = "visitatore";
                setWidegetRegister(view);
                break;
            case "guida":
                userType = "guida";
                setWidegetRegister(view);
                break;
        }
    }

    public void setWidegetRegister(View view) {
        Animation fadeIn = new AlphaAnimation(1, 0); //@Param: fromAlpha e toAlpha => valore alfa iniziale per l'animazione, dove 1.0 significa completamente opaco e 0.0 significa completamente trasparente.
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(100);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);

        Button buttonCuratore = (Button) getView().findViewById(R.id.curatore);
        buttonCuratore.setAnimation(animation);
        buttonCuratore.setVisibility(view.GONE);

        Button buttonVisitatore = (Button) getView().findViewById(R.id.visitatore);
        buttonVisitatore.setAnimation(animation);
        buttonVisitatore.setVisibility(view.GONE);

        Button buttonGuida = (Button) getView().findViewById(R.id.guida);
        buttonGuida.setAnimation(animation);
        buttonGuida.setVisibility(view.GONE);

        TextView who = (TextView) getView().findViewById(R.id.who);
        who.setAnimation(animation);
        who.setVisibility(view.GONE);

        ln.setVisibility(View.VISIBLE);
        if(userType == "guida"){
            Spinner spec = (Spinner) getView().findViewById(R.id.specializzazione);
            spec.setVisibility(View.VISIBLE);
        }
        if(userType == "curatore"){
            LinearLayout subForm = (LinearLayout) getView().findViewById(R.id.subFormCuratore);
            subForm.setVisibility(View.VISIBLE);

            LinearLayout subFormSeparator = (LinearLayout) getView().findViewById(R.id.subFormCuratoreSeparator);
            subFormSeparator.setVisibility(View.VISIBLE);
        }
        Button myButton = (Button) getView().findViewById(R.id.guida);
        myButton.setEnabled(false);
    }

    public void addImageToForm(View view){
        System.out.println("PRENDO");
        try {
            if(ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Permessi Galleria")
                            .setMessage("Questi permessi sono necessari al fine di poter impostare la tua immagine di profilo personale. Puoi anche non concederli se non vuoi impostare un immagine di profilo")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }
            }else{
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
            break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            try {
                //encode image to base64 string
                final Uri imageUri = data.getData();
                final InputStream inputStream = ((HomeActivity)getActivity()).getContentResolverAct(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG,50,baos);
                byte[] b = baos.toByteArray();
                String encImage = Base64.encodeToString(b, Base64.DEFAULT);
                System.out.println(encImage);

                // show the image to the user
                ImageView targetImage = this.getView().findViewById(R.id.imgToSet);
                targetImage.setImageBitmap(selectedImage);
                ImageView nullImage = this.getView().findViewById(R.id.nullImg);
                nullImage.setVisibility(GONE);
                targetImage.setVisibility(View.VISIBLE);

                //Set variable not visible
                TextView base64Img = this.getView().findViewById(R.id.Base64ImgString);
                base64Img.setText(encImage);

            } catch (Exception e) {
                e.printStackTrace();
                // show a message to the user indictating that the image is unavailable.
                Toast.makeText(this.getContext(), "Unable to open image", Toast.LENGTH_LONG).show();
            }

        }
    }



    private JSONObject createDataContainer(View view) {
        JSONObject data = new JSONObject();
        View v =  (View) view.getParent();
        EditText nomeForm =  v.findViewById(R.id.nomeFormRef);
        EditText cognomeForm =  v.findViewById(R.id.cognomeFormRef);
        EditText emailForm =  v.findViewById(R.id.emailFormRef);
        EditText passwordForm =  v.findViewById(R.id.passwordFormRef);
        Spinner specializzazione =  v.findViewById(R.id.specializzazione);
        Spinner zonaForm =  v.findViewById(R.id.tipoZona);
        EditText nomeZonaForm =  v.findViewById(R.id.nomeZona);
        EditText cittaZonaForm =  v.findViewById(R.id.cittaZona);

        Spinner mySpinnerDay = (Spinner) v.findViewById(R.id.day_spinner);
        String dayText = mySpinnerDay.getSelectedItem().toString();

        Spinner mySpinnerMounth = (Spinner) v.findViewById(R.id.mounth_spinner);
        String mounthtext = mySpinnerMounth.getSelectedItem().toString();

        Spinner mySpinnerYear = (Spinner) v.findViewById(R.id.years_spinner);
        String yearText = mySpinnerYear.getSelectedItem().toString();

        String date = dayText + "/" + mounthtext + "/" + yearText;
        Date expiredDate = stringToDate(date, "dd/MM/yyyy");

        TextView imgText = v.findViewById(R.id.Base64ImgString);
        String uriString = imgText.getText().toString();

        try {
            data.put("id", 0);
            data.put("nome", nomeForm.getText().toString());
            data.put("cognome",cognomeForm.getText().toString());
            data.put("email",emailForm.getText().toString());
            data.put("password", passwordForm.getText().toString());
            data.put("data_nascita", date);
            data.put("propic", uriString);
            if(userType == "curatore"){
                data.put("tipoZona", zonaForm.getSelectedItem().toString());
                data.put("nomeZona", nomeZonaForm.getText().toString());
                data.put("cittaZona", cittaZonaForm.getText().toString());
            }
            if(userType == "guida"){
                data.put("specializzazione", specializzazione.getSelectedItem().toString());
            }
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date stringToDate(String aDate,String aFormat) {
        if (aDate == null) {
            return null;
        } else {
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
            Date stringDate = simpledateformat.parse(aDate, pos);
            return stringDate;
        }
    }

    public void saveDataInDb(String typeUser,JSONObject data) {
        switch (userType){
            case "curatore":
                saveDataInDbCuratore(data);
            break;
            case "visitatore":
                System.out.println("STO SALVANDO IN VISITATORE");
                saveDataInDbVisitatore(data);
            break;
            case "guida":
                saveDataInDbGuida(data);
            break;
        }
    }

    private void saveDataInDbGuida(JSONObject data) {
        try {
            Guida guida = new Guida(data);
            guida.createDataDb(getView().getContext(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status = response.getBoolean("status");
                        guida.setStatusComputation(status);
                        if (status) {
                            Toast.makeText(getView().getContext(), "Registrazione avvenuta con successo! Benvenuto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), MainActivity.class);
                            intent.putExtra("User", userType);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getView().getContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){ }
    }


    private void saveDataInDbVisitatore(JSONObject data) {
        try {
            Visitatore visitatore = new Visitatore(data);
            System.out.println("COMPUTO");
            visitatore.createDataDb(getView().getContext(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status = response.getBoolean("status");
                        visitatore.setStatusComputation(status);
                        if (status) {
                            Toast.makeText(getView().getContext(), "Registrazione avvenuta con successo! Benvenuto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), MainActivity.class);
                            intent.putExtra("User", userType);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getView().getContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){ }
    }


    private void saveDataInDbCuratore(JSONObject data) {
        try {
            Zona zona = new Zona();
            zona.setDenominazione(data.getString("nomeZona"));
            zona.setTipo(data.getString("tipoZona"));
            zona.setLuogo(data.getString("cittaZona"));
            zona.setOnline(true);

            zona.createDataDb(getView().getContext(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status = response.getBoolean("status");
                        zona.setStatusComputation(status);
                        if (status) {
                            zona.setDataFromJSON(response.getJSONObject("data"));
                            data.put("zona", zona.getId());
                            System.out.println(data);
                            CuratoreMuseale curatore = new CuratoreMuseale(data);
                            curatore.setZona(zona.getId());
                            curatore.createDataDb(getView().getContext(), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Boolean status = response.getBoolean("status");
                                        curatore.setStatusComputation(status);
                                        if (status) {
                                            Toast.makeText(getView().getContext(), "Registrazione avvenuta con successo! Benvenuto", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getView().getContext(), MainActivity.class);
                                            intent.putExtra("user", userType);
                                            intent.putExtra("idUser", curatore.getId());
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getView().getContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getView().getContext(), HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getView().getContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){ }
    }

    public boolean controllData(View v) {
        try {
            controllDataCuratore();
        }catch(Exception e){}
        return true;
    }

    public void finalResultControll() throws JSONException {
        System.out.println("c1: "+c1+ "\nc2: "+c2+"\nc3: "+c3);
        if(!c1 && !c2 && !c3){
            System.out.println("DONE!");
            JSONObject data = createDataContainer(getView());
            if(controllDataContainer(data)){
                saveDataInDb(userType, data);
            }else{
                Toast.makeText(getView().getContext(), "Attenzione! Alcuni campi obbligatori sono vuoti", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean controllDataContainer(JSONObject data) throws JSONException {
        switch (userType){
            case "curatore":
                return !data.getString("nome").isEmpty() && !data.getString("cognome").isEmpty() && !data.getString("email").isEmpty() && !data.getString("password").isEmpty() && !(data.getString("data_nascita").contains("Days") || data.getString("data_nascita").contains("Mounth") || data.getString("data_nascita").contains("Years")) && !data.getString("tipoZona").equals("Tipo zona") && !data.getString("nomeZona").isEmpty() && !data.getString("cittaZona").isEmpty();
            case "visitatore":
                return !data.getString("nome").isEmpty() && !data.getString("cognome").isEmpty() && !data.getString("email").isEmpty() && !data.getString("password").isEmpty() && !(data.getString("data_nascita").contains("Days") || data.getString("data_nascita").contains("Mounth") || data.getString("data_nascita").contains("Years"));
            case "guida":
                return !data.getString("nome").isEmpty() && !data.getString("cognome").isEmpty() && !data.getString("email").isEmpty() && !data.getString("password").isEmpty() && !(data.getString("data_nascita").contains("Days") || data.getString("data_nascita").contains("Mounth") || data.getString("data_nascita").contains("Years")) && !data.getString("specializzazione").equals("Specialization");
        }
        return false;
    }

    private void controllDataCuratore() throws JSONException, ParseException, InterruptedException {
        JSONObject datatmp = createDataContainer(getView());
        CuratoreMuseale curatoreMuseale = new CuratoreMuseale();
        curatoreMuseale.setEmail(datatmp.getString("email"));
        curatoreMuseale.controllEmail(getView().getContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    if (status) {
                        curatoreMuseale.setStatusComputation(true);
                        Toast.makeText(getView().getContext(), "Email già utilizzata", Toast.LENGTH_SHORT).show();
                        setCuratoreCondition(true);
                        controllDataVisitatore();
                    }else{
                        curatoreMuseale.setStatusComputation(false);
                        setCuratoreCondition(false);
                        controllDataVisitatore();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setCuratoreCondition(boolean esit){
        c1 = esit;
    }

    private void controllDataVisitatore() throws JSONException, ParseException {
        JSONObject data = createDataContainer(getView());
        Visitatore visitatore = new Visitatore();
        visitatore.setEmail(data.getString("email"));
        visitatore.controllEmail(getView().getContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    if (status) {
                        visitatore.setStatusComputation(false);
                        Toast.makeText(getView().getContext(), "Email già utilizzata", Toast.LENGTH_SHORT).show();
                        setVisitatoreCondition(true);
                        controllDataGuida();
                    }else{
                        visitatore.setStatusComputation(true);
                        setVisitatoreCondition(false);
                        controllDataGuida();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setVisitatoreCondition(boolean esit){
        c2 = esit;
    }

    private void controllDataGuida() throws JSONException, ParseException {
        JSONObject data = createDataContainer(getView());
        Guida guida = new Guida();
        guida.setEmail(data.getString("email"));
        guida.setPassword(data.getString("password"));
        guida.controllEmail(getView().getContext(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    if (status) {
                        guida.setStatusComputation(false);
                        Toast.makeText(getView().getContext(), "Email già utilizzata", Toast.LENGTH_SHORT).show();
                        setGuidaCondition(true);
                        finalResultControll();
                    }else{
                        guida.setStatusComputation(true);
                        setGuidaCondition(false);
                        finalResultControll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setGuidaCondition(boolean esit){
        c3 = esit;
    }
}
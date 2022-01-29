package it.uniba.pioneers.testtool.home.ui;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.transition.MaterialFade;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.sqlite.DbContract;
import it.uniba.pioneers.testtool.EditorActivity;
import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.home.HomeActivity;
import it.uniba.pioneers.widget.ListaNodi;
import it.uniba.pioneers.widget.WidgetCuratoreRegister;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    TextView textTargetUri;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WidgetCuratoreRegister ln;

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
                setWidegetCuratore(view);
                break;
            case "visitatore":
                Toast.makeText(view.getContext(), "visitatore", Toast.LENGTH_SHORT).show();
                break;
            case "guida":
                Toast.makeText(view.getContext(), "guida", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setWidegetCuratore(View view) {
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
    }

    public void addImageToForm(View view){
        try {
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
            } else {
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
            Uri imageUri = data.getData();
            TextView uriImg = this.getView().findViewById(R.id.uriImgString);
            uriImg.setText(data.getDataString());

            // declare a stream to read the image data from the SD Card.
            InputStream inputStream;

            // we are getting an input stream, based on the URI of the image.
            try {
                inputStream = ((HomeActivity)getActivity()).getContentResolverAct(imageUri);

                // get a bitmap from the stream.
                Bitmap image = BitmapFactory.decodeStream(inputStream);

                // show the image to the user
                ImageView targetImage = this.getView().findViewById(R.id.imgToSet);
                targetImage.setImageBitmap(image);
                ImageView nullImage = this.getView().findViewById(R.id.nullImg);
                nullImage.setVisibility(GONE);
                targetImage.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
                // show a message to the user indictating that the image is unavailable.
                Toast.makeText(this.getContext(), "Unable to open image", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void registerCuratoreFragment(View view){
        JSONObject data = new JSONObject();
        View v =  (View) view.getParent();
        EditText nomeCuratore =  v.findViewById(R.id.nomeCuratore);
        EditText cognomeCuratore =  v.findViewById(R.id.cognomeCuratore);
        EditText emailCuratore =  v.findViewById(R.id.emailCuratore);
        EditText passwordCuratore =  v.findViewById(R.id.passwordCuratore);

        Spinner mySpinnerDay = (Spinner) v.findViewById(R.id.mounth_spinner);
        String dayText = mySpinnerDay.getSelectedItem().toString();

        Spinner mySpinnerMounth = (Spinner) v.findViewById(R.id.mounth_spinner);
        String mounthtext = mySpinnerMounth.getSelectedItem().toString();

        Spinner mySpinnerYear = (Spinner) v.findViewById(R.id.years_spinner);
        String yearText = mySpinnerYear.getSelectedItem().toString();

        String date = yearText + "-" + mounthtext + "-" + dayText;
        Date expiredDate = stringToDate(date, "EEE MMM d HH:mm:ss zz yyyy");

        TextView imgText = v.findViewById(R.id.uriImgString);
        String uriString = imgText.getText().toString();

        try {
            data.put("id", 0);
            data.put("nome", nomeCuratore.getText().toString());
            data.put("cognome",cognomeCuratore.getText().toString());
            data.put("email",emailCuratore.getText().toString());
            data.put("password", passwordCuratore.getText().toString());
            data.put("data_nascita", date);
            data.put("propic", uriString);
            data.put("zona", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveDataInDb("Curatore", data);
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
        switch (typeUser){
            case "Curatore":
                saveDataInDbCuratore(data);
            break;
        }
    }

    private void saveDataInDbCuratore(JSONObject data) {

        class RunCreate extends Thread{
            public void run() {
                try {
                    it.uniba.pioneers.data.users.CuratoreMuseale curatore = new it.uniba.pioneers.data.users.CuratoreMuseale(data);
                    curatore.createDataDb(getView().getContext());
                    SystemClock.sleep(2000);
                    System.out.println(curatore.getStatusComputation());
                    if(curatore.getStatusComputation()){
                        Intent intent = new Intent(getView().getContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getView().getContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception e){ }
            }
        }
        try{
            RunCreate thread = new RunCreate();
            thread.setPriority(10);
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
            
        }
    }

}
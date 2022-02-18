package it.uniba.pioneers.testtool.accesso;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.uniba.pioneers.data.Zona;
import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.data.users.Visitatore;
import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.widget.WidgetRegister;

public class RegisterFragment extends Fragment {
    //FOR PHOTO REQUEST
    private static final int PICK_FROM_GALLERY = 1; //Verrà utilizzato per la richiesta dei permessi

    /****************************************************
     * Variabili utilizzate in fase di controllo dei dati
     ***************************************************/
    boolean c1;
    boolean c2;
    boolean c3;
    /****************************************************/

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WidgetRegister ln; //Riferimento che conterrà il widget presente nel layout del fragment
    private String userType = null; //Attore scelto dall'utente

    private String mParam1;
    private String mParam2;

    public RegisterFragment() {

    }

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
        setRetainInstance(true); //Metodo utilizzato per evitare di perdere lo stato ad un cambio di configurazione
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    /**************************************************************************************
     * Metodo che viene invocato quando la View del fragment è stata creata correttamente
     * In questo metodo inserisco la logica di manipolazione della view
     * In particolare rendo GONE il widget dedicato alla registrazione e imposto dei
     * listener su alcuni campi del form
     * @param view la view creata.
     * @param savedInstanceState oggetto utilizzato per il salvataggio dello stato.
     *************************************************************************************/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final EditText usernameEditText = this.getView().findViewById(R.id.emailFormRef);
        final EditText passwordEditText = this.getView().findViewById(R.id.passwordFormRef);
        final Button registerButton = this.getView().findViewById(R.id.registerForm);

        /**************************************************************
         * Iposto le animazioni delle view grazie alla classe Animation
         * ************************************************************/
        setAnimationToButton(view);

        /*********************************************************
         * Rendo invisibile il widget dedicato alla registrazione.
         * Sarà visibile solo dopo la scelta dell'attore
         * *******************************************************/
        ln = this.getView().findViewById(R.id.formForRegistration);
        ln.setVisibility(View.GONE);
         /********************************************************/

        /*********************************************************
         * Preparo dei listener che saranno aggiunti ai campi
         * email e password per effettuare dei controlli
         * *******************************************************/
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            /*********************************************************
             * Verifica della correttezza sui campi email e password
             * *******************************************************/
            @Override
            public void afterTextChanged(Editable s) {
                if (!isEmailValid(usernameEditText.getText().toString())) {
                    usernameEditText.setError("Inserisci una mail valida");
                    registerButton.setEnabled(false);
                } else if (!isPasswordValid(passwordEditText.getText().toString())) {
                    passwordEditText.setError("Inserisci una password con più di 5 caratteri");
                    registerButton.setEnabled(false);
                } else {
                    /*********************************************************
                     * Se i campi sono corretti abilito la registrazione
                     * *******************************************************/
                    registerButton.setEnabled(true);
                }
            }
        };
        /*********************************************************
         * Aggiungo i listener ai campi email e password per la
         * verifica della correttezza
         * *******************************************************/
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
    }

    /**************************************************************************************
     * Metodo che viene invocato per controllare se l'email inserita dall'
     * utente rispettano i requisiti di correttezza
     * @param email email inserita
     * @return true se l'email è corretta oppure false se l'email è scorretta
     *************************************************************************************/
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@") && (email.contains(".it") || email.contains(".com") || email.contains(".org") || email.contains(".net"))) {
            return true;
        } else {
            return false;
        }
    }

    /**************************************************************************************
     * Metodo che viene invocato per controllare se la password inserita dall'
     * utente rispettano i requisiti di correttezza
     * @param password password inserita
     * @return true se la password è corretta oppure false se la password è scorretta
     *************************************************************************************/
    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*********************************************************
     * Metodo che viene invocato per impostare un animazione
     * con l'utilizzo della classe Animation
     *
     * @param view Parameter 1.
     ********************************************************/
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

    /*************************************************************
     * Metodo che viene invocato quando l'utente sceglie
     * l'attore(curatore, visitaote, guida) per la registrazione.
     * Una volta impostato l'attore rendo visibile il form di
     * registrazione tramite l'apposito metodo setWidegetRegister
     * @param view Parameter 1.
     * param string Parameter 2 per l'esito della scelta.
     ************************************************************/
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

    /*************************************************************
     * Metodo che viene invocato subito dopo che l'utente ha
     * scelto l'attore e che mostra un form costruito adHoc per l'
     * attore scelto
     * @param view Parameter 1.
     ************************************************************/
    public void setWidegetRegister(View view) {

        /**************************************************************
         * INIZIO FASE IN CUI VIENE NASCOSTO LA SCELTA DELL'ATTORE
         * E DEL TITOLO 'CHI SEI'
         * * **********************************************************/
        LinearLayout l = (LinearLayout) getActivity().findViewById(R.id.presentation);
        l.setVisibility(View.GONE);

        /*************************************************************
         * Rendo il widget relativo al form di registrazione visible
         * * **********************************************************/
        ln.setVisibility(View.VISIBLE);
        if(userType == "guida"){
            /************************************************************
             * Faccio comparire dei campi in più che sono specifici per
             * la guida
             ***********************************************************/
            Spinner spec = (Spinner) getView().findViewById(R.id.specializzazione);
            spec.setVisibility(View.VISIBLE);
        }
        if(userType == "curatore"){
            /************************************************************
             * Faccio comparire dei campi in più che sono specifici per
             * la il Curatore Museale
             ***********************************************************/
            LinearLayout subForm = (LinearLayout) getView().findViewById(R.id.subFormCuratore);
            subForm.setVisibility(View.VISIBLE);

            /*********************************************************
             *  Rendo visibile un separatore che divide la sezione
             *  dell'anagrafica del curatore dalla sezione relativa
             *  all'inserimento della zona che gestisce
             *********************************************************/
            LinearLayout subFormSeparator = (LinearLayout) getView().findViewById(R.id.subFormCuratoreSeparator);
            subFormSeparator.setVisibility(View.VISIBLE);
        }
    }

    /*************************************************************
     * Metodo che viene invocato quando l'utente vuole inserire
     * una propria immagine di profilo durante il processo di
     * registrazione;
     * Il metodo accede allo storage del dispositivo,
     * pertanto, ogni volta, verifica che i permessi per farlo siano
     * attivi
     * @param view Parameter 1.
     ************************************************************/
    public void addImageToForm(View view){
        try {
            /***************************************************************************
             * Effettuo la richiesta dei permessi; in ordine eseguo:
             * 1) Controllo se i permessi sono stati concessi con checkSelfPermission
             * 2) Se non sono stati concessi spiego all'utente l'utilità di tali permessi
             *    con il metodo shouldShowRequestPermissionRationale()
             * 3) Nel caso in cui non ho bisogno di dare spiegazioni procedo con la
             *    richiesta attraverso requestPermissions()
             *************************************************************************/
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
                /***************************************************************************
                 * I permessi sono già stati concessi, posso leggere dallo storage
                 *************************************************************************/
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*************************************************************
     * Metodo di callBack che viene invocato dopo che l'utente
     * fornisce la sua risposta alla richiesta dei permessi
     * @param requestCode contiene il resultCode precedentemente
     *                    passato a requestPermission per specificare
     *                    la richiesta
     *                    Esso sarà identico a PICK_FROM_GALLERY
     *                    qualora i permessi vengano concessi
     * @param permissions Permessi richiesti
     * @param grantResults Esito della computazione
     ************************************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*******************************************************************
                     * I permessi sono stati concessi, posso leggere lo storage esterno
                     *******************************************************************/
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);

                } else {
                    /******************************************************
                     * I permessi non sono stati concessi, Graceful Degree
                     ******************************************************/
                    new AlertDialog.Builder(getView().getContext())
                            .setTitle("Permessi Galleria")
                            .setMessage("I permessi non sono stati concessi. Potrai comunque procedere alla registrazione nell'app senza impostare una foto profilo ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            break;
        }
    }

    /*********************************************************************************
     * Metodo di callBack che viene invocato dopo che l'utente ha selezionato la foto
     * In questo metodo, se la computazione precedente è andata a buon fine, prima
     * converto l'immagine in un array di byte e poi lo codifico in base 64: tale
     * operazione viene fatta per salvare l'immagine direttamente nel database nel
     * campo "propic"
     * Inoltre, nel processo di encoding, viene utilizzata una bitmap(matrice di
     * pixel) che permetterà di visualizzare l'immagine scelta nel form
     * @param requestCode Codice della richiesta
     * @param resultCode Esito della computazione
     * @param data Risultato ottenuto
     *********************************************************************************/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            try {
                //Ottengo l'uri dell'immagine che identifica la foto
                final Uri imageUri = data.getData();

                //Apro un inputStream nel quale risiede l'immagine
                final InputStream inputStream = ((IntroActivity)getActivity()).getContentResolverAct(imageUri);

                //Decodifico l'immagine presente nello stream in una bitmap
                final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);

                //Creo un outputStream nel quale sarà contenuta l'immagine compressata
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                //Comprimo l'immagine in formato jpeg e con qualità 50 e la inserisco in baos
                selectedImage.compress(Bitmap.CompressFormat.JPEG,50,baos);

                //Recupero l'immagine dall'outputStream sottoforma di byte
                byte[] b = baos.toByteArray();

                //Codifico i byte dell'immagine in base 64
                String encImage = Base64.encodeToString(b, Base64.DEFAULT);

                // show the image to the user
                ImageView targetImage = this.getView().findViewById(R.id.imgToSet);
                //Strampo la bitmap nell'imageview
                targetImage.setImageBitmap(selectedImage);
                ImageView nullImage = this.getView().findViewById(R.id.nullImg);

                //Nascondo l'immagine predefinita che appariva prima e mostro quella selezionata
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

    /*********************************************************************************
     * Metodo di supporto che consente di raccogliere i dati da ogni campo del form e
     * di formare un conteiner di dati che può essere utilizzato dove richiesto.
     * Il metodo è univoco e funziona per tutti i tre gli attori: ciò significa che
     * sarà restituito un conteiner di dati specifico per ogni attore
     * @param view vista del fragment
     * @return JSONObject container di dati
     *********************************************************************************/
    private JSONObject createDataContainer(View view) {

        /***************************************************************
         * Prendo il riferimento ad ogni campo del form tramite R
         ***************************************************************/
        JSONObject data = new JSONObject();
        View v =  (View) view.getParent();
        EditText nomeForm =  v.findViewById(R.id.nomeFormRef);
        EditText cognomeForm =  v.findViewById(R.id.cognomeFormRef);
        EditText emailForm =  v.findViewById(R.id.emailFormRef);
        EditText passwordForm =  v.findViewById(R.id.passwordFormRef);
        Spinner specializzazione =  v.findViewById(R.id.specializzazione);
        Spinner zonaForm =  v.findViewById(R.id.tipoZona);
        EditText nomeZonaForm =  v.findViewById(R.id.nomeZona);
        Spinner cittaZonaForm =  v.findViewById(R.id.cittaZona);
        /***************************************************************/


        /***************************************************************
         * Prendo il riferimento agli spinner della data nel form
         ***************************************************************/
        Spinner mySpinnerDay = (Spinner) v.findViewById(R.id.day_spinner);
        String dayText = mySpinnerDay.getSelectedItem().toString();

        Spinner mySpinnerMounth = (Spinner) v.findViewById(R.id.mounth_spinner);
        String mounthtext = mySpinnerMounth.getSelectedItem().toString();

        Spinner mySpinnerYear = (Spinner) v.findViewById(R.id.years_spinner);
        String yearText = mySpinnerYear.getSelectedItem().toString();
        /***************************************************************/


        /***************************************************************
         * Creo la data in base ai valori degli spinner
         ***************************************************************/
        String date = yearText + "-" + mounthtext + "-" + dayText +"T02:50:12.208Z";
        Date expiredDate = stringToDate(date, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        TextView imgText = v.findViewById(R.id.Base64ImgString);
        String img = imgText.getText().toString();


        /**********************************************************************************
         * Creo il container di dati
         **********************************************************************************/
        try {
            data.put("id", 0);
            data.put("nome", nomeForm.getText().toString());
            data.put("cognome",cognomeForm.getText().toString());
            data.put("email",emailForm.getText().toString());
            data.put("password", passwordForm.getText().toString());
            data.put("data_nascita", date);
            data.put("propic", img);
            if(userType == "curatore"){
                data.put("tipoZona", zonaForm.getSelectedItem().toString());
                data.put("nomeZona", nomeZonaForm.getText().toString());
                data.put("cittaZona", cittaZonaForm.getSelectedItem().toString());
            }
            if(userType == "guida"){
                data.put("specializzazione", specializzazione.getSelectedItem().toString());
            }
            /**********************************************************************************/

            //Restituisco il container
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*********************************************************************************
     * Metodo di supporto che viene utilizzato per convertire la data da stringa a
     * Date
     * @param aDate data passata come stringa
     * @param aFormat formato di conversione
     * @return Date data convertita in un tipo Date
     *********************************************************************************/
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


    /*********************************************************************************
     * Processo di salvataggio dei dati all'interno del dataBase; esso chiama il metodo
     * corretto in base al tipo di attore
     * @param data container di dati da salvare
     *********************************************************************************/
    public void saveDataInDb(JSONObject data) {
        switch (userType){
            case "curatore":
                saveDataInDbCuratore(data);
            break;
            case "visitatore":
                saveDataInDbVisitatore(data);
            break;
            case "guida":
                saveDataInDbGuida(data);
            break;
        }
    }

    /*********************************************************************************
     * Processo di salvataggio dei dati relativo alla Guida all'interno del dataBase;
     * esso utilizza il metodo createDataDb definito all'interno del DAO Guida
     * @param data container di dati da salvare
     *********************************************************************************/
    private void saveDataInDbGuida(JSONObject data) {
        try {
            Guida guida = new Guida(data);

            /*********************************************************************************
             * Metodo di salvataggio di dati vero e prorio; restituisce la notifica dell'esito
             * della compatuzione che viene catturato dal listener
             *********************************************************************************/
            guida.createDataDb(getView().getContext(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status = response.getBoolean("status");
                        guida.setStatusComputation(status);

                        if (status) {
                            /*********************************************************************************************************
                             * Se i dati sono stati salvati correttamente possiamo far accedere l'utente alla mainActivity passando
                             * l'id dell'utente e il tipo di utente
                             ********************************************************************************************************/
                            Toast.makeText(getView().getContext(), "Registrazione avvenuta con successo! Benvenuto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), MainActivity.class);
                            intent.putExtra("typeUser", userType);
                            intent.putExtra("idUser", (int)response.getJSONObject("data").getInt("id"));
                            startActivity(intent);
                        } else {
                            /*********************************************************************************************************
                             * Registrazione non andata a buon fine
                             ********************************************************************************************************/
                            Toast.makeText(getView().getContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), IntroActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){ }
    }

    /*********************************************************************************
     * Processo di salvataggio dei dati relativo al Visitatore all'interno del dataBase;
     * esso utilizza il metodo createDataDb definito all'interno del DAO Visitatore
     * @param data container di dati da salvare
     *********************************************************************************/
    private void saveDataInDbVisitatore(JSONObject data) {
        try {
            Visitatore visitatore = new Visitatore(data);

            /*********************************************************************************
             * Metodo di salvataggio di dati vero e prorio; restituisce la notifica dell'esito
             * della compatuzione che viene catturato dal listener
             *********************************************************************************/
            visitatore.createDataDb(getView().getContext(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status = response.getBoolean("status");
                        visitatore.setStatusComputation(status);

                        if (status) {
                            /*********************************************************************************************************
                             * Se i dati sono stati salvati correttamente possiamo far accedere l'utente alla mainActivity passando
                             * l'id dell'utente e il tipo di utente
                             ********************************************************************************************************/
                            Toast.makeText(getView().getContext(), "Registrazione avvenuta con successo! Benvenuto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), MainActivity.class);
                            intent.putExtra("typeUser", userType);
                            intent.putExtra("idUser", (int)response.getJSONObject("data").getInt("id"));
                            startActivity(intent);
                        } else {
                            /*********************************************************************************************************
                             * Registrazione non andata a buon fine
                             ********************************************************************************************************/
                            Toast.makeText(getView().getContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), IntroActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){ }
    }

    /*********************************************************************************
     * Processo di salvataggio dei dati relativo al Curatore e alla zona all'interno
     * del dataBase; esso utilizza il metodo createDataDb definito all'interno del DAO
     * Curatore e Zona.
     * In tale computazione prima viene crata la zona associata al curatore, una volta
     * fatto ciò si ottiene l'id della zona appena inserita;
     * Successivamente si crea il visitatore e gli si associa la zona appena creata
     * trammite l'id della zona precedentemente ottenuto
     * @param data container di dati da salvare
     *********************************************************************************/
    private void saveDataInDbCuratore(JSONObject data) {
        try {
            Zona zona = new Zona();

            zona.setDenominazione(data.getString("nomeZona"));
            zona.setTipo(data.getString("tipoZona"));
            zona.setLuogo(data.getString("cittaZona"));
            zona.setOnline(true);

            /*********************************************************************************
             * Metodo di salvataggio di dati vero e prorio riguardante la zona; restituisce
             * la notifica dell'esito della compatuzione che viene catturato dal listener
             *********************************************************************************/
            zona.createDataDb(getView().getContext(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status = response.getBoolean("status");
                        zona.setStatusComputation(status);
                        if (status) {

                            /*********************************************************************************************************
                             * Se i dati sono stati salvati correttamente possiamo procedere al salvataggio del curatore nel database
                             * utilizzando l'id della zona ottenuto dalla precedente computazione
                             ********************************************************************************************************/
                            zona.setDataFromJSON(response.getJSONObject("data"));
                            data.put("zona", zona.getId());
                            CuratoreMuseale curatore = new CuratoreMuseale(data);
                            curatore.setZona(zona.getId());

                            /*********************************************************************************
                             * Metodo di salvataggio di dati vero e  riguardante il Curatore; restituisce la
                             * notifica dell'esito della compatuzione che viene catturato dal listener
                             *********************************************************************************/
                            curatore.createDataDb(getView().getContext(), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Boolean status = response.getBoolean("status");
                                        curatore.setStatusComputation(status);

                                        if (status) {
                                            /*********************************************************************************************************
                                             * Se i dati sono stati salvati correttamente possiamo far accedere l'utente alla mainActivity passando
                                             * l'id dell'utente e il tipo di utente
                                             ********************************************************************************************************/
                                            Toast.makeText(getView().getContext(), "Registrazione avvenuta con successo! Benvenuto", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getView().getContext(), MainActivity.class);
                                            intent.putExtra("typeUser", userType);
                                            intent.putExtra("idUser", (int)response.getJSONObject("data").getInt("id"));
                                            startActivity(intent);
                                        } else {
                                            /*********************************************************************************************************
                                             * Registrazione non andata a buon fine
                                             ********************************************************************************************************/
                                            Toast.makeText(getView().getContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getView().getContext(), IntroActivity.class);
                                            startActivity(intent);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            /*********************************************************************************************************
                             * Registrazione non andata a buon fine
                             ********************************************************************************************************/
                            Toast.makeText(getView().getContext(), "Non è avenuto nessun cambio dati, verifica che i valori siano validi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getView().getContext(), IntroActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch(Exception e){ }
    }

    /****************************************************************************
     * Metodo invocato dall'activity prima del salvataggio dei dati nel database
     * per controllare la correttezza e la completezza dei dati
     * Da qui in poi partiranno tre metodi in sequenza che controlleranno se l'
     * email esiste già; se tali controlli confermano che l'email non è mai stata
     * utilizzata, si procede al controllo della completezza dei dati;
     * Se anche questi ultimi controlli passano, si procederà alla registrazione
     * @param v vista del fragment
     ****************************************************************************/
    public boolean controllData(View v) {
        try {
            controllDataCuratore();
        }catch(Exception e){}
        return true;
    }

    /************************************************************************
     * Metodo invocato dopo aver effettuato tutti i controlli sull'esistenza
     * della mail nelle tre tabelle del dataBase riguardanti i tre attori.
     * Ogniuno dei tre metodi invocati precedentemente registra l'esito della
     * computazione in una variabile temporanea(rispettivamente c1, c2 e c3)
     * se almeno una delle variabili è true vuol dire che la mail è già
     * stata utilizzata da uno dei tre attori
     *************************************************************************/
    public void finalResultControll() throws JSONException {
        if(!c1 && !c2 && !c3){
            /****************************************************************
             * La mail non è mai stata utilizzata, procedi alla registrazione
             ****************************************************************/
            JSONObject data = createDataContainer(getView());

            /****************************************************************
             * Verifico che tutti i campi sono stati riempiti correttamente
             ****************************************************************/
            if(controllDataContainer(data)){

                /*************************************************************
                 * Salvo i dati nel database
                 ************************************************************/
                saveDataInDb(data);
            }else{

                /****************************************************************
                 * Alcuni campi sono vuoti
                 ****************************************************************/
                Toast.makeText(getView().getContext(), "Attenzione! Alcuni campi obbligatori sono vuoti", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /************************************************************************
     * Metodo invocato prima del salvataggio per controllare se i campi
     * sono stati tutti compilati (cioè se i campi sono vuoti)
     * @param data container di dati da salvare
     * @return true se tutti campi sono stati riempiti correttamente
     * @return false se esiste almeno un campo non riempito
     *************************************************************************/
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

    /************************************************************************
     * Metodo invocato prima del salvataggio per controllare se la email
     * inserita dall'utente nel form è già utilizzata da un curatore esistente
     *************************************************************************/
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
                        /*********************************************************************************************************
                         * Email già utilizzata! non si può effettuare la registrazione con l'email inserita nel form
                         ********************************************************************************************************/
                        curatoreMuseale.setStatusComputation(true);
                        Toast.makeText(getView().getContext(), "Email già utilizzata", Toast.LENGTH_SHORT).show();

                        /*********************************************************************************************************
                         * Registro l'esito della computazione con setCuratoreCondition impostando true alla variabile c1
                         ********************************************************************************************************/
                        setCuratoreCondition(true);

                        /*********************************************************************************************************
                         * Controllo se l'email è già stata utilizzata da un visitatore
                         ********************************************************************************************************/
                        controllDataVisitatore();
                    }else{
                        /*********************************************************************************************************
                         * Email NON utilizzata da un curatore esistente; Procedi con i controlli
                         ********************************************************************************************************/
                        curatoreMuseale.setStatusComputation(false);

                        /*********************************************************************************************************
                         * Registro l'esito della computazione con setCuratoreCondition impostando false alla variabile c1
                         ********************************************************************************************************/
                        setCuratoreCondition(false);

                        /*********************************************************************************************************
                         * Controllo se l'email è già stata utilizzata da un visitatore
                         ********************************************************************************************************/
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


    /************************************************************************
     * Metodo invocato prima del salvataggio per controllare se la email
     * inserita dall'utente nel form è già utilizzata da un visitatore esistente
     *************************************************************************/
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
                        /*********************************************************************************************************
                         * Email già utilizzata! non si può effettuare la registrazione con l'email inserita nel form
                         ********************************************************************************************************/
                        visitatore.setStatusComputation(true);
                        Toast.makeText(getView().getContext(), "Email già utilizzata", Toast.LENGTH_SHORT).show();

                        /*********************************************************************************************************
                         * Registro l'esito della computazione con setVisitatoreCondition impostando true alla variabile c2
                         ********************************************************************************************************/
                        setVisitatoreCondition(true);

                        /*********************************************************************************************************
                         * Controllo se l'email è già stata utilizzata da una Guida
                         ********************************************************************************************************/
                        controllDataGuida();
                    }else{
                        /*********************************************************************************************************
                         * Email NON utilizzata da un visitatore esistente; Procedi con i controlli
                         ********************************************************************************************************/
                        visitatore.setStatusComputation(false);

                        /*********************************************************************************************************
                         * Registro l'esito della computazione con setVisitatoreCondition impostando false alla variabile c2
                         ********************************************************************************************************/
                        setVisitatoreCondition(false);

                        /*********************************************************************************************************
                         * Controllo se l'email è già stata utilizzata da una guida
                         ********************************************************************************************************/
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

    /************************************************************************
     * Metodo invocato prima del salvataggio per controllare se la email
     * inserita dall'utente nel form è già utilizzata da una guida esistente
     *************************************************************************/
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
                        /*********************************************************************************************************
                         * Email già utilizzata! non si può effettuare la registrazione con l'email inserita nel form
                         ********************************************************************************************************/
                        guida.setStatusComputation(true);
                        Toast.makeText(getView().getContext(), "Email già utilizzata", Toast.LENGTH_SHORT).show();

                        /*********************************************************************************************************
                         * Registro l'esito della computazione con setGuidaCondition impostando true alla variabile c3
                         ********************************************************************************************************/
                        setGuidaCondition(true);

                        /*********************************************************************************************************
                         * Controllo il risultato finale ottenuto da i tre metodi chiamati precedentemente
                         ********************************************************************************************************/
                        finalResultControll();
                    }else{
                        /*********************************************************************************************************
                         * Email NON utilizzata da una guida esistente; Procedi con i controlli
                         ********************************************************************************************************/
                        guida.setStatusComputation(false);

                        /*********************************************************************************************************
                         * Registro l'esito della computazione con setGuidaCondition impostando false alla variabile c3
                         ********************************************************************************************************/
                        setGuidaCondition(false);

                        /*********************************************************************************************************
                         * Controllo il risultato finale ottenuto da i tre metodi chiamati precedentemente
                         ********************************************************************************************************/
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
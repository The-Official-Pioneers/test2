package it.uniba.pioneers.testtool.home.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Response;
import org.json.JSONObject;
import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.data.users.Visitatore;
import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /****************************************************
         * Variabili utilizzate per i controlli
         ***************************************************/
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        /****************************************************/

        /**********************************************************************************
         * Imposto un listener per intercettare gli eventi sui campi di testo
         **********************************************************************************/
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            /**********************************************************************************
             * Ogni qual volta che il testo cambia, controllo se il valore del campo rispetta i
             * requisiti di correttezza tramite i metodi isUserNameValid e isPasswordValid
             **********************************************************************************/
            @Override
            public void afterTextChanged(Editable s) {
                if (!isUserNameValid(usernameEditText.getText().toString())) {
                    usernameEditText.setError("Inserisci una mail valida");
                    loginButton.setEnabled(false);
                } else if (!isPasswordValid(passwordEditText.getText().toString())) {
                    passwordEditText.setError("Inserisci una password con più di 5 caratteri");
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }
        };
        /**********************************************************************************
         * Aggiungo i listener ai campi di email e password della view
         **********************************************************************************/
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        /**********************************************************************************
         * Aggiungo un listener al bottone relativo al login
         **********************************************************************************/
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                Context context = getView().getContext();

                try {
                    /**********************************************************************************
                     * Controllo se la mail e la password corrispondono ad un account di un curatore
                     **********************************************************************************/
                    tryLoginCuratore(email, password, context);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });
    }

    /**************************************************************************************
     * Metodo che viene invocato per controllare se la mail e la password
     * corrispondono ad un account di un curatore
     * @param email email inserita
     * @param password password inserita
     * @param context contesto
     *************************************************************************************/
    private void tryLoginCuratore(String email, String password, Context context) {
        CuratoreMuseale curatore = new CuratoreMuseale();
        curatore.setEmail(email);
        curatore.setPassword(password);

        /**********************************************************************************
         * Invoco il metodo effettivo per per effettuare il login con i dati a disposizione
         **********************************************************************************/
        curatore.login(context, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    curatore.setStatusComputation(status);
                    if (status) {
                        /************************************************************************************
                         * Esiste un account Curatore associato all'email e la password inseriti dall'utente
                         ************************************************************************************/
                        curatore.setId(response.getJSONObject("data").getInt("id"));
                        Toast.makeText(context, R.string.bentornato_login + response.getJSONObject("data").getString("nome"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("typeUser", "curatore");
                        intent.putExtra("idUser", (int)curatore.getId());

                        /************************************************************************************
                         * Faccio accedere il curatore e lo reindirizzo alla MainActivity
                         ************************************************************************************/
                        context.startActivity(intent);
                    } else {
                        /****************************************************************************************
                         * NON Esiste un account Curatore associato all'email e la password inseriti dall'utente
                         * Controllo se la mail e la password corrispondono ad un account di un Visitatore
                         ***************************************************************************************/
                        tryLoginVisitatore(email, password, context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**************************************************************************************
     * Metodo che viene invocato per controllare se la mail e la password
     * corrispondono ad un account di un visitatore
     * @param email email inserita
     * @param password password inserita
     * @param context contesto
     *************************************************************************************/
    private void tryLoginVisitatore(String email, String password, Context context) {
        Visitatore visitatore = new Visitatore();
        visitatore.setEmail(email);
        visitatore.setPassword(password);

        /**********************************************************************************
         * Invoco il metodo effettivo per per effettuare il login con i dati a disposizione
         **********************************************************************************/
        visitatore.login(context, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    visitatore.setStatusComputation(status);
                    if (status) {

                        /************************************************************************************
                         * Esiste un account Visitatore associato all'email e la password inseriti dall'utente
                         ************************************************************************************/
                        visitatore.setId(response.getJSONObject("data").getInt("id"));
                        Toast.makeText(context, R.string.bentornato_login + response.getJSONObject("data").getString("nome"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("typeUser", "visitatore");
                        intent.putExtra("idUser", (int)visitatore.getId());

                        /************************************************************************************
                         * Faccio accedere il visitatore e lo reindirizzo alla MainActivity
                         ************************************************************************************/
                        context.startActivity(intent);
                    } else {
                        /****************************************************************************************
                         * NON Esiste un account Visitatore associato all'email e la password inseriti dall'utente
                         * Controllo se la mail e la password corrispondono ad un account di una Guida
                         ***************************************************************************************/
                        tryLoginGuida(email, password, context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**************************************************************************************
     * Metodo che viene invocato per controllare se la mail e la password
     * corrispondono ad un account di una Guida
     * @param email email inserita
     * @param password password inserita
     * @param context contesto
     *************************************************************************************/
    private void tryLoginGuida(String email, String password, Context context) {
        Guida guida = new Guida();
        guida.setEmail(email);
        guida.setPassword(password);

        /**********************************************************************************
         * Invoco il metodo effettivo per per effettuare il login con i dati a disposizione
         **********************************************************************************/
        guida.login(context, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Boolean status = response.getBoolean("status");
                    guida.setStatusComputation(status);
                    if (status) {

                        /************************************************************************************
                         * Esiste un account Guida associato all'email e la password inseriti dall'utente
                         ************************************************************************************/
                        guida.setId(response.getJSONObject("data").getInt("id"));
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("typeUser", "guida");
                        intent.putExtra("idUser", (int)guida.getId());
                        /************************************************************************************
                         * Faccio accedere la Guida e lo reindirizzo alla MainActivity
                         ************************************************************************************/
                        context.startActivity(intent);
                        Toast.makeText(context, R.string.bentornato_login + response.getJSONObject("data").getString("nome"), Toast.LENGTH_SHORT).show();
                    } else {
                        /*******************************************************************************
                         * NON Esiste un account associato all'email e la password inseriti dall'utente
                         *******************************************************************************/
                        Toast.makeText(context, R.string.cambio_dati_no_validi, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**************************************************************************************
     * Metodo che viene invocato per controllare se l'email inserita dall'
     * utente rispettano i requisiti di correttezza
     * @param username email inserita
     * @return true se l'email è corretta oppure false se l'email è scorretta
     *************************************************************************************/
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@") && (username.contains(".it") || username.contains(".com") || username.contains(".org") || username.contains(".net"))) {
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
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
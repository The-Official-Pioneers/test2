package it.uniba.pioneers.testtool.home.data;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

import java.io.IOException;

import it.uniba.pioneers.data.users.CuratoreMuseale;
import it.uniba.pioneers.data.users.Guida;
import it.uniba.pioneers.data.users.Visitatore;
import it.uniba.pioneers.testtool.MainActivity;
import it.uniba.pioneers.testtool.R;
import it.uniba.pioneers.testtool.home.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String email, String password, Context context) {

        try {
            CuratoreMuseale curatore = new CuratoreMuseale();
            curatore.setEmail(email);
            curatore.setPassword(password);
            curatore.login(context, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Boolean status = response.getBoolean("status");
                        curatore.setStatusComputation(status);
                        if (status) {
                            curatore.setId(response.getJSONObject("data").getInt("id"));

                            Toast.makeText(context, R.string.bentornato_login + response.getJSONObject("data").getString("nome"), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("typeUser", "curatore");
                            intent.putExtra("idUser", curatore.getId());
                            context.startActivity(intent);
                        } else {
                            Visitatore visitatore = new Visitatore();
                            visitatore.setEmail(email);
                            visitatore.setPassword(password);
                            visitatore.login(context, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Boolean status = response.getBoolean("status");
                                        visitatore.setStatusComputation(status);
                                        if (status) {
                                            visitatore.setId(response.getJSONObject("data").getInt("id"));

                                            Toast.makeText(context, R.string.bentornato_login + response.getJSONObject("data").getString("nome"), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.putExtra("typeUser", "visitatore");
                                            intent.putExtra("idUser", visitatore.getId());
                                            context.startActivity(intent);
                                        } else {
                                            Guida guida = new Guida();
                                            guida.setEmail(email);
                                            guida.setPassword(password);
                                            guida.login(context, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        Boolean status = response.getBoolean("status");
                                                        guida.setStatusComputation(status);
                                                        if (status) {
                                                            guida.setId(response.getJSONObject("data").getInt("id"));
                                                            Intent intent = new Intent(context, MainActivity.class);
                                                            intent.putExtra("typeUser", "guida");
                                                            intent.putExtra("idUser", guida.getId());
                                                            context.startActivity(intent);

                                                            Toast.makeText(context, R.string.bentornato_login + response.getJSONObject("data").getString("nome"), Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            Toast.makeText(context, R.string.cambio_dati_no_validi, Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println(curatore.getId());
            LoggedInUser userLogged = new LoggedInUser("10","Dino", "curatore");
            return new Result.Success<>(userLogged);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
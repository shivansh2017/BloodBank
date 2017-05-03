package com.pushpam.materialdesignblooddonor.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pushpam.materialdesignblooddonor.R;
import com.pushpam.materialdesignblooddonor.helper.SessionManager;
import com.pushpam.materialdesignblooddonor.network.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
   static public EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    boolean check;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if(new CheckInternetConnection(LoginActivity.this).isNetworkAvailable()) {
                    // Check for empty data in the form
                    if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        // error handling here
                        inputEmail.setError("Email Seems To be InCorrect");
                    }
                    if (password.isEmpty() || password.length() < 4 || password.length() > 16) {
                        // error handling here
                        inputPassword.setError("Password must be  atleast 4 characters and maximum 15");
                    } else {
                        //   checkLogin(email, password);
                        new ExtendAsc(email, password, LoginActivity.this).execute();
                        pDialog.setMessage("Please Wait , Logging in ...");
                        showDialog();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if(new CheckInternetConnection(LoginActivity.this).isNetworkAvailable()){

                }else {
                    Toast.makeText(LoginActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }



    public void LinkControl(final String email, final String password, String serverreturninfo){
        hideDialog();
        String success =serverreturninfo;
        String name =email;
        String message ="Wrong Credentials";
        if (success.equals("\"Login success\"")) {
            //create session
            session.setUserEmail(email);
            session.setUserName(name);
            session.setLogin(true);
            //user logged in
            Toast.makeText(getApplicationContext(), "Welcome " + session.getUserName(), Toast.LENGTH_SHORT).show();
            // Launch login activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Error in login. Get the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

class ExtendAsc extends AsyncTask<String,Void, String> {
    String email, password;
    String sa;
    String abc="";
    URL url;
    HttpURLConnection httpURLConnection;
    BufferedOutputStream outputStreamWriter;
    LoginActivity loginActivity;

    ExtendAsc(String email, String password, LoginActivity loginActivity){
        this.email=email;
        this.password=password;
        this.loginActivity=loginActivity;
    }
    @Override
    protected String doInBackground(String... strings) {





        try {
            JSONObject myJson=new JSONObject();
            myJson.put("Email",email);
            myJson.put("Password",password);

            url=new URL(AppConfig.URL_LOGIN);
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            outputStreamWriter=new BufferedOutputStream(httpURLConnection.getOutputStream());
            outputStreamWriter.write(myJson.toString().getBytes());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            if ((sa=bufferedReader.readLine())!=null){
                abc+=sa;
                System.out.println(sa);
                if(abc.equals("\"Login success\""))
                System.out.println("hey abc "+abc);

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return abc;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        loginActivity.LinkControl(email,password,s);

    }
}


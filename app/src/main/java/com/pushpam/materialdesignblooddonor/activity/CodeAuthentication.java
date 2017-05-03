package com.pushpam.materialdesignblooddonor.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pushpam.materialdesignblooddonor.R;
import com.pushpam.materialdesignblooddonor.helper.SessionManager;

public class CodeAuthentication extends Activity {
    int codeData;
    Button code_authentication;
    EditText input_code;
    SessionManager sessionManager;

    public void onCreate(Bundle savedInstanceState)
    {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.code_authentication_activity);

                sessionManager=new SessionManager(this);

                input_code=(EditText)findViewById(R.id.code_auth);
                code_authentication=(Button)findViewById(R.id.login_auth_button);

                code_authentication.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String codeValue=input_code.getText().toString().trim();
                        codeData= Integer.parseInt(codeValue);
                        checkUserAuthentication();
                    }
                });
    }

    public void checkUserAuthentication(){
       //showing the dialog box
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CodeAuthentication.this);
        alertDialog.setTitle("Confirmation");
        if (codeData== UserCode.getCode()){
            alertDialog.setCancelable(false);
            alertDialog.setMessage("You Have Successfully Registered!!");
            alertDialog.setIcon(R.drawable.logo);
            alertDialog.setPositiveButton("Login Me", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //inserting the user email into edit text for login
                    LoginActivity.inputEmail.setText(sessionManager.getUserEmail());
                    Intent intent = new Intent(CodeAuthentication.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            alertDialog.create();
            alertDialog.show();
        }
        else{
            alertDialog.setMessage("Oops You have Entered a Wrong Code!!");
            alertDialog.setIcon(R.drawable.logo);
            alertDialog.setPositiveButton("Try Agin", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing

                }
            });
            alertDialog.create();
            alertDialog.show();
        }
    }
}

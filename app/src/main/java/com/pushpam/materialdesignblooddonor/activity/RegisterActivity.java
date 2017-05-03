package com.pushpam.materialdesignblooddonor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.pushpam.materialdesignblooddonor.R;
import com.pushpam.materialdesignblooddonor.helper.SessionManager;
import com.pushpam.materialdesignblooddonor.model.BloodGroupSelectorFilter;
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

public class RegisterActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputphone;
    private EditText inputAgeText;
 //   public static TextView inputAgeText;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Spinner spinnerlastDonation, spinnerBlood;
    private String genderData;
    public static String ageData;
    private AutoCompleteTextView cityAutoText;
    private RadioGroup genderRadioGroup;
    private RadioButton genderMaleRadioButton, genderFemaleRadioButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing the controls
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        inputAgeText = (EditText) findViewById(R.id.age);
        inputphone = (EditText) findViewById(R.id.phone);
        spinnerlastDonation = (Spinner) findViewById(R.id.lastdonate_spinner);
        spinnerBlood = (Spinner) findViewById(R.id.blood_spinner);
        cityAutoText = (AutoCompleteTextView) findViewById(R.id.city);
        genderRadioGroup = (RadioGroup) findViewById(R.id.radio_gender_group);
        genderMaleRadioButton = (RadioButton) findViewById(R.id.radio_male);
        genderFemaleRadioButton = (RadioButton) findViewById(R.id.radio_female);


        //setting spinners
        ArrayAdapter<CharSequence> adapter_spinner_donation = ArrayAdapter.createFromResource(this,
                R.array.last_duration_array, android.R.layout.simple_spinner_item);
        adapter_spinner_donation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlastDonation.setAdapter(adapter_spinner_donation);

        ArrayAdapter<CharSequence> adapter_spinner_blood = ArrayAdapter.createFromResource(this,
                R.array.blood_array, android.R.layout.simple_spinner_item);
        adapter_spinner_blood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBlood.setAdapter(adapter_spinner_blood);

        //setting the auto complete view
        final String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter_auto_city = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        cityAutoText.setAdapter(adapter_auto_city);

        //radio settings and listener
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int id = ((RadioGroup) findViewById(R.id.radio_gender_group)).getCheckedRadioButtonId();

                if (id == genderMaleRadioButton.getId()) {
                    genderData = "male";
                } else if (id == genderFemaleRadioButton.getId()) {
                    genderData = "female";
                } else {
                    genderData = "";
                }
                return;


            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nameData = inputFullName.getText().toString().trim();
                String emailData = inputEmail.getText().toString().trim();
                String passwordData = inputPassword.getText().toString().trim();
                String phoneData = inputphone.getText().toString().trim();
                String cityData = cityAutoText.getText().toString();
                String ageData=inputAgeText.getText().toString().trim();
                String bloodData = String.valueOf(spinnerBlood.getSelectedItem());
                String lastDonationData = String.valueOf(spinnerlastDonation.getSelectedItem());

                //getting blood group from spinners and filtering it
                BloodGroupSelectorFilter.setBloodgroup(bloodData);
                bloodData=BloodGroupSelectorFilter.getBloodgroup();


                //checking for empty fields
                int flagCheck = 0;
                if (nameData.isEmpty() || nameData.length() <= 2) {
                    inputFullName.setError("Name is too Short");
                    flagCheck = 1;
                }
                if (emailData.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailData).matches()) {
                    inputEmail.setError("Email Invalid");
                    flagCheck = 1;
                }
                if (passwordData.isEmpty() || passwordData.length() < 4 || passwordData.length() > 16) {
                    inputPassword.setError("Password must be  atleast 4 characters and maximum 15");
                    flagCheck = 1;
                }
                if (phoneData.isEmpty() || phoneData.length() < 10) {
                    inputphone.setError("Enter a valid Phone Number");
                    flagCheck = 1;
                }
               if (ageData.isEmpty()) {

                    inputAgeText.setError("Enter your Age");
                    flagCheck = 1;
                }
                if (cityData.isEmpty() || cityData.length() <= 2) {
                    cityAutoText.setError("PLease Enter a city you belong");
                    flagCheck = 1;
                }
                if (bloodData.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Please Select your Blood Group", Toast.LENGTH_SHORT).show();
                    flagCheck = 1;
                }
                if (lastDonationData.isEmpty()) {

                    Toast.makeText(RegisterActivity.this, "Please Select your Blood Group", Toast.LENGTH_SHORT).show();
                    flagCheck = 1;
                } else if (flagCheck == 0) {
                    if(new CheckInternetConnection(RegisterActivity.this).isNetworkAvailable()) {
                        new ExtendAscReg(nameData, emailData, passwordData, phoneData, cityData, ageData,
                                genderData, bloodData, lastDonationData, RegisterActivity.this).execute();
                        pDialog.setMessage("Registering User...");
                        showDialog();
                    }else {
                        Toast.makeText(RegisterActivity.this,"No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(RegisterActivity.this, "Please Check Your Input Data", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

     /*   inputAgeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });*/



    }



    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    public void registerUser(final String name, final String email, String r) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        hideDialog();

        if (r.equals("\"Data Updated SuceessFul\"")) {
            //user successfully registered
            session.setUserEmail(email);
            session.setUserName(name);
            Toast.makeText(getApplicationContext(), "Thank you , please Login to continue!"
                    , Toast.LENGTH_LONG).show();
            // Launch login activity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else if(r.equals("\"allready register\"")){
            Toast.makeText(getApplicationContext(),
                    "Email Allready Register", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(),
                    "Connection Problem", Toast.LENGTH_LONG).show();
        }
       /* StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    //boolean error = jObj.getBoolean("error");
                    String success = jObj.getString("success");
                    if (jObj.getString("success").equals("true")) {
                        //user successfully registered
                        session.setUserEmail(email);
                        session.setUserName(name);
                        Toast.makeText(getApplicationContext(), "Thank you , please Login to continue!"
                                , Toast.LENGTH_LONG).show();
                        // Launch login activity
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                "Connection Problem", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Can not connect to server", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);
                params.put("city", city);
                params.put("age", age);
                params.put("gender", gender);
                params.put("bloodgroup", bloodGroup);
                params.put("lastdonate", lastDonate);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);*/


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

class ExtendAscReg extends AsyncTask<String,Void, String> {
    String email,password,name,phone,city,age,gender,bloodGroup,lastDonate;
    String sa;
    String abc="";
    URL url;
    HttpURLConnection httpURLConnection;
    BufferedOutputStream outputStreamWriter;
    RegisterActivity registerActivity;

    ExtendAscReg(final String name, final String email,
                 final String password, final String phone, final String city,
                 final String age, final String gender, final String bloodGroup,
                 final String lastDonate, RegisterActivity registerActivity){
        this.name=name;
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.city=city;
        this.age=age;
        this.gender=gender;
        this.bloodGroup=bloodGroup;
        this.lastDonate=lastDonate;
        this.registerActivity=registerActivity;
    }
    @Override
    protected String doInBackground(String... strings) {





        try {
            JSONObject myJson=new JSONObject();
            myJson.put("Name",name);
            myJson.put("Email",email);
            myJson.put("Password",password);
            myJson.put("Phone",phone);
            myJson.put("City",city);
            myJson.put("Age",age);
            myJson.put("Gender",gender);
            myJson.put("BloodGroup",bloodGroup);
            myJson.put("LastDonate",lastDonate);

            //  String logindata= URLEncoder.encode(myJson.toString(), "UTF-8");

            url=new URL(AppConfig.URL_REGISTER);
            httpURLConnection= (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");

            outputStreamWriter=new BufferedOutputStream(httpURLConnection.getOutputStream());
            outputStreamWriter.write(myJson.toString().getBytes());
            outputStreamWriter.flush();
            outputStreamWriter.close();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            //s = bufferedReader.readLine();
            if ((sa=bufferedReader.readLine())!=null){
                abc+=sa;
                System.out.println(sa);
               if(abc.equals("\"Data Updated SuceessFul\""))
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
        registerActivity.registerUser(email,password,s);

    }
}


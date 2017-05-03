package com.pushpam.materialdesignblooddonor.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pushpam.materialdesignblooddonor.R;
import com.pushpam.materialdesignblooddonor.helper.SessionManager;
import com.pushpam.materialdesignblooddonor.network.AppConfig;
import com.pushpam.materialdesignblooddonor.network.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class FriendsFragment extends Fragment {

    private static String TAG = FriendsFragment.class.getSimpleName();
    String switch_status;
    TextView status, msg;
    Switch aSwitch;
    SessionManager session,sessiont;
    private ProgressDialog pDialog;
    int o=9;
    ProgressDialog dialog;
    public static String DONOR_STATUS;
    public static int MAIN_ASYN_CHECK=0;
    public static int TOGGLE_ASYN_CHECK=1;
    public static int val=1;
    static int inter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        val=1;
    }

    @Override
    public void onPause() {
        super.onPause();
        val=1;
    }

    @Override
    public void onResume() {
        super.onResume();
        val=1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        status = (TextView) view.findViewById(R.id.switchStatus);
        msg = (TextView) view.findViewById(R.id.switch_msg);
        aSwitch = (Switch) view.findViewById(R.id.toggle_switch);

        // Session manager
        session = new SessionManager(getActivity());
       String emailid = session.getUserEmail();
        System.out.println("your email id is:      "+ emailid);

        if(new CheckInternetConnection(getActivity()).isNetworkAvailable()) {

            new AsyncProfile(FriendsFragment.this, emailid, "3", MAIN_ASYN_CHECK).execute();
            showpDialog();
        }else{
            Toast.makeText(getContext(),"No Internent Connection",Toast.LENGTH_LONG).show();
        }


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
if(new CheckInternetConnection(getActivity()).isNetworkAvailable()) {

    Boolean mySwitch = aSwitch.isChecked();
    String switch_value, email;
    email = session.getUserEmail();

    if(inter==9){
        inter=0;
        return;
    }
    if (mySwitch == true && val == 2) {

        val = 1;
        return;
    }
    if (mySwitch == true && val == 1) {
        new AsyncProfile(FriendsFragment.this, email, "1", TOGGLE_ASYN_CHECK).execute();
        showpDialog();
        System.out.println("switch:my  " + mySwitch);
    }
    if (mySwitch == false && val == 2) {
        val = 1;
        return;
    }
    if (mySwitch == false && val == 1) {

        new AsyncProfile(FriendsFragment.this, email, "0", TOGGLE_ASYN_CHECK).execute();
        showpDialog();
        System.out.println("switch:my  " + mySwitch);

    }



}else {
    inter=9;
    Toast.makeText(getContext(),"No Internent Connection",Toast.LENGTH_LONG).show();
    msg.setText("no internet connection please check your internet connection");
    status.setText("OFF");
    aSwitch.setChecked(false);
    inter=0;

}



            }
        });
        return view;
    }
public void m2(String responsetog){
    hidepDialog();
    if(responsetog!=null) {
        if (responsetog.equals("\"Donor Mode ON\"")) {

            Toast.makeText(getContext(), "Donor Mode ON", Toast.LENGTH_LONG).show();
            msg.setText("Now You Are In Donor Mode if you want your profile as Needer or hide form every one switch off toggle");
            status.setText("ON");
        } else if (responsetog.equals("\"Donor Mode off\"")) {
            msg.setText("Now You Are In Needer Mode if you want your profile as Blood Donor switch ON toggle");
            status.setText("OFF");
            Toast.makeText(getContext(), "Donor Mode OFF", Toast.LENGTH_LONG).show();
        } else {
            msg.setText("Reteriving Profile Contains Error");
            status.setText("OFF");
            Toast.makeText(getContext(), "Reteriving Profile Contains Error", Toast.LENGTH_LONG).show();
        }
    }else {

        msg.setText("Server Under Maintenance");
        status.setText("OFF");
        Toast.makeText(getContext(), "Server Under Maintenance", Toast.LENGTH_LONG).show();
        aSwitch.setChecked(false);
    }
}
   public void m1(String response) {
       hidepDialog();
       DONOR_STATUS = response;
       if (response != null) {
           if (DONOR_STATUS.equals("\"1\"")) {
               val = 2;
               aSwitch.setChecked(true);
               msg.setText("Now You Are In Donor Mode if you want your profile as Needer or hide form every one switch off toggle");
               status.setText("ON");
               System.out.println("Donnar Mode " + DONOR_STATUS);

           } else if (DONOR_STATUS.equals("\"0\"")) {
               val = 2;
               aSwitch.setChecked(false);
               val = 1;
               msg.setText("Now You Are In Needer Mode if you want your profile as Blood Donor switch ON toggle");
               status.setText("OFF");
               System.out.println("Donnar Mode " + DONOR_STATUS);

           }else {
               msg.setText("Reteriving Profile Contains Error");
               status.setText("OFF");
               Toast.makeText(getContext(),"Reteriving Profile Contains Error",Toast.LENGTH_LONG).show();
           }

       }
       else{
           msg.setText("Serever Under Maintenance Try After Some Time");
           status.setText("OFF");
           Toast.makeText(getContext(),"Serever Under Maintenance Try After Some Time",Toast.LENGTH_LONG).show();
       }
   }



    private void showpDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loding Profile...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hidepDialog() {

            dialog.dismiss();
    }


}//class end

class AsyncProfile extends AsyncTask<String,Void,String>{
    FriendsFragment friendsFragment;
    HttpURLConnection httpURLConnection;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    URL url;
    String email,toggleValue;
    String valueComingFromServer;
    BufferedOutputStream outputStreamWriter;
    static  int Check_CALL;

    AsyncProfile(FriendsFragment friendsFragment,String email,String toggleValue,int checkcall){
        this.friendsFragment=friendsFragment;
        this.email=email;
        this.toggleValue=toggleValue;
        this.Check_CALL=checkcall;
    }
    @Override
    protected String doInBackground(String... params) {
        String response = null;


        try{
            JSONObject myJson=new JSONObject();
            myJson.put("Email",email);
            myJson.put("Id",toggleValue);

           url =new URL("http://www.myworkecho.com/blooddonnerapi.svc/profile");
          //  System.out.println(AppConfig.URL_PROFILE+"/"+email+"/"+toggleValue);
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                outputStreamWriter=new BufferedOutputStream(httpURLConnection.getOutputStream());
                outputStreamWriter.write(myJson.toString().getBytes());
                outputStreamWriter.flush();
                outputStreamWriter.close();

            }catch (Exception e){
                System.out.println("erro open  "+e.getMessage());
            }

try {
   bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
    if((response=bufferedReader.readLine())!=null){
        System.out.println(" buffer value  "+response);
    }
}catch(Exception e){
                System.out.println("error buffer "+e.getMessage());
            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("catch error");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (Check_CALL==0) {
            System.out.println("CHECK Call0  "+s);
            friendsFragment.m1(s);
        }else if(Check_CALL==1){
            System.out.println("CHECK Call1  "+s);
                friendsFragment.m2(s);
        }
    }




}


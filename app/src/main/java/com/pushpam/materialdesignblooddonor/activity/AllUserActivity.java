package com.pushpam.materialdesignblooddonor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.pushpam.materialdesignblooddonor.R;
import com.pushpam.materialdesignblooddonor.network.AppConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AllUserActivity extends ActionBarActivity {
ProgressDialog dialog;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

if(new CheckInternetConnection(AllUserActivity.this).isNetworkAvailable()) {

    new ExtendAsyAllUser(this).execute();
    dialog = new ProgressDialog(this);
    dialog.setMessage("Loding Data...");
    dialog.setCancelable(false);
    // dialog.setInverseBackgroundForced(false);
    dialog.show();
}else {
    Intent intent = new Intent(AllUserActivity.this, ErrorPageShowWhenNoInternet.class);
    startActivity(intent);
    finish();

}

    }

    public void getAllDataWhichFillInList(ArrayList<Holder> arrayList){
        Holder holder;

        dialog.hide();

if(arrayList.size()>=0) {
    listView = (ListView) findViewById(R.id.tabitem_list_view);
    listView.setAdapter(new ExtendBaseAdapterForFillingDataInTabItemListView(this, arrayList));
}else {
    Toast.makeText(AllUserActivity.this,"Server Is Under maintenance", Toast.LENGTH_LONG).show();
}

    }
}

class ExtendAsyAllUser extends AsyncTask<String,Void,ArrayList<Holder>> {
    AllUserActivity allUserActivity;
    HttpURLConnection urlConnection;
    ArrayList<Holder> arrayList;

    ExtendAsyAllUser(AllUserActivity allUserActivity){
        this.allUserActivity=allUserActivity;
        arrayList=new ArrayList<Holder>();
    }
    @Override
    protected ArrayList<Holder> doInBackground(String... strings) {
        String response = null;
    try {
            URL url = new URL(AppConfig.URL_GET_USER);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Connection", "Keep-Alive");

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        response = new ChangeInputStreamToString(in).converter();

        JSONArray jsonArray = new JSONArray(response);

        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject= jsonArray.getJSONObject(i);


            String phone=jsonObject.getString("Phone");
            String name=jsonObject.getString("Name");
            String bloodgroup=jsonObject.getString("BloodGroup");
            String city=jsonObject.getString("City");

            arrayList.add(new Holder(name,phone,bloodgroup,city));

        }


    }catch (Exception e){

    }
        return arrayList;

    }

    @Override
    protected void onPostExecute(ArrayList<Holder> arrayList) {
        super.onPostExecute(arrayList);
        allUserActivity.getAllDataWhichFillInList(arrayList);
    }




}

class Holder{
    String phone;
    String name;
    String bloodgroup;
    String city;
    Holder(String name, String phone, String bloodgroup, String city){
        this.name=name;
        this.phone=phone;
        this.bloodgroup=bloodgroup;
        this.city=city;

    }
}


class ChangeInputStreamToString {
    InputStream inputStream;
    ChangeInputStreamToString(InputStream inputStream){
        this.inputStream=inputStream;
    }

    public String converter(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                inputStream.close();

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("main Alldata: "+sb.toString());
        return sb.toString();

    }

}

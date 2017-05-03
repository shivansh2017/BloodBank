package com.pushpam.materialdesignblooddonor.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.pushpam.materialdesignblooddonor.R;
import com.pushpam.materialdesignblooddonor.adapter.CustomListAdapter;
import com.pushpam.materialdesignblooddonor.helper.SessionManager;
import com.pushpam.materialdesignblooddonor.model.UserData;
import com.pushpam.materialdesignblooddonor.network.AppConfig;
import com.pushpam.materialdesignblooddonor.network.AppController;
import com.pushpam.materialdesignblooddonor.network.CustomRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchResultsActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    SessionManager sessionManager;
    private ProgressDialog pDialog;
    private List<UserData> dataList = new ArrayList<UserData>();
    private ListView listView;
    private CustomListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        sessionManager = new SessionManager(this);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, dataList);
        listView.setAdapter(adapter);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);

            if (query != null) {
                pDialog = new ProgressDialog(this);
                // Showing progress dialog before making http request
                pDialog.setMessage("Loading Data...");
                pDialog.show();

                // HashMap<String, String> params = new HashMap<String, String>();
                //params.put("city", query);


                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, AppConfig.URL_GET_USER, null, new Response.Listener<JSONObject>() {

                    /* JsonArrayRequest movieReq = new JsonArrayRequest(url,
                             new Response.Listener<JSONArray>() {
                                 @Override*/
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        try {
                            //JSONArray ja = response.getJSONArray("products");
                            // JSONArray ja = response.getJSONArray("");
                            // JSONObject jsonRootObject = new JSONObject("");
                            JSONArray ja = response.getJSONArray("users");
                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject jsonObject = ja.getJSONObject(i);
                                UserData data = new UserData();
                                data.setName(jsonObject.getString("name"));
                                data.setBloodGroup("Blood Group : " +jsonObject.getString("bloodgroup"));
                                data.setCity("City : " + jsonObject.getString("city"));
                                data.setPhone(jsonObject.getString("phone"));
                                data.setAge(jsonObject.getString("age"));
                                data.setEmailUser(jsonObject.getString("email"));


                                // adding movie to movies array
                                dataList.add(data);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage()+" Check Your Netwrok Connection", Toast.LENGTH_LONG).show();
                        hidePDialog();

                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("city", query);
                        return params;
                    }
                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsObjRequest);
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}


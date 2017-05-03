package com.pushpam.materialdesignblooddonor.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.pushpam.materialdesignblooddonor.R;
import com.pushpam.materialdesignblooddonor.helper.SessionManager;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {

    //private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        //session management
        session = new SessionManager(getApplicationContext());

        // display the first navigation drawer view on app launch
        displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
      /*  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //clearing the stored sessions
            session.setLogin(false);
            Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

      /*  if (id == R.id.search) {
            return true;
        }*/

        if (id==R.id.alluser){

                Intent intent = new Intent(MainActivity.this, AllUserActivity.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new FriendsFragment();
                title = getString(R.string.title_friends);
                break;
            case 2:
                // fragment = new ShareFragment();
                //title = getString(R.string.title_share);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download Blood Bank Android App"
                        + " : " + "https://play.google.com/store/apps/details?id="+this.getPackageName());
                startActivity(Intent.createChooser(shareIntent, "Donate Someone Needs You"));
                break;
            case 3:
                // fragment = new RateFragment();
                //title = getString(R.string.title_rate);
                //break;
             //   String url = "https://play.google.com/store?hl=en";
              //  Intent i = new Intent(Intent.ACTION_VIEW);
              //  i.setData(Uri.parse(url));
               // startActivity(i);
                Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try
                {
                    startActivity(myAppLinkToMarket);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(this, " Sorry, Not able to open!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                fragment = new AboutFragment();
                title = getString(R.string.title_about);
                break;
            default:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }
}

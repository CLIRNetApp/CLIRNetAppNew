package app.clirnet.com.clirnetapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.clirnet.com.clirnetapp.R;
import app.clirnet.com.clirnetapp.Utility.ConnectionDetector;
import app.clirnet.com.clirnetapp.Utility.SyncDataService;
import app.clirnet.com.clirnetapp.app.AppConfig;
import app.clirnet.com.clirnetapp.app.AppController;
import app.clirnet.com.clirnetapp.fragments.ConsultationLogFragment;
import app.clirnet.com.clirnetapp.fragments.HomeFragment;
import app.clirnet.com.clirnetapp.fragments.PoHistoryFragment;
import app.clirnet.com.clirnetapp.fragments.ReportFragment;
import app.clirnet.com.clirnetapp.helper.SQLController;
import app.clirnet.com.clirnetapp.helper.SQLiteHandler;
import app.clirnet.com.clirnetapp.helper.SessionManager;
import app.clirnet.com.clirnetapp.models.CallAsynOnce;
import app.clirnet.com.clirnetapp.models.LoginModel;
import app.clirnet.com.clirnetapp.models.RegistrationModel;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, ConsultationLogFragment.OnFragmentInteractionListener, PoHistoryFragment.OnFragmentInteractionListener
        , ReportFragment.OnFragmentInteractionListener {

    private static final String TAG = "Login";
    private FragmentManager fragmentManager;


    private static final String msg = "Android : ";
    private SQLiteHandler dbController;
    private SQLController sqlController;

    private ProgressDialog pDialog;
    private String docName;
    private String emailId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //  SharedPreferences pref = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);


        View hView = navigationView.getHeaderView(0);
        ImageView imgvw = (ImageView) hView.findViewById(R.id.imageView);
        TextView u_name = (TextView) hView.findViewById(R.id.user_name);
        TextView email = (TextView) hView.findViewById(R.id.email);
        imgvw.setImageResource(R.drawable.emp_img);



        dbController = new SQLiteHandler(NavigationActivity.this);

        //this will start the background service which sends data to server on 30 min interval
        Intent serviceIntent = new Intent(this, SyncDataService.class);
        startService(serviceIntent);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

//chk internet connection
        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        Cursor cursor = null;
        try {

            sqlController = new SQLController(NavigationActivity.this);
            sqlController.open();

            docName = sqlController.getDocdoctorName();

            emailId = sqlController.getDocdoctorEmail();

            u_name.setText("Welcome,  Dr. " + docName);
            email.setText(emailId);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }








      /*  fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();*/

        Fragment fragment = getSupportFragmentManager().
                findFragmentById(R.id.flContent);
        if ( fragment == null ) {
            fragment = new HomeFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.flContent, fragment, "SOME_TAG_IF_YOU_WANT_TO_REFERENCE_YOUR_FRAGMENT_LATER")
                    .commit();
        }
        else{
            Log.e("fragment","Fragment is allready opened");
        }

        Glide.get(NavigationActivity.this).clearMemory();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //This will used to navigate user from one menu to another
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        selectDrawerItem(item);
        return true;
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.nav_po:
                fragment = new HomeFragment();
                break;

            case R.id.nav_consultLog:
                fragment = new ConsultationLogFragment();
                break;

            case R.id.nav_pohistory:
                fragment = new PoHistoryFragment();
                break;


            case R.id.nav_manage:

                fragment = new HomeFragment();
                break;

            case R.id.nav_logout:

                //  db.deleteUsers();

                // Launching the login activity
                goToLoginActivity();

                fragment = new HomeFragment();
                break;

        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        drawer.closeDrawers();

    }

    private void goToLoginActivity() {
        Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
      //  System.exit(0);
    }

    private void logoutUser() {


        //db.deleteUsers();

        // Launching the login activity
        goToLoginActivity();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    /** Called just before the activity is destroyed. */
    /** Called when the activity is no longer visible. */
    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");

        System.gc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
        // session.setLogin(false);
        //Close the all database connection opened here 31/10/2008 By. Ashish
        if(sqlController != null){
            sqlController = null;
        }
        if(dbController != null){
            dbController=null;
        }
        if(fragmentManager != null){
            fragmentManager=null;
        }

        System.gc();
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
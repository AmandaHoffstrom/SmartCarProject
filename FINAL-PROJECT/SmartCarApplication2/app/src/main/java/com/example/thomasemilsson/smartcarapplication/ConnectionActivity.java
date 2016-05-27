package com.example.thomasemilsson.smartcarapplication;

/**
 * Created by thomasemilsson on 5/12/16.
 * TODO: Add description to class and all(I can help here) public methods
 */

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// Nav Drawer imports, might be duplicates. Will tidy up here on Sunday

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.transition.Slide;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Set;

public class ConnectionActivity extends Activity
                    // implements NavigationView.OnNavigationIntemSelectedListener
{


    ListView deviceList;
    TextView textEnterIP;
    Button connectButton;

    boolean display = true;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;

    public static String EXTRA_ADDRESS = "device_address";

    
    /**
     * Navigation Drawer is created in onCreate...
     */
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        
    
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // initialize variable views
        deviceList = (ListView) findViewById(R.id.deviceList);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        textEnterIP = (TextView) findViewById(R.id.textEnterIP);
        connectButton = (Button) findViewById(R.id.buttonConnect);


        // Default IP Hostname
        //textEnterIP.setText("192.168.43.220");
        textEnterIP.setText("172.20.10.6");
        //textEnterIP.setText("192.168.43.140");

        // Check for existing bluetooth connection
        if (myBluetooth == null) {
            // Display error message
            message("Bluetooth Device is not Available");
            finish();
        } else if (!myBluetooth.isEnabled()) {
            Intent turnBluetoothOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivityForResult(turnBluetoothOn, 1);
        }

        if (display) {
            showDeviceList();
        }


        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Set Singleton for wifiStatus to true
                Properties.getInstance().wifiStatus = true;

                // Get the text from the textField and set the IP Singleton to that
                IP.getInstance().activeIP = textEnterIP.getText().toString();

             //   Intent intent = new Intent(ConnectionActivity.this, BasicActivity.class);
                Intent intent = new Intent(ConnectionActivity.this, ConnectionActivity.class);

                intent.putExtra(EXTRA_ADDRESS, "==");
                // Change Activity
                //intent.putExtra(EXTRA_ADDRESS, address);

                startActivity(intent);
            }
        });
    }
    
    


    // Go through all paired Devices, and
    // put them in ArrayAdapter to be displayed
    private void showDeviceList() {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {

            // Search through paired device and get name/address & Display Them
            for (BluetoothDevice bluetooth : pairedDevices) {
                list.add(bluetooth.getName() + "\n" + bluetooth.getAddress());
            }
        } else {
            message("No Paired Bluetooth Devices Found");
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(myListClickListener);

        display = false;
    }

    // On Click listener after selecting paired device
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Properties.getInstance().wifiStatus = false;
            // Get Device Information
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an Intent to change activities
           // Intent intent = new Intent(ConnectionActivity.this, BasicActivity.class);
            Intent intent = new Intent(ConnectionActivity.this, ControlActivity.class);

            // Change Activity putExtra() is necessary
            intent.putExtra(EXTRA_ADDRESS, address);
            startActivity(intent);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
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

    // Error Message on Phone Method
    // Simply enter the desired string to show up on the screen
    public void message(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
            }
        }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


    int id = item.getItemId();

    if(id == R.id.nav_first_layout){
        Intent intent = new Intent(ConnectionActivity.this, ControlActivity.class);
        startActivity(intent);
            }

    /*else if(id == R.id.nav_second_layout){
 Intent intent = new Intent(SlideMenu.this, TiltControl.class);
 startActivity(intent);
 }*/

// Handle navigation view item clicks here.
//int id = item.getItemId();

// FragmentManager fm = getFragmentManager();

// MenuItem to enable Joystick
//if (id == R.id.nav_first_layout) {
//    fm.beginTransaction()
//            .replace(R.id.content_joystick,
//                    new Test())
//            .commit();
// MenuItem to enable Tilt
//  } else if (id == R.id.nav_second_layout) {
//     fm.beginTransaction()
//             .replace(R.id.content_tilt,
//                     new Tilt())
//             .commit();
//  Intent intent

//}


    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.END);
    return true;
    }

}



package com.uggrator.vpnproxyapp;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.uggrator.ovpn.VpnClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import echoExchange.Client;
import echoExchange.Combo;

//import com.uggrator.ovpn.VpnClient;

public class MainActivity extends AppCompatActivity {
    String appIp = "31.173.217.131"; //IP app server
    int appPort = 15444;        //   port
    String vpnkey;

    private String fconfPath ;//= getFilesDir().getPath();
    private String fconf;// = fconfPath+File.separator+"ovpn"+File.separator+"proxy.txt" ;
    //File inDir = getFilesDir();
    private final String disconnected = "true";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button bttn = findViewById(R.id.button);
        bttn.setEnabled(false);
        bttn.setText("Wait");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Spinner с регионами - выбор
        final Spinner cbreg = findViewById(R.id.cbRegion);
        cbreg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Combo st = (Combo)cbreg.getSelectedItem();
                if (st.getId()>0){
                    loadNetwork(st.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        // Spinner с сетями - выбор
        final Spinner cbnet = findViewById(R.id.cbNetwork);
        cbnet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Combo st = (Combo)cbnet.getSelectedItem();
                if (st.getId()>0){
                    loadVpnProxy(st.getId());
                    loadproxyGw(st.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        fconfPath = getFilesDir().getPath();
        fconf = fconfPath+File.separator+"ovpn"+File.separator+"proxy.txt" ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


     /*
    Загружаем список регионов
    */
    private void loadRegion(){
        TextInputEditText text = findViewById(R.id.t_code);
        vpnkey = text.getText().toString();
        Client cl = new Client(appIp,appPort,"region:"+vpnkey) ;
        cl.setPath(fconfPath);
        try {
            cl.start();
        } catch (Exception ex) {
        }
        try {
            Spinner cbreg = findViewById(R.id.cbRegion);
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, cl.getListCombo());
            cbreg.setAdapter(spinnerArrayAdapter);
            //cbRegion.getItems().clear();
           // cbRegion.getItems().addAll(cl.getListCombo());
           // cbRegion.getSelectionModel().selectFirst();
        } catch (Exception ex) {
        }

    }

    /*
    Загружаем список Сетей
    */
    public void loadNetwork(int reg){
        TextInputEditText text = findViewById(R.id.t_code);
        String value = text.getText().toString();

        Client cl = new Client(appIp,appPort,"network:"+
                reg +"_"+value);
        cl.setPath(fconfPath);
        try {
            cl.start();
        } catch (Exception ex) {
        }
        try {
            Spinner cbnet = findViewById(R.id.cbNetwork);
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_item, cl.getListCombo());
            cbnet.setAdapter(spinnerArrayAdapter);
        } catch (Exception ex) {
        }
    }

    public void onGetRegionk(View view)
    {
        loadRegion();
    }

    public void onClickCode(View view)
    {
        TextInputEditText text = findViewById(R.id.t_code);
        text.setText("d0d9ef60e8b");
    }

    /*
   Загружаем vpn фалы. Конфиг
   ключ и сетрификаты
   */
    public void loadVpnProxy(int network){
        Client cl1 = new Client(appIp,appPort,"file1:"+vpnkey);
        cl1.setPath(fconfPath);
        //   String.valueOf(network));
        try {
            cl1.start();
        } catch (Exception ex) {
        }
        Client cl2 = new Client(appIp,appPort,"file2:"+vpnkey);
        cl2.setPath(fconfPath);
        //String.valueOf(network));
        try {
            cl2.start();
        } catch (Exception ex) {
        }
        Client cl3 = new Client(appIp,appPort,"file3:"+vpnkey);
        cl3.setPath(fconfPath);
        //String.valueOf(network));
        try {
            cl3.start();
        } catch (Exception ex) {
        }
        Client cl4 = new Client(appIp,appPort,"file4:"+vpnkey);
        cl4.setPath(fconfPath);
        //String.valueOf(network));
        try {
            cl4.start();
        } catch (Exception ex) {
        }
    }
    /*
    Загружаем адреса прокси серверов
     */
    private void loadproxyGw(int network){
        Client cl = new Client(appIp,appPort,"proxy:"+
                network);
        cl.setPath(fconfPath);
        try {
            cl.start();
        } catch (Exception ex) {
        }
        try{
            ArrayList ara = (ArrayList) cl.getListServIp();
            try (FileWriter writer = new FileWriter(fconf)) {
                int size = ara.size();
                for (int i=0;i<size;i++) {
                    String str = ara.get(i).toString();
                    writer.write(str);
                    if(i < size-1){
                        writer.write(System.lineSeparator());
                    }
                }

            }
        }catch(IOException ex){
        }

        Button bttn = findViewById(R.id.button);
        bttn.setEnabled(true);
        bttn.setText("Go");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setConnect(View view){
        VpnClient vpnc = new VpnClient();
        vpnc.connect();
    }

}

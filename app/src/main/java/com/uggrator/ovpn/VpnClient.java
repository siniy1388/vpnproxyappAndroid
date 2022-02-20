package com.uggrator.ovpn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;

import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.uggrator.vpnproxyapp.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;

public class VpnClient extends Activity {
    public VpnClient() {
    }  //extends Activity
    public interface Prefs {
        String NAME = "connection";
        String SERVER_ADDRESS = "server.address";
        String SERVER_PORT = "server.port";
        String SHARED_SECRET = "shared.secret";
        String PROXY_HOSTNAME = "proxyhost";
        String PROXY_PORT = "proxyport";
        String ALLOW = "allow";
        String PACKAGES = "packages";
    }

    String serverAddress ;
    int serverPortPrefValue ;
    String serverPort ;
    String sharedSecret ;
    String proxyHost ;
    int proxyPortPrefValue ;
    String proxyPort ;
    boolean allowed ;
    Set<String> packages ;
    SharedPreferences prefs;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main); //form
        /*
       final TextView serverAddress = findViewById(R.id.address);
        final TextView serverPort = findViewById(R.id.port);
        final TextView sharedSecret = findViewById(R.id.secret);
        final TextView proxyHost = findViewById(R.id.proxyhost);
        final TextView proxyPort = findViewById(R.id.proxyport);
        final RadioButton allowed = findViewById(R.id.allowed);
        final TextView packages = findViewById(R.id.packages);

        *///APP_PREFERENCES, Context.MODE_PRIVATE
        prefs = getSharedPreferences(Prefs.NAME, MODE_PRIVATE);
        String serverAddress = prefs.getString(Prefs.SERVER_ADDRESS, "");
        int serverPortPrefValue = prefs.getInt(Prefs.SERVER_PORT, 0);
        String serverPort = (String.valueOf(serverPortPrefValue == 0 ? "" : serverPortPrefValue));
        String sharedSecret =(prefs.getString(Prefs.SHARED_SECRET, ""));
        String proxyHost = (prefs.getString(Prefs.PROXY_HOSTNAME, ""));
        int proxyPortPrefValue = prefs.getInt(Prefs.PROXY_PORT, 0);
        String proxyPort =(proxyPortPrefValue == 0 ? "" : String.valueOf(proxyPortPrefValue));
        boolean allowed =(prefs.getBoolean(Prefs.ALLOW, true));
        Set<String> packages = Collections.singleton((String.join(", ", prefs.getStringSet(
                Prefs.PACKAGES, Collections.<String>emptySet()))));


     /*   findViewById(R.id.connect).setOnClickListener(v -> {
            if (!checkProxyConfigs(proxyHost, //.getText().toString()
                    proxyPort)) { //.getText().toString()
                return;
            }
            final Set<String> packageSet =packages;
                    //Arrays.stream(packages.getText().toString().split(","))
                    ///        .map(String::trim)
                    //        .filter(s -> !s.isEmpty())
                     //       .collect(Collectors.toSet());
            if (!checkPackages(packageSet)) {
                return;
            }
            int serverPortNum;
            try {
                serverPortNum = Integer.parseInt(serverPort);
            } catch (NumberFormatException e) {
                serverPortNum = 0;
            }
            int proxyPortNum;
            try {
                proxyPortNum = Integer.parseInt(proxyPort);
            } catch (NumberFormatException e) {
                proxyPortNum = 0;
            }
            prefs.edit()
                    .putString(Prefs.SERVER_ADDRESS, serverAddress)
                    .putInt(Prefs.SERVER_PORT, serverPortNum)
                    .putString(Prefs.SHARED_SECRET, sharedSecret)
                    .putString(Prefs.PROXY_HOSTNAME, proxyHost)
                    .putInt(Prefs.PROXY_PORT, proxyPortNum)
                    .putBoolean(Prefs.ALLOW, allowed)
                    .putStringSet(Prefs.PACKAGES, packageSet)
                    .apply();
            Intent intent = VpnService.prepare(VpnClient.this);
            if (intent != null) {
                startActivityForResult(intent, 0);
            } else {
                onActivityResult(0, RESULT_OK, null);
            }
        });


        findViewById(R.id.disconnect).setOnClickListener(v -> {
            startService(getServiceIntent().setAction(VpnServicem.ACTION_DISCONNECT));
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void connect(){

            if (!checkProxyConfigs(proxyHost, //.getText().toString()
                    proxyPort)) { //.getText().toString()
                return;
            }
            final Set<String> packageSet =packages;
            //Arrays.stream(packages.getText().toString().split(","))
            ///        .map(String::trim)
            //        .filter(s -> !s.isEmpty())
            //       .collect(Collectors.toSet());
            if (!checkPackages(packageSet)) {
                return;
            }
            int serverPortNum;
            try {
                serverPortNum = Integer.parseInt(serverPort);
            } catch (NumberFormatException e) {
                serverPortNum = 0;
            }
            int proxyPortNum;
            try {
                proxyPortNum = Integer.parseInt(proxyPort);
            } catch (NumberFormatException e) {
                proxyPortNum = 0;
            }
            prefs.edit()
                    .putString(Prefs.SERVER_ADDRESS, serverAddress)
                    .putInt(Prefs.SERVER_PORT, serverPortNum)
                    .putString(Prefs.SHARED_SECRET, sharedSecret)
                    .putString(Prefs.PROXY_HOSTNAME, proxyHost)
                    .putInt(Prefs.PROXY_PORT, proxyPortNum)
                    .putBoolean(Prefs.ALLOW, allowed)
                    .putStringSet(Prefs.PACKAGES, packageSet)
                    .apply();
            Intent intent = VpnService.prepare(VpnClient.this);
            if (intent != null) {
                startActivityForResult(intent, 0);
            } else {
                onActivityResult(0, RESULT_OK, null);
            }


    }

    public void disconnect(){
        startService(getServiceIntent().setAction(VpnServicem.ACTION_DISCONNECT));
    }

    private boolean checkProxyConfigs(String proxyHost, String proxyPort) {
        final boolean hasIncompleteProxyConfigs = proxyHost.isEmpty() != proxyPort.isEmpty();
        if (hasIncompleteProxyConfigs) {
            Toast.makeText(this, R.string.incomplete_proxy_settings, Toast.LENGTH_SHORT).show();
        }
        return !hasIncompleteProxyConfigs;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean checkPackages(Set<String> packageNames) {
        final boolean hasCorrectPackageNames = packageNames.isEmpty() ||
                getPackageManager().getInstalledPackages(0).stream()
                        .map(pi -> pi.packageName)
                        .collect(Collectors.toSet())
                        .containsAll(packageNames);
        if (!hasCorrectPackageNames) {
            Toast.makeText(this, R.string.unknown_package_names, Toast.LENGTH_SHORT).show();
        }
        return hasCorrectPackageNames;
    }
    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (result == RESULT_OK) {
            startService(getServiceIntent().setAction(VpnServicem.ACTION_CONNECT));
        }
    }
    private Intent getServiceIntent() {
        return new Intent(this, VpnServicem.class);
    }
}

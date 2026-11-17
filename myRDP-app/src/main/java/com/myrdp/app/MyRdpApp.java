// Generato da MyRDP Builder. Estende l'Application di aRDP (GPLv3) per
// precaricare una connessione RDP al primo avvio.
package com.myrdp.app;

import android.content.SharedPreferences;
import android.util.Log;

import com.iiordanov.bVNC.App;
import com.iiordanov.bVNC.ConnectionBean;

public class MyRdpApp extends App {
    private static final String TAG = "MyRdpApp";
    private static final String PREFS = "myrdp_seed";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            seedDefaultConnection();
        } catch (Throwable t) {
            Log.e(TAG, "Impossibile creare la connessione predefinita", t);
        }
    }

    private String res(String name) {
        int id = getResources().getIdentifier(name, "string", getPackageName());
        return id == 0 ? "" : getString(id).trim();
    }

    private void seedDefaultConnection() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (prefs.getBoolean("seeded", false)) {
            return;
        }
        String host = res("myrdp_default_host");
        if (!host.isEmpty()) {
            ConnectionBean c = new ConnectionBean(this);
            c.setAddress(host);
            int port = 3389;
            try {
                port = Integer.parseInt(res("myrdp_default_port"));
            } catch (NumberFormatException ignored) {
            }
            c.setPort(port);
            String nick = res("myrdp_default_nickname");
            c.setNickname(nick.isEmpty() ? host : nick);
            c.setUserName(res("myrdp_default_user"));
            c.setRdpDomain(res("myrdp_default_domain"));
            c.saveAndWriteRecent(false, this);
            Log.i(TAG, "Connessione predefinita creata: " + host + ":" + port);
        }
        prefs.edit().putBoolean("seeded", true).apply();
    }
}

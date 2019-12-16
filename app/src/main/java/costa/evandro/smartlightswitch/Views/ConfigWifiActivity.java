package costa.evandro.smartlightswitch.Views;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import costa.evandro.smartlightswitch.Controllers.MyApp;
import costa.evandro.smartlightswitch.R;

public class ConfigWifiActivity extends AppCompatActivity {
    public String rede_conectada = "";
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_wifi);
        setTitle("Configuração de Rede");

        ctx = MyApp.getmContext();
        Intent intent = getIntent();
        String SSID = intent.getStringExtra("nameSSID");
        final int netId = intent.getIntExtra("NETID", 0);


        final ImageView imv_swipe =  (ImageView) findViewById(R.id.imv_swipe);
        final TextView tv_push =  (TextView)findViewById(R.id.tv_push);
        tv_push.setVisibility(View.VISIBLE);
        final WifiManager wifiManager = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);

        Button btn_restartWifi = (Button)findViewById(R.id.btn_restart);
        btn_restartWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();
                wifiManager.removeNetwork(netId);

                Snackbar.make(v, "Reiniciando Wifi...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
            }
        });


        final WebView wbv = (WebView)findViewById(R.id.wbv_device);
        WebSettings webSettings = wbv.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wbv.setWebViewClient(new WebViewClient());

        final SwipeRefreshLayout swp = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);


        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                tv_push.setVisibility(View.GONE);
                imv_swipe.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swp.setRefreshing(false);
                        if(rede_conectada.contains("ID-")){
                            wbv.loadUrl("http://192.168.4.1");
                            wbv.setVisibility(View.VISIBLE);
                            rede_conectada = "";
                        }else{
                            Toast.makeText(ConfigWifiActivity.this, "Não há dispositivos à serem configurados", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 3000);
                getWifiName(ConfigWifiActivity.this);

            }
        });
    }
    public void getWifiName(Context context) {

        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    if(wifiInfo.getSSID().contains("ID-")){
                        rede_conectada = wifiInfo.getSSID();
                    }
                }
            }
        }

    }
}

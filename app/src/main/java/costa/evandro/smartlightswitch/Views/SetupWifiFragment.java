package costa.evandro.smartlightswitch.Views;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.Controllers.WifiAdapter;
import costa.evandro.smartlightswitch.Models.DBwifi;
import costa.evandro.smartlightswitch.Models.Element;


/**
 * Created by trainning on 22/07/2017.
 */

public class SetupWifiFragment extends Fragment {

    private static SetupWifiFragment instance;
    private ArrayList<String> produtos;
    Context ctx;
    public Boolean container_ssid = false;
    // private Element[] nets; //tao mang dung de chua cac thong so can lay
    public WifiManager wifiManager;
    private List<ScanResult> wifiList;
    // AdapterElements adapterElements;
    private TextView tv;
    WifiAdapter wifiAdapter;
    int size;


    public static SetupWifiFragment getInstance() {
        if (instance == null) {
            instance = new SetupWifiFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ctx = activity;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv = view.findViewById(R.id.txt_label_buscar);

        //Configurações de Wifi
        wifiManager = (WifiManager) ctx.getSystemService(ctx.WIFI_SERVICE);

        //Adapter
        final RecyclerView minhaRecyclerView = view.findViewById(R.id.wifi_recyclerview);


        //Botão flutuante
        FloatingActionButton fab =  view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ctx.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, 25322);
                    }
                }
                wifiManager.startScan();
                //new SearchWifiTask(wifiAdapter, minhaRecyclerView).execute();
                Snackbar.make(view, "Procurando Dispositivos...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        WifiScanReceiver wifiReciever = new WifiScanReceiver();
        ctx.registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        ctx.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context c, Intent intent)
            {
                wifiList = wifiManager.getScanResults();
                size = wifiList.size();

                //nets = new ArrayList<Element>();
                for(int i = 0; i<wifiList.size(); i++ ){
                    ArrayList<Element> nets = new ArrayList<>();
                    String SSID = "";
                    try{
                        String item = wifiList.get(i).toString();
                        String[] mang_item = item.split(",");//cat chuoi nhan duoc thanh roi luu vao mang_item.
                        String item_ssid = mang_item[0]; //lay ra chuoi "SSID: name wifi"
                        //String[] ssid = item_ssid.split(":"); //cat chuoi "SSID: name wifi" => tao thanh mang 2 phan tu la SSID va name wifi|| String ssid la ten => lay o phan tu thu 1 cua mang//                    String ssid = item_ssid.split(": ")[1]; //cat chuoi "SSID: name wifi" => tao thanh mang 2 phan tu la SSID va name wifi|| String ssid la ten => lay o phan tu thu 1 cua mang
                        String ssid = item_ssid.split(": ")[1]; //cat chuoi "SSID: name wifi" => tao thanh mang 2 phan tu la SSID va name wifi|| String ssid la ten => lay o phan tu thu 1 cua mang

                        SSID = ssid;
                        nets = new ArrayList<Element>();
                    }catch (Exception e){

                    }


                    if(SSID.contains("ID")){
                        Element dispositivo = new Element();
                        dispositivo.setDispositivo(SSID);
                        container_ssid = true;
                        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
                        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(MODE_APPEND, StaggeredGridLayoutManager.VERTICAL);
                        nets.add(dispositivo);
                        //wifiAdapter.setElements(nets);
                        //nets.add(dispositivo);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
                        minhaRecyclerView.setLayoutManager(layoutManager);
                        minhaRecyclerView.addItemDecoration(new DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL));
                        wifiAdapter = new WifiAdapter(ctx, nets);
                        minhaRecyclerView.setAdapter(wifiAdapter);

                        DBwifi.getInstance().criar((Activity) ctx);
                    }

                }



                if(container_ssid==false){
                   // Toast.makeText(ctx, "Nenhum Dispositivo encontrado, busque novamente", Toast.LENGTH_SHORT).show();
                    tv.setText("Nenhum Dispositivo encontrado... Clique em buscar para cadastrar novos dispositivos");
                 }else{
                    tv.setText("Clique em buscar para cadastrar novos dispositivos");
                }

            }

        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 25322) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }

    public static class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {

        }
    }



//    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//        webView.loadUrl("http://192.168.4.1");
//    }
//
//    @TargetApi(android.os.Build.VERSION_CODES.M)
//    public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//        // Redirect to deprecated method, so you can use it in all SDK versions
//        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//    }




//    class AdapterElements extends ArrayAdapter<Object> {
//        Activity context;
//
//        public AdapterElements(Activity context) {
//            super(context, R.layout.items, nets);
//            this.context = context;
//        }
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = context.getLayoutInflater();
//
//            View item = inflater.inflate(R.layout.items, null);
//            TextView tvSsid = item.findViewById(R.id.tvSSID);
//
//            tvSsid.setText(nets[position].getDispositivo());
//
//            return item;
//        }
//    }


}

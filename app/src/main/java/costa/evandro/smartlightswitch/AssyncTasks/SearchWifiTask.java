package costa.evandro.smartlightswitch.AssyncTasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import costa.evandro.smartlightswitch.MyApp;
import costa.evandro.smartlightswitch.adapter_ambiente.WifiAdapter;
import costa.evandro.smartlightswitch.fragments.SetupWifiFragment;
import costa.evandro.smartlightswitch.wifimanager.Element;

import static costa.evandro.smartlightswitch.MainActivity.exibirBarra;


public class SearchWifiTask extends AsyncTask<String,Integer,Integer> {
    private List<ScanResult> wifiList;
    public WifiManager wifiManager;
    int size;
    public Boolean container_ssid = false;
    private WifiAdapter myWifiAdpter;
    private RecyclerView mRecyclerView;
    ArrayList<Element> nets;

    public SearchWifiTask(WifiAdapter wifiAdapter, RecyclerView recyclerView){
        this.myWifiAdpter = wifiAdapter;
        this.mRecyclerView = recyclerView;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Integer doInBackground(String... strings){

        MyApp.getmContext().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context c, Intent intent)
            {
                SetupWifiFragment.WifiScanReceiver wifiReciever = new SetupWifiFragment.WifiScanReceiver();
                MyApp.getmContext().registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                wifiList = wifiManager.getScanResults();
                size = wifiList.size();

                //nets = new ArrayList<Element>();
                for(int i = 0; i<wifiList.size(); i++ ){
                    nets = new ArrayList<>();
                    String SSID = "";
                    try{
                        String item = wifiList.get(i).toString();
                        String[] mang_item = item.split(",");
                        String item_ssid = mang_item[0];
                        String ssid = item_ssid.split(": ")[1];
                        SSID = ssid;
                        nets = new ArrayList<Element>();
                    }catch (Exception e){

                    }
                    if(SSID.contains("ID")){
                        Element dispositivo = new Element();
                        dispositivo.setDispositivo(SSID);
                        container_ssid = true;
                        nets.add(dispositivo);
                        myWifiAdpter.setElements(nets);
                    }
                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        return null;
    }

    @Override
    protected void onPostExecute(Integer args) {
       // Toast.makeText(MyApp.getmContext(), "Smart Switch Finalizado", Toast.LENGTH_SHORT).show();
        exibirBarra(false);
        myWifiAdpter = new WifiAdapter(MyApp.getmContext(), nets);
        mRecyclerView.setAdapter(myWifiAdpter);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
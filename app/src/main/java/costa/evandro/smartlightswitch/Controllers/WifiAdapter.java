package costa.evandro.smartlightswitch.Controllers;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import costa.evandro.smartlightswitch.Views.ConfigWifiActivity;
import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.Models.DBwifi;
import costa.evandro.smartlightswitch.Models.Element;

import static android.support.v4.content.ContextCompat.startActivity;

public class WifiAdapter extends RecyclerView.Adapter {
    Context ctx;
    ArrayList<Element> elements;
    Dialog dialog;
    String networkSSID = "";
    String networkPass = "";
    WebView wbv_config;



    //Construtor
    public WifiAdapter(Context ctx, ArrayList<Element> _elements) {
        this.ctx = ctx;
        this.elements = _elements;
    }

    public void setElements(ArrayList<Element> _elements){
        this.elements.clear();
        this.elements = _elements;
        notifyDataSetChanged();
    }

    //Relaciona o layout com o view holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.items, parent, false);
        return new MeuViewHolder(v);
    }

    //
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final MeuViewHolder holder = ((MeuViewHolder) viewHolder);
            final Element element = elements.get(position);

            holder.txtNome.setText(element.getDispositivo());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getWifiName(ctx, element.getDispositivo());
                    //showdialow(element.getDispositivo());
                }
            });

    }

    @Override
    public int getItemCount() {return elements.size();
    }


    //instancia os componentes do layout
    public class MeuViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;
        ImageView imageView;
        CardView cardView;


        public MeuViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.tvSSID);
            cardView = itemView.findViewById(R.id.card_view_wifi);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }

    //********************  SHOW DIALOG  *********************
    public void showdialow(final String ssid) {
        DBwifi.getInstance().criar((Activity) ctx);

        networkSSID = ssid;
        dialog = new Dialog(ctx);
        dialog.setTitle("Digite a senha do wifi");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);

        Button btn_Salvar = dialog.findViewById(R.id.btn_dongy);
        Button btn_Cancel = dialog.findViewById(R.id.btn_huy);
        final EditText edt_Password = dialog.findViewById(R.id.edt_password);

        CheckBox checkBox = dialog.findViewById(R.id.cb_show);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edt_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    edt_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        btn_Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkPass = edt_Password.getText().toString();

                if (TextUtils.isEmpty(networkPass)) {
                    edt_Password.setError("Por favor digite uma senha");
                }else {
                    //Toast.makeText(ctx, "Nome da rede: " + networkSSID + "Senha: " + networkPass, Toast.LENGTH_LONG).show();
                    dialog.dismiss();


                    WifiConfiguration wifiConfig = new WifiConfiguration();
                    wifiConfig.SSID = "\"" + networkSSID + "\"";
                    wifiConfig.preSharedKey = "\""+networkPass +"\"";


                    WifiManager wifiManager = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
                    //wifiManager.addNetwork(conf);
                    //remember id
                    int netId = wifiManager.addNetwork(wifiConfig);
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(netId, true);
                    wifiManager.reconnect();


                    Intent configWifiIntent = new Intent(ctx, ConfigWifiActivity.class);
                    configWifiIntent.putExtra("nameSSID",networkSSID);
                    configWifiIntent.putExtra("NETID",netId);
                    startIntentActivity(configWifiIntent);


                    //showdialow_config();
                }

            }



        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void getWifiName(Context context, String SSID_info) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    String capture = wifiInfo.getSSID();
                    if(!capture.contains(SSID_info)){
                        showdialow(SSID_info);
                    }else {
                        Intent configWifiIntent = new Intent(ctx, ConfigWifiActivity.class);
                        configWifiIntent.putExtra("nameSSID",networkSSID);
                        configWifiIntent.putExtra("NETID",0);
                        startIntentActivity(configWifiIntent);
                    }
                }
            }
        }

    }

    private void startIntentActivity(final Intent configWifiIntent) {
        Toast.makeText(ctx, "Iniciando modo de configuração", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(ctx, configWifiIntent, null);
            }
        }, 3000);
    }
}

package costa.evandro.smartlightswitch.adapter_ambiente;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import costa.evandro.smartlightswitch.AssyncTasks.UrlActionsTask;
import costa.evandro.smartlightswitch.MainActivity;
import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.Utils.Icones;


public class ControleAdapter extends RecyclerView.Adapter {
    Context ctx;
    ArrayList<Ambiente> ambientes;
    boolean state_swt = false;
    private String URL_BASE = "http://192.168.0.";
    private String LIGAR = "/lamp=on";
    private String DESLIGAR = "/lamp=off";

    //Construtor
    public ControleAdapter(Context c) {
        this.ctx = c;
        this.ambientes = SQLHelper.getInstance().carregarValor();
        notifyDataSetChanged();
    }

    public  void setAmbientes(){
        this.ambientes.clear();
        this.ambientes = SQLHelper.getInstance().carregarValor();
        notifyDataSetChanged();
    }

    public void deleteAmbiente(){
        this.ambientes.clear();
        this.ambientes = SQLHelper.getInstance().carregarValor();
        notifyDataSetChanged();
    }


    //Relaciona o layout com o view holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_controles, parent, false);
        return new MeuViewHolder(v);
    }

    //
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final MeuViewHolder holder = ((MeuViewHolder) viewHolder);
        final Ambiente ambiente = ambientes.get(position);
        final Icones icon = new Icones();
        String id_img = ambiente.icone;
        String dispositivo = ambiente.dispositivo;
        int id_device = Integer.parseInt(dispositivo);

        holder.tv_Ambiente.setText(ambiente.cenario);

//        if(id_device==97) {
//            holder.cdv.setBackgroundColor(ctx.getResources().getColor(R.color.bggray));
//            holder.tv_Ambiente.setTextColor(ctx.getResources().getColor(R.color.gray_text));
//        }
//
//        if(id_device==98) {
//            //holder.cdv.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimaryDark));
//            holder.tv_Ambiente.setTextColor(ctx.getResources().getColor(R.color.gray_text));
//        }

        int disp = Integer.parseInt(dispositivo);
        int img = Integer.parseInt(id_img);

        switch (position){
            case 0: state_swt = MainActivity.swt1;
                break;
            case 1: state_swt = MainActivity.swt2;
                break;
            case 2: state_swt = MainActivity.swt3;
                break;
            case 3: state_swt = MainActivity.swt4;
                break;
            case 4: state_swt = MainActivity.swt5;
                break;
            case 5: state_swt = MainActivity.swt6;
                break;
            case 6: state_swt = MainActivity.swt7;
                break;
            case 7: state_swt = MainActivity.swt8;
                break;
            case 8: state_swt = MainActivity.swt9;
                break;
            case 9: state_swt = MainActivity.swt10;
                break;
        }
        holder.mSwitch.setChecked(state_swt);

        holder.img_Ambiente.setImageResource(icon.getImg(img));
        holder.img_Dispositivo.setImageResource(icon.getImg(disp));

        holder.mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = ambiente.ip;
                if(holder.mSwitch.isChecked()){
                    new UrlActionsTask(URL_BASE, LIGAR, ip, 1).execute();
                }else{
                    new UrlActionsTask(URL_BASE, DESLIGAR, ip, 1).execute();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return ambientes.size();
    }

    //instancia os componentes do layout
    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_Ambiente;
        ImageView img_Dispositivo;
        TextView tv_Ambiente;
        Switch mSwitch;
        WebView wbv_SendComands;
        LinearLayout cdv;


        public MeuViewHolder(View itemView) {
            super(itemView);
            img_Ambiente = itemView.findViewById(R.id.img_ambiente);
            img_Dispositivo = itemView.findViewById(R.id.img_dispositivo);
            tv_Ambiente = itemView.findViewById(R.id.tv_nome_ambiente);
            mSwitch = itemView.findViewById(R.id.swt_OnOff);
            wbv_SendComands = itemView.findViewById(R.id.wbv_main);
            cdv = itemView.findViewById(R.id.ll_controle);
        }
    }

}

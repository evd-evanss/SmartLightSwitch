//package costa.evandro.smartlightswitch.adapter_ambiente;
//
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.Dialog;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//
//import costa.evandro.smartlightswitch.R;
//import costa.evandro.smartlightswitch.alarme.Alarm_Receiver;
//import costa.evandro.smartlightswitch.alarme.Alarme;
//
//import static android.content.Context.ALARM_SERVICE;
//
//
//public class EventosAdapter extends RecyclerView.Adapter {
//    Context ctx;
//    ArrayList<Ambiente> ambientes;
//    Dialog dialog_alarme;
//    String hora = "";
//    String minuto = "";
//    static AlarmManager alarm_manager;
//    PendingIntent pending_intent;
//    boolean state_swt = false;
//    //crio intenção a ser enviada ao receiver
//    Intent dados_off;
//    int REQUEST_CODE;
//    final Calendar time = Calendar.getInstance();
//
//
//
//
//    //Construtor
//    public EventosAdapter(Context c) {
//        this.ctx = c;
//        this.ambientes = SQLHelper.getInstance().carregarValor();
//    }
//    public void setAmbientes(){
//        this.ambientes.clear();
//        this.ambientes = SQLHelper.getInstance().carregarValor();
//        notifyDataSetChanged();
//    }
//
//    //Relaciona o layout com o view holder
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(ctx).inflate(R.layout.itens_alarm, parent, false);
//        return new MeuViewHolder(v);
//    }
//
//    //
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
//        final MeuViewHolder holder = ((MeuViewHolder) viewHolder);
//
//        final Ambiente ambiente = ambientes.get(position);
//
//                switch (position){
//                    case 0: state_swt = AlarmeActivity.swt1;
//                        break;
//                    case 1: state_swt = AlarmeActivity.swt2;
//                        break;
//                    case 2: state_swt = AlarmeActivity.swt3;
//                        break;
//                    case 3: state_swt = AlarmeActivity.swt4;
//                        break;
//                    case 4: state_swt = AlarmeActivity.swt5;
//                        break;
//                    case 5: state_swt = AlarmeActivity.swt6;
//                        break;
//                    case 6: state_swt = AlarmeActivity.swt7;
//                        break;
//                    case 7: state_swt = AlarmeActivity.swt8;
//                        break;
//                    case 8: state_swt = AlarmeActivity.swt9;
//                        break;
//                    case 9: state_swt = AlarmeActivity.swt10;
//                        break;
//        }
//
//        holder.txt_Cenario.setText(ambiente.cenario);
//        holder.txt_Status.setText(ambiente.horas+":"+ambiente.minutos);
//        holder.imv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int ip = Integer.parseInt(ambiente.ip);
//                dialogAlarme(position, ip, ambiente.primary_key);
//            }
//        });
//
//
//        holder.switch_enable.setChecked(state_swt);
//
//        holder.switch_enable.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(holder.switch_enable.isChecked()){
//                    int ip = Integer.parseInt(ambiente.ip);
//                    //envia o enable, posição da lista e IP a receber alarme
//                    sendAlarm(true, position, ip);
//                    //salva estado do switch para reutilizar
//                    AlarmeActivity.change_SWT(true, position);
//
//                }else{
//                    //int hora = Integer.parseInt(ambiente.horas);
//                    //int minuto = Integer.parseInt(ambiente.minutos);
//                    int ip = Integer.parseInt(ambiente.ip);
//                    AlarmeActivity.change_SWT(false, position);
//                    sendAlarm(false, position, ip);
//                }
//            }
//        });
//
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return ambientes.size();
//    }
//
//    //instancia os componentes do layout
//    public class MeuViewHolder extends RecyclerView.ViewHolder {
//        ImageView imv;
//        TextView txt_Cenario;
//        TextView txt_Status;
//        Switch switch_enable;
//        CardView linha;
//        LinearLayout id_llayout;
//
//
//
//        public MeuViewHolder(View itemView) {
//            super(itemView);
//            imv = itemView.findViewById(R.id.ic_chrono);
//            txt_Cenario = itemView.findViewById(R.id.id_CenarioAlarme);
//            txt_Status = itemView.findViewById(R.id.id_StatusAlarme);
//            switch_enable = itemView.findViewById(R.id.swt_OnOff_Alarm);
//            linha = itemView.findViewById(R.id.id_linha);
//            id_llayout = itemView.findViewById(R.id.id_llayout);
//
//        }
//
//    }
//
//    //********************  SHOW DIALOG  *********************
//    public void dialogAlarme(final int pos, final int ip, final String key) {
//        SQLHelper.getInstance().criar((Activity) ctx);
//        final int[] hour_select = new int[1];
//        final int[] minute_select = new int[1];
//
//        dialog_alarme = new Dialog(ctx);
//        dialog_alarme.setTitle("Escolha um horário para o alarme");
//        dialog_alarme.setCancelable(true);
//        dialog_alarme.setContentView(R.layout.add_alarme_layout);
//
//        Button btn_Salvar = dialog_alarme.findViewById(R.id.btn_salvarAlarme);
//        Button btn_Cancel =  dialog_alarme.findViewById(R.id.btn_cancelAlarme);
//        final TimePicker alarm_timepicker =  dialog_alarme.findViewById(R.id.timePicker);
//
//        // inicializo alarmmanager
//        alarm_manager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
//        // Crio e instancio um calendário
//
//
//
//        btn_Salvar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//        //get int com horas e minutos
//                int hora = alarm_timepicker.getCurrentHour();
//                int minuto = alarm_timepicker.getCurrentMinute();
//                //conversão de valores int para strings com horas e minutos
//                hour_select[0] = hora;
//                minute_select[0] = minuto;
//
//                if(hora>24){
//                    hora = hora-24;
//                }
//
//                // Salvo hora e minuto do ambiente no calendario
//                time.set(Calendar.HOUR, alarm_timepicker.getCurrentHour());
//                time.set(Calendar.MINUTE, alarm_timepicker.getCurrentMinute());
//
//                REQUEST_CODE = ip;
//
//
//                //alarm_manager.setWindow(ALARM_TYPE, inicio, intervalo, pending_intent);
//                SQLHelper.getInstance().alterarAlarme(key,String.valueOf(hora),String.valueOf(minuto));
//                setAmbientes();
//                notifyItemChanged(pos);
//                dialog_alarme.dismiss();
//            }
//
//        });
//
//        btn_Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog_alarme.dismiss();
//            }
//        });
//        dialog_alarme.show();
//
//    }
//
//    public void AlteraAlarme(int hora, int minuto, int pos){
//        final Ambiente ambiente = ambientes.get(pos);
//        final Alarme alarme = new Alarme();
//        alarme.setMinuto(minuto);
//        if(minuto<10){
//            alarme.setMinuto(0+minuto);
//        }else{
//            alarme.setMinuto(minuto);
//        }
//        if(hora>23){
//            alarme.setHora(hora-23);
//        }else{
//            alarme.setHora(hora);
//        }
//        this.hora = String.valueOf(hora);
//        if(minuto<10){
//            this.minuto = "0"+String.valueOf(minuto);
//        }else{
//            this.minuto = String.valueOf(minuto);
//        }
//        SQLHelper.getInstance().alterarAlarme(ambiente.primary_key,this.hora,this.minuto);
//        setAmbientes();
//        notifyItemChanged(pos);
//    }
//
//    public void sendAlarm(boolean enable, int _pos, int IP) {
//
//        //Crio ambiente a partir da posição da lista
//        final Ambiente ambiente = ambientes.get(_pos);
//        Log.e("EN","enable= "+enable);
//        //crio intenção a ser enviada ao receiver
//        Intent dados_intent = new Intent(ctx, Alarm_Receiver.class);
//
//        //if o switch estiver habilitado
//        if (enable == true) {
//            try {
//                //put in String in my intent
//                dados_intent.putExtra("enable", "alarm_on");
//                //put int in my intent
//                dados_intent.putExtra("ip_choose", IP);
//                //Crie uma intenção pendente que espera até o horário especificado no calendário
//                pending_intent = PendingIntent.getBroadcast(ctx, REQUEST_CODE, dados_intent, PendingIntent.FLAG_ONE_SHOT);
//
//                dados_intent.putExtra("switch", "swt_on");
//                //alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), time.getTimeInMillis(), pending_intent);
//                //alarm_manager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pending_intent);
//                if (android.os.Build.VERSION.SDK_INT>16)
//                {
//                    alarm_manager.setExact(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pending_intent);
//                    Toast.makeText(ctx, "time:"+time.getTimeInMillis(), Toast.LENGTH_SHORT).show();
//                    time.clear();
//                }else
//                {
//                    //alarm_manager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pending_intent);
//                    Toast.makeText(ctx, "time:"+time.getTimeInMillis(), Toast.LENGTH_SHORT).show();
//                    time.clear();
//                }
//            }catch (Exception e){
//                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//            ctx.sendBroadcast(dados_intent);
//        }else{
//            try {
//                dados_intent.putExtra("switch", "swt_off");
//                //put in String in my intent
//                dados_intent.putExtra("enable", "alarm_off");
//                //put int in my intent
//                dados_intent.putExtra("ip_choose", IP);
//                //Cancela o alarme
//                alarm_manager.cancel(pending_intent);
//                // Finaliza webview
//                ctx.sendBroadcast(dados_intent);
//            }catch (Exception e){
//                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//}

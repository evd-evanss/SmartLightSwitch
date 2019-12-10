package costa.evandro.smartlightswitch.alarme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by teste on 11/07/2017.
 */

public class Alarm_Receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Estamos no receptor", "");

        int startId;
        int state;

        //Busca strings extra da intent
        String state_alarm = intent.getExtras().getString("enable","");
        Log.e("Estado do alarme = ", state_alarm);

        //Busca strings extra da intent
        String switch_state = intent.getExtras().getString("switch","");
        Log.e("Estado da chave = ", switch_state);


        //Busca int extra da intent
        int id = intent.getExtras().getInt("ip_choose", 0);
        Log.e("Qual é o ip", String.valueOf(id));

        //Criação de um intent para o Serviço de ringtone
        //Intent service_intent = new Intent(context, AlarmeWebService.class);
        Intent service_intent = new Intent(context, HelloService.class);

        //Passa a string extra a partir de Alarme adapter para AlarmeWebService
        service_intent.putExtra("enable", state_alarm);

        //Passa o int extra a partir da Config activity para Ringtone Playing Service
        service_intent.putExtra("ip_choose", id);


        //inicio o serviço de ringtone
        context.startService(service_intent);

    }
}


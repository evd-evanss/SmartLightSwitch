package costa.evandro.smartlightswitch.alarme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import costa.evandro.smartlightswitch.MainActivity;
import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.SplashActivity;

/**
 * Created by teste on 11/07/2017.
 */

public class AlarmeWebService extends Service {
    Boolean isRunning = false;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Busco os valores de string
        String state = intent.getExtras().getString("enable");
        Log.e("Qual é o estado", state+"");

        //Busco os valores de IP
        int id = intent.getExtras().getInt("ip_choose", 0);

        Log.e("Qual é o estado", id+"");


        //Se o enable estiver true, ativa senão desativa
        assert  state != null;
        switch (state) {
            case "alarm_on":
                startId = 1;
                break;
            case "alarm_off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        //Se outras afirmações
        //Se não houver um tocador de música e o usuário pressionou o alarme
        //A música deveria começar
        if(startId == 1){
            //Crio e instancio um webview
            //de acordo com escolha do usuario

            switch (id){
                case 118:
                    MainActivity.receiveId(118);
                    Log.e("switch", "legal funcionou");
                    break;
                case 119:
                    MainActivity.receiveId(119);
                    Log.e("switch", "legal funcionou");
                    break;
                case 120:
                    MainActivity.receiveId(120);
                    Log.e("switch", "legal funcionou");
                    break;
                case 121:
                    MainActivity.receiveId(121);
                    Log.e("switch", "legal funcionou");
                    break;
                case 122:
                    MainActivity.receiveId(122);
                    Log.e("switch", "legal funcionou");
                    break;
                case 123:
                    MainActivity.receiveId(123);
                    Log.e("switch", "legal funcionou");
                    break;
                case 124:
                    MainActivity.receiveId(124);
                    Log.e("switch", "legal funcionou");
                    break;
                case 125:
                    MainActivity.receiveId(125);
                    Log.e("switch", "legal funcionou");
                    break;
                case 126:
                    MainActivity.receiveId(126);
                    Log.e("switch", "legal funcionou");
                    break;
                case 127:
                    MainActivity.receiveId(127);
                    Log.e("switch", "legal funcionou");
                    break;

                default:
                    break;
            }

            isRunning = true;
            startId = 0;

            //notificações
            //setup das notificações de serviço
            NotificationManager notify_manager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);

//            // Crio identificação da notificaçãon
           int tocar_alarmId = 001;
//
//            //Configurar uma intenção que vá para ConfigActivity
            Intent splash_activity = new Intent(this.getApplicationContext(), SplashActivity.class);
//
//            //setup de uma pendent intent
            PendingIntent resultPendingIntent =

                    PendingIntent.getActivity(this, 0,
                            splash_activity, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            //notificação de parametros
//            Notification notification_popup = new Notification.Builder(this)
//                    .setSmallIcon(R.drawable.ic_clock)
//                    .setContentTitle("Um alarme esta tocando")
//                    .setContentText("Click me")
//                    .setContentIntent(resultPendingIntent)
//                    .setAutoCancel(false)
//                    .build();
//            notification_popup.flags |= Notification.FLAG_AUTO_CANCEL;
////            Setup da chamada de notificações
//
           // notify_manager.notify(tocar_alarmId, notification_popup);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setContentTitle("Um alarme esta tocando")
                            .setSmallIcon(R.drawable.ic_clock)
                            .setContentText("Click me")
                            .setContentIntent(resultPendingIntent)
                            .setAutoCancel(true);

            mBuilder.setContentIntent(resultPendingIntent);
            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());



        }
        //Se houver música e o usuário pressionou o alarme off
        //A música devera parar
        else if(isRunning && startId == 0){
            //stop ringtone
            isRunning = false;
            startId = 0;
            MainActivity.stopAlarmId(id);
            Log.e("finalizar", "stop");
        }
//        //Estes são se o usuário pressiona os botões aleatórios
//        //Apenas para ser a prova de bugs
//        //Se não há música a tocar e o usuário pressiona "alarme off" não faça nada
//        else if(!this.isRunning && startId == 0){
//            isRunning = false;
//            startId = 0;
//        }
//        //Se há música a tocar e o usuário pressiona "alarme on" não faça nada
//        else if(this.isRunning && startId == 1){
//            isRunning = true;
//            startId = 1;
//        }
//        //Não consigo pensar em mais nada, apenas para pegar eventos estranhos
//        else{
//
//        }


        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this, "on destroy called", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        this.isRunning = false;
    }


}

//pedaços de códigos q não utilizei
//
//            NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(this)
//                            .setContentTitle("Um alarme esta tocando")
//                            .setSmallIcon(R.drawable.notification_icon)
//                            .setContentText("Click me")
//                            .setContentIntent(resultPendingIntent)
//                            .setAutoCancel(true);
//
//            mBuilder.setContentIntent(resultPendingIntent);
//            // Sets an ID for the notification
//            int mNotificationId = 001;
//            // Gets an instance of the NotificationManager service
//            NotificationManager mNotifyMgr =
//                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//            // Builds the notification and issues it.
//            mNotifyMgr.notify(mNotificationId, mBuilder.build());
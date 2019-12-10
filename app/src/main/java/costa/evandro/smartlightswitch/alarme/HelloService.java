package costa.evandro.smartlightswitch.alarme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

import costa.evandro.smartlightswitch.MainActivity;
import costa.evandro.smartlightswitch.SplashActivity;

/**
 * Created by teste on 02/11/2017.
 */

public class HelloService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    Boolean isRunning = false;
    int id = 0;

    // Manipulador que recebe mensagens da thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normalmente, nós trabalhamos aqui, como baixar um arquivo.
            // Por exemplo, dormir por 5 segundos.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Pare o serviço usando o startId, para que não paremos
            // o serviço no meio de outro trabalho
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Inicia a thread que executa o serviço. Observe que criamos uma
        // thread separada porque o serviço normalmente é executado no processo da
        // thread principal, que não queremos bloquear. Nós também fazemos isso
        // prioridade background para que o trabalho intensivo na CPU não interrompa nossa UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Obter o Looper do HandlerThread e usá-lo no nosso Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "serviço iniciado", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        //Busco os valores de string
        String state = intent.getExtras().getString("enable");
        Log.e("Qual é o estado", state+"");

        //Busco os valores de IP
        id = intent.getExtras().getInt("ip_choose", 0);

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






        // Se o serviço for morto depois de retornar daqui, reinicie
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        MainActivity.stopAlarmId(id);
        Toast.makeText(this, "fim do serviço", Toast.LENGTH_SHORT).show();
    }
}

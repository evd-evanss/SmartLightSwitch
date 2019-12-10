package costa.evandro.smartlightswitch.AssyncTasks;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import costa.evandro.smartlightswitch.adapter_ambiente.Ambiente;
import costa.evandro.smartlightswitch.adapter_ambiente.SQLHelper;

import static costa.evandro.smartlightswitch.MainActivity.change_SWT;
import static costa.evandro.smartlightswitch.MainActivity.exibirBarra;
import static costa.evandro.smartlightswitch.MainActivity.exibirProgresso;

/**
 * Created by teste on 15/11/2017.
 */


public class UrlActionsTask extends AsyncTask<String,Integer,Integer> {
    //Cria instancia do banco de dados
    ArrayList<Ambiente> ambientes;
    private String url_base;
    private String action;
    private String ip;
    private int REQUEST_CODE;
    final int AGIR_EM_TODOS = 0;
    final int AGIR_EM_UM = 1;

    public UrlActionsTask(String url_base, String action, String ip, int REQUEST_CODE){
        this.url_base = url_base;
        this.action = action;
        this.ip = ip;
        this.REQUEST_CODE = REQUEST_CODE;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        exibirBarra(true);
        //Listo ambientes cadastrados
        ambientes = SQLHelper.getInstance().carregarValor();
        final int sizeAmbientes = ambientes.size();
        for (int i = 0; i < sizeAmbientes; i++) {
            Ambiente ambiente = ambientes.get(i);
            String dispositivo = ambiente.dispositivo;
            int id_device = Integer.parseInt(dispositivo);
            if(id_device==97){
                int pos = Integer.parseInt(ambiente.getPrimary_key());
                if(action.equals("/lamp=on"))change_SWT(true, pos);
                if(action.equals("/lamp=off"))change_SWT(false, pos);
            }
        }
    }

    @Override
    protected Integer doInBackground(String... strings){

        switch (REQUEST_CODE){
            case AGIR_EM_TODOS:
                //Verifico a quantidade de ambientes e guardo em uma variável para pode utilizar posteriormente
                final int sizeAmbientes = ambientes.size();
                for (int i = 0; i < sizeAmbientes; i++) {
                    publishProgress((int) ((i / (float) sizeAmbientes) * 100));
                    Ambiente ambiente = ambientes.get(i);
                    String dispositivo = ambiente.dispositivo;
                    int id_device = Integer.parseInt(dispositivo);
                    if(id_device==97){
                        String ip = ambiente.getIp();
                        try {
                            URL url = new URL(url_base+ip+ action);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.setDoOutput(true);
                            urlConnection.setChunkedStreamingMode(0);
                            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            urlConnection.disconnect();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case AGIR_EM_UM:
                try {
                    publishProgress((int) ((10 / (float) 25) * 100));
                    //Aqui é definida a URL que contém a ação a ser executada pelo módulo wifi
                    URL url = new URL(url_base+ip+action);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    publishProgress((int) ((10 / (float) 50) * 100));
                    //Um objeto do tipo InputStream é responsável por receber as informações, abrir a url e fechar
                    InputStream inputStream;
                    inputStream = url.openStream();
                    publishProgress((int) ((10 / (float) 75) * 100));
                    inputStream.close();
                    publishProgress((int) ((10 / (float) 100) * 100));
                }catch (IOException e){
                    e.printStackTrace();
                    //Toast.makeText(MyApp.getmContext(), "Erro ao processar solicitação", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return null;
    }


    @Override
    protected void onPostExecute(Integer args) {
       // Toast.makeText(MyApp.getmContext(), "Smart Switch Finalizado", Toast.LENGTH_SHORT).show();
        exibirBarra(false);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        exibirProgresso(values[0]);
    }
}
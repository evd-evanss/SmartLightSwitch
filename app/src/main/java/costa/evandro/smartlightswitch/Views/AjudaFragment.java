package costa.evandro.smartlightswitch.Views;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import costa.evandro.smartlightswitch.Controllers.UrlActionsTask;
import costa.evandro.smartlightswitch.Controllers.MyApp;
import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.Models.Ambiente;
import costa.evandro.smartlightswitch.Models.SQLHelper;

import static android.app.Activity.RESULT_OK;
import static costa.evandro.smartlightswitch.Views.MainActivity.update;
import static costa.evandro.smartlightswitch.Views.MainActivity.url_base;


/**
 * Created by Evandro on 22/07/2017.
 * Consultar documentação para implementação de QRdroid em http://qrdroid.com/android-developers/
 */

public class AjudaFragment extends Fragment {
    private static AjudaFragment instance;
    int LER_CODE = 0;
    int GERAR_CODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    static final String ACTION_SCAN = "la.droid.qr.scan";
    static final String ACTION_GENERATE = "la.droid.qr.encode";
    static final String ACTION_RESULT = "la.droid.qr.result";
    static final String INTENT_EXTRA_CODIFICAR = "la.droid.qr.code";
    static final String INTENT_EXTRA_SIZE_CODE = "la.droid.qr.size";
    static final String INTENT_EXTRA_COMPLETO = "la.droid.qr.complete";
    public Uri uri_one;
    Context context;
    String backup;
    String verifica;
    Dialog dialog_add;

    public AjudaFragment() {
    }

    public static AjudaFragment getInstance() {
        if (instance == null) {
            instance = new AjudaFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_email, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        backup = loadDataBase();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uri_one = Uri.parse("market://details?id=la.droid.qr");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        SQLHelper.getInstance().criar((Activity) context);
        Button btnLink = view.findViewById(R.id.btn_link);
        Button btnGerarQR = view.findViewById(R.id.btn_email);
        Button btnLerQr = view.findViewById(R.id.btn_import);
        Button btnReset = view.findViewById(R.id.btn_rst);
        Button btnUpdate = view.findViewById(R.id.btn_update);

        //Chama intent para abrir vídeo no you tube
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri caminho = Uri.parse("https://www.youtube.com/watch?v=lk_gT3MmGsg");
                startActivity(new Intent(Intent.ACTION_VIEW, caminho));
            }
        });
        //Chama método para Gerar Qr Code
        btnGerarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions((Activity) context);
                gerarQrCode(context, "backup.txt", backup);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyApp.getmContext(), "Iniciando atualização...", Toast.LENGTH_SHORT).show();
                new UrlActionsTask(url_base, update,"118", 1).execute();
            }

        });
        //Chama método para Gerar Qr Code
        btnGerarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions((Activity) context);
                gerarQrCode(context, "backup.txt", backup);
            }
        });
        //Chama método para Ler Qr Code
        btnLerQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyStoragePermissions((Activity) context);
                lerQrCode();
            }
        });
        //Chama diálogo para resetar ambientes
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialow_add();
            }
        });

    }

    public String loadDataBase(){
        String data="";
        ArrayList<Ambiente> a = SQLHelper.getInstance().carregarValor();
        data.trim();
        for (int i = 0; i < a.size(); i++) {
            Ambiente bckpAmbiente = a.get(i);
            data = data + bckpAmbiente.getBackup();
        }
        verifica = data;
        return data;
    }
    //Método para gerar Qr Code
    public void gerarQrCode(Context context, String sFileName, String  sBody) {
        try {
            //
            Intent intent = new Intent(ACTION_GENERATE);
            intent.putExtra(INTENT_EXTRA_CODIFICAR, sBody);
            intent.putExtra (INTENT_EXTRA_SIZE_CODE, 50);
            startActivityForResult(intent, GERAR_CODE);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog((AppCompatActivity) context, "Você precisa ter instalado o QR Droid Code Scanner", "Deseja fazer o Download deste App no Google Play?", "Sim", "Não").show();
        }
    }
    //Método para ler Qr Code
    public void lerQrCode()
    {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra( INTENT_EXTRA_COMPLETO , true);
            startActivityForResult(intent, LER_CODE);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog((AppCompatActivity) context, "Você precisa ter instalado o QR Droid Code Scanner", "Deseja fazer o Download deste App no Google Play?", "Sim", "Não").show();
        }
    }
    //Método para resetar configurações de wifi do ambiente
    public void showdialow_add() {

        dialog_add = new Dialog(context);
        dialog_add.setCancelable(true);
        dialog_add.setContentView(R.layout.reset_config_layout);

        Button btn_Salvar = dialog_add.findViewById(R.id.btn_continuar);
        Button btn_Cancel =  dialog_add.findViewById(R.id.btn_cancelar);
        final EditText edt_Reset = dialog_add.findViewById(R.id.edt_rst);
        final WebView wbv_reset =  dialog_add.findViewById(R.id.wbv_rst);

        btn_Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ip = edt_Reset.getText().toString();
                wbv_reset.loadUrl("http://192.168.0."+ip+"/rst");
                dialog_add.dismiss();
            }

        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_add.dismiss();
            }
        });
        dialog_add.show();
    }
    //Método para verificar permissão para ler armazenamento interno
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    //Recebe resultados obtidos no aplicativo QR Droid
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Se botão importar for pressionado
        if (requestCode == LER_CODE) {
            if (resultCode == RESULT_OK) {
                String result = intent.getExtras().getString(ACTION_RESULT);
                //Pega diretorio na raiz do armazenamento interno com o nome Notes
                File root = new File(android.os.Environment.getExternalStorageDirectory(), "Notes");
                //Se diretório não existir cria o caminho especificado acima
                if (!root.exists()) {
                    root.mkdirs();
                }
                try {
                    //Cria o arquivo que receberá o backup e instancio como um Filewriter
                    File file_qrcode = new File(root, "QrCode.txt");
                    FileWriter writer = new FileWriter(file_qrcode);
                    //Escrevo no arquivo o resultado recebido do QR  code
                    writer.append(result);
                    writer.flush();
                    writer.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
                    //String[]leitura = new String[0];
                    ArrayList<String> leitura = new ArrayList<>();
                    File file;
                    String linha;
                    try
                    {
                        file = new File(android.os.Environment.getExternalStorageDirectory(), "Notes/QrCode.txt");
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        int cont = 0;
                        ArrayList<String> listLinhas = new ArrayList<>(0);

                        while ((linha = br.readLine()) != null)
                        {
                            leitura.add(linha);
                        }
                        br.close();
                        Ambiente a = new Ambiente("", "","","","","","");
                        int cursor = 0;
                        SQLHelper.getInstance().limpar();
                        SQLHelper.getInstance().criar((Activity) context);
                        for (int i = 0; i < leitura.size(); i++) {
                            String separar = leitura.get(i);
                            //cenario = separar.split(("/"));
                            if(cursor==4)cursor = 0;
                            switch (cursor){
                                case 0:
                                    a.setCenario(leitura.get(i));
                                    a.setPrimary_key(leitura.get(i));
                                    break;
                                case 1:
                                    a.setIp(leitura.get(i));
                                    break;
                                case 2:
                                    a.setIcone(leitura.get(i));
                                    break;
                                case 3:
                                    a.setDispositivo(leitura.get(i));
                                    SQLHelper.getInstance().inserirValor(a);
                                    break;
                            }
                            cursor = cursor+1;
                        }
                        Mensagem("Scan finalizado");
                    }catch (Exception e)
                    {
                        Mensagem("Erro : " + e.getMessage());
                    }
            }
        }
        //Se botão gerar for pressionado
        if (requestCode == GERAR_CODE) {
            if (resultCode == RESULT_OK) {
                String result = intent.getExtras ().getString(ACTION_RESULT);
                Uri caminho = Uri.parse(result);
                startActivity(new Intent(Intent.ACTION_VIEW, caminho));
            }
        }
    }
    //Recebe os diálogos de Alerta
    private AlertDialog showDialog(final AppCompatActivity act,CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(Intent.ACTION_VIEW, uri_one);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }
    //Método para criar Toast
    private void Mensagem(String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}

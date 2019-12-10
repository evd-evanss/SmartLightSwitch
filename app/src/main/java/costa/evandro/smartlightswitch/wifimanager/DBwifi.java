package costa.evandro.smartlightswitch.wifimanager;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by trainning on 15/07/2017.
 */



public class DBwifi {
    private static DBwifi instance;
    SQLiteDatabase db;//

    final String dbName = "db_dadoswifi"; //nome do banco de dados
    final String tableName = "dados"; //nome da tabela

    String TAG = "logImage";//para Log

    public static DBwifi getInstance() {
        if (instance == null) {
            instance = new DBwifi();
        }
        return instance;
    }

    public void criar(Activity sqlActivity) {
        db = sqlActivity.openOrCreateDatabase(dbName, sqlActivity.MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(senha VARCHAR, ssid VARCHAR);";

        Log.d(TAG, "Query de criação: " + tableQuery);

        db.execSQL(tableQuery);//executa db
    }

    public void limpar() {
        db.execSQL("DROP TABLE " + tableName + ";");
    }

    public void inserirValor(DadosWifi data) {
        ContentValues values = new ContentValues();

        values.put("senha", data.senha);
        values.put("ssid", data.ssid);


        try {
            db.insert(tableName, null, values); //insere na tabela os valores
        } catch (Exception erro) {
            Log.d(TAG, "Erro ao inserir no BD: " + erro);
        }
    }

    public void alterarValor(String ssid, String senha) {

        /**
        id que servirá como identificador, não mudará, e o nome é o nome novo do ambiente
         */
        //novos valores
        ContentValues novosValores = new ContentValues();
        novosValores.put("senha", senha);

        //parametros de comparação
        String[] args = new String[]{ssid};

        try {
            db.update(tableName, novosValores, "senha=?", args);
        } catch (Exception erro) {
            Log.d(TAG, "Erro ao alterar no BD: " + erro);
        }

    }

    public void apagaValor(String senha) {

        try {
            int count = db.delete(tableName, "senha = ?" , new String[]{senha});
            Log.d(TAG, "Removeu " + count+" registros");

        } catch (Exception erro) {
            Log.d(TAG, "Erro ao remover do BD: " + erro);
        }

    }

    public DadosWifi  carregarDadosWifi() {
        String query = "SELECT * FROM " + tableName + ";";
        Cursor myCursor = db.rawQuery(query, null);//apontamento de linhas
        ArrayList<DadosWifi> dataArray = new ArrayList<>();

        DadosWifi data = null;
        if (myCursor.moveToFirst()) {

            do {
                String senha = myCursor.getString(0);//pega a primeira coluna
                String ssid = myCursor.getString(1);//segunda

                //int icone = Integer.parseInt(inter);

                data = new DadosWifi(ssid, senha);

                Log.d(TAG, senha);
                Log.d(TAG, ssid);
                Log.d(TAG, "------------------------");

            } while (myCursor.moveToNext());
        }
        return data;
    }


    }

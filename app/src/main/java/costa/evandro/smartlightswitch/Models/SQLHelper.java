package costa.evandro.smartlightswitch.Models;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Evandro Costa on 15/07/2017.
 */



public class SQLHelper {

    private static SQLHelper instance;
    SQLiteDatabase db;//
    final String dbName = "db_ambientes"; //nome do banco de dados_intent
    final String tableName = "ambientes"; //nome da tabela

    String TAG = "logImage";//para Log

    public static SQLHelper getInstance() {
        if (instance == null) {
            instance = new SQLHelper();
        }
        return instance;
    }

    public void criar(Activity sqlActivity) {
        db = sqlActivity.openOrCreateDatabase(dbName, sqlActivity.MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(cenario VARCHAR, " +
                "ip VARCHAR, key INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, " +
                "iconeDefault VARCHAR, horas VARCHAR, minutos VARCHAR, dispositivo VARCHAR);";

        Log.d(TAG, "Query de criação: " + tableQuery);
        db.execSQL(tableQuery);//executa db
    }

    public void limpar() {
        //db.execSQL("DROP TABLE " + tableName + ";");
        db.execSQL("DROP TABLE IF EXISTS " + tableName+ ";");
    }

    public void inserirValor(Ambiente ambiente) {
        ContentValues values = new ContentValues();
        String defaultHora = "00";
        String defaultMinuto = "00";

        values.put("cenario", ambiente.cenario);
        values.put("ip", ambiente.ip);
        values.put("iconeDefault", ambiente.icone);
        values.put("horas", defaultHora);
        values.put("minutos", defaultMinuto);
        values.put("dispositivo", ambiente.dispositivo);

        try {
            db.insert(tableName, null, values); //insere na tabela os valores
        } catch (Exception erro) {
            Log.d(TAG, "Erro ao inserir no BD: " + erro);
        }
    }

    public void alterarValor(String nome, String id, String primary_key, String icone, String dispositivo) {

        /**
        key servirá como identificador
         */
        //novos valores
        ContentValues novosValores = new ContentValues();
        novosValores.put("cenario", nome);
        novosValores.put("ip", id);
        novosValores.put("dispositivo", dispositivo);

        //parametros de comparação
        String[] args = new String[]{primary_key};
        //String[] args = new String[]{id};

        try {
            db.update(tableName, novosValores, "key ="+primary_key, null);
            //db.update(tableName, novosValores, "cenario=?", args);
        } catch (Exception erro) {
            Log.d(TAG, "Erro ao alterar no BD: " + erro);
        }

    }
    public void alterarImagem(String primary_key, String icone) {

        /**
         id que servirá como identificador, não mudará, e o cenario é o cenario novo do ambiente
         */
        //novos valores
        ContentValues novosValores = new ContentValues();
        novosValores.put("iconeDefault", icone);

        //parametros de comparação
        String[] args = new String[]{primary_key};
        //String[] args = new String[]{id};

        try {
            db.update(tableName, novosValores, "key ="+primary_key, null);
            //db.update(tableName, novosValores, "cenario=?", args);
        } catch (Exception erro) {
            Log.d(TAG, "Erro ao alterar no BD: " + erro);
        }
    }

    public void apagaValor(String key) {

        try {
            int count = db.delete(tableName, "key = ?" , new String[]{key});
            Log.d(TAG, "Removeu " + count+" registros");

        } catch (Exception erro) {
            Log.d(TAG, "Erro ao reomver do BD: " + erro);
        }

    }

    public ArrayList<Ambiente>  carregarValor() {
        String query = "SELECT * FROM " + tableName + ";";
        Cursor myCursor = db.rawQuery(query, null);//apontamento de linhas
        ArrayList<Ambiente> ambientesArray = new ArrayList<>();

        if (myCursor.moveToFirst()) {

            do {
                String cenario = myCursor.getString(0);//pega a primeira coluna
                String ip = myCursor.getString(1);//segunda
                String primary_key = myCursor.getString(2);//segunda
                String icone = myCursor.getString(3);//segunda
                String horas = myCursor.getString(4);//segunda
                String minutos = myCursor.getString(5);//segunda
                String dispositivo = myCursor.getString(6);//segunda
                String backup = cenario + "\r\n"+ip +"\r\n"+icone+ "\r\n"+dispositivo+"\r\n";

                //int iconeDefault = Integer.parseInt(inter);

                Ambiente ambiente = new Ambiente(cenario, ip, primary_key, icone, horas, minutos, dispositivo);
                ambiente.setBackup(backup);
                ambientesArray.add(ambiente);

                Log.d(TAG, cenario);
                Log.d(TAG, ip);
                Log.d(TAG, "------------------------");

            } while (myCursor.moveToNext());
        }
        return ambientesArray;

    }


    }

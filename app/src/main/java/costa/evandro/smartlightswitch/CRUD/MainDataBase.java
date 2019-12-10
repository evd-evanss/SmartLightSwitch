//package costa.evandro.smartlightswitch.CRUD;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import costa.evandro.smartlightswitch.MyApp;
//
///**
// * Created by teste on 01/11/2017.
// */
//
//public class MainDataBase extends SQLiteOpenHelper {
//
//    private static String NOME_DB = "DB_EL";
//    private static int  VERSAO_DB = 1;
//    public static String TABLE_DISPOSITIVOS = "TABLE_DISPOSITIVOS";
//
//    private static MainDataBase instancia;
//
//    public static MainDataBase getInstancia(){
//        if(instancia==null) {
//            instancia = new MainDataBase();
//        }
//        return instancia;
//    }
//
//
//
//    private MainDataBase() {
//        super(MyApp.getmContext(), NOME_DB, null, VERSAO_DB);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//
//    }
//}

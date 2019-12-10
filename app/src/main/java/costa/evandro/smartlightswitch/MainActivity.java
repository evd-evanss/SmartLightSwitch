package costa.evandro.smartlightswitch;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import costa.evandro.smartlightswitch.AssyncTasks.UrlActionsTask;
import costa.evandro.smartlightswitch.adapter_ambiente.Ambiente;
import costa.evandro.smartlightswitch.adapter_ambiente.CadastroAdapter;
import costa.evandro.smartlightswitch.adapter_ambiente.ControleAdapter;
import costa.evandro.smartlightswitch.adapter_ambiente.SQLHelper;
import costa.evandro.smartlightswitch.adapter_ambiente.PageAdapter;
import costa.evandro.smartlightswitch.fragments.AjudaFragment;
import costa.evandro.smartlightswitch.fragments.CadastroFragment;
import costa.evandro.smartlightswitch.fragments.ControleFragment;
import costa.evandro.smartlightswitch.fragments.SetupWifiFragment;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    //Adapter
    public static ControleAdapter controleAdapter;
    public static CadastroAdapter cadastroAdapter;

    static WebView wbvAlarme;

    public static boolean swt1 = false,
            swt2 = false, swt3 = false,
            swt4 = false, swt5 = false,
            swt6 = false, swt7 = false,
            swt8 = false, swt9 = false,
            swt10 = false;

    Dialog dialog_add;
    Dialog dialogInfo_add;

    public String portal_config = "http://192.168.4.1";
    public static String url_base = "http://192.168.0.";
    public static String desativar_alarme = "/desativar";
    public static String ligar = "/lamp=on";
    public static String desligar = "/lamp=off";
    public static String update = "/update";
    public Context ctx;

    public int AGIR_EM_TODOS = 0;
    public static int AGIR_EM_UM = 1;

    //Código criado para telas fragment
    public static final int TELA_CONTROLE = 0;
    public static final int TELA_CADASTRO = 1;
    public static final int TELA_WIFISETUP = 2;
    public static final int TELA_AJUDA = 3;

    //Fragments
    ControleFragment controleFragment;
    CadastroFragment cadastroFragment;
    AjudaFragment ajudaFragment;
    SetupWifiFragment setupWifiFragment;

    private ViewPager viewPager;
    MenuItem prevMenuItem;

    private BottomNavigationView bottomNavigationView;
    static ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        SQLHelper.getInstance().criar(this);
        Context context = MyApp.getmContext();
        // Instancio um viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Crio adapter para troca de telas
        PageAdapter pageAdapter = new PageAdapter(this, getSupportFragmentManager());

        // passo meu adapter como parametro para view pager
        viewPager.setAdapter(pageAdapter);

        // Give the TabLayout the ViewPager
//        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
//        tabLayout.setupWithViewPager(viewPager);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        controleAdapter = new ControleAdapter(this);
        cadastroAdapter = new CadastroAdapter(this);
        wbvAlarme = (WebView) findViewById(R.id.wbv_alarm);
        //Instancio todos os fragments
        controleFragment = ControleFragment.getInstance();//Construtor Singleton
        cadastroFragment = CadastroFragment.getInstance();//Construtor Singleton
        ajudaFragment = AjudaFragment.getInstance();//Construtor Singleton
        setupWifiFragment = SetupWifiFragment.getInstance();

        ctx = this.getApplicationContext();

        mostraTela(TELA_CONTROLE);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setSelectedItemId(R.id.home);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Smart Switch");
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setLogo(R.drawable.smart_logo);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setListeners();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public abstract class UpdateResult {
        public abstract void updateStatus(Integer status);
    }

    public static void exibirBarra(boolean exibir) {
        mProgressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    public static void exibirProgresso(Integer values) {
        mProgressBar.setProgress(values);
    }

    //Create a method to implement listeners over all Navigation Views
    private void setListeners() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    public static void receiveId(int ip) {
        wbvAlarme.loadUrl("http://192.168.0." + ip + "/alarmar");

    }

    public static void stopAlarmId(int ip) {
        wbvAlarme.loadUrl("http://192.168.0." + ip + "/desativar");
    }


    public void mostraTela(int tela) {

        // essa linha é responsável por adicionar o fragment ao stack

        switch (tela) {
            case TELA_CONTROLE:
                viewPager.setCurrentItem(0);
                break;
            case TELA_CADASTRO:
                viewPager.setCurrentItem(1);
                break;
                case TELA_WIFISETUP:
                    viewPager.setCurrentItem(2);
                break;
            case TELA_AJUDA:

                viewPager.setCurrentItem(3);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sobre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.info) {
            Toast.makeText(this, "Smart Switch versão 1.0", Toast.LENGTH_SHORT).show();
        }
        //  Se o item pressionado for o smart, abre o dialogo correspondente
        if (item.getItemId() == R.id.smart) {
            show_smartdialog();
        }

        if (item.getItemId() == R.id.settng) {
            showinfodialow_add();
        }

        return true;
    }

    //********************  SHOW DIALOG  *********************
    public void show_smartdialog() {

        //Cria instancia do banco de dados
        SQLHelper.getInstance().criar(this);
        final ArrayList<Ambiente> ambientes;
        //Listo ambientes cadastrados
        ambientes = SQLHelper.getInstance().carregarValor();
        //Verifico a quantidade de ambientes e guardo em uma variável para pode utilizar posteriormente
        final int sizeAmbientes = ambientes.size();

        dialog_add = new Dialog(this);
        dialog_add.setCancelable(true);
        dialog_add.setContentView(R.layout.smart_layout);

        //Crio instancia dos botões e texto do dialogo
        Button btn_Voltar = dialog_add.findViewById(R.id.btn_voltar);
        Button btn_Acender = dialog_add.findViewById(R.id.btn_acender);
        Button btn_Apagar = dialog_add.findViewById(R.id.btn_apagar);
         TextView txtTitle = dialog_add.findViewById(R.id.txtTitle);

        //Ao clicar no fora do dialogo a ação será cancelada
        btn_Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_add.dismiss();
            }
        });

        //Ao clicar acender, ativa todos os ambientes
        btn_Acender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UrlActionsTask(url_base, ligar,"", AGIR_EM_TODOS).execute();
                controleAdapter.notifyDataSetChanged();

                //change_SWT(true, 10);
                dialog_add.dismiss();
            }

        });
        btn_Apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UrlActionsTask(url_base, desligar,"", AGIR_EM_TODOS).execute();
                controleAdapter.notifyDataSetChanged();
                //change_SWT(false, 10);
                dialog_add.dismiss();
            }
        });
        dialog_add.show();
    }

    //********************  SHOW DIALOG  *********************
    public void showinfodialow_add() {

        dialogInfo_add = new Dialog(this);
        dialogInfo_add.setCancelable(true);
        dialogInfo_add.setContentView(R.layout.info_layout);

        Button btnVoltar = dialogInfo_add.findViewById(R.id.voltar);


        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInfo_add.dismiss();
            }

        });

        dialogInfo_add.show();
    }

    public static void change_SWT(boolean state, int pos) {
        switch (pos) {
            case 1:
                swt1 = state;
                break;
            case 2:
                swt2 = state;
                break;
            case 3:
                swt3 = state;
                break;
            case 4:
                swt4 = state;
                break;
            case 5:
                swt5 = state;
                break;
            case 6:
                swt6 = state;
                break;
            case 7:
                swt7 = state;
                break;
            case 8:
                swt8 = state;
                break;
            case 9:
                swt9 = state;
                break;
            case 10:
                swt10 = state;
                break;
                case 11:
                swt1 = state;
                swt2 = state;
                swt3 = state;
                swt4 = state;
                swt5 = state;
                swt6 = state;
                swt7 = state;
                swt8 = state;
                swt9 = state;
                swt10 = state;
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        FragmentManager fm = this.getSupportFragmentManager();

        //If item is Checked make it unchecked
        if (item.isChecked())
            item.setChecked(false);
        switch (item.getItemId()) {
            case R.id.home:
                viewPager.setCurrentItem(0);
                //Check the Item
                item.setChecked(true);
                break;
            case R.id.cadastro:
                viewPager.setCurrentItem(1);
                //Check the Item
                item.setChecked(true);
                break;
            case R.id.dispositivos:
                //Check the Item
                item.setChecked(true);
                viewPager.setCurrentItem(2);
                break;
            case R.id.ajuda:
                //Check the Item
                item.setChecked(true);
                viewPager.setCurrentItem(3);
                break;
        }
        return true;
    }


}

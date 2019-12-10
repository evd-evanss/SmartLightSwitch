package costa.evandro.smartlightswitch.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import costa.evandro.smartlightswitch.MainActivity;
import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.adapter_ambiente.Ambiente;
import costa.evandro.smartlightswitch.adapter_ambiente.CadastroAdapter;
import costa.evandro.smartlightswitch.adapter_ambiente.SQLHelper;

public class CadastroFragment extends Fragment {
    private static CadastroFragment instance;
    int LAMPADA = 97;
    int TOMADA = 98;
    Context ctx;
    Dialog dialog_add;
    public final int[] dispositivo = {0};

    public static CadastroFragment getInstance() {
        if (instance == null ) {
            instance = new CadastroFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ctx = activity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //LINEARlAYOUT 1
        SQLHelper.getInstance().criar((Activity) ctx);

        final RecyclerView minhaRecyclerView = view.findViewById(R.id.cadastro_recyclerview);
        MainActivity.cadastroAdapter = new CadastroAdapter(ctx);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        minhaRecyclerView.setLayoutManager(layoutManager);
        minhaRecyclerView.addItemDecoration(new DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL));
        minhaRecyclerView.setAdapter(MainActivity.cadastroAdapter);

        //Botão flutuante
        FloatingActionButton fab =  view.findViewById(R.id.fab_cadastro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialow_add();
            }
        });

    }

    //********************  SHOW DIALOG  *********************
    public void showdialow_add() {
        //Instacia de dialogo
        dialog_add = new Dialog(ctx);
        dialog_add.setCancelable(true);
        dialog_add.setContentView(R.layout.add_ambiente_layout);

        //Crio Itens do Dialogo
        Button btn_Salvar = dialog_add.findViewById(R.id.btn_salvar_cenario);
        Button btn_Cancel =  dialog_add.findViewById(R.id.btn_cancel_cenario);
        final Switch swt_Choose =  dialog_add.findViewById(R.id.escolha_dispositivo);
        final EditText edtCenarioadd = dialog_add.findViewById(R.id.cria_cenario);
        final EditText edt_Ip = dialog_add.findViewById(R.id.edt_ip_cenario);
        edtCenarioadd.requestFocus();


        btn_Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String primary_key = "";
                String IMAGE_DEFAULT = String.valueOf(99);

                if(swt_Choose.isChecked()){
                    dispositivo[0] = TOMADA;
                }else{
                    dispositivo[0] = LAMPADA;
                }


                String update_Nome = edtCenarioadd.getText().toString();
                String update_Id = edt_Ip.getText().toString();
                boolean id_verifica = true;
                boolean nome_verifica = true;

                if((update_Id.contentEquals(""))&&(update_Nome.contentEquals(""))){
                    Toast.makeText(ctx, "Os campos estão em branco, digite um cenario e um IP", Toast.LENGTH_SHORT).show();
                    edtCenarioadd.requestFocus();
                    id_verifica = false;
                    nome_verifica = false;
                }else {
                    if(update_Id.contentEquals("")){
                        Toast.makeText(ctx, "O campo ID está em branco, digite um ID válido", Toast.LENGTH_SHORT).show();
                        edt_Ip.requestFocus();
                        id_verifica = false;
                    }
                    if(update_Nome.contentEquals("")){
                        Toast.makeText(ctx, "O campo Nome está em branco, digite um cenario", Toast.LENGTH_SHORT).show();
                        edtCenarioadd.requestFocus();
                        nome_verifica = false;
                    }
                }
                if((id_verifica==true)&&(nome_verifica==true)){
                    Ambiente ambiente = new Ambiente(edtCenarioadd.getText().toString(),
                            edt_Ip.getText().toString(), primary_key,IMAGE_DEFAULT,"", "", String.valueOf(dispositivo[0]));

                    //Insere valores no db
                    SQLHelper.getInstance().inserirValor(ambiente);
                    MainActivity.cadastroAdapter.setAmbientes();
                    MainActivity.controleAdapter.setAmbientes();

                    Toast.makeText(ctx, "Cenário: "+edtCenarioadd.getText().toString()+" cadastrado...", Toast.LENGTH_SHORT).show();
                    edtCenarioadd.setText("");
                    edt_Ip.setText("");
                    dialog_add.dismiss();
                }


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

}

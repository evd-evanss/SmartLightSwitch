package costa.evandro.smartlightswitch.Controllers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import costa.evandro.smartlightswitch.Models.Ambiente;
import costa.evandro.smartlightswitch.Models.SQLHelper;
import costa.evandro.smartlightswitch.Views.MainActivity;
import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.Models.Icones;

public class CadastroAdapter extends RecyclerView.Adapter {
    Context ctx;
    ArrayList<Ambiente> ambientes;
    String PRIMARY_KEY;
    int LAMPADA = 97;
    int TOMADA = 98;

    Dialog dialog_clear;
    Dialog dialog_include;
    Dialog dialog_icons;
    public final int[] img_selected = {0};
    public final int[] dispositivo = {0};

    //Construtor
    public CadastroAdapter(Context ctx) {
        this.ctx = ctx;
        this.ambientes = SQLHelper.getInstance().carregarValor();
        notifyDataSetChanged();
    }

    public  void setAmbientes(){
        this.ambientes.clear();
        this.ambientes = SQLHelper.getInstance().carregarValor();
        notifyDataSetChanged();
    }

    public void deleteAmbiente(){
        this.ambientes.clear();
        this.ambientes = SQLHelper.getInstance().carregarValor();
        notifyDataSetChanged();
    }

    //Relaciona o layout com o view holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_cadastros, parent, false);
        return new MeuViewHolder(v);
    }

    //
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final MeuViewHolder holder = ((MeuViewHolder) viewHolder);
        final Ambiente ambiente = ambientes.get(position);
        final Icones icon = new Icones();

        holder.txtNome.setText(ambiente.cenario);
        holder.txtId.setText(ambiente.ip);
        holder.iconeDefault.setImageResource(icon.getImg(Integer.parseInt(ambiente.icone)));
        holder.iconeDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAlteraIcones(ambiente.primary_key, position);
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dialogAlteraCenario(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogApagaCenario(ambiente.primary_key);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ambientes.size();
    }

    //instancia os componentes do layout
    public class MeuViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;
        TextView txtId;
        ImageButton edit;
        ImageButton delete;
        ImageView iconeDefault;


        public MeuViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txt_nome);
            txtId = itemView.findViewById(R.id.txt_id);
            edit = itemView.findViewById(R.id.altera_cenario);
            delete = itemView.findViewById(R.id.delet_cenario);
            iconeDefault = itemView.findViewById(R.id.change_icon);
        }
    }

    //********************  SHOW DIALOG DE ALTERAÇÃO DE CENÁRIO *********************
    public void dialogAlteraCenario(final int position) {
        //final int ambiente_id = id;
        final int pos = position;
        final Ambiente ambiente = ambientes.get(pos);
        //Instacio e Inflo um Dialogo
        dialog_include = new Dialog(ctx);
        dialog_include.setCancelable(true);
        dialog_include.setContentView(R.layout.update_ambiente_layout);
        //Crio as Views do dialogo
        final Switch swt_Alterar = dialog_include.findViewById(R.id.altera_dispositivo);
        Button btn_Alterar = dialog_include.findViewById(R.id.btn_alterar_cenario);
        Button btn_Cancel =  dialog_include.findViewById(R.id.btn_cancel_alterar);
        final EditText edtCenarioadd = dialog_include.findViewById(R.id.altera_cenario);
        final EditText edt_Ip = dialog_include.findViewById(R.id.altera_ip_cenario);

        edtCenarioadd.setText(ambiente.cenario);
        edtCenarioadd.requestFocus();
        edt_Ip.setText(ambiente.ip);


        btn_Alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(swt_Alterar.isChecked()){
                    dispositivo[0] = TOMADA;
                }else{
                    dispositivo[0] = LAMPADA;
                }
                int img = img_selected[0];
                String img_sel = String.valueOf(img);


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
                    ambiente.setCenario(update_Nome);
                    ambiente.setIp(update_Id);
                    SQLHelper.getInstance().alterarValor(update_Nome,update_Id, ambiente.primary_key, img_sel, String.valueOf(dispositivo[0]));
                    notifyItemChanged(pos);
                    Toast.makeText(ctx, "Cenário alterado...", Toast.LENGTH_SHORT).show();
                    dialog_include.dismiss();
                }



            }

        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_include.dismiss();
            }
        });
        dialog_include.show();
    }

    //********************  SHOW DIALOG PARA APAGAR CENÁRIO *************************
    public void dialogApagaCenario(final String primary_key) {
        SQLHelper.getInstance().criar((Activity) ctx);
        PRIMARY_KEY = primary_key;

        dialog_clear = new Dialog(ctx);
        dialog_clear.setTitle("Deseja apagar o ambiente?");
        dialog_clear.setCancelable(true);
        dialog_clear.setContentView(R.layout.clear_ambiente_layout);

        Button btn_Apagar = dialog_clear.findViewById(R.id.btn_apagar);
        Button btn_Cancel =  dialog_clear.findViewById(R.id.btn_cancel);

        btn_Apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PRIMARY_KEY != "") {
                SQLHelper.getInstance().apagaValor(PRIMARY_KEY);
                setAmbientes();
                deleteAmbiente();
                MainActivity.controleAdapter.deleteAmbiente();

                dialog_clear.dismiss();
                }
                else{
                    Toast.makeText(ctx, "Ambiente nullo", Toast.LENGTH_SHORT).show();
                }


            }

        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_clear.dismiss();
            }
        });
        dialog_clear.show();
    }

    //********************  SHOW DIALOG PARA ALTERAR ICONES DE CENÁRIO ***************
    public void dialogAlteraIcones(final String key, final int pos){
        SQLHelper.getInstance().criar((Activity) ctx);
        IconeAdapter iconeAdapter;
        dialog_icons = new Dialog(ctx);
        dialog_icons.setCancelable(true);
        dialog_icons.setContentView(R.layout.fragment_icones);

        //Adapter
        RecyclerView minhaRecyclerView = dialog_icons.findViewById(R.id.icones_recyclerview);
        iconeAdapter = new IconeAdapter(ctx, dialog_icons, key, pos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        minhaRecyclerView.setLayoutManager(layoutManager);
        minhaRecyclerView.addItemDecoration(new DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL));
        minhaRecyclerView.setAdapter(iconeAdapter);

        dialog_icons.show();
    }

}


package costa.evandro.smartlightswitch.Controllers;


import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import costa.evandro.smartlightswitch.Models.SQLHelper;
import costa.evandro.smartlightswitch.Views.MainActivity;
import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.Models.Icones;


public class IconeAdapter extends RecyclerView.Adapter {
    Context ctx;
    ArrayList<Icones> icones;
    boolean state_swt = false;
    String key;
    int pos;
    Dialog dialog_icones;

    //Construtor
    public IconeAdapter(Context ctx, Dialog dialog, String _key, int _pos) {
        this.ctx = ctx;
        this.key = _key;
        this.pos = _pos;
        this.dialog_icones = dialog;
        notifyDataSetChanged();
    }


    //Relaciona o layout com o view holder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_icones1, parent, false);
        return new MeuViewHolder(v);
    }

    //
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final MeuViewHolder holder = ((MeuViewHolder) viewHolder);
        final Icones icon = new Icones();
        holder.img_choose.setImageResource(icon.getImg(position));
        holder.txt_icon.setText(icon.nome_icone);

        holder.img_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLHelper.getInstance().alterarImagem(key, String.valueOf(position));
                MainActivity.cadastroAdapter.setAmbientes();
                MainActivity.controleAdapter.setAmbientes();
                dialog_icones.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 13;
    }

    //instancia os componentes do layout
    public class MeuViewHolder extends RecyclerView.ViewHolder {
        ImageView img_choose;
        TextView txt_icon;


        public MeuViewHolder(View itemView) {
            super(itemView);
            img_choose = itemView.findViewById(R.id.img_choose);
            txt_icon = itemView.findViewById(R.id.nome_icon);

        }
    }

}

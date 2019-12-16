package costa.evandro.smartlightswitch.Views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import costa.evandro.smartlightswitch.R;
import costa.evandro.smartlightswitch.Controllers.ControleAdapter;
import costa.evandro.smartlightswitch.Models.SQLHelper;

import static costa.evandro.smartlightswitch.Views.MainActivity.controleAdapter;


public class ControleFragment extends Fragment {
    private static ControleFragment instance;
    ArrayList<String> produtos;
    ArrayAdapter adapter;

    Context ctx;


    public static ControleFragment getInstance() {
        if (instance == null) {
            instance = new ControleFragment();
        }
        return instance;
    }

    public void addProduto(String produto){
        instance.produtos.add(produto);
        if(instance.adapter != null){
            instance.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ctx = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_controle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SQLHelper.getInstance().criar((Activity) ctx);
        //DbImage.getInstance().criar((Activity) ctx);


        //Adapter
        RecyclerView minhaRecyclerView = view.findViewById(R.id.controle_recyclerview);
        MainActivity.controleAdapter = new ControleAdapter(ctx);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        minhaRecyclerView.setLayoutManager(layoutManager);
        minhaRecyclerView.addItemDecoration(new DividerItemDecoration(ctx, DividerItemDecoration.VERTICAL));
        minhaRecyclerView.setAdapter(controleAdapter);

    }
}

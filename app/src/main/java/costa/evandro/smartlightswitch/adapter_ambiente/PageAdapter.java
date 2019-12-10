package costa.evandro.smartlightswitch.adapter_ambiente;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import costa.evandro.smartlightswitch.fragments.AjudaFragment;
import costa.evandro.smartlightswitch.fragments.CadastroFragment;
import costa.evandro.smartlightswitch.fragments.ControleFragment;
import costa.evandro.smartlightswitch.fragments.SetupWifiFragment;

/**
 * Created by teste on 17/11/2017.
 */

public class PageAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PageAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new ControleFragment();
        } else if (position == 1){
            return new CadastroFragment();
        } else if (position == 2){
        return new SetupWifiFragment();
    }else{
            return new AjudaFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        String titulo;
        switch (position) {
            case 0:
                return titulo = "Controle";
            case 1:
                return titulo = "Cadastro";
            case 2:
                return titulo = "Procurar Dispositivos";
            case 3:
                return titulo = "Ajuda";
            default:
                return null;
        }
    }

}

package costa.evandro.smartlightswitch.Models;

/**
 * Created by Evandro Costa on 15/07/2017.
 */

public class DadosWifi {
    String ssid;
    String senha;

    public DadosWifi(String ssid_, String senha_){
        ssid = ssid_;
        senha = senha_;
    }

    public void setSenha(String senha_){
        senha = senha_;
    }
    public void setSsid(String ssid_){
        ssid = ssid_;
    }

    public String getSenha(){
        return senha;
    }
    public String getSsid(){
        return ssid;
    }

}

package costa.evandro.smartlightswitch.alarme;

/**
 * Created by teste on 01/11/2017.
 */

public class Alarme {

    public String PRIMARY_KEY;
    public int hora = 00;
    public int minuto = 00;

    public Alarme(){
        this.hora = hora;
        this.minuto = minuto;
    }

    public void setPRIMARY_KEY(String pKey){
        this.PRIMARY_KEY = pKey;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

}

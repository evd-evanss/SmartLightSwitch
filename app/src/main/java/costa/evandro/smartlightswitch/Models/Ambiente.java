package costa.evandro.smartlightswitch.Models;

/**
 * Created por Evandro Ribeiro
 */

public class Ambiente {
    public String cenario;
    public String ip;
    public String primary_key;
    public String icone;
    public String horas;
    public String minutos;
    public String dispositivo;
    public String backup;

    public Ambiente(String cenario, String ip, String primary_key,
                    String icone, String hora_, String minuto_, String _dispositivo) {
        this.cenario = cenario;
        this.ip = ip;
        this.primary_key = primary_key;
        this.icone = icone;
        this.horas = hora_;
        this.minutos = minuto_;
        this.dispositivo = _dispositivo;
    }

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getCenario(){
        this.cenario = cenario;
        return cenario;
    }

    public String getIp(){
        this.ip = ip;
        return ip;
    }

    public void setPrimary_key(String primary_key) {
        this.primary_key = primary_key;
    }

    public String getPrimary_key(){
        this.primary_key = primary_key;
        return  primary_key;
    }
    public String getHoras(){
        return  this.horas;
    }
    public String getMinutos(){
        return  this.minutos;
    }

    public void setCenario(String nome_){
        this.cenario = nome_;
    }

    public void setIp(String id_number_){
        this.ip = id_number_;
    }

}

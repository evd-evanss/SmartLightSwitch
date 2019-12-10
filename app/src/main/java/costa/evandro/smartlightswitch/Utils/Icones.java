package costa.evandro.smartlightswitch.Utils;

import costa.evandro.smartlightswitch.R;

/**
 * Created by teste on 20/10/2017.
 */

public class Icones {

    int img;
    public String nome_icone;

    public void Icones(int _img){
        img = _img;
    }



    public int getImg(int id_img) {

        switch (id_img){
            case 0:
                img = R.drawable.ic_quarto;
                nome_icone = "cama";
                break;
            case 1:
                img = R.drawable.ic_sala;
                nome_icone = "sofá azul";
                break;
            case 2:
                img = R.drawable.ic_cozinha;
                nome_icone = "geladeira";
                break;
            case 3:
                img = R.drawable.ic_lavand;
                nome_icone = "máq de lavar";
                break;
            case 4:
                img = R.drawable.ic_estac;
                nome_icone = "carro vermelho";
                break;
            case 5:
                img = R.drawable.ic_hall;
                nome_icone = "hall";
                break;
            case 6:
                img = R.drawable.ic_garage;
                nome_icone = "garagem";
                break;
            case 7:
                img = R.drawable.ic_sofa;
                nome_icone = "sofá verde";
                break;
            case 8:
                img = R.drawable.ic_loussa;
                nome_icone = "louça";
                break;
            case 9:
                img = R.drawable.ic_tv;
                nome_icone = "televisão";
                break;
            case 10:
                img = R.drawable.ic_shower;
                nome_icone = "box";
                break;
            case 11:
                img = R.drawable.ic_berco;
                nome_icone = "berço";
                break;
            case 12:
                img = R.drawable.ic_bed;
                nome_icone = "cama";
                break;
            case 97:
                img = R.drawable.ic_bulb;
                break;
            case 98:
                img = R.drawable.ic_plug;
                break;

            case 99:
                img = R.drawable.ic_cenario;
                break;
        }

        return img;
    }
}

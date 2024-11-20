package UNLu.Poo.Uno.Modelo;

import java.security.PublicKey;

public class CartaEspecial extends Carta{
    private TipoDeCartaEspecial tipo;
    public CartaEspecial(Color color, TipoDeCartaEspecial tipo) {
        super(color);
        this.tipo= tipo;
    }

    public TipoDeCartaEspecial getTipo(){
        return tipo;
    }

    @Override
    public String mostrarCarta() {
        if (getTipo() != null) {
            return ("{"+this.getColor() + "-" + this.getTipo()+"}");
        } else
            return null;
    }

    @Override
    public boolean esJugableSobre(Carta cartaTope,Color color) {
        if (this.tipo== TipoDeCartaEspecial.MASCUATRO || this.tipo == TipoDeCartaEspecial.CAMBIODECOLOR) {
            return true;
        }else if(this.color==cartaTope.getColor()||this.color==color){
            return true;
        }else if(cartaTope instanceof CartaEspecial){
            return (this.tipo==((CartaEspecial)cartaTope).getTipo());
        }
        return false;
    }
}

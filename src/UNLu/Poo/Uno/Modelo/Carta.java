package UNLu.Poo.Uno.Modelo;

import java.awt.*;

public abstract class Carta{
    protected Color color;

    public Carta(Color color){
        this.color= color;
    }

    public Color getColor(){
        return color;
    }

    public void setColor(Color color){
        this.color= color;
    }

    public abstract String mostrarCarta();

    public abstract boolean esJugableSobre(Carta cartaTope, Color color);
}

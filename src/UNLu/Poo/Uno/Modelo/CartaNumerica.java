package UNLu.Poo.Uno.Modelo;

public class CartaNumerica extends Carta {
    private int numero;

    public CartaNumerica(Color color, int numero) {
        super(color);
        this.numero = numero;
    }

    private int getNumero() {
        return numero;
    }

    @Override
    public String mostrarCarta() {
        if (getColor() != null) {
            return ("{"+getColor() + "-" + getNumero()+"}");
        } else {
            return null;
        }
    }

    @Override
    public boolean esJugableSobre(Carta cartaTope,Color color) {
        if (this.color == cartaTope.getColor()||this.color==color) {
            return true;
        }
        else if(cartaTope instanceof CartaNumerica) {
            return (this.numero == ((CartaNumerica) cartaTope).getNumero());
        }
        return false;
    }
}

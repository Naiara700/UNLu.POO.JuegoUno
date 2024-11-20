package UNLu.Poo.Uno.Modelo;

import java.util.ArrayList;

public class Mano {
    private ArrayList<Carta> cartasEnMano;

    public Mano() {
        cartasEnMano = new ArrayList<>();
    }
    public void agregarCarta(Carta carta) {
        cartasEnMano.add(carta);
    }
    public Carta jugarCarta(int indice) {
        if (indice >= 0 && indice < cartasEnMano.size()) {
            return cartasEnMano.remove(indice);
        } else {
            return null;
        }
    }

    public ArrayList<Carta> obtenerCartas() {
        return cartasEnMano;
    }

    public int cantidadDeCartas() {
        return cartasEnMano.size();
    }

    public void limpar(){
        this.cartasEnMano.clear();
    }
    public int calcularPuntosM(){
        int  puntos=0 ;
        for(Carta c : cartasEnMano){
            if(c instanceof CartaEspecial){//me fijo si es especial
                if(((CartaEspecial) c).getTipo()== TipoDeCartaEspecial.MASDOS||((CartaEspecial) c).getTipo()== TipoDeCartaEspecial.REVERSA||((CartaEspecial) c).getTipo()== TipoDeCartaEspecial.BLOQUEO){
                    puntos+=20;
                }else{
                    puntos+=50;
                }
            }
            //Preguntar si sumar las comunes tambien
        }
        return puntos;
    }

    public boolean contieneCarta(Carta carta) {
        //no se si lo voy a hacer
        return true;
    }

    @Override
    public String toString() {
        if (cartasEnMano.isEmpty()) {
            return "La mano está vacía.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Mano: ");
        for (Carta carta : cartasEnMano) {
            sb.append(carta.toString()).append(" ");
        }
        return sb.toString().trim();
    }
}

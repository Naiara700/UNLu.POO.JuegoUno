package UNLu.Poo.Uno.Modelo;

import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private Mano mano;
    private int puntos;
    protected boolean turno;
    private int victorias = 0;

    public Jugador(String nombre){
        this.nombre = nombre;
        this.puntos=0;
        this.victorias = 0;
        this.mano = new Mano();
    }

    //Gets
    public String getNombre() {
        return nombre;
    }
    public int getCantCartaJugador(){
        return mano.cantidadDeCartas();
    }
    public int getPuntos() {
        return puntos;
    }
    public boolean getTurno() {
        return this.turno;
    }
    public Mano getMano(){
        return mano;
    }
    public int getVictorias(){
        return this.victorias;
    }


    public void aumentarPuntos(int puntos) {
        this.puntos += puntos;
    }
    public void aumentaVictoria() {
        this.victorias++;
    }


    public void robaCarta(Carta carta){
        mano.agregarCarta(carta);
    }
    public Carta desacartar(int indice){
        return mano.jugarCarta(indice);
    }

    public boolean UNO(){
        return mano.cantidadDeCartas()==1;
    }

    public void setMano(Mano mano){
        this.mano = mano;
    }
    public void volverA0Puntos(){//Ver que onda. Esto no deberia estar aca
        this.puntos=0;
    }
    public void ordenarPorColor() {//Ordenar las cartas por colores
    //Ver despues que onda
    }
    public void limpiarMano(){
        mano.limpar();
    }

    public int getCantCartas() {
        return this.mano.cantidadDeCartas();
    }

    public String mostrarMano() {
        return mano.toString();
    }
}

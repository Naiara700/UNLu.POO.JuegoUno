package UNLu.Poo.Uno.Controlador;

import UNLu.Poo.Uno.Modelo.*;
import UNLu.Poo.Uno.Vista.VistaConsola;

import java.util.ArrayList;

import static UNLu.Poo.Uno.Modelo.EventosUNO.JUEGO_INICIADO;

public class Controlador implements IObservador {
    private JuegoUno juegoUno;
    private VistaConsola vistaConsola;
    private String jugador;
    private boolean RobarCarta;

    public Controlador(JuegoUno juegoUNO){
        this.juegoUno = juegoUNO;
        this.juegoUno.registrar(this);
        RobarCarta = true;
    }
    public void setVistaConsola(VistaConsola vistaConsola) {
        this.vistaConsola = vistaConsola;
    }

    public void AgregarJugador(String name) {
        //Jugador jugador = new Jugador(name);
        this.juegoUno.agregarJugador(name);
    }

    public void setJuegoUno(JuegoUno juegoUno) {
        this.juegoUno = juegoUno;
    }

    public int getEstado(){
        return juegoUno.getMiEstado();
    }

    public String getGanador(){
       return juegoUno.getGanador().getNombre();
    }

    public void iniciarJuego(){
        juegoUno.inicioJuego();
    }

    public void cambiarTurno(){
        juegoUno.obtenerSiguienteJugador();
    }

    public ArrayList<Jugador> getJugadores(){
        return juegoUno.getJugadores();
    }

    public void repartirCartas(){
        juegoUno.repartir();
    }

    public int cantdadDeCartas(){
        return juegoUno.getCartasA();
    }

    public Carta mostrarDescarte(){
        return juegoUno.obtenerUltimaCartaTirada();
    }

    public Jugador getJugadorEnTurno(){
        return juegoUno.getJugadorActual();
    }

    public void robarCarta() {
        if(RobarCarta) {
            juegoUno.robarCarta();
        }
            RobarCarta=false;
    }

    public ArrayList<Carta> mostrarCartas(){
        return juegoUno.mostrarCarta();

    }

    public void tirarCarta(int numero){
        juegoUno.tirarCarta(numero);
        RobarCarta= true;
    }

    public void terminarMano(){
        juegoUno.terminaMano();
    }

    public boolean hayCartas(){//no se si lo necesito
        return juegoUno.thereAreCartas();
    }

    public int cantidadDeCatasDescarte(){
        return juegoUno.getCartasDescarte();
    }

    public void reiniciarMano(){
        juegoUno.terminaMano();
    }

    public int cantDerondas(){
        return juegoUno.getCantronda();
    }

    public void reiniciarPartida(){
        juegoUno.reiniciaJuego();
    }



    private boolean manejaTurno(){
        if (juegoUno.getJugadorActual().getNombre().equals(this.jugador))
            return true;
        return  juegoUno.getMiEstado() == JuegoUno.JUGANDO;
    }

    public void solicitarColor() {
        // Llama a la vista para que muestre una interfaz o mensaje que permita seleccionar el color
        Color colorSeleccionado = vistaConsola.seleccionarColor();

        // Una vez seleccionado, establece el color en el juego
        juegoUno.cambiarColor(colorSeleccionado);

        // Notifica a los demás jugadores que el color ha cambiado
        vistaConsola.notificarMensaje("El color ha sido cambiado a: " + colorSeleccionado);
    }

    public void paso(){
        if(!RobarCarta) {
            juegoUno.avanzarTurno();
        }RobarCarta= true;
    }

    @Override
    public void actualizar(EventosUNO event) {
        switch (event) {
            case JUEGO_INICIADO:
                System.out.println("Evento: JUEGO_INICIADO"); // Debug
                if (manejaTurno()) {
                    System.out.println("Llamando a juegoIniciado()");
                    vistaConsola.juegoIniciado();
                } else {
                    System.out.println("Llamando a métodos de vista para mostrar mesa y turno");
                    vistaConsola.juegoIniciado();
                    vistaConsola.turno();
                    //vistaConsola.deshabilitaBotones();
                    vistaConsola.mostrarCartasJugadorNoActual();
                }
                break;
            case FIN_JUEGO:
                this.jugador = null;
                vistaConsola.finJuego();
                break;

            case PUNTOS_CAMBIADOS:
                vistaConsola.reiniciarMano();
                if (manejaTurno())
                    vistaConsola.actualizarCartas();
                else
                    vistaConsola.mostrarCartasJugadorNoActual();
                break;

            case JUGADOR_AGREGADO:
                vistaConsola.actualizaPuntaje();
                break;

            case JUGADORES_MAXIMOS:
                vistaConsola.notificarMensaje("Se ha alcanzado el máximo de jugadores.");
                break;

            case FALTA_UNO:
                vistaConsola.notificarError("Debe cargar un jugador mas para iniciar la partida");;
                break;

            case CARTA_ROBADA:
                if (manejaTurno())
                    vistaConsola.actualizarCartas();
                break;

            case CARTA_TIRADA:
                if (manejaTurno()) {
                    vistaConsola.actualizarCartas();
                    vistaConsola.actualizaPuntaje();
                }
                if (cantidadDeCatasDescarte() != 0)
                    vistaConsola.actualizaDescarte();
                else
                    vistaConsola.descarteVacio();
                break;

            case TURNO_CAMBIADO:
                if (manejaTurno())
                    vistaConsola.cambiaTurno();
                else
                    vistaConsola.limpiaTurno();
                break;

            case DEBE_ROBAR:
                if (manejaTurno())
                    vistaConsola.notificarError("Debe robar una carta para tirar");
                break;

            case MISMO_NOMBRE:
                vistaConsola.notificarError("El nombre del jugador ya está en uso. Elige otro nombre.");
                break;

            case MAS4:
                vistaConsola.notificarMensaje("¡El jugador siguiente debe robar 4 cartas!");
                break;

            case MAS2:
                vistaConsola.notificarMensaje("¡El jugador siguiente debe robar 2 cartas!");
                break;

            case CAMBIACOLOR:
                solicitarColor();
                vistaConsola.notificarMensaje("El color del juego ha cambiado.");
                break;

            case BLOQUEO:
                vistaConsola.notificarMensaje("¡Turno bloqueado!");
                break;

            case CAMBIARONDA:
                vistaConsola.notificarMensaje("Se ha cambiado el sentido de la ronda.");
                break;

            case FIN_MANO:
                vistaConsola.notificarMensaje("La mano ha terminado.");
                break;

            case JUGADA_INVALIDA:
                vistaConsola.notificarError("Jugada inválida. Intenta con otra carta.");
                break;

            case SOLICITAR_COLOR:
                if (manejaTurno()) { // Solo el jugador actual puede cambiar el color
                    solicitarColor();
                } else {
                    vistaConsola.notificarMensaje("Esperando a que el jugador actual seleccione un color.");
                }
                break;
            case UNO:
                vistaConsola.notificarMensaje("¡UNO! Al jugador le queda una carta.");
                break;
        }
    }
    public ArrayList<Jugador> getListaJugadores(){
        return this.juegoUno.getCantidadJugadores();
    }

    public void mostrarManosJugadores() {
        for (Jugador jugador : juegoUno.getJugadores()) {
            System.out.println(jugador.getNombre() + " tiene: " + jugador.mostrarMano());
        }
    }
}

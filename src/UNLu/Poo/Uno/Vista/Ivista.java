package UNLu.Poo.Uno.Vista;

import UNLu.Poo.Uno.Controlador.Controlador;

public interface Ivista {
    void actualizaPuntaje();

    void setControlador(Controlador controlador);

    void reiniciarMano();

    void finJuego();

    void notificarMensaje(String string);

    void notificarError(String mensaje);

    void juegoIniciado();

    void turno();

    void MouseClicked(Object objeto);

    void verTop();

    void cargaJuego() ;

    void iniciar();

    void actualizarCartas() ;

    void actualizaDescarte() ;

    void robaCartaDescarte() ;

    void cambiaTurno();

    void limpiaTurno();

    void deshabilitaBotones();

    void mostrarCartasJugadorNoActual();

    void descarteVacio();

    void muestraMesa();
}

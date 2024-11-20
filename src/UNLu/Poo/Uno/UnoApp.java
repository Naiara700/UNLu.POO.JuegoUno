package UNLu.Poo.Uno;

import UNLu.Poo.Uno.Controlador.Controlador;
import UNLu.Poo.Uno.Modelo.JuegoUno;
import UNLu.Poo.Uno.Vista.Ivista;
import UNLu.Poo.Uno.Vista.VistaConsola;

public class UnoApp {
    public static void main(String[] args) {
        JuegoUno juegoUno = new JuegoUno();
        Controlador controlador = new Controlador(juegoUno);
        VistaConsola vistaConsola = new VistaConsola(controlador);

        controlador.setVistaConsola(vistaConsola);
        vistaConsola.iniciar();
    }
}

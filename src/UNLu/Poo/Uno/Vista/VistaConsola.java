package UNLu.Poo.Uno.Vista;

import UNLu.Poo.Uno.Controlador.Controlador;
import UNLu.Poo.Uno.Modelo.Carta;
import UNLu.Poo.Uno.Modelo.Color;
import UNLu.Poo.Uno.Modelo.JuegoUno;
import UNLu.Poo.Uno.Modelo.Jugador;

import java.util.ArrayList;
import java.util.Scanner;

public class VistaConsola implements Ivista{

    private Controlador miControlador;
    private Scanner teclado = new Scanner(System.in);

    public VistaConsola(Controlador controlador) {
        this.miControlador = controlador;
        this.miControlador.setVistaConsola(this);
        //teclado = new Scanner(System.in);
    }

    private int mostrarMenu(){
        int opcion = -1;
        while (opcion < 0 || opcion > 3) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("--                                  UNO                                     --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("--                        Menu de Configuracion                             --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("--                                Opciones                                  --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("-- 1 - Agregar Jugadores                                                    --");
            System.out.println("-- 2 - Mostrar Lista de Jugadores                                           --");
            System.out.println("-- 3 - Comenzar Partida                                                     --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("-- 0 - Salir del Juego                                                      --");
            System.out.println("------------------------------------------------------------------------------");
            opcion = teclado.nextInt();
        }
        return opcion;
    }
    private void agregarJugador(){
        Scanner nombre1 = new Scanner(System.in);
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("--                          Agregando Jugador                               --");
        System.out.println("------------------------------------------------------------------------------");
        System.out.print("-- Ingresar nombre : ");
        String nombre = nombre1.nextLine();
        miControlador.AgregarJugador(nombre);
        System.out.println("Jugador agregado con exito");
        esperarEnter();
    }

    public void mostrarJugadores(ArrayList<Jugador>Jugadores){
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("--                            Lista de Jugadores                            --");
        System.out.println("------------------------------------------------------------------------------");
        for (Jugador j : miControlador.getJugadores())
            System.out.println(j.getNombre());
        esperarEnter();
    }


    public void limpiarPantalla() {
        for (int i = 1; i < 100; i++)
            System.out.println("");
    }

    public void mostrarCartas(){
        ArrayList<Carta> cartas = miControlador.mostrarCartas();
        int i = 1 ;
        for (Carta carta : cartas) {
            System.out.println(i +carta.mostrarCarta());
            i++;
        }
    }

    public String mostrarCartaDescarte(){
        if (miControlador.mostrarDescarte() != null)
            return miControlador.mostrarDescarte().mostrarCarta();
        else
            return null;
    }

    public int mostrarMenuJugador(){
        System.out.println("----------------------------------------------");
        System.out.println("¿Que desea hacer?");
        System.out.println("1- Para robar una carta del mazo");
        System.out.println("2- Tirar carta");
        System.out.println("3- Pasar");
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("CARTA DEL DESCARTE");
        if (mostrarCartaDescarte() == null)
            System.out.println("Descarte vacio");
        else
            System.out.println(mostrarCartaDescarte());
        System.out.println("-------------------------------");
        System.out.println("-------------------------------");
        System.out.println("Cartas del jugador actual");
        mostrarCartas();
        int opcion = teclado.nextInt();
        while (opcion < 1 || opcion > 3) {
            System.out.println("Ingrese una opcion valida. (1-3)");
            opcion = teclado.nextInt();}
        return opcion;
    }

    public Color seleccionarColor() {
        System.out.println("Selecciona un color:");
        System.out.println("1. Rojo");
        System.out.println("2. Azul");
        System.out.println("3. Verde");
        System.out.println("4. Amarillo");

        Scanner scanner = new Scanner(System.in);
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1: return Color.ROJO;
            case 2: return Color.AZUL;
            case 3: return Color.VERDE;
            case 4: return Color.AMARILLO;
            default:
                System.out.println("Opción inválida. Selecciona un color válido.");
                return seleccionarColor(); // Vuelve a pedir la selección
        }
    }


    private void tirarCarta(){
        System.out.println("Ingrese la posicion de la carta");
        int numero = teclado.nextInt();
        while (numero < 1 || numero > miControlador.cantdadDeCartas()){
            System.out.println("Ingrese una posicion valida ");
            numero = teclado.nextInt();}
        miControlador.tirarCarta(numero);
        esperarEnter();

        if (miControlador.getEstado() != JuegoUno.FIN_JUEGO) {
            if (miControlador.getEstado() != JuegoUno.FIN_MANO && miControlador.getEstado() != 0) {
                System.out.println("Cambiando turno...");
                miControlador.cambiarTurno();
            }
        }
        esperarEnter();
    }



    public void puntos(){
        for (Jugador jugador : miControlador.getJugadores()) {
            System.out.println("Puntos de " + jugador.getNombre() + " : " + jugador.getPuntos());
        }
    }


    @Override
    public void actualizaPuntaje() {

    }

    @Override
    public void setControlador(Controlador controlador) {
        this.miControlador = controlador;
    }

    @Override
    public void reiniciarMano() {
        System.out.println("Mano finalizada.");
        System.out.println("El ganador de la mano es " + miControlador.getJugadorEnTurno().getNombre());
        puntos();
        miControlador.reiniciarMano();
        esperarEnter();
    }

    @Override
    public void finJuego() {
        System.out.println("-----------------------");
        System.out.println("UNO finalizado");
        System.out.println("-----------------------");
        System.out.println("El ganador es " + miControlador.getGanador());
        puntos();
        System.out.println("Cantidad de rondas " + miControlador.cantDerondas());
        System.out.println("-----------------------");
    }

    @Override
    public void notificarMensaje(String string) {
        System.out.println(string);
    }

    @Override
    public void notificarError(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public void juegoIniciado() {
        while (miControlador.getEstado() != JuegoUno.FIN_JUEGO &&
                miControlador.getEstado() != 0) {
            turno();
            int opcion = mostrarMenuJugador();
            switch (opcion) {
                case 1:
                    miControlador.robarCarta();
                    esperarEnter();
                    break;
                case 2:
                    tirarCarta();
                    break;
                case 3:
                    pasar();
                    break;
            }
        }
    }

    private void pasar() {
        miControlador.paso();
    }

    @Override
    public void turno() {
        System.out.println("----------------------------------------------");
        System.out.println("Es el turno del jugador : " + miControlador.getJugadorEnTurno().getNombre());
    }

    @Override
    public void MouseClicked(Object objeto) {

    }

    @Override
    public void verTop() {

    }

    @Override
    public void cargaJuego() {
        mostrarMenuJugador();
    }

    @Override
    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            opcion = mostrarMenu();
            switch (opcion) {
                case 1:
                    // Agregar Jugador
                    agregarJugador();
                    break;
                case 2:
                    // mostrar lista de Jugadores
                    mostrarJugadores(miControlador.getJugadores());
                    break;
                case 3:
                    // Comenzar Juego
                    miControlador.iniciarJuego();
                    esperarEnter();
                    break;
            }
        }
        System.out.println("Juego finalizado.");

    }

    @Override
    public void actualizarCartas() {

    }

    @Override
    public void actualizaDescarte() {

    }

    @Override
    public void robaCartaDescarte() {

    }

    @Override
    public void cambiaTurno() {

    }

    @Override
    public void limpiaTurno() {

    }

    @Override
    public void deshabilitaBotones() {

    }

    @Override
    public void mostrarCartasJugadorNoActual() {
        Jugador jugador;
        if (miControlador.getJugadorEnTurno().getNombre().equals(miControlador.getListaJugadores().get(0).getNombre()))
            jugador = miControlador.getListaJugadores().get(1);
        else
            jugador = miControlador.getListaJugadores().get(0);

        for (Carta cartas : jugador.getMano().obtenerCartas()) {
            cartas.mostrarCarta();
        }

    }

    @Override
    public void descarteVacio() {

    }

    @Override
    public void muestraMesa() {

    }

    private void esperarEnter() {
        System.out.print("Presione ENTER para continuar...");
        Scanner sc = new Scanner(System.in);
        String pausa = sc.nextLine();
    }

}

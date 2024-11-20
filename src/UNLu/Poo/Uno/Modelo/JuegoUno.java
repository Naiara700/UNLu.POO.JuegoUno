package UNLu.Poo.Uno.Modelo;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class JuegoUno implements Observado{
    private int Cantronda = 0;
    private MazoPrincipal mazo;
    private PilaDeDescarte descarte;
    private ArrayList<Jugador> jugadores;
    private ArrayList<IObservador> observadores;
    private int turno;
    private Jugador ganador;
    private final int MAX_PUNTOS = 500;
    private int jugadorActual;//eefrerncia
    private boolean sentidoDeRonda = true; //sentido horario
    private Color colorActual;
    private Carta cartaActual; //ver si me sirve mas

    //Estados en el juego
    public static final int SETEANDO = 0;
    public static final int JUGADOR_AGREGADO = 1;
    public static final int JUEGO_INICIADO = 2;
    public static final int JUGANDO = 3;
    public static final int FIN_MANO = 4;
    public static final int FIN_JUEGO = 5;
    public static final int PUNTOS_CAMBIADOS = 8;
    private int miEstado = SETEANDO;


    public JuegoUno() {
        this.jugadores = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.mazo = null;
        this.descarte = null;
        this.turno = 0;
        this.jugadorActual = 0;
        this.ganador = null;

    }


    //Gets
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    public int getCantronda() {
        return Cantronda;
    }
    public PilaDeCartas getMaso() {
        return mazo;
    }
    public Jugador getGanador() { return this.ganador;
    }
    public int getTurno() {
        return turno;
    }
    public PilaDeCartas getDescarte(){return descarte;}
    public int getMiEstado(){
        return miEstado;
    }

    //Movimientos de turnos
    public int obtenerSiguienteJugador() {
        if (sentidoDeRonda) {
            return (jugadorActual + 1) % jugadores.size();
        } else {
            return (jugadorActual - 1 + jugadores.size()) % jugadores.size();
        }
    }
    public void avanzarTurno() {
        jugadorActual = obtenerSiguienteJugador();
        notificar(EventosUNO.TURNO_CAMBIADO);notificar(EventosUNO.TURNO_CAMBIADO);
    }
    public Carta obtenerUltimaCartaTirada(){
        cartaActual = descarte.obtenerUltimaCarta();
        return cartaActual;
    }
    public void esGanador(){
        for(Jugador j : jugadores){
            if(j.getPuntos() == 500){
                ganador = j;
            }
        }//ver que pasa con mas jugadores y como ver que el primero que llegue a 500 gane
    }//algo con el turno

    //JUEGO

    public void inicioJuego() {
        if (this.jugadores.size() >= 2) { // 2 jugadores mínimo
            try {
                System.out.println("Inicializando el mazo...");
                this.mazo = new MazoPrincipal(); // Asegúrate de que no haya errores aquí
                this.mazo.mezclarMazo();
                this.descarte = new PilaDeDescarte();
                System.out.println("Repartiendo cartas...");
                this.repartir();
                System.out.println("Creando pila de descarte...");
                jugadorActual = 0; // Empieza el turno con el primer jugador
                miEstado = JUEGO_INICIADO; // Cambia el estado del juego
                this.notificar(EventosUNO.JUEGO_INICIADO);
                System.out.println("Juego iniciado con éxito.");
            } catch (Exception e) {
                System.err.println("Error al iniciar el juego: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            if (this.jugadores.size() == 1) {
                notificar(EventosUNO.FALTA_UNO);
            } else {
                System.out.println("No hay jugadores suficientes para iniciar el juego.");
            }
        }
    }


    private void verificarFinDelJuego() {
        for (Jugador jugador : jugadores) {
            if (jugador.getPuntos() >= MAX_PUNTOS) {
                miEstado = FIN_JUEGO;
                ganador = jugador;
                notificar(EventosUNO.FIN_JUEGO);
                return;
            }//Ya no la necesito
        }
    }

    public void repartir() {
        for(int i =0; i<3;i++){//repartir 7 cartas cada uno
            for(Jugador j : jugadores){
                j.robaCarta(mazo.sacarCarta());
                j.ordenarPorColor();//no implementada
            }
        }
        descarte.agregarCarta(mazo.sacarCarta());//le pongo la primer carta
        cartaActual= descarte.obtenerUltimaCarta();
        if(cartaActual.getClass() == CartaEspecial.class){
            procesarEfectosCartaEspecial((CartaEspecial)cartaActual);
        }
    }

    public void reiniciarRonda() {
        descarte.vaciarCartas();
        for(Jugador j : jugadores){
            j.limpiarMano();
        }
        mazo = new MazoPrincipal();
        repartir();
        notificar(EventosUNO.PUNTOS_CAMBIADOS);
        calcularPuntos();//calculo los puntos de esta ronda
    }

    private void calcularPuntos() {
        int puntos = 0;
        for (Jugador jugador : jugadores) {
            if (jugador != jugadores.get(jugadorActual)) {//sumo todos menos el actual. Va a ser el ganador
                puntos += jugador.getMano().calcularPuntosM();
            }
        }
        jugadores.get(jugadorActual).aumentarPuntos(puntos);
    }


    public void robarCarta(){
        if (mazo.isVacio()) {
            descarte.reciclarEnMazo(mazo);
        }
        this.jugadores.get(jugadorActual).robaCarta(mazo.sacarCarta());
        this.jugadores.get(jugadorActual).ordenarPorColor();//no implementada
        notificar(EventosUNO.CARTA_ROBADA);

    }

    public void agregarJugador(String name){
       if (miEstado == SETEANDO) {
            if (this.jugadores.size() == 1 && this.jugadores.getFirst().getNombre().equals(name))
                notificar(EventosUNO.MISMO_NOMBRE);
            else {
                if (this.jugadores.size() < 10) {
                    this.jugadores.add(new Jugador(name));
                    notificar(EventosUNO.JUGADOR_AGREGADO);
                }
                else
                    notificar(EventosUNO.JUGADORES_MAXIMOS);
            }
       }
    }

    public boolean thereAreCartas(){//Ver si lo necesito
        Jugador jugadorActualObj = jugadores.get(jugadorActual);
        for(Carta carta : jugadorActualObj.getMano().obtenerCartas()){
            if(carta.esJugableSobre(cartaActual,colorActual)){
                return true;
            }
        }
        return false;
    }


    public void tirarCarta(int numero) {
        Jugador jugadorActualObj = jugadores.get(jugadorActual);
        Carta carta;
        if(numero==-1){
            notificar(EventosUNO.DEBE_ROBAR);
            robarCarta();
            carta = jugadorActualObj.getMano().obtenerCartas().getLast();
        }else {
            carta = jugadorActualObj.getMano().obtenerCartas().get(numero-1);
        }
        // Validar si el jugador tiene la carta seleccionada
        if (!jugadorActualObj.getMano().contieneCarta(carta)) {
            notificar(EventosUNO.JUGADA_INVALIDA);
            return;
        }
        // Validar si la carta es jugable
        //Carta cartaTope = descarte.obtenerUltimaCarta();
        if (!carta.esJugableSobre(cartaActual, colorActual)) {
            notificar(EventosUNO.JUGADA_INVALIDA);
            return;
        }
        // Jugar la carta
        jugadorActualObj.desacartar(numero-1);//hice quilomo con el indice y la carta
        descarte.agregarCarta(carta);
        notificar(EventosUNO.CARTA_TIRADA);
        colorActual = carta.getColor();
        this.cartaActual = carta; // Actualizar la carta en juego

        // Aplicar efectos si es una carta especial
        if (carta.getClass() == CartaEspecial.class) {
            procesarEfectosCartaEspecial((CartaEspecial) carta);
        }

        // Verificar si el jugador ganó la mano
        if (jugadorActualObj.getMano().cantidadDeCartas() == 0){
            miEstado = FIN_MANO;
            notificar(EventosUNO.FIN_MANO);
            terminaMano();
            return;
        }
        //verificar si es UNO
        if (jugadorActualObj.getMano().cantidadDeCartas()==1){
            notificar(EventosUNO.UNO);
        }
        // Cambiar turno
        avanzarTurno();
    }


    public void terminaMano() {
        // Verificar si algún jugador terminó su mano
        Jugador ganadorRonda = jugadores.get(jugadorActual);
        if (ganadorRonda.getMano().cantidadDeCartas() == 0) {
            calcularPuntos();//calcula los puntos del jugador actual (los suma)
            if (ganadorRonda.getPuntos() >= MAX_PUNTOS) {
                miEstado = FIN_JUEGO;
                this.ganador = ganadorRonda;
                notificar(EventosUNO.FIN_JUEGO);
            } else {
                // Reiniciar la ronda
                Cantronda++;
                reiniciarRonda();
                miEstado = JUGANDO;
                notificar(EventosUNO.FIN_MANO);
            }
        } else {
            // Si nadie ganó la mano, continuar el juego.no se si lo necesito
            avanzarTurno();
            //notificar(EventosUNO.TURNO_CAMBIADO);
        }
    }

    private void procesarEfectosCartaEspecial(CartaEspecial carta) {
        switch (carta.getTipo()) {
            case BLOQUEO:
                bloqueo();
                break;

            case REVERSA:
                cambiarRonda();
                break;

            case CAMBIODECOLOR:
                notificar(EventosUNO.SOLICITAR_COLOR);
                cambiarColorC(colorActual);// este no seria necesario
                break;

            case MASDOS:
                mas2();
                break;

            case MASCUATRO:
                // Notifica que se necesita un color antes de forzar el robo de cartas
                notificar(EventosUNO.SOLICITAR_COLOR);
                mas4(colorActual);
                break;
        }
    }


    public void cambiarColor(Color nuevoColor) {
        this.colorActual = nuevoColor;
    }


    //LOGICA DE LAS CARTAS
    private void cambiarRonda() {
        // Invertir la dirección
        sentidoDeRonda = !sentidoDeRonda;
        notificar(EventosUNO.CAMBIARONDA);
        avanzarTurno();
        //notificar(EventosUNO.TURNO_CAMBIADO);
    }
    public void cambiarColorC(Color nuevoColor) {
        this.colorActual = nuevoColor; // Actualiza el estado
        //this.cartaActual.setColor(nuevoColor);//Ver si me combiene de esta forma
        //System.out.println("El color se cambió a: " + nuevoColor);
        notificar(EventosUNO.CAMBIACOLOR);
    }
    public void bloqueo(){
        jugadorActual = obtenerSiguienteJugador();
        notificar(EventosUNO.BLOQUEO);
    }
    public void mas4(Color nuevoColor){
        this.cambiarColorC(nuevoColor);//Cambia color
        int siguienteJugador= obtenerSiguienteJugador();
        for(int i = 0; i<4; i++) {//le hago robar 4. Ver si lo puedo mejorar
            jugadores.get(siguienteJugador).robaCarta(mazo.sacarCarta());
        }
        notificar(EventosUNO.MAS4);
        avanzarTurno();
        //notificar(EventosUNO.TURNO_CAMBIADO);
    }
    public void mas2(){
        int siguienteJugador= obtenerSiguienteJugador();
        for(int i = 0; i<2; i++) {//le hago robar 2. Ver como sacar el for
            jugadores.get(siguienteJugador).robaCarta(mazo.sacarCarta());
        }
        notificar(EventosUNO.MAS2);
        avanzarTurno();
        //notificar(EventosUNO.TURNO_CAMBIADO);
    }

    public void reiniciaJuego(){
        //topFive.agregarAlTop(jugadores.get(jugadorActual));
        //persistir.guardarObjetos("top.dat", topFive);
        jugadores.clear();
        miEstado = SETEANDO;
        descarte = new PilaDeDescarte();
        mazo = new MazoPrincipal();
        this.Cantronda = 0;
    }


    //OBSERVER
    @Override
    public void notificar(EventosUNO evento) {
        for(IObservador o : this.observadores){
            o.actualizar(evento);
        }
    }

    @Override
    public void registrar(IObservador observador) {
        this.observadores.add(observador);
    }

    @Override
    public void desregistrar(IObservador observador) {
        this.observadores.remove(observador);
    }

    public ArrayList<Jugador> getCantidadJugadores(){
        return this.jugadores;
    }
    public int getJugadorEnTurnoNum(){
        return this.jugadorActual;
    }

    public Jugador getJugadorActual() {
        return jugadores.get(jugadorActual);
    }

    public int getCartasA() {
        return this.jugadores.get(jugadorActual).getCantCartas();
    }

    public ArrayList<Carta> mostrarCarta() {
        //this.jugadores.get(jugadorActual).mostrarMano();
        return this.jugadores.get(jugadorActual).getMano().obtenerCartas();
    }

    public int getCartasDescarte(){
        return descarte.cantDeCartas();
    }
}
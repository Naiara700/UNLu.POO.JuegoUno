package UNLu.Poo.Uno.Modelo;

public class MazoPrincipal extends PilaDeCartas{
    public MazoPrincipal(){
        super();
        Color [] colores = {Color.ROJO,Color.AMARILLO,Color.VERDE,Color.AZUL};
        TipoDeCartaEspecial[] tipos = {TipoDeCartaEspecial.BLOQUEO,TipoDeCartaEspecial.MASDOS,TipoDeCartaEspecial.REVERSA};
        TipoDeCartaEspecial []negras = {TipoDeCartaEspecial.CAMBIODECOLOR,TipoDeCartaEspecial.MASCUATRO};
        for(int j=0; j<2;j++){// 2 cartas de cada uno
            for(int i = 1; i<10; i++){//Agregar de la 1 a la 9
                for(Color color : colores){
                    Carta carta = new CartaNumerica(color,i);
                    agregarCarta(carta);
                }
            }
            for(TipoDeCartaEspecial tipo: tipos){
                for(Color color:colores){
                    Carta carta = new CartaEspecial(color,tipo);
                    agregarCarta(carta);
                }
            }

        }
        for (TipoDeCartaEspecial tipo : negras) {
            for (int i = 0; i < 4; i++) { // 4 de cada carta negra
                Carta carta = new CartaEspecial(Color.NEGRO, tipo);
                agregarCarta(carta);
            }
        }
        for (Color color : colores) {//Agregar los 4 0
            Carta carta = new CartaNumerica(color, 0);
            agregarCarta(carta);
        }

    }

    public boolean isVacio() {
        return cartas.isEmpty();
    }
}

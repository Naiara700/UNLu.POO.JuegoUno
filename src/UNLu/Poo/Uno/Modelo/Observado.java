package UNLu.Poo.Uno.Modelo;

public interface Observado {
        void notificar (EventosUNO evento);
        void registrar (IObservador observador);
        void desregistrar (IObservador observador);
}

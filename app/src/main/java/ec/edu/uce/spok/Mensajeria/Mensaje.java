package ec.edu.uce.spok.Mensajeria;

/**
 * Created by usuario on 02/07/2017.
 */

public class Mensaje {
    private String id;
    private String mensaje;
    private String hora;
    private int tipo;

    public Mensaje() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

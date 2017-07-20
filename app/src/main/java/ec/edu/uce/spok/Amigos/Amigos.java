package ec.edu.uce.spok.Amigos;

/**
 * Created by Gigabyte on 17/07/2017.
 */

public class Amigos {

    private int fotoPerfil;
    private String usuario;
    private String nombresCompletos;
    private String ultimoMensaje;
    private String horaMensaje;

    public Amigos() {

    }

    public int getFotoPerfil() {

        return fotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {

        this.fotoPerfil = fotoPerfil;
    }

    public String getUsuario() {

        return usuario;
    }

    public void setUsuario(String usuario) {

        this.usuario = usuario;
    }

    public String getNombresCompletos() {

        return nombresCompletos;
    }

    public void setNombresCompletos(String nombresCompletos) {
        this.nombresCompletos = nombresCompletos;
    }

    public String getUltimoMensaje() {

        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {

        this.ultimoMensaje = ultimoMensaje;
    }

    public String getHoraMensaje() {

        return horaMensaje;
    }

    public void setHoraMensaje(String horaMensaje) {

        this.horaMensaje = horaMensaje;
    }

}

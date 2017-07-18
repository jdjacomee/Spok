package ec.edu.uce.spok.Amigos;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Gigabyte on 17/07/2017.
 */

public class Amigos {

    private int fotoPerfil;
    private String nombre;
    private String ultimoMensaje;
    private String horaMensaje;

    public Amigos(){

    }

    public int getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

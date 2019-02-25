package com.example.usuario.dinamicview;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import android.content.Context;

/**
 * @author  Hugo Armando Rodríguez Flores
 * @version 1.0
 * @since   2016-09-14
 */
public class Serializador{
    private String nombreArchivo;
    private FileOutputStream archivoSalida;
    private ObjectOutputStream salida;
    private FileInputStream archivoEntrada;
    private ObjectInputStream entrada;
    private Context contexto;

    /**
     * Construye un nuevo objeto Serializador
     * @param    unNombreArchivo     es el nombre de archivo que va a ser serializado y desearializado
     */
    public Serializador(String unNombreArchivo, Context unContexto){
        this.nombreArchivo = unNombreArchivo;
        this.archivoSalida = null;
        this.salida = null;
        this.archivoEntrada = null;
        this.entrada = null;
        this.contexto = unContexto;
    }

//      todo: sera removido en las siguientes versiones
//    /**
//     * Serializa una tabla de precedencias
//     * @param    unaTabla    es un objeto TablaPrecedencias que sera serializado
//     */
//    public void serializaTablaPrecedencias(TablaPrecedencias unaTabla) throws IOException{
//        this.archivoSalida = this.contexto.openFileOutput(this.nombreArchivo, Context.MODE_PRIVATE);
//        this.salida = new ObjectOutputStream(this.archivoSalida);
//        this.salida.writeObject(unaTabla);
//        this.salida.close();
//        this.archivoSalida.close();
//        this.salida = null;
//        this.archivoSalida = null;
//    }
//
//    /**
//     * Deserializa una tabla de precedencias
//     * @return               un objeto TablaPrecedencias que ha de ser desearializado
//     */
//    public TablaPrecedencias deserializaTablaPrecedencias() throws IOException, ClassNotFoundException {
//        TablaPrecedencias tablaRes = null;
//
//        this.archivoEntrada = this.contexto.openFileInput(this.nombreArchivo);
//        this.entrada = new ObjectInputStream(this.archivoEntrada);
//        tablaRes = (TablaPrecedencias)this.entrada.readObject();
//        this.entrada.close();
//        this.archivoEntrada.close();
//        this.entrada = null;
//        this.archivoEntrada = null;
//        return tablaRes;
//    }
}
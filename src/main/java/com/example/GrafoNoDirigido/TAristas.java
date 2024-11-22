package com.example.GrafoNoDirigido;

import java.util.Collection;
import java.util.Iterator;

import java.util.LinkedList;

public class TAristas extends LinkedList<TArista> {

    private final static String SEPARADOR_ELEMENTOS_IMPRESOS = "-";
    //private Collection<TArista> aristas  = new LinkedList<TArista>();

    /**
     * Busca dentro de la lista de aristas una arista que conecte a etOrigen con
     * etDestino
     *
     * @param etOrigen
     * @param etDestino
     * @return
     */
    public TArista buscar(Comparable etOrigen, Comparable etDestino) {
        for (TArista tArista : this) {
            if (tArista.etiquetaOrigen.equals(etOrigen) && tArista.etiquetaDestino.equals(etDestino)) {
                return tArista;
            }
        }
        return null;
    }

    /**
     * Busca la arista de menor costo que conecte a cualquier nodo de VerticesU
     * con cualquier otro de VerticesV y cuyo costo sea el minimo
     *
     * @param VerticesU - Lista de vertices U
     * @param VerticesV - Lista de vertices V
     * @return
     */
    public TArista buscarMin(Collection<TVertice> VerticesU, Collection<TVertice> VerticesV) {
        TArista minArista = null;
        double minCosto = Double.MAX_VALUE;
        for (TVertice uVertice : VerticesU) {
            for (TVertice tVertice : VerticesV) {
                TArista tempArista = buscar(uVertice.getEtiqueta(), tVertice.getEtiqueta());
                if (tempArista != null) {
                    if (tempArista.costo < minCosto) {
                        minArista = tempArista;
                        minCosto = tempArista.costo;
                    }
                }
            }
        }
        return minArista;
    }

    public String imprimirEtiquetas() {
        if (this.isEmpty()) {
            return null;
        }
        StringBuilder salida = new StringBuilder();
        //TODO: Completar codigo que imprime todas las aristas de la lista en el siguiente formato:
        //ORIGEN - DESTINO - COSTO
        return salida.toString();
    }

    void insertarAmbosSentidos(Collection<TArista> aristas) {
        if (aristas == null) {
            return;
        }
        TArista tempArista;
        for (TArista ta : aristas) {
            this.add(ta);
            this.add(ta.aristaInversa());
        }
    }

}

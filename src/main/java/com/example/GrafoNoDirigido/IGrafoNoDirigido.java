package com.example.GrafoNoDirigido;

import java.util.Collection;
import java.util.LinkedList;

public interface IGrafoNoDirigido {

    public Collection<TVertice> bea();

    public Collection<TVertice> bea(Comparable etiquetaOrigen);

    public TGrafoNoDirigido Prim();

    public TGrafoNoDirigido Kruskal();

    public LinkedList<TVertice> puntosArticulacion(Comparable etOrigen);

    public boolean esConexo();
}

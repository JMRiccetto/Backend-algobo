package com.example.GrafoNoDirigido;

import java.util.Collection;
import java.util.LinkedList;
import java.util.*;

public class TVertice<T> implements IVertice {

    private Comparable etiqueta;
    private LinkedList<TArista> aristas;
    private LinkedList<TAdyacencia> adyacentes;
    private boolean visitado;
    private T datos;

    public Comparable getEtiqueta() {
        return etiqueta;
    }

    public LinkedList<TAdyacencia> getAdyacentes() {
        return adyacentes;
    }

    public T getDatos() {
        return datos;
    }

    public TVertice(Comparable unaEtiqueta) {
        this.etiqueta = unaEtiqueta;
        adyacentes = new LinkedList();
        aristas = new LinkedList();
        visitado = false;
    }

    public LinkedList<TArista> getAristas(){
        return aristas;
    }

    public void setAristas(LinkedList<TArista> aristas) {
        this.aristas = aristas;
    }

    public void setArista(TArista arista) {
        this.aristas.addLast(arista);
    }

    public void setVisitado(boolean valor) {
        this.visitado = valor;
    }

    public boolean getVisitado() {
        return this.visitado;
    }

    @Override
    public TAdyacencia buscarAdyacencia(TVertice verticeDestino) {
        if (verticeDestino != null) {
            return buscarAdyacencia(verticeDestino.getEtiqueta());
        }
        return null;
    }

    @Override
    public Double obtenerCostoAdyacencia(TVertice verticeDestino) {
        TAdyacencia ady = buscarAdyacencia(verticeDestino);
        if (ady != null) {
            return ady.getCosto();
        }
        return Double.MAX_VALUE;
    }

    @Override
    public boolean insertarAdyacencia(Double costo, TVertice verticeDestino) {
        if (buscarAdyacencia(verticeDestino) == null) {
            TAdyacencia ady = new TAdyacencia(costo, verticeDestino);
            return adyacentes.add(ady);
        }
        return false;
    }

    @Override
    public boolean eliminarAdyacencia(Comparable nomVerticeDestino) {
        TAdyacencia ady = buscarAdyacencia(nomVerticeDestino);
        if (ady != null) {
            adyacentes.remove(ady);
            return true;
        }
        return false;
    }

    @Override
    public TVertice primerAdyacente() {
        if (this.adyacentes.getFirst() != null) {
            return this.adyacentes.getFirst().getDestino();
        }
        return null;
    }

    @Override
    public TAdyacencia buscarAdyacencia(Comparable etiquetaDestino) {
        for (TAdyacencia adyacencia : adyacentes) {
            if (adyacencia.getDestino().getEtiqueta().compareTo(etiquetaDestino) == 0) {
                return adyacencia;
            }
        }
        return null;
    }

    @Override
    public void bpf(Collection<TVertice> visitados) {
        setVisitado(true);
        visitados.add(this);
        for (TAdyacencia adyacente : adyacentes) {
            TVertice vertAdy = adyacente.getDestino();
            if (!vertAdy.getVisitado()) {
                vertAdy.bpf(visitados);
            }
        }
    }

    @Override
    public TCaminos todosLosCaminos(Comparable etVertDest, TCamino caminoPrevio, TCaminos todosLosCaminos) {
        visitado = true;
        for (TAdyacencia adyacencia : adyacentes) {
            TVertice destino = adyacencia.getDestino();
            if (!destino.getVisitado()) {
                if (destino.getEtiqueta().compareTo(etVertDest) == 0) {
                    TCamino copia = caminoPrevio.copiar();
                    copia.agregarAdyacencia(adyacencia);
                    todosLosCaminos.getCaminos().add(copia);
                } else {
                    caminoPrevio.agregarAdyacencia(adyacencia);
                    destino.todosLosCaminos(etVertDest, caminoPrevio, todosLosCaminos);
                    caminoPrevio.eliminarAdyacencia(adyacencia);
                }
            }
            
        }
        visitado = false;
        return todosLosCaminos;
    }

    @Override
    public void bea(Collection<TVertice> visitados) {
        Queue<TVertice> colaDeVertices = new LinkedList<>();
        colaDeVertices.add(this);
        visitado = true;
        visitados.add(this);
        while (!colaDeVertices.isEmpty()) {
            TVertice verticeX = colaDeVertices.poll();
            LinkedList<TAdyacencia> adyacentes = verticeX.getAdyacentes();
            for (TAdyacencia adyacente : adyacentes) {
                TVertice verticeY = adyacente.getDestino();
                if (!verticeY.getVisitado()) {
                    verticeY.setVisitado(true);
                    colaDeVertices.add(verticeY);
                    visitados.add(verticeY);
                }
            }
        }
    }

    @Override
    public TVertice siguienteAdyacente(TVertice w) {
        TAdyacencia adyacente = buscarAdyacencia(w.getEtiqueta());
        int index = adyacentes.indexOf(adyacente);
        if (index + 1 < adyacentes.size()) {
            return adyacentes.get(index + 1).getDestino();
        }
        return null;
    }

    public boolean tieneCiclo() {
        visitado = true;
        for (TAdyacencia adyacente : adyacentes) {
            TVertice vertAdy = adyacente.getDestino();
            if (vertAdy.getVisitado()) {
                return true;
            } else {
                vertAdy.tieneCiclo();
            }
        }
        return false;
    }

    @Override
    public boolean tieneCiclo(LinkedList<Comparable> camino) {
        visitado = true;
        camino.add(etiqueta);
        for (TAdyacencia adyacente : adyacentes) {
            TVertice vertAdy = adyacente.getDestino();
            if (vertAdy.getVisitado()) {
                return true;
            } else {
                vertAdy.tieneCiclo(camino);
            }
        }
        return false;
    }

    @Override
    public boolean conectadoCon(TVertice destino) {
        TAdyacencia resultado = buscarAdyacencia(destino.getEtiqueta());
        return resultado != null;
    }
    
    public int puntosArticulacion(int[] numeracionSrc, LinkedList<TVertice> lista, Map<Comparable, Integer> numeraciones, TVertice padre) {
        visitado = true;
        
        numeracionSrc[0]++;
        int numeracion = numeracionSrc[0];
        numeraciones.put(getEtiqueta(), numeracion);
        
        // Almacena solamente el máximo de los bajos de sus hijos para compararlo con su numeración.
        int bajoDeHijoMaximo = -1;
        
        // Almacenar número de hijos para confirmar caso del vértice.
        int numHijos = 0;
        
        int bajo = numeracion;
        for (TAdyacencia adyacencia : getAdyacentes()) {
            TVertice destino = adyacencia.getDestino();
            
            if (!destino.visitado) {
                numHijos++;
                
                // Calcular en post-orden el bajo del hijo (y revisar si es punto de articulación)
                int bajoDelHijo = destino.puntosArticulacion(numeracionSrc, lista, numeraciones, this);
                
                // Calcular mínimo para el bajo
                if (bajoDelHijo < bajo) {
                    bajo = bajoDelHijo;
                }
                
                // Calcular máximo de los bajos de los hijos
                if (bajoDelHijo > bajoDeHijoMaximo) {
                    bajoDeHijoMaximo = bajoDelHijo;
                }
                
            } else if (destino != padre) {
                
                int numDestino = numeraciones.get(destino.getEtiqueta());
                
                // Calcular mínimo para el hijo
                if (numDestino < bajo) {
                    bajo = numDestino;
                }
                
            }
        }
        
        if (padre != null) {
            if (bajoDeHijoMaximo >= numeracion) {
                lista.add(this);
            }
        } else {
            if (numHijos >= 2) {
                lista.add(this);
            }
        }
        
        return bajo;
    }

}

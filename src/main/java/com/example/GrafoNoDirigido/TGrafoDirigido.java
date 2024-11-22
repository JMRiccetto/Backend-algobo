package com.example.GrafoNoDirigido;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class TGrafoDirigido implements IGrafoDirigido {

    private final Map<Comparable, TVertice> vertices; //lista de vertices del grafo.-
    

    public TGrafoDirigido(Collection<TVertice> vertices, Collection<TArista> aristas) {
        this.vertices = new HashMap<>();
        for (TVertice vertice : vertices) {
            insertarVertice(vertice.getEtiqueta());
        }
        for (TArista arista : aristas) {
            insertarArista(arista);
        }
    }

    /**
     * Metodo encargado de eliminar una arista dada por un origen y destino. En
     * caso de no existir la adyacencia, retorna falso. En caso de que las
     * etiquetas sean invalidas, retorna falso.
     *
     * @param nomVerticeOrigen
     * @param nomVerticeDestino
     * @return
     */
    @Override
    public boolean eliminarArista(Comparable nomVerticeOrigen, Comparable nomVerticeDestino) {
        if ((nomVerticeOrigen != null) && (nomVerticeDestino != null)) {
            TVertice vertOrigen = buscarVertice(nomVerticeOrigen);
            if (vertOrigen != null) {
                return vertOrigen.eliminarAdyacencia(nomVerticeDestino);
            }
        }
        return false;
    }

    /**
     * Metodo encargado de eliminar un vertice en el grafo. En caso de no
     * existir el v�rtice, retorna falso. En caso de que la etiqueta sea
     * inv�lida, retorna false.
     *
     * @param nombreVertice
     * @return
     */
    @Override
    public boolean eliminarVertice(Comparable nombreVertice) {
        if (nombreVertice != null) {
            getVertices().remove(nombreVertice);
            return getVertices().containsKey(nombreVertice);
        }
        return false;
    }

    /**
     * Metodo encargado de verificar la existencia de una arista. Las etiquetas
     * pasadas por par�metro deben ser v�lidas.
     *
     * @param etiquetaOrigen
     * @param etiquetaDestino
     * @return True si existe la adyacencia, false en caso contrario
     */
    @Override
    public boolean existeArista(Comparable etiquetaOrigen, Comparable etiquetaDestino) {
        TVertice vertOrigen = buscarVertice(etiquetaOrigen);
        TVertice vertDestino = buscarVertice(etiquetaDestino);
        if ((vertOrigen != null) && (vertDestino != null)) {
            return vertOrigen.buscarAdyacencia(vertDestino) != null;
        }
        return false;
    }

    /**
     * Metodo encargado de verificar la existencia de un vertice dentro del
     * grafo.-
     *
     * La etiqueta especificada como par�metro debe ser v�lida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a buscar.-
     * @return True si existe el vertice con la etiqueta indicada, false en caso
     * contrario
     */
    @Override
    public boolean existeVertice(Comparable unaEtiqueta) {
        return getVertices().get(unaEtiqueta) != null;
    }

    /**
     * Metodo encargado de verificar buscar un vertice dentro del grafo.-
     *
     * La etiqueta especificada como parametro debe ser valida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a buscar.-
     * @return El vertice encontrado. En caso de no existir, retorna nulo.
     */
    private TVertice buscarVertice(Comparable unaEtiqueta) {
        return getVertices().get(unaEtiqueta);
    }

    /**
     * Matodo encargado de insertar una arista en el grafo (con un cierto
     * costo), dado su vertice origen y destino.- Para que la arista sea valida,
     * se deben cumplir los siguientes casos: 1) Las etiquetas pasadas por
     * parametros son v�lidas.- 2) Los vertices (origen y destino) existen
     * dentro del grafo.- 3) No es posible ingresar una arista ya existente
     * (miso origen y mismo destino, aunque el costo sea diferente).- 4) El
     * costo debe ser mayor que 0.
     *
     * @param arista
     * @return True si se pudo insertar la adyacencia, false en caso contrario
     */
    @Override
    public boolean insertarArista(TArista arista) {
        boolean tempbool = false;
        if ((arista.getEtiquetaOrigen() != null) && (arista.getEtiquetaDestino() != null)) {
            TVertice vertOrigen = buscarVertice(arista.getEtiquetaOrigen());
            TVertice vertDestino = buscarVertice(arista.getEtiquetaDestino());
            tempbool = (vertOrigen != null) && (vertDestino != null);
            if (tempbool) {
                //getLasAristas().add(arista);
                return vertOrigen.insertarAdyacencia(arista.getCosto(), vertDestino);
            }

        }
        return false;
    }

    /**
     * Metodo encargado de insertar un vertice en el grafo.
     *
     * No pueden ingresarse v�rtices con la misma etiqueta. La etiqueta
     * especificada como par�metro debe ser v�lida.
     *
     * @param unaEtiqueta Etiqueta del v�rtice a ingresar.
     * @return True si se pudo insertar el vertice, false en caso contrario
     */
    public boolean insertarVertice(Comparable unaEtiqueta) {
        if ((unaEtiqueta != null) && (!existeVertice(unaEtiqueta))) {
            TVertice vert = new TVertice(unaEtiqueta);
            getVertices().put(unaEtiqueta, vert);
            return getVertices().containsKey(unaEtiqueta);
        }
        return false;
    }

    @Override
    public boolean insertarVertice(TVertice vertice) {
        Comparable unaEtiqueta = vertice.getEtiqueta();
        if ((unaEtiqueta != null) && (!existeVertice(unaEtiqueta))) {
            getVertices().put(unaEtiqueta, vertice);
            return getVertices().containsKey(unaEtiqueta);
        }
        return false;
    }


    public Object[] getEtiquetasOrdenado() {
        TreeMap<Comparable, TVertice> mapOrdenado = new TreeMap<>(this.getVertices());
        return mapOrdenado.keySet().toArray();
    }

    @Override
    public void desvisitarVertices() {
        for (TVertice vertice : this.vertices.values()) {
            vertice.setVisitado(false);
        }
    }

    /**
     * @return the vertices
     */
    @Override
    public Map<Comparable, TVertice> getVertices() {
        return vertices;
    }

    @Override
    public Collection<TVertice> bpf(TVertice vertice) {
        Collection<TVertice> visitados = new LinkedList<>();
        vertice.bpf(visitados);
        visitados.addAll(bpf());
        return visitados;
    }

    

    @Override
    public boolean tieneCiclo(TCamino camino) {
        Collection<Comparable> elCamino = new LinkedList<>();
        elCamino.add(camino.getOrigen().getEtiqueta());
        Collection<Comparable> otrosVertices = camino.getOtrosVertices();
        
        for (Comparable actual : otrosVertices) {
            if (elCamino.contains(actual)) {
                return true;
            } else {
                elCamino.add(actual);
            }
        }
        return false;
    }

    @Override
    public Collection<TVertice> bpf() {
        Collection<TVertice> visitados = new LinkedList<>();
        vertices.forEach((etiqueta, vertice) -> {
            if (!vertice.getVisitado()) {
                vertice.bpf(visitados);
            }
        });
        desvisitarVertices();
        return visitados;
    }

    @Override
    public Collection<TVertice> bpf(Comparable etiquetaOrigen) {
        Collection<TVertice> visitados = new LinkedList<>();
        TVertice origen = vertices.get(etiquetaOrigen);
        origen.bpf(visitados);
        visitados.addAll(bpf());
        return visitados;
    }

    @Override
    public Comparable centroDelGrafo() {
        Map<Comparable, Double> excentricidades = obtenerExcentricidades();
        Double minimo = Double.MAX_VALUE;
        Comparable etiquetaMinimo = "";
        for (Map.Entry<Comparable, Double> entry : excentricidades.entrySet()) {
            if (entry.getValue() < minimo && entry.getValue() > -1) {
                minimo = entry.getValue();
                etiquetaMinimo = entry.getKey();
            }
        }
        return etiquetaMinimo;
    }

    @Override
    public Double[][] floyd() {
        Double[][] matrizFloyd = UtilGrafos.obtenerMatrizCostos(vertices);
        Double[][] matrizPredecesores = new Double[matrizFloyd.length][matrizFloyd.length];
        int tamanio = vertices.size();
        
        for (int k = 0; k < tamanio; k++) {
            for (int i = 0; i < tamanio; i++) {
                if (matrizFloyd[i][k] == Double.MAX_VALUE) {
                    continue;
                }
                for (int j = 0; j < tamanio; j++) {
                    if (matrizFloyd[i][k] + matrizFloyd[k][j] < matrizFloyd[i][j]) {
                        matrizFloyd[i][j] = matrizFloyd[i][k] + matrizFloyd[k][j];
                        matrizPredecesores[i][j] = (double)k;
                    }
                }
            }
        }
        return matrizFloyd;
    }
    
    private Map<Comparable, Double> obtenerExcentricidades() {
        Double[][] matrizFloyd = floyd();
        int tamanio = vertices.size();
        
        Comparable[] etiquetas = new Comparable[tamanio];
        int i = 0;
        for (Comparable eti : vertices.keySet()) {
            etiquetas[i++] = eti;
        }
        Map<Comparable, Double> res = new HashMap<>(tamanio * 4 / 3);
        for (int y = 0; y < tamanio; y++) {
            double max = 0;
            for (int x = 0; x < tamanio; x++) {
                if (max < matrizFloyd[x][y]) {
                    max = matrizFloyd[x][y];
                }
            }
            if (max == Double.MAX_VALUE) {
                max = -1;
            }
            res.put(etiquetas[y], max);
        }
        
        return res;
    }

    @Override
    public Comparable obtenerExcentricidad(Comparable etiquetaVertice) {
        return obtenerExcentricidades().get(etiquetaVertice);
    }
    
    public boolean obtenerConectividad(Comparable verticeOrigen, Comparable verticeDestino) {
        boolean[][] warshall = warshall();
        int tamanio = vertices.size();
        
        Comparable[] etiquetas = new Comparable[tamanio];
        int i = 0;
        for (Comparable etiqueta : vertices.keySet()) {
            etiquetas[i++] = etiqueta;
        }
        
        for (int y = 0; y < tamanio; y++) {
            if (etiquetas[y].equals(verticeOrigen)) {
                for (int x = 0; x < tamanio; x++) {
                    if (etiquetas[x].equals(verticeDestino))
                        return warshall[x][y];
                }
            }
        }  
        return false;
    }

    @Override
    public boolean[][] warshall() {
        Double[][] C = UtilGrafos.obtenerMatrizCostos(getVertices());
        boolean[][] A = new boolean[C.length][C.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                if (C[i][j] != Double.MAX_VALUE) {
                    A[i][j] = true;
                }
            }
        }
        for (int k = 0; k < A.length; k++) {
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < A.length; j++) {
                    if (A[i][j] == false) {
                        A[i][j] = A[i][k] && A[k][j];                        
                    }
                }
            }
        }
        return A;
    }

    @Override
    public TCaminos todosLosCaminos(Comparable etiquetaOrigen, Comparable etiquetaDestino) {
        TCaminos todosLosCaminos = new TCaminos();
        TVertice v = vertices.get(etiquetaOrigen);
        if (v != null) {
            TCamino caminoPrevio = new TCamino(v);
            v.todosLosCaminos(etiquetaDestino, caminoPrevio, todosLosCaminos);
            return todosLosCaminos;
        }
        return null;
    }

    @Override
    public boolean tieneCiclo(Comparable etiquetaOrigen) {
        TVertice vertice = vertices.get(etiquetaOrigen);
        if (vertice.tieneCiclo()) {
            desvisitarVertices();
            return true;
        }
        desvisitarVertices();
        return false;
    }

    @Override
    public boolean tieneCiclo() {
        for (TVertice vertice : vertices.values()) {
            if (vertice.tieneCiclo()) {
                return true;
            }
            desvisitarVertices();
        }
        return false;
    }

    @Override
    public Collection<TVertice> bea() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

   
}

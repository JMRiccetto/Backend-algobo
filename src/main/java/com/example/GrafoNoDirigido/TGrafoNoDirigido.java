package com.example.GrafoNoDirigido;

import java.util.Collection;
import java.util.Collections;
import java.util.*;
import java.util.LinkedList;

public class TGrafoNoDirigido extends TGrafoDirigido implements IGrafoNoDirigido {

    protected TAristas lasAristas = new TAristas();

    /**
     *
     * @param vertices
     * @param aristas
     */
    public TGrafoNoDirigido(Collection<TVertice> vertices, Collection<TArista> aristas) {
        super(vertices, aristas);
        lasAristas.insertarAmbosSentidos(aristas);

    }

    @Override
    public boolean insertarArista(TArista arista) {
        boolean tempbool = false;
        TArista arInv = new TArista(arista.getEtiquetaDestino(), arista.getEtiquetaOrigen(), arista.getCosto());
        tempbool = (super.insertarArista(arista) && super.insertarArista(arInv));
        return tempbool;
    }

    public TAristas getLasAristas() {
        return lasAristas;
    }

    @Override
    public TGrafoNoDirigido Prim() {
        if (!esConexo()) {
            return null;
        }
        LinkedList<TArista> tResultado = new LinkedList<>();
        LinkedList<TVertice> uVertices = new LinkedList<>();
        LinkedList<TVertice> vertices = new LinkedList<>();
        vertices.addAll(getVertices().values());
        uVertices.add(vertices.pollFirst());
        double costoPrim = 0;
        while (!vertices.isEmpty()) {
            TArista tempArista = lasAristas.buscarMin(uVertices, vertices);
            if (tempArista != null) {
                tResultado.add(tempArista);
                TVertice vert = getVertices().get(tempArista.etiquetaDestino);
                vertices.remove(vert);
                uVertices.add(vert);
                costoPrim += tempArista.costo;
            }
            
        }
        return new TGrafoNoDirigido(uVertices, tResultado);
    }

    @Override
    public TGrafoNoDirigido Kruskal() {
        if (!esConexo()) {
            return null;
        }
        TAristas F = new TAristas();
        int numVertices = getVertices().size();
        Map<Comparable, Integer> componentes = new HashMap<>();
        int i = 0;
        for (Comparable vertice : getVertices().keySet()) {
            componentes.put(vertice, i);
            i++;
        }
        int contador = numVertices - 1;

        Iterator<TArista> iter = lasAristas.iterator();
        while (contador > 0 && iter.hasNext()) {
            TArista arista = iter.next();
            Integer comp1 = componentes.get(arista.etiquetaOrigen);
            Integer comp2 = componentes.get(arista.etiquetaDestino);
            if (!comp1.equals(comp2)) {
                F.add(arista);
                for (Comparable vertice : getVertices().keySet()) {
                    if (componentes.get(vertice).equals(comp1)) {
                        componentes.put(vertice, comp2);
                    }
                }
                contador--;
            }
        }
        if (contador > 0) {
            return null;
        }
        TGrafoNoDirigido kruskal = new TGrafoNoDirigido(getVertices().values(), F);
        return kruskal;
    }

    @Override
    public Collection<TVertice> bea(Comparable etiquetaOrigen) {
        Collection<TVertice> visitados = new LinkedList<>();
        TVertice vertice = getVertices().get(etiquetaOrigen);
        if (vertice != null) {
            vertice.bea(visitados);
        }
        visitados.addAll(bea());
        return visitados;
    }
    
    public Collection<TVertice> bea() {
        Collection<TVertice> visitados = new LinkedList<>();
        getVertices().forEach((etiqueta, vertice) -> {
            if (!vertice.getVisitado()) {
                vertice.bea(visitados);
            }
        });
        desvisitarVertices();
        return visitados;
    }

    @Override
    public LinkedList<TVertice> puntosArticulacion(Comparable etOrigen) {
        if (this.getVertices().isEmpty()) {
            return null;
        }
        TVertice origen = this.getVertices().values().iterator().next();
        LinkedList<TVertice> res = new LinkedList<>();
        origen.puntosArticulacion(new int[1], res, new HashMap<>(), null);
        desvisitarVertices();
        return res;
    }

    @Override
    public boolean esConexo() {
        Collection<TVertice> vertices = getVertices().values();
        if (!vertices.isEmpty()) {
            TVertice unVertice = vertices.iterator().next();
            LinkedList<TVertice> visitados = new LinkedList<>();
            unVertice.bpf(visitados);
            for (TVertice vertice : vertices) {
                if (!vertice.getVisitado()) {
                    desvisitarVertices();
                    return false;
                }
            }
            desvisitarVertices();
            return true;
        }
        desvisitarVertices();
        return false;
    }

    public double[][] floydWarshall() {
        int n = this.getVertices().size();
        double[][] distancias = this.obtenerMatrizAdyacencia(); 
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && distancias[i][j] == 0) {
                    distancias[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }
        
        return distancias;
    }

    public double[][] obtenerMatrizAdyacencia() {
        int n = this.getVertices().size();
        double[][] matriz = new double[n][n];
        List<Comparable> etiquetas = new ArrayList<>(this.getVertices().keySet());

        for (TArista arista : this.getLasAristas()) {
            int i = etiquetas.indexOf(arista.getEtiquetaOrigen());
            int j = etiquetas.indexOf(arista.getEtiquetaDestino());
            matriz[i][j] = arista.getCosto();
            matriz[j][i] = arista.getCosto(); 
        }
        return matriz;
    }

    //TODO: definir que devuelve si lsita de aristas o nodos, o ambas.
    //Este metodo se ejecutara en un metodo main o algo por el estilo
    //entonces antes de ejecutarlo correr un floydwarshall para ya
    //tener el camino mas optimo y los valores de todos los otros. *a chequear igual*
    public void caminoMasSeguro(){
        //Obtener camino optimo planteado por el floydwarshall ejecutado inicialemnte
        LinkedList<TArista> aristasCaminoOptimo = new LinkedList<>();

        //enviar el paquete a traves del camino optimo (el obtenido anteriormente)
        //Aca es donde se puede dar que el enviarPaquete devuelva false y ahi replantear
        //buscar otro camino
        for(TArista arista : aristasCaminoOptimo){
            if(!arista.enviarPaquete()){
                //buscar otro camino
            }
        }
        //LLegados a este punto el paquete llegó al nodo destino ya sea a traves de el camino
        //optimo inicial o el encontrado si es que se cayo algun nodo
    }

}

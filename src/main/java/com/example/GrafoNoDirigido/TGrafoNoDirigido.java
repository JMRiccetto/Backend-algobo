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
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TGrafoNoDirigido Kruskal() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public Double[][] floydWarshall(int[][] predecesores) {
        Double[][] matrixFloyd = UtilGrafos.obtenerMatrizCostos(getVertices());
        if(predecesores == null){
            predecesores = new int[matrixFloyd.length][matrixFloyd.length];
        }
        for (int i = 0; i < matrixFloyd.length; i++) {
            for (int j = 0; j < matrixFloyd.length; j++) {
                if (i != j && matrixFloyd[i][j] != Double.POSITIVE_INFINITY) {
                    predecesores[i][j] = i;
                } else {
                    predecesores[i][j] = -1;
                }
            }
        }
        int i,j,k;
        for (k = 0; k < this.getVertices().size(); k++){
            for (i = 0; i < this.getVertices().size(); i++){
                for (j = 0; j < this.getVertices().size(); j++){
                    if(matrixFloyd[i][k] + matrixFloyd[k][j] < matrixFloyd[i][j]){
                        matrixFloyd[i][j] = matrixFloyd[i][k] + matrixFloyd[k][j];
                        predecesores[i][j] =predecesores[k][j];
                    }
                }
            }
        }
        return matrixFloyd;
    }

//    public HashMap<Comparable, Double> dijkstra(Comparable origen, Comparable destino) {
//        HashMap<Comparable,Double> fi = new HashMap<>();
//        fi.put(origen, 0.0);
//
//        LinkedList<TVertice> visitados = new LinkedList<>();
//        LinkedList<TVertice> X = new LinkedList<>();
//        TVertice nodoOrigen = getVerticePorEtiqueta(origen);
//        X.add(nodoOrigen);
//        visitados.add(nodoOrigen);
//
//        while(true){
//
//            Double min = Double.POSITIVE_INFINITY;
//            Double costoActual = min;
//            TArista aristaMenorCosto = null;
//
//            LinkedList<TArista> aristasVert = new LinkedList<>();
//
//            for(TVertice vert : X){
//                LinkedList<TArista> vertAristas = vert.getAristas();
//                for (TArista a : vertAristas) {
//                    if(!visitados.contains(
//                            this.getVerticePorEtiqueta(aristaMenorCosto.etiquetaDestino)
//                    )) {
//                        aristasVert.add(a);
//                    }
//                }
//
//            }
//
//            for (TArista arista : aristasVert) {
//                var verticeOrigenArista = this.getVerticePorEtiqueta(aristaMenorCosto.etiquetaOrigen);
//                costoActual = fi.get(verticeOrigenArista) + arista.getCosto();
//                if (costoActual <= min) {
//                    min = costoActual;
//                    aristaMenorCosto = arista;
//                }
//            }
//            TVertice vertx = this.getVerticePorEtiqueta(aristaMenorCosto.etiquetaDestino);
//            X.add(vertx);
//            visitados.push(vertx);
//            fi.put(vertx.getEtiqueta(), min);
//
//            if(destino == aristaMenorCosto.etiquetaDestino){
//                return fi;
//            }
//        }
//    }

public HashMap<Comparable, Double> dijkstra(Comparable origen, Comparable destino) {
    HashMap<Comparable,Double> fi = new HashMap<>();
    fi.put(origen, 0.0);

    LinkedList<TVertice> visitados = new LinkedList<>();
    LinkedList<TVertice> X = new LinkedList<>();
    TVertice nodoOrigen = getVerticePorEtiqueta(origen);
    X.add(nodoOrigen);
    visitados.add(nodoOrigen);
    HashMap<Comparable, Comparable> predecesores = new HashMap<>();
    predecesores.put(origen, null);
    while(true){

        Double min = Double.POSITIVE_INFINITY;
        Double costoActual = min;
        TArista aristaMenorCosto = null;

        LinkedList<TArista> aristasVert = new LinkedList<>();

        for(TVertice vert : X){
            LinkedList<TArista> vertAristas = vert.getAristas();
            for (TArista a : vertAristas) {
                if(!visitados.contains(this.getVerticePorEtiqueta(a.aristaInversa().etiquetaDestino))) {
                    aristasVert.add(a.aristaInversa());
                }
            }

        }

        for (TArista arista : aristasVert) {
            TVertice verticeOrigenArista = this.getVerticePorEtiqueta(arista.etiquetaOrigen);
            costoActual = fi.get(verticeOrigenArista.getEtiqueta()) + arista.getCosto();
            if (costoActual <= min) {
                min = costoActual;
                aristaMenorCosto = arista;
            }
        }
        TVertice vertx = this.getVerticePorEtiqueta(aristaMenorCosto.etiquetaDestino);
        X.add(vertx);
        visitados.push(vertx);
        fi.put(vertx.getEtiqueta(), min);
        predecesores.put(vertx.getEtiqueta(), aristaMenorCosto.etiquetaOrigen);

        if(destino == aristaMenorCosto.etiquetaDestino){
            System.out.println(predecesores);
            return fi;
        }
    }
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

    public LinkedList<TVertice> getPredecesores(int[][] predecesores, Comparable vert1, Comparable vert2) {
        Object[] etiquetas = this.getVertices().keySet().toArray();
        ArrayList<Object> x = new ArrayList<>(etiquetas.length);
        for (int i = 0; i < etiquetas.length; i++) {
            x.add(etiquetas[i]);
        }
        int indexOfVert1 = x.indexOf(vert1);
        int indexOfVert2 = x.indexOf(vert2);
        boolean foundFinalVert = false;

        int newIndex = predecesores[indexOfVert1][indexOfVert2];
        LinkedList<Integer> indexResult = new LinkedList<>();
        indexResult.add(indexOfVert1);
        //indexResult.add(newIndex);

        while(!foundFinalVert){
            if(newIndex == indexOfVert1){
                indexResult.add(indexOfVert2);
                foundFinalVert = true;
            }else{
                indexOfVert1 = newIndex;
                newIndex = predecesores[indexOfVert1][indexOfVert2];
                indexResult.add(newIndex);
            }
        }

        LinkedList<Object> res = new LinkedList<>();
        for(Integer i : indexResult){
            res.add(i);
        }

        //obtiene el vertice mediante su etiqueta
        LinkedList<TVertice> res2 = new LinkedList<>();
        for(Object o : res){
            res2.add(this.getVertices().get(etiquetas[(Integer) o]));
        }

        return res2;
    }

    public LinkedList<TArista> getAristasPorCaminoDeVertices(LinkedList<TVertice> vertices){
        LinkedList<TArista> aristas = new LinkedList<>();
        for(int i = 0; i < vertices.size(); i++){
            TVertice v1 = vertices.get(i);
            TVertice v2 = vertices.get(i+1);
            for(TArista a : (LinkedList<TArista>) v1.getAristas()){
                if(a.etiquetaDestino == v2.getEtiqueta()){
                    aristas.add(a);
                    break;
                }
            }
        }
        return aristas;
    }


    public TVertice getVerticePorEtiqueta(Comparable etiqueta){
        return this.getVertices().get(etiqueta);
    }

    public boolean buscarNodoAnteriorConMasDeUnaArista(int indexAristaAnterior, LinkedList<TArista> aristasCaminoOptimo){
        TArista aristaAnterior = aristasCaminoOptimo.get(indexAristaAnterior);
        Comparable etiquetaAnterior = aristaAnterior.getEtiquetaOrigen();
        TVertice verticeAnterior = this.getVerticePorEtiqueta(etiquetaAnterior);
        if(verticeAnterior.getAristas().size() > 1){
            return true;
        }
        return false;
    }

    //TODO: definir que devuelve si lsita de aristas o nodos, o ambas.
    //Este metodo se ejecutara en un metodo main o algo por el estilo
    //entonces antes de ejecutarlo correr un floydwarshall para ya
    //tener el camino mas optimo y los valores de todos los otros. *a chequear igual*
    public void caminoMasSeguro(){
        //Obtener camino optimo planteado por el floydwarshall ejecutado inicialemnte
        int[][] pred = new int[this.getVertices().size()][this.getVertices().size()];
        this.floydWarshall(pred);
        LinkedList<TVertice> verticesCaminoOptimo = this.getPredecesores(pred, "A", "L"); //TODO:Poner los vert como params del caminoMasSeguro
        LinkedList<TArista> aristasCaminoOptimo = this.getAristasPorCaminoDeVertices(verticesCaminoOptimo);

        //enviar el paquete a traves del camino optimo (el obtenido anteriormente)
        //Aca es donde se puede dar que el enviarPaquete devuelva false y ahi replantear
        //buscar otro camino
        for(TArista arista : aristasCaminoOptimo){
            if(!arista.enviarPaquete()){
                arista.caido = true;
                boolean hacerDijkstra = false;
                int indexArista = aristasCaminoOptimo.indexOf(arista);
                int contador = 0;
                while(!hacerDijkstra){
                    contador++;
                    int index = indexArista-contador;
                    hacerDijkstra = this.buscarNodoAnteriorConMasDeUnaArista(index, aristasCaminoOptimo);//TODO:Chequear caso borde cuando da 0-1
                }
                int indexAristaAnterior = aristasCaminoOptimo.indexOf(arista) - contador;//TODO:Chequear caso borde cuando da 0-1
                TArista aristaAnterior = aristasCaminoOptimo.get(indexAristaAnterior);
                Comparable etiquetaAnterior = aristaAnterior.getEtiquetaOrigen();
                TVertice verticeAnterior = this.getVerticePorEtiqueta(etiquetaAnterior);
                LinkedList<TVertice> caminoOptimoDijkstra = this.dijkstra(etiquetaAnterior, "L");


            }
        }
        //LLegados a este punto el paquete llegó al nodo destino ya sea a traves de el camino
        //optimo inicial o el encontrado si es que se cayo algun nodo
    }

}

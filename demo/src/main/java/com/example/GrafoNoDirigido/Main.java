/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.example.GrafoNoDirigido;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class Main {

    public static void main(String[] args) {
        
        // Ejercicio 1

        TVertice v1 = new TVertice("V1");
        TVertice v2 = new TVertice("V2");
        TVertice v3 = new TVertice("V3");
        TVertice v4 = new TVertice("V4");

        LinkedList<TVertice> vertices = new LinkedList();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);

        TArista a1 = new TArista("V1", "V2", 5);
        TArista a2 = new TArista("V4", "V3", 8);
        TArista a3 = new TArista("V1", "V3", 3);
        TArista a4 = new TArista("V2", "V3", 1);

        LinkedList<TArista> aristas = new LinkedList<>();
        aristas.add(a1);
        aristas.add(a2);
        aristas.add(a3);
        aristas.add(a4);

        TGrafoNoDirigido gf = new TGrafoNoDirigido(vertices, aristas);

        Double[][] matrizCostos = UtilGrafos.obtenerMatrizCostos(gf.getVertices());
        UtilGrafos.imprimirMatrizMejorado(matrizCostos, gf.getVertices(), "Matriz de costo");

        for (TVertice vertice : gf.bpf()) {
            System.out.print(vertice.getEtiqueta() + ", ");
        }
        System.out.println("");

        gf.todosLosCaminos("V1", "V3").imprimirCaminosConsolaCosto();

        // Ejercicio 2

        TGrafoNoDirigido gfPrim = gf.Prim();

        Double[][] matrizCostosPrim = UtilGrafos.obtenerMatrizCostos(gfPrim.getVertices());
        UtilGrafos.imprimirMatrizMejorado(matrizCostosPrim, gfPrim.getVertices(), "Matriz de costos Prim");
        
        // Ejercicio 3
        TVertice verticeA = new TVertice("a");
        TVertice verticeB = new TVertice("b");
        TVertice verticeC = new TVertice("c");
        TVertice verticeD = new TVertice("d");
        TVertice verticeE = new TVertice("e");
        TVertice verticeF = new TVertice("f");
        TVertice verticeG = new TVertice("g");
        List<TVertice> verticesGrafoND = List.of(verticeA, verticeB, verticeC, verticeD, verticeE, verticeF, verticeG);
        
        TArista aristaAB = new TArista(verticeA.getEtiqueta(), verticeB.getEtiqueta(), 0);
        TArista aristaAE = new TArista(verticeA.getEtiqueta(), verticeE.getEtiqueta(), 0);
        TArista aristaBE = new TArista(verticeB.getEtiqueta(), verticeE.getEtiqueta(), 0);
        TArista aristaBD = new TArista(verticeB.getEtiqueta(), verticeD.getEtiqueta(), 0);
        TArista aristaEC = new TArista(verticeE.getEtiqueta(), verticeC.getEtiqueta(), 0);
        TArista aristaCF = new TArista(verticeC.getEtiqueta(), verticeF.getEtiqueta(), 0);
        TArista aristaCG = new TArista(verticeC.getEtiqueta(), verticeG.getEtiqueta(), 0);
        List<TArista> aristasGrafoND = List.of(aristaAB, aristaAE, aristaBE, aristaBD, aristaEC, aristaCF, aristaCG);
        
        TGrafoNoDirigido grafoND = new TGrafoNoDirigido(verticesGrafoND, aristasGrafoND);
        Collection<TVertice> grafoNDBea = grafoND.bea("g");
        StringBuilder sb = new StringBuilder();
        for (TVertice tVertice : grafoNDBea) {
            sb.append(tVertice.getEtiqueta()).append(" -> ");
        }
        System.out.println(sb.toString());
    }
}

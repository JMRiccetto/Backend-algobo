package com.example.GrafoNoDirigido;

import java.util.Random;

public class TArista implements IArista {

    protected Comparable etiquetaOrigen;
    protected Comparable etiquetaDestino;
    protected double costo;
    public int paquetesEnviados = 0;
    public int paquetesConfirmados = 0;


    public TArista(Comparable etiquetaOrigen, Comparable etiquetaDestino, double costo) {
        this.etiquetaOrigen = etiquetaOrigen;
        this.etiquetaDestino = etiquetaDestino;
        this.costo = costo;
    }

    
    
    @Override
    public Comparable getEtiquetaOrigen() {
        return etiquetaOrigen;
    }

    @Override
    public void setEtiquetaOrigen(Comparable etiquetaOrigen) {
        this.etiquetaOrigen = etiquetaOrigen;
    }

    @Override
    public Comparable getEtiquetaDestino() {
        return etiquetaDestino;
    }

    @Override
    public void setEtiquetaDestino(Comparable etiquetaDestino) {
        this.etiquetaDestino = etiquetaDestino;
    }

    @Override
    public double getCosto() {
        return calcularErrorRate();
    }

    @Override
    public void setCosto(double costo) {
        this.costo = costo;
    }
    public TArista aristaInversa (){
        return new TArista(this.getEtiquetaDestino(), this.getEtiquetaOrigen(),this.getCosto());
    }

    public boolean enviarPaquete(){
        this.paquetesEnviados++;
        double rate = this.calcularErrorRate();
        int rnd = new Random().nextInt(99)+1;
        if(rate*100 > rnd){
            //logica del retry
            for(int i=0; i<=2; i++ ){
                rnd = new Random().nextInt(99)+1;
                //TODO: considerar si cada retry cuenta como paquete enviado/ de momento no;
                if(rate*100 < rnd){
                    this.paquetesConfirmados++;
                    return true;
                }
            }//end logica retry
            return false; //no llega el paquete
        }
        this.paquetesConfirmados++;
        return true;//llega el paquete
    }

    public double calcularErrorRate(){
        return (double) this.paquetesEnviados / this.paquetesConfirmados;
    }

    
}

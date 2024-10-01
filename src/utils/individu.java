/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.io.*;
import weka.core.* ;

public class individu implements Serializable, Cloneable {
    private double fitness;
    private chromosome chromo;
    private int L; //longueur d'un chromosome
    
    public individu(int longueur, Instances dataset){
        this.fitness = 0.0;
        this.L=longueur;
        chromo = new chromosome(L,dataset);
    }
    public individu(){
        
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        individu i = (individu) super.clone();
        i.L = this.L;
        i.fitness = this.fitness;
        i.chromo = (chromosome) this.chromo.clone();
        return i; //To change body of generated methods, choose Tools | Templates.
    }
    
    public void print()
  {
     chromo.print();
     System.out.print("   ");
     System.out.println(fitness);
  }
    
    public int get_length()
  {
     return L;
  }
    
   public void set_fitness(double fit)
  {
     fitness = fit;
  }
    
    public double get_fitness(){
        return fitness;
    }
    
    public void setGene(int index,double val){
        chromo.setGene(index,val);
    }
    
    public double getGene(int index){
        return chromo.getGene(index);
    }
    
  
    
     public chromosome get_chromosome()
  {
     return chromo;
  }
     
    @Override
     public boolean equals(Object obj){
         if (this == obj)
             return true;
         if(obj == null)
             return false;
         individu i1 = (individu)obj;
         return this.L == i1.L
                 && this.chromo.equals(i1.chromo)
                 && this.fitness == i1.fitness;
     }
     
  private void copy(chromosome source, chromosome destination)
  {
     for (int i=0; i<L; i++)
     {
         destination.setGene(i,source.getGene(i));
     }
  }
  public void set_chrom(chromosome ch)
  {
     copy(ch,chromo);
  }
    
}


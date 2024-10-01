/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.io.*;
import java.util.Random;
import weka.core.*;


public class chromosome implements Serializable, Cloneable {
     double[] genes; //ce tableau contient les gènes d'un chromosome
     int L; //la longuer d'un chromosome
     static Random r = new Random();
  
        //le codage
    public chromosome(int L_chrom, Instances dataset){
        L = L_chrom;
        genes = new double[L];
        Fonctions f=new Fonctions();
        int i,j;
        i=0; //indice du tableau des gènes
        j=0;//indice des attributs
        //partie 1 : générer la partie condition:
        
        while(i<L-1){
            genes[i]=r.nextDouble()<0.6? 1:0; //attribut actif ou inactif
            i++;
            if(dataset.attribute(j).type()==1) //attribut_nominal
                {
                genes[i]=0; //pour l'opérateurs "=" 
                i++;
                int nb_val = dataset.attribute(j).numValues();//le nombre de valeurs possibles pour l'attribut j
                genes[i]=(int)r.nextInt(nb_val); //pour générer des valeurs entières entre 0 et nb_val
      	    i++;
            j++;
            }
            else
                if(dataset.attribute(j).type()==0)//attribut numérique
                {
                genes[i]=(int)r.nextInt(2); //pour les opérateurs possibles <= ou >=
                i++;
                
                int nb_val = dataset.numDistinctValues(j);//le nombre de valeurs possibles pour l'attribut j
                double valeurs[] = new double[nb_val]; //initialiser le tableau contenant les valeurs distincts de l'attribut j
                valeurs=f.val_att(dataset,j);//on remplit le tableau
                int nb=r.nextInt(nb_val);//on génère aléatoirement un indice entre 0 et le nombre de valeurs possibles de l'attribut j
                genes[i]=valeurs[nb]; //on met une valeur aléatoire parmis les valeurs possibles de l'attribut j
                i++;
                j++;   
                }
        }
        //Partie 2 : générer la partie conséquent (l'attribut classe)
        if(dataset.attribute(j).type()==1) //attribut nominal
                {
                int nb_val = dataset.attribute(j).numValues();//le nombre de valeurs possibles pour l'attribut j
                genes[i]=(int)r.nextInt(nb_val); //pour générer des valeurs entières entre 0 et nb_val
            }
            else
                if(dataset.attribute(j).type()==0)//attribut numérique
                {         
                int nb_val = dataset.numDistinctValues(j);//le nombre de valeurs possibles pour l'attribut j
                double valeurs[] = new double[nb_val]; //initialiser le tableau contenant les valeurs distincts de l'attribut j
                valeurs=f.val_att(dataset,j);//on remplit le tableau
                int nb=r.nextInt(nb_val);//on génère aléatoirement un indice entre 0 et le nombre de valeurs possibles de l'attribut j
                genes[i]=valeurs[nb];
        }
        /*System.out.println("chromo = ");
        for(int k=0; k<L; k++) System.out.print(genes[k]+" ");*/
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        chromosome c = (chromosome) super.clone();
        c.L = this.L;
        c.genes = new double[L];
        for(int k=0; k<L;  k++){
            c.genes[k] = this.genes[k];
        }
        return c; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    public void setGene(int index,double val){
        genes[index]=val;
    }
    
    public double getGene(int index){
        return genes[index];
    }
    
    
   public void print()
  {
    for(int i=0; i<L; i++)
      System.out.print(genes[i]+" | ");
  }
   
   @Override
     public boolean equals(Object obj){
         if (this == obj)
             return true;
         if(obj == null)
             return false;
         else {
         chromosome c1 = (chromosome)obj;
         boolean check = true;
         for(int m=0; m<L; m++){
             if(this.getGene(m) != c1.getGene(m)) check = false;
         }
         return check;
         }
     
     }
    
}

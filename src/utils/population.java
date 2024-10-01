/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import weka.core.*;


public class population implements Cloneable{
   int  popsize;  //le nombre d'individus
   individu pop[];   // un tableau d'individus

   int L_chrom;   // la longueur d'un chromosome

  private int     bestp; // The position of the best  individual: [0..popsize-1]
  private int     worstp;// The position of the worst individual: [0..popsize-1]
  private double  bestf; // The best fitness of the present population
  private double  avgf;  // The average fitness of the present population
  private double  worstf;// The worst fitness of the present population
  private double  BESTF; // The best fitness ever found during the search

  public population(int pops, int length, Instances dataset){
      popsize = pops;
      pop = new individu[popsize];
      L_chrom = length;
      fitness fit = new fitness();
      Fonctions f = new Fonctions();
      for(int i=0;i<popsize;i++)
    {
        pop[i] = new individu(L_chrom,dataset);//initializons l'individu courant
        String regle=f.decoder_instance(pop[i],dataset);//on fait le décodage pour avoir la règle
//        System.out.println("pop_decoder :"+regle);
        if(f.vide(regle)) { 
//            System.out.println("vide");
            regle="null"; 
        i--;}
        else
        {
            
            //on calcule la fitness :.
            double fitness = fit.fitness_calcul(dataset, regle);
            pop[i].set_fitness(fitness);
        }
    }
      
      bestp = 0;     worstp = 0;    bestf = 0.0;   
    avgf   = 0.0;   worstf = 9999999999.0;    BESTF = 0.0;
  }
  
  public int get_popsize()
  {
    return popsize;
  }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        population p = (population) super.clone();
        p.popsize = this.popsize;
        p.pop = new individu[popsize];
        for(int i=0;i<popsize;i++)
        {
            p.pop[i]=(individu)this.pop[i].clone();
        }
        p.L_chrom = this.L_chrom;
        p.avgf = this.avgf;
        p.bestf = this.bestf;
        p.worstf = this.worstf;
        p.BESTF = this.BESTF;
        
        
        return p; //To change body of generated methods, choose Tools | Templates.
    }
  
  
  
  public int    get_worstp() { return worstp; }
  public int    get_bestp()  { return bestp;  }
  public double get_worstf() { return worstf; }
  public double get_avgf()   { return avgf;   }
  public double get_bestf()  { return bestf;  }
  public double get_BESTF()  { return BESTF;  }
  
  public individu get_individu(int index)throws Exception
  {
    if ((index<popsize) && (index>=0))
    return pop[index];
    else
    throw new Exception("Indice invalide.");
  }
  
  public void set_individu(int index, individu ind)throws Exception
  {
    if ((index<popsize) && (index>=0))
    pop[index]= (individu) ind.clone();
    else
    throw new Exception("Indice invalide.");
  }
  
  public void compute_stats()
  {
    double f, total;

    total  = 0.0;
    worstf = pop[0].get_fitness();     worstp = 0;
    bestf  = pop[0].get_fitness();     bestp  = 0;

    for(int i=0;i<popsize;i++)
    {   f = pop[i].get_fitness();
    if(f<=worstf) {worstf = f; worstp = i;}
    if(f>=bestf)  {bestf  = f; bestp  = i;}
    if(f>=BESTF)  {BESTF  = f;            }
      total += f;
    }

    avgf = total/(double)popsize;
  }
  
  public void set_fitness( int index, double fitness ) throws Exception
  {
    pop[index].set_fitness(fitness);
      System.out.println("fitness : "+ fitness);
  }
  
  public void print()
  {
    for(int i=0;i<popsize;i++)
    {  System.out.print(i);    System.out.print("   ");
      pop[i].print();
    }
  }
  
  public population delete_bestp(population pop, Instances dataset) throws Exception{
      //cette méthode est utilisé dans la sélection elitist pour enlever le meilleur individu choisi et l'autre individu à chaque itération 
      population tr = new population(popsize-1, L_chrom, dataset);
      for(int u=0,f=0; u<popsize; u++){
          if(u == pop.get_bestp()) {
              continue;
            } else {
              tr.set_individu(f++, pop.get_individu(u));
          }
        }
      return tr;
      
  } 
  
  public void print_stats()
  {
    System.out.println("la meilleure fitness trouvée: "+BESTF);  
    System.out.println("la meilleure fitness pour la population courante :"+bestf);  System.out.print("   ");
    System.out.println("la fitness moyenne pour la population courante :"+avgf);   System.out.print("   ");
    System.out.println("la pire fitness pour la population courante :"+worstf); System.out.print("   ");
    System.out.println("la position du meilleur individu :"+bestp);  System.out.print("   ");
    System.out.println("la position du pire individu :"+worstp);
  }
}

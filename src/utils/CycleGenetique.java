/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.util.ArrayList;
import java.util.Random;
import weka.core.*;


public class CycleGenetique {
  private  int popsize; //la taille d'une population
  private  double pm; // la probabilité de mutation
  private  population pop; // la population initiale
  
  fitness fit=new fitness();
  Fonctions fct=new Fonctions();
  Random r = new Random();
  
  //l'initialisation de la population
  public CycleGenetique(int pops, population pop, double pm)
  throws Exception
  {
    this.pm   = pm;
    this.pop=pop;
    this.popsize = pops;
    pop.compute_stats();//on calcule les stats
  }
  //Phase 1 : Recombinaison
  //Générer la population intermédiaire Pop_Int de taille N
  public population recombinaison(Instances dataset, int x) throws Exception
  {     
      individu parent1=new individu();
      individu parent2=new individu();
      population pop_int = (population)pop.clone();
      int p1, p2; //les deux parents qui vont être sélectionnés aléatoirement
      int w=0;
        //Croisement HUX
        while(w<popsize){
            p1 = (int)(r.nextDouble()* (double)popsize + 0.5);
//    System.out.println("indice du parent1 sélectionné = "+p1);
    if(p1>popsize-1) p1=popsize-1;
    parent1 = pop.get_individu(p1);
        
    p2 = (int)(r.nextDouble()* (double)popsize + 0.5); 
//    System.out.println("indice du parent2 sélectionné = "+p2);
    if(p1!=p2){
        if(p2>popsize-1) p2=popsize-1;
        parent2 = pop.get_individu(p2);
    }
    else{//p1==p2
        do
    {  p2 = (int)(r.nextDouble()* (double)popsize + 0.5);  
      if(p2>popsize-1) p2=popsize-1;
    }
    while (p1==p2);        
//        System.out.println("p2 ="+p2);
        parent2 = pop.get_individu(p2);
    }
    
    int g = parent1.get_length()-1;
//            System.out.println("parent1 :");
//      parent1.print();
//            System.out.println("parent2 :");
//      parent2.print();
//on initialize les deux enfants avec les gènes des deux parents
	individu enfant1 = (individu)parent1.clone();
        individu enfant2 = (individu)parent2.clone();
        Random r0 = new Random();
        int p = dataset.numAttributes()-1;
        ArrayList indice = new ArrayList();
	if(fct.hamming(g,parent1,parent2,dataset)>x){ //il faut s’assurer que la distance de Hamming entre 
			//ces deux parents soit supérieure au seuil d’accouplement x
	for(int j=0; j<p/2; j++)
		{
                int v;
                         do{
                             v=r0.nextInt(p);
                         }
                         while(indice.contains(v));
                        indice.add(v);
                         int numatt= v*3;
                        double c = enfant1.getGene(numatt);
                        enfant1.setGene(numatt, enfant2.getGene(numatt));
                        enfant2.setGene(numatt, c);
                        
                        int numatt2= v*3+1;
                        double c2 = enfant1.getGene(numatt2);
                        enfant1.setGene(numatt2, enfant2.getGene(numatt2));
                        enfant2.setGene(numatt2, c2);
                        
                        int numatt3= v*3+2;
                        double c3 = enfant1.getGene(numatt3);
                        enfant1.setGene(numatt3, enfant2.getGene(numatt3));
                        enfant2.setGene(numatt3, c3);
                }
        //on ajoute les deux enfants dans la population intermédiare pop_int
		pop_int.set_individu(w,enfant1);
		pop_int.set_individu(w+1,enfant2);
        }
        w=w+2;
        }
        pop_int.compute_stats();//on calcule les stats
        return pop_int;
  }
  
    //Phase 2 : génération de la population suivante Pop_Nouv
	//selection elitiste
  public population select_elitist(population pop_int, Instances dataset) throws Exception
  {
  int y=popsize;
  population pop2 = (population)pop.clone();
  population pop_int2 = (population)pop_int.clone();
  //on crée la nouvelle population pop_nouv
  population pop_nouv = (population)pop.clone();
  while(y>0){
//      System.out.println("\npopsize = "+y);
  //on compare le meilleur individu de la nouvelle génération et le meilleur individu de la génération précédente
	//le meilleur entre eux est conservé
  
        if(pop2.get_individu(pop2.get_bestp()).get_fitness() >= pop_int2.get_individu(pop_int2.get_bestp()).get_fitness())
            pop_nouv.set_individu(y-1,pop2.get_individu(pop2.get_bestp()));
        else
            pop_nouv.set_individu(y-1,pop_int2.get_individu(pop_int2.get_bestp()));
            
        pop2=pop2.delete_bestp(pop2, dataset);
        pop_int2=pop_int2.delete_bestp(pop_int2, dataset);
  y--;
  }
//      System.out.println("\n Pop nouv after elitist selection : ");
      pop_nouv.compute_stats();//on calcule les stats
  return pop_nouv;
  
  }
  
  
  //méthode 3 : mutation cataclysmique
  public population mutation_cata(population pop_nouv, individu indiv, Instances dataset)  throws Exception
  {
      int y=1, m, op;
      int l = indiv.get_length();
      boolean exist = false;
      Random r0 = new Random();
      //on ajoute le meilleur individu au pop_nouv
      pop_nouv.set_individu(0, indiv);
      while(y<popsize){
          individu indiv_nouv = (individu) indiv.clone();
//générer les autres individus à partir de l’individu indiv 
//en mutant aléatoirement pm de ses gènes (généralement 35%)
      for(int i=0; i<l; i++)
      { //on fait la mutation
          
              if(i%3==0) //le gène "actif/inactif"
              {
                  if (r.nextDouble()<=pm) //on vérifie d'abord la proba de mutation
                  {
                  if(indiv_nouv.getGene(i)==0)
                      indiv_nouv.setGene(i,1);
                  else
                      indiv_nouv.setGene(i,0);
                  }
                  i++;
              }
                  if(i%3==1)//le gène "opérateur"
                  {
                      if (r.nextDouble()<=pm)
                      {
                      int j=i/3;//l'attribut correspondant
                     // if(dataset.attribute(j).type()==1) => Attribut nominal //Ne pas toucher l'opérateur '='
                          if(dataset.attribute(j).type()==0)//attribut numérique
                          {
                              op=(int)r0.nextInt(2);//Opérateur:>=,<=
                              while(indiv_nouv.getGene(i)==op){
                                  op=(int)r0.nextInt(2);}
                              indiv_nouv.setGene(i,op);
                          }
                      }
                      i++;
                  }
              if(i%3==2)//le gène correspond à la valeur de l'attribut
              {
                  if (r.nextDouble()<=pm)
                  {
                  int j=i/3;//L'attribut correspondant
                  if(dataset.attribute(j).type()==1)//Attribut nominal
                  {
                      int nb_val = dataset.attribute(j).numValues();//le nombre de valeurs
                      if (nb_val == 1) i++; //si l'attribut possède une seule valeur
                      else
                      {
                          m=(int)r0.nextInt(nb_val);
                          while(indiv_nouv.getGene(i)==m)
                              m=(int)r0.nextInt(nb_val);
                          indiv_nouv.setGene(i,m);
                          i++;
                      }
                  }
                  else//l'attribut correspondant est numérique
                  {
                      int nb_val=dataset.numDistinctValues(j);
                      if(nb_val==1)
                          i++;
                      else
                      {
                          double valeurs[]= fct.val_att(dataset, j);
                          int nb=r0.nextInt(nb_val);//tirer un nombre aléatoire entre 0 et le nombre de valeurs distincts de l'attribut j
                          double nbre=valeurs[nb];
                          while(indiv_nouv.getGene(i)==nbre)
                          {
                              nb=r0.nextInt(nb_val);
    	        	      nbre=valeurs[nb];
                          }
                          indiv_nouv.setGene(i,nbre);
                          i++;
                      }
                  }
              }
                  else i++;
          }
      }
      for(int q=0; q < pop_nouv.get_popsize(); q++){
          if(pop_nouv.get_individu(q).equals(indiv_nouv)) exist = true;
      }
//          System.out.println("\nexiste");
          if(!exist){
              pop_nouv.set_individu(y, indiv_nouv);
          }
          y++;
//          System.out.println("\n La mutation cata est terminée!");
      }
//    System.out.println("\nPop nouv after mutation: ");
    pop_nouv.compute_stats();//on calcule les stats
      return pop_nouv;
  }
  
  //Pop←Pop_Nouv ;
   public population remplacer_pop(population pop_nouv) throws Exception
  {
	int i=0;
	while(i<popsize){
	pop.set_individu(i, pop_nouv.get_individu(i));
        i++;
	}
        return pop;
  }
        
  //méthode 4 : get solution
  public individu get_solution() throws Exception
  {
    return pop.get_individu(pop.get_bestp());
  }
 
  

        
  public int    get_worstp() { return pop.get_worstp(); }
  public int    get_bestp()  { return pop.get_bestp();  }
  public double get_worstf() { return pop.get_worstf(); }
  public double get_avgf()   { return pop.get_avgf();   }
  public double get_bestf()  { return pop.get_bestf();  }
  public double get_BESTF()  { return pop.get_BESTF();  }
}

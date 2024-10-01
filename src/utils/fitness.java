/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.util.*;
import weka.core.*;


public class fitness {
   public double fitness_calcul(Instances dataset,String regle){
        int nb_att = dataset.numAttributes();
        Fonctions f = new Fonctions();
        ArrayList regle1 = f.decoup_chaine(regle,",");
        String reg,ins;
        int t = nb_att-1;
        int G = 0; //le nombre d'instance bien classées par la regle
        for(int i=0; i<dataset.numInstances(); i++){
            String inst = dataset.instance(i).toString();
            ArrayList inst1 = f.decoup_chaine(inst,",");
            //System.out.println(inst1);
            int cp = 0;
            //System.out.println("boucle1");
            for(int k=0; k<t; k++){
                reg = regle1.get(k).toString();
                ins = inst1.get(k).toString();
                if(dataset.attribute(k).type()==1)//attribut nominal
                { //on cherche les instances couvertes par la regle
                    if(reg.contains("?")) cp++;
                    else
                        if(reg.contains("=")){
                            reg=reg.substring(1);
                            if(ins.compareTo(reg)==0) cp++;
                        }
                        }
                else//type numérique
                {
                    if(reg.contains("?")) cp++;
                    else
                        if(reg.contains(">=")){
                            reg=reg.substring(2);
                            double val1=Double.valueOf(reg);
                            double val2=Double.valueOf(ins);
                            if(val2>=val1) cp++;
                        }
                    else
                          if(reg.contains("<=")){
                            reg=reg.substring(2);
                            double val1=Double.valueOf(reg);
                            double val2=Double.valueOf(ins);
                            if(val2<=val1) cp++;
                        }  
                }
                }
            //on doit tester si l'attribut classe de l'instance courante est couvert par la regle
            if(cp == t)//l'instance est couverte
            {
                reg = regle1.get(t).toString();
                ins = inst1.get(t).toString();
                
                if(reg.compareTo(ins)==0)
  		 	G++; //l'instance est bien classée
            }
        }
        return G;
    }
    
    public double precision(Instances dataset,ArrayList C){
        Fonctions f = new Fonctions();
        System.out.println("\nTaille du classificateur: "+C.size());
        int nb_ins = dataset.numInstances();//récupérer la taille du dataset
        int G = 0; //le nombre d'instance bien classées par la regle
        for(int i=0; i<nb_ins; i++){
            String inst = dataset.instance(i).toString();//récupérer l'instance i
            String regle = f.regle_couvrante(C, inst, dataset);//chercher la regle qui couvre cette instance
//            System.out.println("\n"+i+" la regle couvrante "+regle.toString());
            if(!regle.equals("null")){ //si une regle couvrant cette instance existe
                G++;
            }
            }
        
        return G/(double)nb_ins;
    }
    
   
}

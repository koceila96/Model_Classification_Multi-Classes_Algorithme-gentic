/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.util.*;
import weka.core.*;


public class Fonctions {
    
     public ArrayList decoup_chaine(String ligne,String sep)
         {  //cette méthode permet de découper une chaine de caractère
	if (ligne==null)
	{
		return null;
	}

    int index=0;
	ArrayList line=new ArrayList();
	String temp=ligne;
	if(temp!=null)
	{
            index=temp.indexOf(sep);
            while (index>=0)
            {
                line.add(temp.substring(0,index));
                temp=temp.substring(index+sep.length(),temp.length());
                index=temp.indexOf(sep);
            }

		//le dernier élément
    	line.add(temp);
	}
	return line;
}
     
      //cette méthode renvoie les valeurs distincts d'un attribut numérique 
    double [] val_att(Instances dataset, int j) {
        int nb_val = dataset.numDistinctValues(j);//le nombre de valeurs possibles pour l'attribut j
        double val_att[] = new double[nb_val]; //initialiser le tableau contenant les valeurs distincts de l'attribut
        String instance1 = dataset.instance(0).toString(); //on récupère la première instance
        Fonctions f = new Fonctions();
        ArrayList instance1_decoup=f.decoup_chaine(instance1,","); //on la stocke sans les vergules dans un arraylist 
        ArrayList listf=new ArrayList(); //pour stocker les valeurs distincts de l'attribut sous forme de chaines de caractères 
        String instance1_att=instance1_decoup.get(j).toString();//on récupère la valeur de l'attribut pour la première instance 
        listf.add(instance1_att);
        for(int i=1;i<dataset.numInstances();i++)
    {
  	  String instance = dataset.instance(i).toString();//on récupère l'instance suivante
  	  ArrayList instance_decoup=f.decoup_chaine(instance,",");//on la stocke sans les vergules dans un nouveau arraylist 
  	  String instance_att=instance_decoup.get(j).toString();//on récupère la valeur de l'attribut pour l'instance courante
  	  if(listf.contains(instance_att)==false)
  	     listf.add(instance_att);//on cherche à ajouter que des valeurs distincts
   }
        for(int l=0; l<nb_val; l++)
   {
 	  String chaine=listf.get(l).toString();
 	  double val=Double.parseDouble(chaine);
 	  val_att[l]=val;
   }
        return val_att;
    }
    
     
    public boolean vide(String regle)
{ //pour tester si la regle contient des conditions ou si elle est vide
      int cp=0;
    Fonctions f = new Fonctions();
    ArrayList regle1=f.decoup_chaine(regle,",");
    // est un tableau qui contient le résultat de découpage
    for(int i=0;i<regle1.size()-1;i++)
    {
        String reg=regle1.get(i).toString();
    	if(reg.contains("?"))
    		cp++;
    }
    if(cp==regle1.size()-1)
    	return true; //ça veut dire que la regle est vide
    else
        return false;
}
    
    public String decoder_instance(individu indiv, Instances dataset){
        //Cette méthode permet de transformer un individu en une règle sans "if"
        String regle="";
        int i=0; //indice de gène
        int j=0; //indice de l'attribut
        int nb_att = dataset.numAttributes(); //le nombre d'attributs
        int taille = (nb_att-1)*3;
        
        while(i<taille){
            if(indiv.getGene(i)==0){ //attribut inactif
                i=i+3;
                regle=regle+"?"+",";
                j++;
            }
    
            else//attribut actif
            
            {
                i++;
                if(dataset.attribute(j).type()==1)//attribut nominal
                {
                    if(indiv.getGene(i)==0)
      			regle=regle+"=";
      			i++;//on passe à la valeur de l'attribut
                        int m=(int)indiv.getGene(i);
                        String r=dataset.attribute(j).value(m);
                        regle=regle+r+",";
                }
                
                if(dataset.attribute(j).type()==0)//attribut numérique
                {
                    if(indiv.getGene(i)==0) //on décode l'opérateur
      			regle=regle+">=";
                    if(indiv.getGene(i)==1)
      			regle=regle+"<=";
      			
                    i++;
                    regle=regle+indiv.getGene(i); //on récupère la valeur de l'attribut pour l'instance courante
                    regle=regle+",";
                }
                i++;
                j++;
            }
        }
        //décodons l'attribut classe
        if(dataset.attribute(j).type()==1)//attribut nominal
                {
            int m=(int)indiv.getGene(i);
            String r=dataset.attribute(j).value(m); //on récupère la valeur de la classe pour l'instance courante
//                    System.out.println("m ="+ m + " r = "+ r);
            regle=regle+r;
        }
        
        if(dataset.attribute(j).type()==0)//attribut numérique
        {
            regle=regle+indiv.getGene(i); //on récupère la valeur de la classe pour l'instance courante
        }
        return regle;
    }
    
    public String decodage(individu indiv, Instances dataset){
//Cette méthode permet de transformer un individu en une règle avec "if"
    String regle="if ";
    String regle1="";
    int i=0;
    int j=0;
    int nb_att = dataset.numAttributes(); //le nombre d'attributs
    int taille = (nb_att-1)*3;
    while(i<taille){
        if(indiv.getGene(i)==0){ //attribut inactif
            i=i+3;
            j++;
            }
            else//attribut actif
            {
            regle=regle+dataset.attribute(j).name();
            i++;
            if(dataset.attribute(j).type()==1)//attribut nominal
            {
                    regle=regle+"=";
                i++;//on passe à la valeur de l'attribut
                int m=(int)indiv.getGene(i);
                String r=dataset.attribute(j).value(m);
                regle=regle+r+" and ";
                }
                
                if(dataset.attribute(j).type()==0)//attribut numérique
                {
                    if(indiv.getGene(i)==0) //on décode l'opérateur
      			regle=regle+">=";
                    if(indiv.getGene(i)==1)
      			regle=regle+"<=";
      			
                    i++;
                    regle=regle+indiv.getGene(i); //on récupère la valeur de l'attribut pour l'instance courante
                    regle=regle+" and ";
                }
                i++;
                j++;
            }
        }
    if(!"if ".equals(regle))
       {
             int n=regle.length()-5;//pour supprimer le and
              regle1="";
             for(int l=0;l<n;l++)
        	    regle1 = regle1 +regle.charAt(l);
             regle1 = regle1 +" then ";
             //décodons l'attribut classe
             if(dataset.attribute(j).type()==1)//attribut nominal
             {
                 int m=(int)indiv.getGene(i);
                 String r=dataset.attribute(j).value(m); //on récupère la valeur de la classe pour l'instance courante
                 regle1=regle1+dataset.attribute(j).name()+"="+r;
             }
             else
                 if(dataset.attribute(j).type()==0)//attribut numérique
                 {
                     regle1=regle1+dataset.attribute(j).name()+"="+indiv.getGene(i); //on récupère la valeur de la classe pour l'instance courante
                 }
                 
             }
    return regle1;
    }
    
    public ArrayList couverte(String regle, Instances dataset){
        //cette méthode renvoie les indices des instances couvrantes par une règle
        ArrayList indices=new ArrayList();
        int nb_att = dataset.numAttributes();
        ArrayList regle1 = decoup_chaine(regle,",");
        String reg,ins;
        int t = nb_att - 1;
        for(int i=0;i<dataset.numInstances();i++) {
	   String inst = dataset.instance(i).toString();
           ArrayList inst1 = decoup_chaine(inst,",");
//        System.out.println("la regle : "+regle1.toString());
//        System.out.println("instance "+i+" : "+inst1.toString());
           //on récupère la regle en entrée et l'instace courante les deux découpées (sans ";")
           int cp = 0;
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
           if(cp == t)
               {
                reg = regle1.get(t).toString();
                ins = inst1.get(t).toString();    
                if(reg.compareTo(ins) == 0){
//        System.out.println("reg : "+reg);
//        System.out.println("ins "+i+" : "+ins);
//        System.out.println("oui");
          indices.add(i);
                }
           }
        }
        return indices;
    }
    
       
    public String regle_couvrante(ArrayList C, String instance, Instances dataset){
         //cette méthode nous permet d'avoir la règle du classificateur qui couvre l'instance donnée
         boolean trouve = false;
         int j=0;
         String regle="";
         while(j<C.size() && !trouve){
             regle=C.get(j).toString();
             ArrayList indices = couverte(regle, dataset);
             if(!indices.isEmpty()){//si le tableau des instances couvertes n'est pas vide
                 int s=0;
                 while(s<indices.size()){
                     String n=indices.get(s).toString();
                     double db=Double.parseDouble(n);                     
                     String ins = dataset.instance((int)db).toString();//on récupère l'instance correspondante
                     if(ins.compareTo(instance)==0)//donc l'instance est couverte par la regle
  		 	 trouve = true; //l'instance est bien classée;
                     s++;
                     }
                 j++;
                 }
             else j++;
         }
         if(!trouve) return "null";
         else return regle;
     }
    
    //la distance de hamming :
	public int hamming(int L, individu parent1, individu parent2, Instances dataset)
		{
	int d = 0, j=0, i=0;
        while(i < L ){
            j=i%3;//l'attribut correspondant
            if(i%3==0) //le gène "actif/inactif"
              {
                
                  if(parent1.getGene(i)== 0 && parent2.getGene(i)==0) {//sont inactifs
                      i=i+3;
                  }
                  else if(parent1.getGene(i)==1 && parent2.getGene(i)==1)//sont actifs
                      i++;
                  else if(parent1.getGene(i)==0 && parent2.getGene(i)==1 || parent1.getGene(i)==1 && parent2.getGene(i)==0) {//le cas où un est actif et l'autre inactif
                      d++;
                      i=i+3;
                  }
            }
            
            if(i%3==1){//on passe à l'opérateur
                      if(dataset.attribute(j).type()==1)//Attribut nominal
                          i++; //ils ont les deux l'opérateur '='
                      else
                          if(dataset.attribute(j).type()==0)//attribut numérique
                          {
                           if(parent1.getGene(i)==0 && parent2.getGene(i)==0 // l'opérateur =
                                   || parent1.getGene(i)==1 && parent2.getGene(i)==1  //l'opérateur <=
                                   || parent1.getGene(i)==2 && parent2.getGene(i)==2 )//l'opérateur >=
                               i++;
                           else { //le cas où ils n'ont pas le même opérateur
                               d++;
                               i=i+2;
                           }
                          }
            }
                      //on passe à la valeur de la condition
            if(i%3 == 2){
                  if(parent1.getGene(i)==parent2.getGene(i))
                      i++; //ils ont la même valeur
                  else{
                      d++; //ils n'ont pas la même valeur
                      i++;
                  }
            }
            }
//                    System.out.println("\nHamming = "+d);
	return d;
                }

}

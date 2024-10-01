/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;
import java.util.ArrayList;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;


public class main {
  public static void main(String[] args) throws Exception {

ArrayList classificateur = new ArrayList<>();
ArrayList classif_decode = new ArrayList<>();
ArrayList <individu> classif_initial = new ArrayList<>();
DataSource source = new DataSource("C:\\Users\\WALID\\Desktop\\M2\\PFE\\iris.2D.arff");
Instances dataset = source.getDataSet();
Fonctions f = new Fonctions();
fitness fit = new fitness();
population pop;
CycleGenetique AG;
int nb_att = dataset.numAttributes(); //le nombre d'attributs
System.out.println("nb att : "+nb_att);
int L_chrom = (nb_att - 1) * 3 + 1; //la longuer d'un chromsome
individu indiv;
int nb_it=10; //le nombre d'itérations
int popsize=50;
if(popsize % 2 == 1) popsize++;
double pm = 0.35;
//Initialiser le seuil d’accouplement : x=L/4 ;
int x = (nb_att-1)/4;
if(x==0) x++;
      System.out.println("seuil = "+ x);
//Division du dataset
int taille = dataset.numInstances();//taille du dataset
double d=taille*80/100;//taille du dataset d'apprentissage
int T = (int) d; //taille du dataset d'apprentissage
Instances app = new Instances(dataset); //on crée le dataset d'apprentissage à partir du dataset global
Instances DT = new Instances(dataset); //on crée le dataset du test à partir du dataset global
app.delete(); //on vide le dataset d'apprentissage
DT.delete(); //on vide le dataset du test DT
ArrayList classes = new ArrayList();//pour stocker les classes
int class_indice = dataset.numAttributes()-1;
int nb_class = dataset.numDistinctValues(class_indice);//récupèrer le nombre de classes distinctes
System.out.println("\nle nombre de classes distinctes = "+nb_class);
String instance1 = dataset.instance(0).toString(); //on récupère la première instance
ArrayList instance1_decoup=f.decoup_chaine(instance1,","); //on la stocke sans les vergules dans un arraylist
String instance1_att=instance1_decoup.get(class_indice).toString();//on récupère la valeur de l'attribut classe pour la première instance
classes.add(instance1_att);
for(int i=1;i<dataset.numInstances();i++)
    {
  	  String instance = dataset.instance(i).toString();//on récupère l'instance suivante
  	  ArrayList instance_decoup=f.decoup_chaine(instance,",");//on la stocke sans les vergules dans un nouveau arraylist 
  	  String instance_att=instance_decoup.get(class_indice).toString();//on récupère la valeur de l'attribut pour l'instance courante
  	  if(classes.contains(instance_att)==false)
  	     classes.add(instance_att);//on cherche à ajouter que des valeurs distincts
   }

      System.out.println("\nLes classes distinctes :");
      for(int re = 0 ; re < classes.size(); re++){
          System.out.println("\n classe "+re+" : "+classes.get(re).toString());
      }
      int count =0;
int tab_nb[] = new int[classes.size()]; //on construit le tableau contenant le nombre d'occurances de chaque classe
for(int j=0; j<classes.size();j++){
    count=0; 
    for(int k =0 ; k<dataset.numInstances(); k++){
        String instance = dataset.instance(k).toString();//on récupère l'instance k
        ArrayList instance_decoup=f.decoup_chaine(instance,",");//on la stocke sans les vergules dans un nouveau arraylist 
  	String instance_att=instance_decoup.get(class_indice).toString();//on récupère la valeur de l'attribut classe pour l'instance courante
  	if(classes.get(j).toString().equals(instance_att)) count++;
    }
    System.out.println("\n le nombre d'instances de la classe"+j+" = "+count);
    tab_nb[j] = count;
}

for(int i=0; i< tab_nb.length; i++){
    int n=tab_nb[i]*80/100;
    int to=0;
    System.out.println("\n Nombre d'instances de classe "+ i+ " pour le dataset d'apprentissage = " +n);
    for(int k =0 ; k<dataset.numInstances(); k++){
    String instance = dataset.instance(k).toString();//on récupère l'instance k
    ArrayList instance_decoup=f.decoup_chaine(instance,",");//on la stocke sans les vergules dans un nouveau arraylist 
    String instance_att=instance_decoup.get(class_indice).toString();//on récupère la valeur de l'attribut classe pour l'instance courante
    if(classes.get(i).toString().equals(instance_att)) {
        to++;
        if(to<=n) app.add(dataset.instance(k));//ajouter 80% d'instance de la classe i au dataset d'apprentissage
        else DT.add(dataset.instance(k));//ajouter 20% d'instaces de la classe i au dataset du test
    } 
        }
    
}
//System.out.println("\nLe dataset d'app : ");
//for(int b=0; b<app.size(); b++){
//    System.out.println(b+" :"+app.instance(b).toString());
//}
System.out.println("\n Taille du dataset du test = "+ DT.numInstances());
//System.out.println("\nLes instances de test : ");
//for(int b=0; b<DT.size(); b++){
//    System.out.println(b+" :"+DT.instance(b).toString());
//}

System.out.println("\nLa taille de population = "+popsize+"\n Le nombre d'itération : "+nb_it);
System.out.println("\nLa taille initiale du dataset d'apprentissage : "+T);
while(T>0)//La base d'apprentissage n'est pas vide
{ //on fait appel au cycle génétique CHC
    population pop_int;
    population pop_nouv;
    pop = new population(popsize,L_chrom,app);//initialiser la population initiale
    AG = new CycleGenetique(popsize, pop, pm); //initialiser le cycle génétique
    for(int i=0; i<nb_it; i++){
        System.out.println("\nItération "+ i);
        pop_int=AG.recombinaison(app, x);//Générer la population intermédiaire Pop_Int de taille N 
//        System.out.println("\nSelection elitiste\n----------------+++++++");
        pop_nouv=AG.select_elitist(pop_int, app);//génération de la population suivante Pop_Nouv
        int k=0,cmpt = 0;
        while(k<popsize)
        {
            if(pop.get_individu(k).equals(pop_nouv.get_individu(k)))
                cmpt++;
            k++;
        } 
        if(cmpt==popsize) //on vérifie si pop=pop_nouv 
        {
            x--;
            if(x == 0){
                indiv = AG.get_solution(); //on récupère le meilleur chromosome durant la recherche
//                System.out.println("\nMutation cataclysmique\n------------------------*********");
                pop_nouv = AG.mutation_cata(pop_nouv, indiv, app); //on lance la mutation cataclysmique
                x  = (nb_att-1)/4;
                if(x==0) x++;
//                System.out.println("\nseuil = "+x);
            }
        }
        pop = AG.remplacer_pop(pop_nouv);
        pop.compute_stats();
        
    }
    pop.print_stats();
    individu best_indiv = AG.get_solution();
    System.out.println("\nLe meilleur individu :");
    best_indiv.print();
    classif_initial.add(best_indiv);//ce classificateur est utilisé pour le post traitement
//supprimer les instances couvertes par best_indiv
String regle_decode = f.decoder_instance(best_indiv,app);//on doit faire le décodage sans "if"
//    double fitness2 = fit.fitness_calcul(app, regle_decode);
//    best_indiv.set_fitness(fitness2);
//    System.out.println("fitness : "+best_indiv.get_fitness());
ArrayList inst_couvertes = f.couverte(regle_decode, app);//on reçoit les indices des instances couvertes par la meilleure solution
//System.out.println("les indices couvertes : "+inst_couvertes.toString());
int nb_couv = inst_couvertes.size();//le nombre d'indices
System.out.println("nombre d'instances couvertes : "+nb_couv);
for(int l=nb_couv-1; l>=0; l--){
   //Récupérer l'indice
   if(T>0)
   {
       String n=inst_couvertes.get(l).toString();
//Convertir l'indice  string vers integer
double db=Double.parseDouble(n);
int indice=(int)db;
//       System.out.println("indice = "+indice);
app.delete(indice);
T--;
   }
   
}
if(T == 1) T--;
    System.out.println("\nLe nombre d'instances restantes= "+ T);
   
}
//le post traitement
//suppression des règles contradictoires
//System.out.println("\nLe classificateur initial avant la suppression des règles contradictoire = ");
//for(int e=0; e<classif_initial.size() ; e++){
//    System.out.print(e+"  ");
//    classif_initial.get(e).print();
//}
for(int i=0; i<classif_initial.size(); i++){
    for(int j=0; j<classif_initial.size(); j++){
        int l=classif_initial.get(i).get_length()-1;
        if(!classif_initial.get(i).equals(classif_initial.get(j))) //on compare deux règles sous forme individu
        {
            int cmp = f.hamming(l, classif_initial.get(i), classif_initial.get(j), dataset);
//            classif_initial.get(i).print();
//            classif_initial.get(j).print();
//            System.out.println("hamming entre les deux regles "+i+" et "+ j +" = "+ cmp+"\n");
            if(cmp==0) {
                if(classif_initial.get(i).getGene(l-1) != classif_initial.get(j).getGene(l-1)) //ces deux règle n'ont pas la même classe
                {//on cherche celle qui est la moins précise et on la supprime
                    if(classif_initial.get(i).get_fitness()>classif_initial.get(j).get_fitness())
                        classif_initial.remove(j);
                    else classif_initial.remove(i);
                }
            }
            }
        }
    }

//on remplit le classificateur classif_decode avec les règle sans if puis calculer la précision
for(int t=0; t<classif_initial.size(); t++){
    String regle_post = f.decoder_instance(classif_initial.get(t), app);
    classif_decode.add(regle_post);
}

//supprimer les règle spécifiques
ArrayList tab = new ArrayList();
    for(int i=0; i<classif_decode.size(); i++){
    for(int j=0; j<classif_decode.size(); j++){
        if(!classif_decode.get(i).toString().equals(classif_decode.get(j).toString())){
        ArrayList regle1 = f.decoup_chaine(classif_decode.get(i).toString(),",");
        ArrayList regle2 = f.decoup_chaine(classif_decode.get(j).toString(),",");
        if(regle1.get(nb_att-1).equals(regle2.get(nb_att-1))){ //si les deux règle ont la même classe
            int cp=0;
                for(int k=0; k<nb_att-1; k++){
               String reg1 = regle1.get(k).toString();
                String reg2 = regle2.get(k).toString();
                if(app.attribute(k).type()==1)//attribut nominal
                { //on cherche les instances couvertes par la regle
                    if(reg1.contains("?")) cp++;
                    else
                        if(reg1.contains("=")){
                            reg1=reg1.substring(1);
                            if(reg2.contains("=")){
                            reg2=reg2.substring(1);
                            if(reg1.equals(reg2)) cp++;
                            }
                        }
                        }
                else//type numérique
                {
                    if(reg1.contains("?")) cp++;
                    else
                        if(reg1.contains(">=")){
                            reg1=reg1.substring(2);
                            if(reg2.contains(">=")){
                                reg2=reg2.substring(2);
                                double val1=Double.valueOf(reg1);
                                double val2=Double.valueOf(reg2);
                                if(val2>=val1) cp++;
                            }
                        }
                    else
                          if(reg1.contains("<=")){
                            reg1=reg1.substring(2);
                            if(reg2.contains("<=")) {
                                reg2=reg2.substring(2);
                                double val1=Double.valueOf(reg1);
                                double val2=Double.valueOf(reg2);
                                if(val2<=val1) cp++;}
                          }  
                }
                }
                if(cp == nb_att-1) {
                    tab.add(j);
                }
        }
    }
}
    }
    //on élimine la règle spécifique
    for(int j=0; j<tab.size(); j++){
        classif_decode.remove(j);//si reg2 est une généralité de reg1
    }
                    
//on remplit le classificateur final
for(int t=0; t<classif_initial.size(); t++){
    String regle1 = f.decoder_instance(classif_initial.get(t), app);
    if(classif_decode.contains(regle1)){//si le classificateur decode contient la règle qui représente l'individu t
    String regle2 = f.decodage(classif_initial.get(t), app);
    classificateur.add(regle2);//on ajoute la règle sous forme if ... then
    }
}
double pr = fit.precision(DT, classif_decode) *100;//on calcule la précision dela regle courante



System.out.println("\nLa précision du classificateur : "+ pr+"\nLes règles obtenues = ");
for(int c=0;c<classificateur.size();c++){
    System.out.println(classificateur.get(c));
}

}

}
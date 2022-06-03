package fr.ensim.interop.introrest.model.joke;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public final class Jokes {

    private static Jokes instance;
    private AtomicInteger id = new AtomicInteger(0);
    private Map<Integer,Joke> listBlague = new HashMap();

    private Jokes(){

        /*Création des blagues : */
        Joke j1 = new Joke(id.incrementAndGet(), "Plage", "La plage dit à l'océan:\ndire que tout le monde aime l'eau c'est assez vague.", 5);
        listBlague.put(j1.getId(), j1);

        Joke j2 = new Joke(id.incrementAndGet(), "Alligator", "Comment appelle-t-on un alligator qui enquête? \n Un investi-gator.",9);
        listBlague.put(j2.getId(), j2);

        listBlague.put(id.incrementAndGet(),new Joke(id.get(),"Mathématique","Qu'est-ce que le livre de mathématiques dit au conseiller d'orientation?\nJ'ai tellement de problèmes.", 6));
        listBlague.put(id.incrementAndGet(),new Joke(id.get(),"Poisson","Pourquoi tant de poissons vivent-ils dans l'eau salée?\n" +
                "Parce que l'eau poivrée les ferait éternuer!", 7));
        listBlague.put(id.incrementAndGet(),new Joke(id.get(),"Hologe","Si une horloge sonne 13 fois, quelle heure est-il?\n" +
                "Il est l'heure d'acheter une nouvelle horloge!", 1));
        listBlague.put(id.incrementAndGet(),new Joke(id.get(), "Couleur Chat","Quelle est la couleur préférée d'un chat?\nLe rrrrrouge.", 3));
        listBlague.put(id.incrementAndGet(), new Joke(id.get(), "Ordinateur Puce","Pourquoi un ordinateur voudrait-il se gratter?\nParce qu'il a des puces!",2));
        listBlague.put(id.incrementAndGet(), new Joke(id.get(), "Choco-lat","Quel bonbon est toujours blasé?\nLe choco-las",4));
        listBlague.put(id.incrementAndGet(), new Joke(id.get(), "tomate","Quel est le type de blague préféré d'une tomate?\nCelles bien juteuses!",8));
        listBlague.put(id.incrementAndGet(), new Joke(id.get(), "Ordinateur Puce","Quelles lettres ne se trouvent pas dans l'alphabet?\nCelles qui ne sont pas dans le courrier.",10));
    }

    public static Jokes getInstance(){
        if (instance == null){
            instance = new Jokes();
        }
        return instance;
    }

    public Map<Integer, Joke> value(){
        return listBlague;
    }

    public Joke getRandom() {

        Random random = new Random();
        int value = random.nextInt(10);

        return listBlague.get(value);
    }
}

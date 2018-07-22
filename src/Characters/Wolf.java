package Characters;
import java.util.ArrayList;

public class Wolf extends Character {

    public Wolf(String name) {
        super(name);
        this.identity = "Wolf";
    }

    public Character kill_vote(ArrayList<Character> characters){
        System.out.println("Kill vote starts.");
        return super.vote(characters);
    }
    public void get_other_wolves(ArrayList<Wolf> wolves){
        System.out.println("All wolves:");
        for (Wolf w: wolves){
            System.out.println(w.toString());
        }
    }
}

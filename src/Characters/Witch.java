package Characters;

import java.util.ArrayList;
import java.util.Scanner;

public class Witch extends Character{
    private boolean saved; //是否已用解药
    private boolean poisoned; //是否已用毒药


    public Witch(String name) {
        super(name);
        this.saved = false;
        this.poisoned = false;
        this.identity = "Witch";
    }

    public boolean save(Character victim){
        System.out.println(victim.toString() + "has died. Do you want to save him? 0 for no and 1 for yes");
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        while (n != 1 && n != 0){
            System.out.println("Invalid input. Please retry:");
            n = reader.nextInt();
        }
        reader.close();
        if (n == 1)
            this.saved = true;
        return this.saved;
    }

    public Character poison (ArrayList<Character> characters){
        System.out.println("Do you want to use poison? 0 for no and 1 for yes");
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        while (n != 1 && n != 0){
            System.out.println("Invalid input. Please retry:");
            n = reader.nextInt();
        }
        if (n == 1){
            this.poisoned = true;
            return super.vote(characters);
        }
        return null;
    }

    public boolean getPoisonStat() {
        return this.poisoned;
    }

    public boolean getSavedStat() {
        return this.saved;
    }
}


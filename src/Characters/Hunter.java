package Characters;

import java.util.ArrayList;
import java.util.Scanner;

public class Hunter extends Character{

    public Hunter(String name) {
        super(name);
        this.identity = "Hunter";
    }

    public Character shoot(ArrayList<Character> characters){
        System.out.println("You have been killed. Do you want to shoot a person? 0 for no and 1 for yes.");
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        while (n != 1 && n != 0){
            System.out.println("invalid input. Please retry:");
            n = reader.nextInt();
        }
        reader.close();
        if (n == 1)
            return super.vote(characters);
        return null;
    }
}

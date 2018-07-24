import Characters.*;
import Characters.Character;
import Characters.Oracle;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private ArrayList<Character> table;
    private Character currentVictim;
    private int wolfNum;
    private int villagerNum;
    private int specialNum; // special identity

    private Game() {
        this.table = new ArrayList<Character>();
        this.currentVictim = null;
        this.wolfNum = 0;
        this.villagerNum = 0;
        this.specialNum = 0;
    }

    private void set_up_game(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Welcome. Now enter players' name. Enter 'NEXT' to go to next step.");
        String name = "";
        boolean contains;
        ArrayList<String> names = new ArrayList<>();
        while (true){
            System.out.println("Please enter your character's name:");
            name = reader.next();
            if (name.equals("NEXT"))
                break;
            contains = false;
            for (String c: names){
                if (c.equals(name)){
                    System.out.println("Redundant name. Retry.");
                    contains = true;
                }
            }
            if (!contains){
                System.out.println("Your id is " + names.size());
                names.add(name);
                System.out.println("Player list: ");
                for (int i = 0; i < names.size(); i++){
                    System.out.println("Id: " + i + " " + "Name: " + names.get(i));
                }
            }
        }
        int n = names.size();
        this.wolfNum = this.askNum("How many wolves in this game: ", n);
        n -= this.wolfNum;
        int oraclesNum = this.askNum("How many oracles in this game: ", n);
        n -= oraclesNum;
        int witchesNum = this.askNum("How many witches in this game: ", n);
        n -= witchesNum;
        int huntersNum = this.askNum("How many hunters in this game: ", n);
        n -= huntersNum;
        this.villagerNum = n;
        this.specialNum = oraclesNum + huntersNum + witchesNum;
        reader.close();
        this.distributeIdentity(this.wolfNum, oraclesNum, witchesNum, huntersNum, names);
    }

    private boolean game_over(){
        return this.wolfNum == 0 || this.specialNum == 0 || this.villagerNum == 0;
    }

    private void vote_process(){ /////////////

    }  //实现投票进程
    private void wolf_vote(){} //杀人投票 //////////////

    private void distributeIdentity(int wolf, int oracle, int witch, int hunter, ArrayList<String> names) {
        ArrayList<String> a = new ArrayList<>();
        a.add("Hunter");
        a.add("Wolf");
        a.add("Oracle");
        a.add("Witch");
        a.add("Villager");

        String identity;
        int i;
        for (String c : names) {
            if (hunter == 0)
                a.remove("Hunter");
            if (wolf == 0)
                a.remove("Wolf");
            if (oracle == 0)
                a.remove("Oracle");
            if (witch == 0)
                a.remove("Witch");
            i = (int) (Math.random() * a.size());
            identity = a.get(i);
            switch (identity) {
                case "Hunter":
                    this.table.add(new Hunter(c));
                    hunter--;
                    break;
                case "Witch":
                    this.table.add(new Witch(c));
                    witch--;
                    break;
                case "Oracle":
                    this.table.add(new Oracle(c));
                    oracle--;
                    break;
                case "Wolf":
                    this.table.add(new Wolf(c));
                    wolf--;
                    break;
                case "Villager":
                    this.table.add(new Villager(c));
                    break;
                default:
                    System.out.println("Wtf?????");
            }
        }
    }

    private int askNum(String msg, int max){
        Scanner reader = new Scanner(System.in);
        System.out.println(msg);
        int n = reader.nextInt();
        while (n > max){
            System.out.println("Invalid input. Please retry:");
            n = reader.nextInt();
        }
        return n;
    }

    public static void main(String[] args) { /////////////////
        Game game = new Game();
        game.set_up_game();
        while (! game.game_over()){

        }
    }
}

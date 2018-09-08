import Characters.*;
import Characters.Character;
import Characters.Oracle;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private ArrayList<Character> table;
    private Character currentVictim;
    private Character witchsFav; // Victim poisoned by the witch
    private int wolfNum;
    private int villagerNum;
    private int specialNum; // special identity
    private int day;

    private Game() {
        this.table = new ArrayList<>();
        this.currentVictim = null;
        this.witchsFav = null;
        this.wolfNum = 0;
        this.villagerNum = 0;
        this.specialNum = 0;
        this.day = 1;
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
    private void wolf_vote(){ //杀人投票
        boolean allSame = false; // To prevent the wolves from voting different victims
        if (this.wolfNum > 0) { // Only works if there are still wolves in the game.
            while (!allSame) {
                for (Character c : this.table) {
                    if (c instanceof Wolf) {
                        if (this.currentVictim == null) {
                            this.currentVictim = ((Wolf) c).kill_vote(this.table); //Typecast since c is actually a wolf
                            this.currentVictim.incrementWolfVote(); // To check how many time he/she has been voted
                        } else {
                            if (this.currentVictim != ((Wolf) c).kill_vote(this.table)) {
                                System.out.println("The wolves need to agree on one target.");
                                System.out.println("Please discuss and vote again!");
                                this.currentVictim.resetWolfVote();
                                // Will change later to 209 a4 broadcast like function
                                break;
                            }
                        }
                    }
                }
                // If all the wolf votes the same person, then that person dies and this function will end
                if (currentVictim != null) { // To remove potential null pointer exception cuz intelliJ so noisy...
                    if (this.currentVictim.getWolfVotes() == this.wolfNum) {
                        allSame = true;
                        this.currentVictim.resetWolfVote(); //After making sure this person is killed, can reset kill votes
                        // (maybe not needed, we'll see...)
                    } else {
                        this.currentVictim = null;
                    }
                }
            }
        }
    }

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

    private boolean[] oracleSeeking(String name) { // Looks like n^2 but amortized is not actually n^2 i think?
        boolean[] b = new boolean[2];
        b[0] = true; // Indicator of player's identity, true for wolf and false for others
        b[1] = false; // Indicator of player validity

        for (Character c : this.table) {
            if (c instanceof Oracle) {
                for (Character c1 : this.table) {
                    if (c1.getName().equals(name)) {
                        b[0] = ((Oracle) c).check(c1); // Store the answer for oracle in here
                        b[1] = true; // Indicates that the player entered is a valid player
                    }
                }
            }
        }
        return b;
    }

    private Character dyingHunter() {
        return new Character("Not implemented yet!");
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

    private String askName() {
        Scanner sc = new Scanner(System.in);
        String s;
        s = sc.next();
        return s;
    }

    private void poisonOrSave() { // Witch can choose to poison or save people once per game
        for (Character c: this.table) {
            if (c instanceof Witch) {
                String s = witchOptions();
                if (s.equals("Save") && !((Witch)c).getSavedStat()) {
                    ((Witch)c).save(this.currentVictim);
                    this.currentVictim = null; //Since this person has been saved, he won't be the victim this round
                } else if (s.equals("Poison") && !((Witch)c).getPoisonStat()) {
                    this.witchsFav = ((Witch)c).poison(this.table); //Extra person that is going to die
                                                                    // (may or may not be same as victim of wolf)
                }
            }
        }
    }

    private String witchOptions() {
        Scanner sc = new Scanner(System.in);
        String s;
        s = sc.next();
        while (!s.equals("Save") && !s.equals("Poison")) {
            System.out.println("Enter Save or Poison with first letter caps!");
            s = sc.next();
        }

        return s;
    }

    private void votePolice() {
        
    }

    private int getDay() {
        return this.day;
    }

    public static void main(String[] args) { /////////////////
        Game game = new Game();
        game.set_up_game();
        while (! game.game_over()){
            System.out.println("The night has come.");
            System.out.println("Wolves, please wake up and kill one person."); //Wolves' turn to vote
            game.wolf_vote();

            // After voting phase for wolves, its oracles turn to check
            System.out.println("Oracle, please declare who's identity you want to verify"); // Oracle's turn
            boolean[] status;
            String s = game.askName();
            status = game.oracleSeeking(s);
            while (!status[1]) { // Blocks the game until oracle checks a valid player
                System.out.println("The player you want to check in invalid! Try again!");
                s = game.askName();
                status = game.oracleSeeking(s);
            }

            // After passing the while loop, the player name is valid for sure, so identity check completes
            if (status[0]) {
                System.out.println("The person you just picked is a wolf");
            } else {
                System.out.println("The person you just picked is not a wolf");
            }

            // After oracles' checking phase, its time for witch to save/poison people.
            game.poisonOrSave();

            //After Witch poison/save people, the night is over, and the sun is out.
            System.out.println("The night is over! Everyone please wake up!");

            // If the game is in day 1, need to vote for police first
            if (game.getDay() == 1) {
                game.votePolice();
            }
        }
    }
}

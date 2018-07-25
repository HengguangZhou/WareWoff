package Characters;
import java.util.ArrayList;
import java.util.Scanner;


public class Character {
    private String name;
    private boolean alive; // dead or alive
    private int votes; // 投票时使用，结算后清零
    private boolean leader; //是否为警长
    private boolean poisoned; //是否被毒死
    private int wolfVotes; //被狼人投票殺死的次數
    String identity;

    public  Character(String name) {
        this.name = name;
        this.alive = true;
        this.votes = 0;
        this.leader =false;
        this.poisoned = false;
        this.wolfVotes = 0;
    }

    public String getIdentity() {
        return this.identity;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public int getVotes() {
        return this.votes;
    }

    public boolean isLeader() {
        return this.leader;
    }

    public boolean isPoisoned() {
        return this.poisoned;
    }

    public void kill() {
        this.alive = false;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }


    public Character vote(ArrayList<Character> characters){

        /*
          Vote process
          precondition: all person in characters must be alive
          @param model Initialized model
         */
        int i = 0;
        for(Character cha: characters){
            System.out.println(i + " : " + cha.toString());
            i++;
        }
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the index of the person you want to vote to : ");
        int n = reader.nextInt();
        while  (n >= characters.size()){
            System.out.println("Invalid index; please retry");
            n = reader.nextInt();
        }
        reader.close();
        return characters.get(n);
    }

    public void set_poisoned(){
        poisoned = true;
    }

    public void beLeader(){
        this.leader = true;
    }

    public String toString(){
        if (this.leader)
            return this.name + " (leader)";
        return this.name;
    }

    public void incrementWolfVote() { // Incrementing the times this character has been voted by wolves this round
        this.wolfVotes++;
    }

    public int getWolfVotes() {
        return this.wolfVotes;
    }

    public void resetWolfVote() { // Reset votes to 0 after round ends or wolves not agreeing targets
        this.wolfVotes = 0;
    }
}
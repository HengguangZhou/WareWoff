package Characters;

public class Oracle extends Character{

    public Oracle(String name) {
        super(name);
        this.identity = "Oracle";
    }

    public boolean check(Character target){

        return target.identity.equals("Wolf");
    }

}

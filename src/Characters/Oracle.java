package Characters;

public class Oracle extends Character{
    public Oracle(String name) {
        super(name);
        this.identity = "Oracle";
    }

    public void check(Character target){
        if (target.identity.equals("Wolf"))
            System.out.println("This person is wolf");
        else
            System.out.println("This person is NOT wolf");
    }

}

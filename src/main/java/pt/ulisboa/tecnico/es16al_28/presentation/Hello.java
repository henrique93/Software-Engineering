package pt.ulisboa.tecnico.es16al_28.presentation;

public class Hello {

    public static void main(String[] args) {
        System.out.println("Hello MyDrive!");
    }
    public static void bye(String[] args) {
        System.out.println("Godbye MyDrive!");
    }
    public static void greet(String[] args) {
        System.out.println("Hello "+args[0]);
    }
    public static void execute(String[] args) {
        for (String s: args)
            System.out.println("Execute "+args[0]+"?");
    }
    
}

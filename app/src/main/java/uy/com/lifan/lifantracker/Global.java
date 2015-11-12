package uy.com.lifan.lifantracker;

public class Global   {

    //Clase que agrupa todas las variables globales de la app.

    private static Global instance ;
    private String user ;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private Global() {}

    public static Global getInstance() {
        if(instance == null) instance = new Global();
        return instance;
    }
}

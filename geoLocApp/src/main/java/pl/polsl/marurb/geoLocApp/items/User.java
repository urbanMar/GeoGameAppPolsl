package pl.polsl.marurb.geoLocApp.items;

/**
 * Created by B3stia on 23.12.13.
 */
public class User {

    private static long id;
    private static String mail;
    private static String pass;
    private static String login;


    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        User.login = login;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }





}

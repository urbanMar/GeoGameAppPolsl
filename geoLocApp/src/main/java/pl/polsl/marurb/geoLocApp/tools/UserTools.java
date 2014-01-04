package pl.polsl.marurb.geoLocApp.tools;

import pl.polsl.marurb.geoLocApp.items.User;

/**
 * Created by B3stia on 26.12.13.
 */
public class UserTools {

    private String pass = "1233";

    public boolean passwordChcker(User currentUser){
        if( currentUser.getPass().equalsIgnoreCase(pass)){
            return true;
        } else {
            return false;
        }
    }
}

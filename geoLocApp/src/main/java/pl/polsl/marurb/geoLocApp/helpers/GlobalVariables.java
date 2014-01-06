package pl.polsl.marurb.geoLocApp.helpers;

import pl.polsl.marurb.geoLocApp.items.Task;
import pl.polsl.marurb.geoLocApp.items.User;

/**
 * Created by B3stia on 26.12.13.
 */
public class GlobalVariables {
    public static Boolean isInternet = true;
    public static Boolean isGps = true;

    public static final  String baseUrl = "";

    private static final String userLogin = "/users/login-me/login/[USER_MAIL]/pass/[USER_PASS]";
    private static final String userLogout = "/users/logout-me";
    private static final String userRegister = "/users/register-me/login/[USER_LOGIN]/email/[USER_MAIL]/pass/[USER_PASS]";
    private static final String userForgot = "/users/forgot-pass/email/[USER_MAIL]";
    private static final String userInfo = "/users/get-user-info/email/[USER_MAIL]";
    private static final String userEdit = "/users/edit-user/email/[USER_MAIL]";
    private static final String taskStart = "/task/start/email/[USER_MAIL]";
    private static final String taskEnd = "/task/end/email/[USER_MAIL]";

    private static User user;
    private static Task task;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        GlobalVariables.user = user;
    }

    public static Task getTask() {
        return task;
    }

    public static void setTask(Task task) {
        GlobalVariables.task = task;
    }

    public static String getBaseUserUrl() {
        return baseUrl;
    }

    public static String getUserLogin(String mail, String pass) {

        String url = userLogin.replace("[USER_MAIL]", mail);
        url = url.replace("[USER_PASS]", pass);

        return getBaseUserUrl() + url;
    }


    public static String getUserRegister(String login, String mail, String pass) {

        String url = userRegister.replace("[USER_MAIL]", mail);
        url = url.replace("[USER_PASS]", pass);
        url = url.replace("[USER_LOGIN]", login);

        return getBaseUserUrl() + url;
    }



    public static String getUserForgot(String mail) {

        return getBaseUserUrl() + userForgot.replace("[USER_MAIL]", mail);
    }


    public static String getUserInfo() {

        return getBaseUserUrl() + userInfo.replace("[USER_MAIL]", user.getMail());
    }

    public static String getUserLogout() {
        return getBaseUserUrl() + userLogout.replace("[USER_MAIL]", user.getMail());
    }


    public static String getUserEdit() {
        return getBaseUserUrl() + userEdit.replace("[USER_MAIL]", user.getMail());
    }

    public static String getTaskInfo() {
        return getBaseUserUrl() + taskStart.replace("[USER_MAIL]", user.getMail());
    }

}

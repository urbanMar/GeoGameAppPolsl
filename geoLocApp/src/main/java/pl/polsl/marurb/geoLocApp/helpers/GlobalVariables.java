package pl.polsl.marurb.geoLocApp.helpers;

import pl.polsl.marurb.geoLocApp.items.Task;
import pl.polsl.marurb.geoLocApp.items.User;

/**
 * Created by B3stia on 26.12.13.
 */
public class GlobalVariables {
    public static Boolean isInternet = true;
    public static Boolean isGps = true;

    public static final  String baseUrl = "http://89.25.149.91:8080/";

    private static final String userLogin = "GeoGame/mobile/game/login/";
    private static final String userLogout = "GeoGame/mobile/game/logout";
    private static final String userRegister = "GeoGame/mobile/game/register";
    private static final String userForgot = "GeoGame/mobile/game/reminder";
    private static final String userSendCoordinates = "GeoGame/mobile/game/coords";
    private static final String taskAnswear = "GeoGame/mobile/game/checkNode";

    private static final String sendMsg = "GeoGame/mobile/messages/send";
    private static final String getMsg = "GeoGame/mobile/messages/read?userId=[ID]&all=true";

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

    public static String getUserLogin() {

        return getBaseUserUrl() + userLogin;
    }


    public static String getUserRegister() {

        return getBaseUserUrl() + userRegister;
    }



    public static String getUserForgot() {

        return getBaseUserUrl() + userForgot;
    }


    public static String getUserSendCoordinates() {

        return getBaseUserUrl() + userSendCoordinates;
    }

    public static String getUserLogout() {
        return getBaseUserUrl() + userLogout;
    }

    public static String getTaskAnswear() {
        return getBaseUserUrl() + taskAnswear;
    }

    public static String getSendMsg() {
        return getBaseUserUrl() + sendMsg;
    }

    public static String getGetMsg(String id) {
        String url = getMsg.replace("[ID]", id);
        return getBaseUserUrl() + url;
    }
}

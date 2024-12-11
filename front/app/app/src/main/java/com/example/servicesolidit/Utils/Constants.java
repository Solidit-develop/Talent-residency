package com.example.servicesolidit.Utils;

public abstract class Constants {
    public static String SERVER_IP_PROD =  "http://86.38.204.102:";
    public static String SERVER_IP_QA =  "http://189.190.212.181:";
    public static String PORT =  "4000";
    public static String BASE_URL =  SERVER_IP_PROD+PORT+"/api/v1/";

    public static String MY_PREFERENCES = "MyPreferences";
    public static String IS_LOGGED = "IsLogged";
    public static String GET_LOGGED_USER_ID = "LoggedUserId";
    public static String GET_EMAIL_USER = "LoggedUserEmail";
    public static String GET_NAME_USER = "LoggedUserName";
}

public class Session {
    private static int userId;
    private static String userName;

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserName(String name) {
        userName = name;
    }

    public static String getUserName() {
        return userName;
    }
}
package com.example.appfinalproject;

public class ManagerInformation {
    private static ManagerInformation instance;
    public final static String managerEmail = "manager@gmail.com";
    public final static String managerPassword = "manager";
    public String Email;
    public String Bookname;
    public String Image;
    public boolean isManager = false;

    private ManagerInformation() {
        // 私有構造函數，防止外部實例化
    }

    public static synchronized ManagerInformation getInstance() {
        if (instance == null) {
            instance = new ManagerInformation();
        }
        return instance;
    }
}
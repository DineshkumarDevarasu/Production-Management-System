package com.witsoman.maharaja.Util;

public class DBTables {
    public static String REGISTRATION_TABLE="chefregistration_table";
    public static String UNIQUE_ID="id";

    public static String CREATE_TABLE="create table "+ REGISTRATION_TABLE+"("+UNIQUE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DBConstants.CHEFID +" VARCHAR(500),"+DBConstants.CHEFNAME+" VARCHAR(150),"+DBConstants.ORDERID+" VARCHAR(150)"+")";






}

package com.project.hangmani.config.query;

public class UserQueryConst {
    public static final String getUserByOAuthIdSql = "SELECT u.ID, a.oauthid, a.oauthtype, u.email, u.age, u.gender " +
            "FROM user u LEFT JOIN oauth a ON a.ID = u.ID WHERE a.oauthid=?";

    public static final String getUserByIDSql = "SELECT u.ID, a.oauthid, a.oauthtype, u.email, u.age, u.gender FROM user u LEFT JOIN oauth a ON a.ID = u.ID WHERE u.id=?";
    public static final String deleteOAuthSql = "DELETE FROM oauth WHERE ID=?; ";
    public static final String deleteUserSql = "DELETE FROM USER WHERE ID=?;";
    public static final String insertUserSql = "insert into user(email, age, gender, id) values(?,?,?,?);";
    public static final String insertOAuthSql = "insert into oauth(id, oauthtype, oauthID) values(?,?,?);";
}

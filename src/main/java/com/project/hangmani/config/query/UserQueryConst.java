package com.project.hangmani.config.query;

public class UserQueryConst {
    public static final String getUserByOAuthIdSql = "SELECT \n" +
            "    user.ID, oauth.oauthid, oauth.oauthtype, user.email, user.age, user.gender\n" +
            "FROM \n" +
            "    oauth\n" +
            "    JOIN user ON oauth.ID = user.ID\n" +
            "WHERE\n" +
            "  oauth.OAUTHID=?;";

    public static final String getUserByIDSql = "SELECT \n" +
            "    user.ID, oauth.oauthid, oauth.oauthtype, user.email, user.age, user.gender\n" +
            "FROM \n" +
            "    oauth\n" +
            "    JOIN user ON oauth.ID = user.ID\n" +
            "WHERE\n" +
            "  user.id=?;";
    public static final String deleteUserSql = "DELETE FROM oauth WHERE ID = ?; DELETE FROM user WHERE ID = ?;";
    public static final String insertUserSql = "insert into user(email, age, gender, id) values(?,?,?,?);\n"+
            "insert into oauth(id, oauthtype, oauthID) values(?,?,?);";
}

package com.project.hangmani.config.query;

public class StoreQueryConst {
    public static final String getStoreInfoByLatitudeLongitudeSql = "SELECT * FROM store " +
            " where (? < storelatitude and storelatitude < ?) and (? < storelongitude and storelongitude < ?) limit ?;";
    public static final String getStoreInfoByUuid = "SELECT * FROM store where storeuuid=?;";
    public static final String getStoreInfoWithWinCountBySidoSigugun = "SELECT s.storeUuid, s.storeName, s.storeaddress, l.lottoname,\n" +
            "SUM(CASE WHEN w.winRank = 1 THEN 1 ELSE 0 END) AS win1stCount,\n" +
            "SUM(CASE WHEN w.winRank = 2 THEN 1 ELSE 0 END) AS win2stCount\n" +
            "s.storelatitude, s.storelongitude\n"+
            "FROM\n" +
            "    store s\n" +
            "    JOIN win_history w ON s.storeUuid = w.storeUuid\n" +
            "    JOIN lotto_type l ON w.lottoId = l.lottoId\n" +
            "where s.storesido=? and s.storesigugun=?\n" +
            "GROUP BY\n" +
            "    s.storeUuid,\n" +
            "    s.storeName,\n" +
            "    w.winRank,\n" +
            "   l.lottoname\n" +
            "order by l.lottoname, win1stcount, win2stcount desc;";
    public static final String getStoreInfoWithWinCountByLatitudeLongitude = "SELECT s.storeUuid, s.storeName, s.storeaddress, l.lottoname,\n" +
            "SUM(CASE WHEN w.winRank = 1 THEN 1 ELSE 0 END) AS win1stCount,\n" +
            "SUM(CASE WHEN w.winRank = 2 THEN 1 ELSE 0 END) AS win2stCount\n" +
            "s.storelatitude, s.storelongitude\n"+
            "FROM\n" +
            "    store s\n" +
            "    JOIN win_history w ON s.storeUuid = w.storeUuid\n" +
            "    JOIN lotto_type l ON w.lottoId = l.lottoId\n" +
            "where (? < storelatitude and storelatitude < ?) and (? < storelongitude and storelongitude < ?)\n" +
            "GROUP BY\n" +
            "    s.storeUuid,\n" +
            "    s.storeName,\n" +
            "    w.winRank,\n" +
            "   l.lottoname\n" +
            "order by l.lottoname, win1stcount, win2stcount desc;";
    public static final String updateStoreInfoByUuid = "UPDATE store SET storename=?,storeaddress=?,"+
            "storelatitude=?,storelongitude=?,storebizno=?,storetelnum=?,storemobilenum=?,storeopentime=?,"+
            "storeclosetime=?,storeisactivity=?,storesido=?,storesigugun=? where storeuuid=?;";
    public static final String getStoreInfoByName = "SELECT * FROM store WHERE storename=? and "+
            "storelatitude like CONCAT(?, '%') and storelongitude like CONCAT(?, '%');";
    public static final String insertStoreInfo = "insert into store(storeuuid, storename, storeaddress,storelatitude, storelongitude," +
            "storebizno,storetelnum, STOREMOBILENUM, STOREOPENTIME,STORECLOSETIME,STORESIDO,STORESIGUGUN) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?); insert into ";

    public static final String insertStoreAttachment = "insert info store_attachment(attatchment_no, original_file_name, SAVED_FILE_NAME, FILE_SIZE, UPLOAD_TIME, FILE_DATA, storeuuid)" +
            " values(?,?,?,?,?,?,?)";
    public static final String deleteStoreInfoByUuid = "UPDATE  store SET STOREISACTIVITY=1 where storeuuid=?";
    public static final String findBySidoSigugun = "SELECT * from store WHERE storesido=? and storesigugun=?";
    public static final String deleteStoreInfoByLatiLongi = "UPDATE  store SET STOREISACTIVITY=1 where storelatitude=? and storelongitude=?";
}

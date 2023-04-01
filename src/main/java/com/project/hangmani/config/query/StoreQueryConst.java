package com.project.hangmani.config.query;

public class StoreQueryConst {
    public static final String getStoreInfoByLatitudeLongitudeSql = "SELECT s.storeUuid, s.storeName, s.storeAddress, s.storeLatitude, s.storeLongitude, " +
            "       s.storeBizNo, s.storeTelNum, s.storeMobileNum, s.storeOpenTime, s.storeCloseTime, " +
            "       s.storeIsActivity, s.storeSido, s.storeSigugun, COALESCE(GROUP_CONCAT(DISTINCT sa.saved_file_name ORDER BY sa.saved_file_name ASC SEPARATOR ', '), '') AS saved_file_names\n" +
            " FROM store s join store_attachment sa on s.storeuuid=sa.storeuuid " +
            " where (? < s.storelatitude and s.storelatitude < ?) and (? < s.storelongitude and s.storelongitude < ?) limit ?;";
    public static final String getStoreInfoByUuid = "SELECT s.storeUuid, s.storeName, s.storeAddress, s.storeLatitude, s.storeLongitude, " +
            "s.storeBizNo, s.storeTelNum, s.storeMobileNum, s.storeOpenTime, s.storeCloseTime, " +
            "s.storeIsActivity, s.storeSido, s.storeSigugun, " +
            "COALESCE(GROUP_CONCAT(DISTINCT sa.saved_file_name ORDER BY sa.saved_file_name ASC SEPARATOR ', '), '') AS saved_file_names " +
            "FROM store s join store_attachment sa on s.storeuuid=sa.storeuuid " +
            "where s.storeuuid=?;";
    public static final String getStoreInfoWithWinCountBySidoSigugun = "SELECT s.storeUuid, s.storeName, s.storeaddress, l.lottoname, " +
            "SUM(CASE WHEN w.winRank = 1 THEN 1 ELSE 0 END) AS win1stCount, " +
            "SUM(CASE WHEN w.winRank = 2 THEN 1 ELSE 0 END) AS win2stCount," +
            "s.storelatitude, s.storelongitude, " +
            "COALESCE(GROUP_CONCAT(DISTINCT sa.saved_file_name ORDER BY sa.saved_file_name ASC SEPARATOR ', '), '') AS saved_file_names, " +
            "distance(?, ?, s.storelatitude, s.storelongitude) AS distance "+
            "FROM " +
            "    store s\n" +
            "    JOIN win_history w ON s.storeUuid = w.storeUuid\n" +
            "    JOIN lotto_type l ON w.lottoId = l.lottoId\n" +
            "    LEFT JOIN store_attachment sa ON s.storeUuid = sa.storeUuid\n" +
            "WHERE s.storesido=? and s.storesigugun=? and l.lottoname=?\n" +
            "GROUP BY s.storeUuid, s.storeName, s.storeaddress, l.lottoname, s.storelatitude, s.storelongitude\n";
    public static final String getStoreInfoWithWinCountByLatitudeLongitude = "SELECT s.storeUuid, s.storeName, s.storeaddress, l.lottoname, " +
            "SUM(CASE WHEN w.winRank = 1 THEN 1 ELSE 0 END) AS win1stCount, " +
            "SUM(CASE WHEN w.winRank = 2 THEN 1 ELSE 0 END) AS win2stCount, " +
            "s.storelatitude, s.storelongitude, "+
            "COALESCE(GROUP_CONCAT(DISTINCT sa.saved_file_name ORDER BY sa.saved_file_name ASC SEPARATOR ', '), '') AS saved_file_names, " +
            "distance(?, ?, s.storelatitude, s.storelongitude) AS distance "+
            "FROM " +
            "    store s " +
            "    JOIN win_history w ON s.storeUuid = w.storeUuid " +
            "    JOIN store_attachment sa ON s.storeUuid = sa.storeUuid " +
            "    JOIN lotto_type l ON w.lottoId = l.lottoId " +
            "where (? < storelatitude and storelatitude < ?) and (? < storelongitude and storelongitude < ?) and l.lottoname=? " +
            "GROUP BY s.storeUuid, s.storeName, s.storeaddress, l.lottoname, s.storelatitude, s.storelongitude\n";
    public static final String orderBy1st = "order by win1stcount desc;";
    public static final String orderBy2st = "order by win2stcount desc;";
    public static final String orderByDistance = "order by distance;";
    public static final String updateStoreInfoByUuid = "UPDATE store SET storename=?,storeaddress=?,"+
            "storelatitude=?,storelongitude=?,storebizno=?,storetelnum=?,storemobilenum=?,storeopentime=?,"+
            "storeclosetime=?,storeisactivity=?,storesido=?,storesigugun=? where storeuuid=?;";
    public static final String getStoreInfoByName = "SELECT * FROM store WHERE storename=? and "+
            "storelatitude like CONCAT(?, '%') and storelongitude like CONCAT(?, '%');";
    public static final String insertStoreInfo = "insert into store(storeuuid, storename, storeaddress,storelatitude, storelongitude," +
            "storebizno,storetelnum, STOREMOBILENUM, STOREOPENTIME,STORECLOSETIME,STORESIDO,STORESIGUGUN) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?); insert into ";

    public static final String insertStoreAttachment = "insert into store_attachment(original_file_name, SAVED_FILE_NAME, FILE_SIZE, UPLOAD_TIME, storeuuid)" +
            " values(?,?,?,?,?)";
    public static final String deleteStoreInfoByUuid = "UPDATE  store SET STOREISACTIVITY=1 where storeuuid=?";
    public static final String findBySidoSigugun = "SELECT * from store WHERE storesido=? and storesigugun=?";
    public static final String deleteStoreInfoByLatiLongi = "UPDATE  store SET STOREISACTIVITY=1 where storelatitude=? and storelongitude=?";
}
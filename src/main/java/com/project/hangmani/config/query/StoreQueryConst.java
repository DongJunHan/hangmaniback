package com.project.hangmani.config.query;

public class StoreQueryConst {
    public static final String getStoreInfoByLatitudeLongitudeSql = "SELECT s.storeUuid, s.storeName, s.storeAddress, s.storeLatitude, s.storeLongitude, " +
            "       s.storeBizNo, s.storeTelNum, s.storeMobileNum, s.storeOpenTime, s.storeCloseTime, " +
            "       s.storeIsActivity, s.storeSido, s.storeSigugun, COALESCE(GROUP_CONCAT(DISTINCT sa.saved_file_name ORDER BY sa.saved_file_name ASC SEPARATOR ', '), '') AS saved_file_names\n" +
            " FROM store s join store_attachment sa on s.storeuuid=sa.storeuuid " +
            " where (? < s.storelatitude and s.storelatitude < ?) and (? < s.storelongitude and s.storelongitude < ?);";
    public static final String getStoreInfoWithWinHistory = "SELECT s.storeUuid, s.storeName, s.storeAddress, s.storeLatitude, s.storeLongitude, " +
            "s.storeBizNo, s.storeTelNum, s.storeMobileNum, s.storeOpenTime, s.storeCloseTime, " +
            "s.storeIsActivity, s.storeSido, s.storeSigugun, w.winround, w.winrank, w.lottoid " +
            "FROM store s " +
            "LEFT JOIN win_history w ON s.storeUuid = w.storeUuid ";
    // GROUP BY s.storeuuid
    public static final String getStoreInfoByUuidTest = "SELECT s.storeUuid, s.storeName, s.storeAddress, s.storeLatitude, s.storeLongitude, " +
            "s.storeBizNo, s.storeTelNum, s.storeMobileNum, s.storeOpenTime, s.storeCloseTime, " +
            "s.storeIsActivity, s.storeSido, s.storeSigugun,  " +
            "(SELECT COUNT(*) FROM win_history WHERE storeuuid = s.storeuuid AND winRank = 1) AS win1stCount, " +
            "(SELECT COUNT(*) FROM win_history WHERE storeuuid = s.storeuuid AND winRank = 2) AS win2stCount, " +
            "(SELECT COALESCE(GROUP_CONCAT(l.lottoname ORDER BY l.lottoname ASC SEPARATOR ','),'') AS lottonames " +
            "FROM lotto_type_handle lh JOIN lotto_type l ON l.lottoid=lh.lottoid " +
            "WHERE lh.storeuuid=s.storeuuid) AS lottonames, "+
            "COALESCE(GROUP_CONCAT(DISTINCT sa.saved_file_name ORDER BY sa.saved_file_name ASC SEPARATOR ','), '') AS saved_file_names " +
            "FROM store s " +
            "LEFT JOIN store_attachment sa ON s.storeUuid = sa.storeUuid WHERE s.storeuuid=?";
    public static final String getStoreInfoWithWinHistoryDistance =
                    "SELECT s.storeUuid, s.storeName, s.storeaddress," +
                    "get_distance(?, ?, s.storelatitude, s.storelongitude) AS distance , w.winround, w.winrank, " +
                    "s.storelatitude, s.storelongitude, s.storeisActivity, w.lottoid " +
                    "FROM store s LEFT JOIN win_history w ON s.storeUuid = w.storeUuid ";
    public static final String whereSidoSigugunLottoId = "WHERE s.storesido=? and s.storesigugun=? and l.lottoid=? and s.storeIsActivity=false ";
    public static final String whereSidoSigugun = "WHERE s.storesido=? and s.storesigugun=? ";
    public static final String whereSido = "WHERE s.storesido=? ";
    public static final String whereStoreUuid = "WHERE s.storeuuid=? ";
    public static final String whereLatitudeLongitude = "WHERE (? < s.storelatitude and s.storelatitude < ?) " +
            "and (? < s.storelongitude and s.storelongitude < ?) ";
    public static final String orderByFirstDesc = "ORDER BY s.storeUuid desc; ";

    public static final String orderByFirst = "ORDER BY s.storeUuid; ";
    public static final String getLottoName = "SELECT s.storeuuid, l.lottoname, l.lottoid " +
            "FROM store s LEFT JOIN lotto_type_handle lh ON s.storeuuid=lh.storeuuid JOIN lotto_type l ON lh.lottoid=l.lottoid \n";
    public static final String getStoreAttachment =  "SELECT sa.storeuuid, sa.saved_file_name, sa.original_file_name " +
            "FROM store s LEFT JOIN store_attachment sa ON s.storeuuid=sa.storeuuid ";
    public static final String getStoreInfoWithWinCountByLatitudeLongitude =
            "SELECT s.storeUuid, s.storeName, s.storeaddress, " +
            "(SELECT COUNT(*) FROM win_history WHERE storeuuid = s.storeuuid AND winRank = 1) AS win1stCount, " +
            "(SELECT COUNT(*) FROM win_history WHERE storeuuid = s.storeuuid AND winRank = 2) AS win2stCount, " +
            "s.storelatitude, s.storelongitude, "+
            "(SELECT COALESCE(GROUP_CONCAT(l.lottoname ORDER BY l.lottoname ASC SEPARATOR ','),'') AS lottonames " +
                "FROM lotto_type_handle lh JOIN lotto_type l ON l.lottoid=lh.lottoid " +
                "WHERE lh.storeuuid=s.storeuuid) AS lottonames, "+
            "COALESCE(GROUP_CONCAT(DISTINCT sa.saved_file_name ORDER BY sa.saved_file_name ASC SEPARATOR ','), '') AS saved_file_names, " +
            "get_distance(?, ?, s.storelatitude, s.storelongitude) AS distance "+
            "FROM store s " +
            "JOIN lotto_type_handle lh ON s.storeuuid = lh.storeuuid\n" +
            "JOIN lotto_type l on l.lottoid=lh.lottoid "+
            "LEFT JOIN store_attachment sa ON s.storeUuid = sa.storeUuid " +
            "WHERE (? < storelatitude and storelatitude < ?) and (? < storelongitude and storelongitude < ?) and l.lottoid=? " +
            "GROUP BY s.storeUuid, s.storeName, s.storeaddress, l.lottoname, s.storelatitude, s.storelongitude\n";
    public static final String getLottoTypeHandleBySidoSigugun =
            "SELECT s.storeuuid, s.storename, COALESCE(GROUP_CONCAT(l.lottoname ORDER BY l.lottoname ASC SEPARATOR ','),'') AS lottonames "+
            "FROM store s JOIN lotto_type_handle lh ON s.storeuuid=lh.storeuuid JOIN lotto_type l ON l.lottoid=lh.lottoid "+
            "WHERE s.storesido=? and s.storesigugun=? group by s.storeuuid ;";
    public static final String orderBy1st = "order by win1stcount desc";
    public static final String orderBy2st = "order by win2stcount desc";
    public static final String orderByDistance = "order by distance";
    public static final String updateStoreInfoByUuid = "UPDATE store SET storename=?,storeaddress=?,"+
            "storelatitude=?,storelongitude=?,storebizno=?,storetelnum=?,storemobilenum=?,storeopentime=?,"+
            "storeclosetime=?,storeisactivity=?,storesido=?,storesigugun=? where storeuuid=?;";
    public static final String getStoreInfoByName = "SELECT * FROM store WHERE storename=? and "+
            "storelatitude like CONCAT(?, '%') and storelongitude like CONCAT(?, '%');";
    public static final String add = "insert into store(storeuuid, storename, storeaddress,storelatitude, storelongitude," +
            "storebizno,storetelnum, STOREMOBILENUM, STOREOPENTIME,STORECLOSETIME,STORESIDO,STORESIGUGUN) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?);";

    public static final String insertStoreAttachment = "insert into store_attachment(original_file_name, SAVED_FILE_NAME, FILE_SIZE, storeuuid)" +
            " values(?,?,?,?)";
    public static final String deleteStoreInfoByUuid = "UPDATE  store SET storeIsActivity=1 where storeuuid=?";
//    public static final String deleteStoreInfoByLatiLongi = "UPDATE  store SET storeIsActivity=1 where storelatitude=? and storelongitude=?";
}

package com.project.hangmani.repository;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.domain.LottoType;
import com.project.hangmani.domain.Store;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.domain.WinHistory;
import com.project.hangmani.dto.StoreDTO.*;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

import static com.project.hangmani.config.query.StoreQueryConst.*;

@Repository
@Slf4j
public class StoreRepository {
    private JdbcTemplate template;
    private RequestConvert requestConvert;
    private ConvertData convertData;

    private final Map<String, String> sidoMap;
    private Util util;

    public StoreRepository(DataSource dataSource, Util util) {
        log.info("StoreRepository={}", dataSource);
        this.template = new JdbcTemplate(dataSource);
        this.convertData = new ConvertData();
        this.requestConvert = new RequestConvert();
        this.util = util;
        this.sidoMap = new HashMap<>();
        sidoMap.put("경기도", "경기");
        sidoMap.put("서울특별시", "서울");
        sidoMap.put("세종특별자치시", "세종");
        sidoMap.put("제주특별자치도", "제주");
        sidoMap.put("제주도", "제주");
        sidoMap.put("인천광역시", "인천");
        sidoMap.put("광주광역시", "광주");
        sidoMap.put("울산광역시", "울산");
        sidoMap.put("대구광역시", "대구");
        sidoMap.put("부산광역시", "부산");
        sidoMap.put("대전광역시", "대전");
    }

    public List<List<String>> getTableList() {
        return template.query("SHOW TABLES;", listRowMapper());
    }
    private RowMapper<List<String>> listRowMapper() {
        return (rs, rowNum) -> {
            List<String> ret = new ArrayList<>();
            ret.add(rs.getString("TABLE_NAME"));
            ret.add(rs.getString("TABLE_SCHEMA"));
            return ret;
        };
    }
    public List<Store> getStoreInfoBySido(RequestStoreFilterDTO requestDTO) {
        return template.query(getStoreInfoWithWinHistory + whereSido + orderByFirst,
                new Object[]{requestDTO.getSido()}, storeWinRankRowMapper());
    }
    public List<Store> getStoreInfoByLatitudeLongitude(RequestStoresDTO requestStoresDTO) {
        Double startLatitude = requestStoresDTO.getStartLatitude();
        Double endLatitude = requestStoresDTO.getEndLatitude();
        Double startLongitude = requestStoresDTO.getStartLongitude();
        Double endLongitude = requestStoresDTO.getEndLongitude();
        int limit = requestStoresDTO.getLimit();

        return template.query(getStoreInfoByLatitudeLongitudeSql, new Object[]{startLatitude, endLatitude
                , startLongitude, endLongitude, limit}, storesRowMapper());
    }
    public List<Store> getStoreInfoByUuid(String storeUuid) {
        return template.query(getStoreInfoWithWinHistory + whereStoreUuid , new Object[]{storeUuid}, storeWinRankRowMapper());
    }
    public List<LottoType> getLottoNameByUuid(String storeUuid) {
        return template.query(getLottoName + whereStoreUuid, new Object[]{
                        storeUuid},
                lottoTypeRowMapper());
    }
    public List<StoreAttachment> getStoreAttachmentByUuid(String storeUuid) {
        return template.query(getStoreAttachment + whereStoreUuid, new Object[]{
                        storeUuid},
                storeAttachmentRowMapper());
    }
    public List<Store> getStoreInfoWithWinRankBySidoSigugun(RequestStoreFilterDTO requestDTO) {
        return template.query(getStoreInfoWithWinHistoryDistance + whereSidoSigugun + orderByFirstDesc,
                        new Object[]{requestDTO.getUserLatitude(), requestDTO.getUserLongitude()
                                ,requestDTO.getSido(), requestDTO.getSigugun()},
                storeFilterRowMapper());

    }
    public List<LottoType> getLottoNameBySidoSigugun(RequestStoreFilterDTO requestDTO) {
        String sqlQuery;
        if (null == requestDTO.getSigugun()) {
            sqlQuery = getLottoName + whereSido + orderByFirst;
            return template.query(sqlQuery, new Object[]{
                            requestDTO.getSido()},
                    lottoTypeRowMapper());
        }
        else{
            sqlQuery = getLottoName + whereSidoSigugun + orderByFirst;
            return template.query(sqlQuery, new Object[]{
                requestDTO.getSido(), requestDTO.getSigugun()},
                lottoTypeRowMapper());
        }
    }

    public List<LottoType> getLottoNameByLatitudeLongitude(RequestStoreFilterDTO requestDTO) {
        final String sqlQuery = getLottoName + whereLatitudeLongitude + orderByFirst;
        return template.query(sqlQuery, new Object[]{
                        requestDTO.getStartLatitude(), requestDTO.getEndLatitude(),
                        requestDTO.getStartLongitude(), requestDTO.getEndLongitude()},
                lottoTypeRowMapper());
    }
    private RowMapper<LottoType> lottoTypeRowMapper() {
        return (rs, rowNum) -> {
            LottoType l = new LottoType();
            l.setLottoId(rs.getInt("lottoid"));
            l.setStoreUuid(rs.getString("storeuuid"));
            l.setLottoName(rs.getString("lottoname"));
            return l;
        };
    }

    public List<StoreAttachment> getStoreAttachmentBySidoSigugun(RequestStoreFilterDTO requestDTO) {
        String sqlQuery;
        if (null == requestDTO.getSigugun()) {
            sqlQuery = getStoreAttachment + whereSido + orderByFirst;
            return template.query(sqlQuery, new Object[]{
                            requestDTO.getSido()},
                    storeAttachmentRowMapper());
        }
        else {
            sqlQuery = getStoreAttachment + whereSidoSigugun + orderByFirst;
            return template.query(sqlQuery, new Object[]{
                            requestDTO.getSido(), requestDTO.getSigugun()},
                    storeAttachmentRowMapper());
        }
    }
    private RowMapper<StoreAttachment> storeAttachmentRowMapper() {
        return (rs, rowNum) -> {
            StoreAttachment s = new StoreAttachment();
            s.setStoreUuid(rs.getString("storeuuid"));
            s.setSavedFileName((rs.getString("saved_file_name")));
            return s;
        };
    }
    public List<Store> getStoreInfoWithWinRankByLatitudeLongitude(RequestStoreFilterDTO requestDTO) {
        log.debug("storeinfo latitude logitude requestDTO: {}", requestDTO);
        return template.query(getStoreInfoWithWinHistoryDistance + whereLatitudeLongitude + orderByFirstDesc,
                new Object[]{requestDTO.getUserLatitude(), requestDTO.getUserLongitude(),
                        requestDTO.getStartLatitude(), requestDTO.getEndLatitude(), requestDTO.getStartLongitude(),
                        requestDTO.getEndLongitude()},
                storeFilterRowMapper());
    }


    public int updateStoreInfo(String StoreUuid, RequestStoreUpdateDTO requestStoreUpdateDTO) {
        Store store = requestConvert.convertEntity(requestStoreUpdateDTO);
        Object[] objects = {store.getStoreName(),store.getStoreAddress(), store.getStoreLatitude(),
        store.getStoreLongitude(), store.getStoreBizNo(), store.getStoreTelNum(), store.getStoreMobileNum(),
        store.getStoreOpenTime(), store.getStoreCloseTime(), store.getStoreIsActivity(), store.getStoreSido(),
        store.getStoreSigugun(), StoreUuid};
        return template.update(updateStoreInfoByUuid, objects);
    }
    public Store getStoreByNameLatiLongi(RequestStoreInsertDTO requestStoreDTO) {
        String storeName = requestStoreDTO.getStoreName();
        Double storeLatitude = convertPoint(requestStoreDTO.getStoreLatitude());
        Double storeLongitude = convertPoint(requestStoreDTO.getStoreLongitude());

        List<Store> query = template.query(getStoreInfoByName,
                new Object[]{storeName, storeLatitude, storeLongitude},
                storeTableMapper());
        if (query.size() == 0)
            return null;
        else
            return query.get(0);
    }


    public int deleteByStoreUuid(RequestStoreDeleteDTO requestStoreDeleteDTO) {
        String uuid = requestStoreDeleteDTO.getStoreUuid();
//        deleteByUuid
        return template.update(deleteStoreInfoByUuid, new Object[]{uuid});
    }

    public int deleteByLatitudeLongitude(RequestStoreDeleteDTO requestStoreDeleteDTO) {
        return template.update(deleteStoreInfoByLatiLongi, new Object[]{requestStoreDeleteDTO.getLatitude(),
                requestStoreDeleteDTO.getLongitude()});
    }
    /**
     * @param requestStoreDTO
     * @return
     */
    public String insertStoreInfo(RequestStoreInsertDTO requestStoreDTO) {
        //make store uuid
        UUID uuid = this.util.getUUID();
        String[] sidoSigugun = extractSidoSigugunByAddress(requestStoreDTO.getStoreAddress());
        if (sidoSigugun[0] == null)
            return null;
        int ret =  template.update(insertStoreInfo, new Object[]{uuid.toString(), requestStoreDTO.getStoreName(),
                requestStoreDTO.getStoreAddress(), requestStoreDTO.getStoreLatitude().toString(), requestStoreDTO.getStoreLongitude().toString(),
                requestStoreDTO.getStoreBizNo(), requestStoreDTO.getStoreTelNum(), requestStoreDTO.getStoreMobileNum(),
                requestStoreDTO.getStoreOpenTime(), requestStoreDTO.getStoreCloseTime(), sidoSigugun[0], sidoSigugun[1]});
        if (ret == 0){
            throw new FailInsertData();
        }
        return uuid.toString();
    }

    private String[] extractSidoSigugunByAddress(String address) {
        String[] ret = new String[2];
        String[] splitAddress = address.split(" ");
        if (splitAddress[0].length() == 0){
            return ret;
        }
        String convertSido = sidoMap.get(splitAddress[0]);
        if (convertSido != null) {
            splitAddress[0] = convertSido;
            if (splitAddress[0].equals("세종")){
                ret[0] = splitAddress[0];
                ret[1] = null;
                return ret;
            }
        }
        ret[0] = splitAddress[0];
        int firstLength = splitAddress[1].length();
        int secondLength = splitAddress[2].length();
        if (splitAddress[1].substring(firstLength - 1, firstLength).equals("시") &&
                splitAddress[2].substring(secondLength - 1, secondLength).equals("구"))
            ret[1] = splitAddress[1] + " " + splitAddress[2];
        else
            ret[1] = splitAddress[1];
        return ret;

    }
    private Double convertPoint(Double value) {
        String[] splitPoint = value.toString().split("\\.");
        String cutPoint = splitPoint[1].substring(0,3);
        return Double.parseDouble(splitPoint[0]+ "." + cutPoint);
    }
    private RowMapper<Store> storeFilterRowMapper() {
        return (rs, rowNum) -> {
            Store store = new Store();
            store.setStoreUuid(rs.getString("storeUuid"));
            store.setStoreName(rs.getString("storeName"));
            store.setStoreAddress(rs.getString("storeAddress"));
            store.setStoreLatitude(rs.getDouble("storeLatitude"));
            store.setStoreLongitude(rs.getDouble("storeLongitude"));
            store.setDistance(rs.getDouble("distance"));
            store.setStoreIsActivity(rs.getBoolean("storeIsActivity"));
            store.setWinRank(rs.getInt("winRank"));
            store.setWinRound(rs.getInt("winRound"));
            return store;
        };
    }
    private RowMapper<WinHistory> winHistoryRowMapper() {
        return (rs, rowNum) -> {
            WinHistory w = new WinHistory();
            w.setStoreUuid(rs.getString("storeuuid"));
            w.setWinRank(rs.getInt("winRank"));
            w.setWinRound(rs.getInt("winRound"));
            w.setLottoId(rs.getInt("lottoId"));
            return w;
        };
    }
    private RowMapper<Store> storeRowMapper() {
        return (rs, rowNum) -> {
            Store store = new Store();
            store.setStoreUuid(rs.getString("storeuuid"));
            store.setStoreName(rs.getString("storename"));
            store.setStoreAddress(rs.getString("storeaddress"));
            store.setStoreLatitude(rs.getDouble("storelatitude"));
            store.setStoreLongitude(rs.getDouble("storelongitude"));
            store.setStoreBizNo(rs.getString("storebizno"));
            store.setStoreTelNum(rs.getString("storetelnum"));
            store.setStoreMobileNum(rs.getString("storemobilenum"));
            store.setStoreOpenTime(rs.getString("storeopentime"));
            store.setStoreCloseTime(rs.getString("storeclosetime"));
            store.setStoreIsActivity(rs.getBoolean("storeisactivity"));
            store.setStoreSido(rs.getString("storesido"));
            store.setStoreSigugun(rs.getString("storesigugun"));
            store.setLottoName(rs.getString("lottonames"));
            store.setWin1stCount(rs.getInt("win1stCount"));
            store.setWin2stCount(rs.getInt("win2stCount"));
            store.setSavedFileNames(rs.getString("saved_file_names"));
            return store;
        };
    }


    private RowMapper<Store> storesRowMapper(){
        return (rs, rowNum) -> {
            Store store = new Store();
            store.setStoreUuid(rs.getString("storeuuid"));
            store.setStoreName(rs.getString("storename"));
            store.setStoreAddress(rs.getString("storeaddress"));
            store.setStoreLatitude(rs.getDouble("storelatitude"));
            store.setStoreLongitude(rs.getDouble("storelongitude"));
            store.setStoreBizNo(rs.getString("storebizno"));
            store.setStoreTelNum(rs.getString("storetelnum"));
            store.setStoreMobileNum(rs.getString("storemobilenum"));
            store.setStoreOpenTime(rs.getString("storeopentime"));
            store.setStoreCloseTime(rs.getString("storeclosetime"));
            store.setStoreIsActivity(rs.getBoolean("storeisactivity"));
            store.setStoreSido(rs.getString("storesido"));
            store.setStoreSigugun(rs.getString("storesigugun"));
            store.setSavedFileNames(rs.getString("saved_file_names"));
            return store;
        };
    }
    private RowMapper<Store> storeWinRankRowMapper(){
        return (rs, rowNum) -> {
            Store store = new Store();
            store.setStoreUuid(rs.getString("storeuuid"));
            store.setStoreName(rs.getString("storename"));
            store.setStoreAddress(rs.getString("storeaddress"));
            store.setStoreLatitude(rs.getDouble("storelatitude"));
            store.setStoreLongitude(rs.getDouble("storelongitude"));
            store.setStoreBizNo(rs.getString("storebizno"));
            store.setStoreTelNum(rs.getString("storetelnum"));
            store.setStoreMobileNum(rs.getString("storemobilenum"));
            store.setStoreOpenTime(rs.getString("storeopentime"));
            store.setStoreCloseTime(rs.getString("storeclosetime"));
            store.setStoreIsActivity(rs.getBoolean("storeisactivity"));
            store.setStoreSido(rs.getString("storesido"));
            store.setStoreSigugun(rs.getString("storesigugun"));
            store.setWinRank(rs.getInt("winRank"));
            store.setWinRound(rs.getInt("winRound"));
            return store;
        };
    }

    private RowMapper<Store> storeTableMapper() {
        return (rs, rowNum) -> {
            Store store = new Store();
            store.setStoreUuid(rs.getString("storeuuid"));
            store.setStoreName(rs.getString("storename"));
            store.setStoreAddress(rs.getString("storeaddress"));
            store.setStoreLatitude(rs.getDouble("storelatitude"));
            store.setStoreLongitude(rs.getDouble("storelongitude"));
            store.setStoreBizNo(rs.getString("storebizno"));
            store.setStoreTelNum(rs.getString("storetelnum"));
            store.setStoreMobileNum(rs.getString("storemobilenum"));
            store.setStoreOpenTime(rs.getString("storeopentime"));
            store.setStoreCloseTime(rs.getString("storeclosetime"));
            store.setStoreIsActivity(rs.getBoolean("storeisactivity"));
            store.setStoreSido(rs.getString("storesido"));
            store.setStoreSigugun(rs.getString("storesigugun"));
            return store;
        };
    }
}

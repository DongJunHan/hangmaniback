package com.project.hangmani.repository;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO.*;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public List<Store> getStoreInfoByLatitudeLongitude(RequestStoresDTO requestStoresDTO) {
        Double startLatitude = requestStoresDTO.getStartLatitude();
        Double endLatitude = requestStoresDTO.getEndLatitude();
        Double startLongitude = requestStoresDTO.getStartLongitude();
        Double endLongitude = requestStoresDTO.getEndLongitude();
        int limit = requestStoresDTO.getLimit();

        return template.query(getStoreInfoByLatitudeLongitudeSql, new Object[]{startLatitude, endLatitude
                , startLongitude, endLongitude, limit}, storesRowMapper());
    }

    public Store getStoreInfoByUuid(String storeUuid) {
        return template.queryForObject(getStoreInfoByUuid, new Object[]{storeUuid}, storeRowMapper());
    }
    public List<Store> getStoreInfoWithWinCountBySidoSigugun(RequestStoreFilterDTO requestDTO) {
        String sqlQuery;
        if (requestDTO.getFilter().equals("2st"))
            sqlQuery = getStoreInfoWithWinCountBySidoSigugun + orderBy2st;
        else if (requestDTO.getFilter().equals("1st"))
            sqlQuery = getStoreInfoWithWinCountBySidoSigugun + orderBy1st;
        else
            sqlQuery = getStoreInfoWithWinCountBySidoSigugun + orderByDistance;
        return template.query(sqlQuery, new Object[]{ requestDTO.getUserLatitude(), requestDTO.getUserLongitude(),
                        requestDTO.getSido(), requestDTO.getSigugun(), requestDTO.getLottoID()},
                storeWithWinCountLottoNameRowMapper());
    }

    public List<Store> getStoreInfoWithWinCountByLatitudeLongitude(RequestStoreFilterDTO requestDTO) {
        String sqlQuery;
        if (requestDTO.getFilter().equals("2st"))
            sqlQuery = getStoreInfoWithWinCountByLatitudeLongitude + orderBy2st;
        else if (requestDTO.getFilter().equals("1st"))
            sqlQuery = getStoreInfoWithWinCountByLatitudeLongitude + orderBy1st;
        else
            sqlQuery = getStoreInfoWithWinCountByLatitudeLongitude + orderByDistance;
        return template.query(sqlQuery, new Object[]{requestDTO.getUserLatitude(), requestDTO.getUserLongitude(),
                        requestDTO.getStartLatitude(), requestDTO.getEndLatitude(), requestDTO.getStartLongitude(),
                        requestDTO.getEndLongitude(), requestDTO.getLottoID()},
                storeWithWinCountLottoNameRowMapper());
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
                storeRowMapper());
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

        int ret =  template.update(insertStoreInfo, new Object[]{uuid.toString(), requestStoreDTO.getStoreName(),
                requestStoreDTO.getStoreAddress(), requestStoreDTO.getStoreLatitude(), requestStoreDTO.getStoreLongitude(),
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

    private RowMapper<Store> storeWinCountMapper() {
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
    private RowMapper<Store> storeWithWinCountLottoNameRowMapper() {
        return (rs, rowNum) -> {
            Store store = new Store();
            store.setStoreUuid(rs.getString("storeuuid"));
            store.setStoreName(rs.getString("storename"));
            store.setStoreAddress(rs.getString("storeaddress"));
            store.setWin1stCount(rs.getInt("win1stcount"));
            store.setWin2stCount(rs.getInt("win2stcount"));
            store.setStoreLatitude(rs.getDouble("storelatitude"));
            store.setStoreLongitude(rs.getDouble("storelongitude"));
            store.setLottoName(rs.getString("lottonames"));
            store.setSavedFileNames(rs.getString("saved_file_names"));
            store.setDistance(rs.getDouble("distance"));
            return store;
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
            return store;
        };
    }

    private RowMapper<Store> storesRowMapper(){
        return (rs, rowNum) -> {
            /**
             * String storeUuid;
             * String storeName;
             * String storeAddress;
             * String storeLatitude;
             * String storeLongitude;
             * String storeBizNo;
             * String storeTelNum;
             * String storeMobileNum;
             * String storeOpenTime;
             * String storeCloseTime;
             * Boolean storeIsActivity;
             * String storeSido;
             *  String storeSigugun;
             */
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



}

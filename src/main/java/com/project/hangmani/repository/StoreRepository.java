package com.project.hangmani.repository;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO.RequestStoreDeleteDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreInsertDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreUpdateDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoresDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@Slf4j
public class StoreRepository {
    private final String findByLatitudeLongitudeSql = "SELECT * FROM store " +
            " where (? < storelatitude and storelatitude < ?) or (? < storelongitude and storelongitude < ?) limit ?;";
    private final String findByUuid = "SELECT * FROM store where storeuuid=?;";
    private final String updateByUuid = "UPDATE store SET storename=?,storeaddress=?,"+
            "storelatitude=?,storelongitude=?,storebizno=?,storetelnum=?,storemobilenum=?,storeopentime=?,"+
            "storeclosetime=?,storeisactivity=?,storesido=?,storesigugun=? where storeuuid=?;";
    private final String findByName = "SELECT * FROM store WHERE storename=? and "+
            "storelatitude like CONCAT(?, '%') and storelongitude like CONCAT(?, '%');";
    private final String insert = "insert into store(storeuuid, storename, storeaddress,storelatitude, storelongitude," +
            "storebizno,storetelnum, STOREMOBILENUM, STOREOPENTIME,STORECLOSETIME,STORESIDO,STORESIGUGUN) " +
            "values(?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String deleteByUuid = "UPDATE  store SET STOREISACTIVITY=1 where storeuuid=?";
    private final String deleteByLatiLongi = "UPDATE  store SET STOREISACTIVITY=1 where storelatitude=? and storelongitude=?";
    private JdbcTemplate template;
    private RequestConvert requestConvert;

    private final Map<String, String> sidoMap;

    public StoreRepository(DataSource dataSource) {
        this.requestConvert = new RequestConvert();
        this.template = new JdbcTemplate(dataSource);
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

    public List<Store> findStoreInfoByLatitudeLongitude(RequestStoresDTO requestStoresDTO) {
        Double startLatitude = requestStoresDTO.getStartLatitude();
        Double endLatitude = requestStoresDTO.getEndLatitude();
        Double startLongitude = requestStoresDTO.getStartLongitude();
        Double endLongitude = requestStoresDTO.getEndLongitude();
        int limit = requestStoresDTO.getLimit();

        return template.query(findByLatitudeLongitudeSql, new Object[]{startLatitude, endLatitude
                , startLongitude, endLongitude, limit}, storesRowMapper());
    }

    public Store findStoreInfoByUuid(String storeUuid) {
        return template.queryForObject(findByUuid, new Object[]{storeUuid}, storeRowMapper());
    }

    public int updateStoreInfo(String StoreUuid, RequestStoreUpdateDTO requestStoreUpdateDTO) {
        Store store = requestConvert.convertEntity(requestStoreUpdateDTO);
        Object[] objects = {store.getStoreName(),store.getStoreAddress(), store.getStoreLatitude(),
        store.getStoreLongitude(), store.getStoreBizNo(), store.getStoreTelNum(), store.getStoreMobileNum(),
        store.getStoreOpenTime(), store.getStoreCloseTime(), store.getStoreIsActivity(), store.getStoreSido(),
        store.getStoreSigugun(), StoreUuid};
        return template.update(updateByUuid, objects);
    }
    public Store findStoreByNameLatiLongi(RequestStoreInsertDTO requestStoreDTO) {
        String storeName = requestStoreDTO.getStoreName();
        Double storeLatitude = convertPoint(requestStoreDTO.getStoreLatitude());
        Double storeLongitude = convertPoint(requestStoreDTO.getStoreLongitude());
        log.info("latitude={}", storeLatitude);
        log.info("longitude={}", storeLongitude);
        log.info("storename={}", storeName);
        log.info("sql={}", findByName);
        List<Store> query = template.query(findByName,
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
        return template.update(deleteByUuid, new Object[]{uuid});
    }

    public int deleteByLatitudeLongitude(RequestStoreDeleteDTO requestStoreDeleteDTO) {
        return template.update(deleteByUuid, new Object[]{requestStoreDeleteDTO.getLatitude(),
                requestStoreDeleteDTO.getLongitude()});
    }
    /**
     * @param requestStoreDTO
     * @return
     */
    public int insertStoreInfo(RequestStoreInsertDTO requestStoreDTO) {
        //make store uuid
        UUID uuid = UUID.randomUUID();
        String[] sidoSigugun = extractSidoSigugunByAddress(requestStoreDTO.getStoreAddress());
        return template.update(insert, new Object[]{uuid.toString(), requestStoreDTO.getStoreName(),
                requestStoreDTO.getStoreAddress(), requestStoreDTO.getStoreLatitude(), requestStoreDTO.getStoreLongitude(),
                requestStoreDTO.getStoreBizNo(), requestStoreDTO.getStoreTelNum(), requestStoreDTO.getStoreMobileNum(),
                requestStoreDTO.getStoreOpenTime(), requestStoreDTO.getStoreCloseTime(), sidoSigugun[0], sidoSigugun[1]});
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
        log.info("sido={}", ret[0]);
        int firstLength = splitAddress[1].length();
        int secondLength = splitAddress[2].length();
        if (splitAddress[1].substring(firstLength - 1, firstLength).equals("시") &&
                splitAddress[2].substring(secondLength - 1, secondLength).equals("구"))
            ret[1] = splitAddress[1] + " " + splitAddress[2];
        else
            ret[1] = splitAddress[1];
        log.info("sigugun={}", ret[1]);
        return ret;

    }
    private Double convertPoint(String value) {
        String[] splitPoint = value.split("\\.");
        String cutPoint = splitPoint[1].substring(0,3);
        return Double.parseDouble(splitPoint[0]+ "." + cutPoint);
    }


    private RowMapper<Store> storeRowMapper() {
        return (rs, rowNum) -> {
            Store store = new Store();
            store.setStoreUuid(rs.getString("storeuuid"));
            store.setStoreName(rs.getString("storename"));
            store.setStoreAddress(rs.getString("storeaddress"));
            store.setStoreLatitude(rs.getString("storelatitude"));
            store.setStoreLongitude(rs.getString("storelongitude"));
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
            store.setStoreLatitude(rs.getString("storelatitude"));
            store.setStoreLongitude(rs.getString("storelongitude"));
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

package com.project.hangmani.repository;

import com.project.hangmani.convert.RequestConvert;
import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO.RequestStoreUpdateDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoresDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Slf4j
public class StoreRepository {
    private final String findByLatitudeLongitudeSql = "SELECT * FROM store " +
            " where (? < storelatitude and storelatitude < ?) or (? < storelongitude and storelongitude < ?) limit ?;";
    private final String findByUuid = "SELECT * FROM store where storeuuid=?";
    private final String updateByUuid = "UPDATE store SET storename=?,storeaddress=?,"+
            "storelatitude=?,storelongitude=?,storebizno=?,storetelnum=?,storemobilenum=?,storeopentime=?,"+
            "storeclosetime=?,storeisactivity=?,storesido=?,storesigugun=? where storeuuid=?";
    private JdbcTemplate template;
    private RequestConvert requestConvert;
    public StoreRepository(DataSource dataSource) {
        this.requestConvert = new RequestConvert();
        this.template = new JdbcTemplate(dataSource);
    }

    public List<Store> findStoreInfoByLatitudeLongitude(RequestStoresDTO requestStoresDTO) {
        Double startLatitude = requestStoresDTO.getStartLatitude();
        Double endLatitude = requestStoresDTO.getEndLatitude();
        Double startLongitude = requestStoresDTO.getStartLongitude();
        Double endLongitude = requestStoresDTO.getEndLongitude();
        int limit = requestStoresDTO.getLimit();

        List<Store> list = template.query(findByLatitudeLongitudeSql, new Object[]{startLatitude, endLatitude
                , startLongitude, endLongitude, limit}, storesRowMapper());
        return list;
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
        int update = template.update(updateByUuid, objects);
        return update;
    }

    private String[] extractSidoSigugunByAddress(String address) {
        String[] sido = new String[2];
        String[] addressList = address.split(" ");

        return sido;

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

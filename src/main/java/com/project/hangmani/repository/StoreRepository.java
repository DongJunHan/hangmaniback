package com.project.hangmani.repository;

import com.project.hangmani.domain.Store;
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
    private JdbcTemplate template;
    public StoreRepository(DataSource dataSource) {
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

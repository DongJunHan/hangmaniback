package com.project.hangmani.repository;

import com.project.hangmani.domain.Store;
import com.project.hangmani.dto.StoreDTO;
import com.project.hangmani.dto.StoreDTO.RequestStoreDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Slf4j
public class StoreRepository {
    private final String findByAreaSql = "SELECT * FROM store " +
            " where (? < storelatitude and storelatitude < ?) or (? < storelongitude and storelongitude < ?) limit ?;";
    private JdbcTemplate template;
    public StoreRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public List<Store> findStoreInfoByArea(RequestStoreDTO requestStoreDTO) {
        Double startLatitude = requestStoreDTO.getStartLatitude();
        Double endLatitude = requestStoreDTO.getEndLatitude();
        Double startLongitude = requestStoreDTO.getStartLongitude();
        Double endLongitude = requestStoreDTO.getEndLongitude();
        int limit = requestStoreDTO.getLimit();

        List<Store> list = template.query(findByAreaSql, new Object[]{startLatitude, endLatitude
                , startLongitude, endLongitude, limit}, storeRowMapper());
        return list;
    }
    private RowMapper<Store> storeRowMapper(){
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

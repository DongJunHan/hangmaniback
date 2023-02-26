package com.project.hangmani.model.store;

import com.project.hangmani.model.winhistory.WinHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoreRepository {
    private final String findByAreaSql = "SELECT * FROM store" +
            " where store.storesido= ? and store.storesigugun= ?;";
    private JdbcTemplate template;
    public StoreRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public List<Store> findStoreInfoByArea(String sido, String sigugun) {
        List<Store> list = template.query(findByAreaSql,new Object[]{sido, sigugun}, storeRowMapper());
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

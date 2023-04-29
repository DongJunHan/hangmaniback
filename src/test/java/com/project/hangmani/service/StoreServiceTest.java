package com.project.hangmani.service;

import com.project.hangmani.config.WebClientConfig;
import com.project.hangmani.dto.StoreDTO.*;
import com.project.hangmani.exception.AlreadyExistStore;
import com.project.hangmani.repository.FileRepository;
import com.project.hangmani.repository.StoreRepository;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class StoreServiceTest {
    @Autowired
    private StoreService storeService;
    @Autowired
    private JdbcTemplate template;


    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;
        private final Util util;
        TestConfig(DataSource dataSource, Util util) {
            this.dataSource = dataSource;
            this.util = util;

        }
        FileRepository fileRepository() {
            return new FileRepository(dataSource,new Util());}
        StoreRepository storeRepository() {
            return new StoreRepository(dataSource, this.util);
        }
        WebClientConfig webClient() {
            return new WebClientConfig();
        }
        FileService fileService() {return new FileService(fileRepository(), storeRepository(), webClient(), this.util);}
        @Bean
        StoreService StoreServiceV1() {
            return new StoreService(storeRepository(),fileService());
        }

    }
//    @AfterEach
    void afterEach() {
        String delSql = "delete from store where storelatitude=? and storelongitude=?";
        template.update(delSql, new Object[] {"11212.488214","1234.054415"});
    }

    @Test
    @DisplayName("H2 데이터베이스 조건별 상점데이터 확인")
    void filterStoreInfoTest() {
        Double userLatitude = 37.3914817;
        Double userLongitude = 127.0777273;


        List<ResponseStoreFilterDTO> storeInfo =
                storeService.getStoreInfo(new RequestStoreFilterDTO().builder()
                .filter("2st")
                .userLatitude(userLatitude)
                .userLongitude(userLongitude)
                .sido("인천")
                .sigugun("부평구")
                .lottoID(1)
                .build());
        for(ResponseStoreFilterDTO f:storeInfo){
            log.info("TEST={}", f);
        }
        assertThat(storeInfo).isNotNull();

    }

    @Test
    @DisplayName("H2 데이터베이스 상점 데이터 확인")
    void StoresInfoTest() {

        Double startLatitude = 37.463;
        Double endLatitude = 37.528;
        Double startLongitude = 127.019;
        Double endLongitude = 127.107;
        RequestStoresDTO requestStoreDTO = new RequestStoresDTO(
                startLatitude,
                endLatitude,
                startLongitude,
                endLongitude,
                129);
        List<ResponseStoreDTO> storeInfoList = storeService.getStoreInfo(requestStoreDTO);
        log.info("result={}", storeInfoList);
        assertThat(storeInfoList.size()).isEqualTo(129);
    }

    @Test
    @DisplayName("H2 데이터베이스 하나의 상점 데이터 확인")
    void StoreInfoTest(){
        String storeUuid = "4d1054b8-ac2c-11ed-9b15-12ebd169e012";
        ResponseStoreDTO storeInfo = storeService.getStoreInfo(storeUuid);
        assertThat(storeInfo).isInstanceOf(ResponseStoreDTO.class);
    }

//    @Test
//    @DisplayName("H2 데이터베이스 상점정보 수정")
//    void StoreInfoUpdate() {
//        String storeUuid = "4d1054b8-ac2c-11ed-9b15-12ebd169e012";
//        RequestStoreUpdateDTO requestStoreUpdateDTO = new RequestStoreUpdateDTO();
//        requestStoreUpdateDTO.setStoreName();
//    }

    @Test
    @DisplayName("H2 데이터베이스 동일한 상점정보 삽입")
    void StoreInfoInsertFail() {
        RequestStoreInsertDTO requestStoreInsertDTO = new RequestStoreInsertDTO();
        requestStoreInsertDTO.setStoreAddress("서울 강남구 언주로30길 56");
        requestStoreInsertDTO.setStoreLatitude(37.488214);
        requestStoreInsertDTO.setStoreLongitude(127.054415);
        requestStoreInsertDTO.setStoreName("CU(타워팰리스점)");
        assertThatThrownBy(() -> storeService.insertStoreInfo(requestStoreInsertDTO))
                .isInstanceOf(AlreadyExistStore.class);
    }

    @Test
    @DisplayName("H2 데이터베이스 동일한 상점정보 삽입")
    void StoreInfoInsert() {
        RequestStoreInsertDTO requestStoreInsertDTO = new RequestStoreInsertDTO();
        requestStoreInsertDTO.setStoreAddress("서울 강남구 언주로30길 56");
        requestStoreInsertDTO.setStoreLatitude(11212.488214);
        requestStoreInsertDTO.setStoreLongitude(1234.054415);
        requestStoreInsertDTO.setStoreName("CU(테스트테스트)");
        requestStoreInsertDTO.setStoreBizNo("111-111");
        requestStoreInsertDTO.setStoreMobileNum(null);
        requestStoreInsertDTO.setStoreTelNum(null);
        requestStoreInsertDTO.setStoreCloseTime(null);
        requestStoreInsertDTO.setStoreOpenTime(null);
        ResponseStoreDTO responseStoreDTO = storeService.insertStoreInfo(requestStoreInsertDTO);
        assertThat(responseStoreDTO).isNotNull();
        if (responseStoreDTO.getStoreUuid() != null) {
            Assumptions.assumeTrue(true);
        }

    }

    @Test
    @DisplayName("주소에서 시/도, 시/구/군 파싱하는 테스트")
    void AddressParsing() {
        //ex. 서울 강남구(서울 강남구 테헤란로63길 12 LG선릉에클라트 B동 107호),
        // 경기 성남시(경기 성남시 분당구 양현로94번길 21 구두28호점), 경남 창원시 마산합포구(경남 창원시 마산합포구 해안대로 58 1층)

        //1. 시/도를 파싱 '특별시'는 뺀 예외 - 세종
        //2. 시/구/군에서 시로 시작하면 뒤에 구가 있는지 확인.
        //3. 구가 있으면 구까지 파싱 아니면 그 앞까지만 시/구/군으로 파싱
        Map<String, String> sidoMap = new HashMap<>();
        String[] ret = new String[2];
        sidoMap.put("경기도", "경기");
        sidoMap.put("서울특별시", "서울");
        sidoMap.put("세종특별자치시", "세종");
        sidoMap.put("제주특별자치도", "제주");
        sidoMap.put("제주도", "제주");
        String address = "제주특별자치도 서귀포시 일주동로 8665(서귀동)";
//        String address = "세종특별자치시 정안세종로 1567(산울동)";
//        String address = "경기도 성남시 분당구 양현로94번길 21 구두28호점";
        String[] splitAddress = address.split(" ");
        String convertSido = sidoMap.get(splitAddress[0]);
        if (convertSido != null) {
            splitAddress[0] = convertSido;
            if (splitAddress[0].equals("세종")){
                ret[0] = splitAddress[0];
                ret[1] = null;
                log.info("sido={}", ret[0]);
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
        }
        @Test
        @DisplayName("algorithm")
        void solution(){
//            String today = "2020.01.01";
//            String[] privacies = {"2019.01.01 D", "2019.11.15 Z", "2019.08.02 D", "2019.07.01 D", "2018.12.28 Z"};
//            String[] terms = {"Z 3", "D 5"};

            String today = "2022.05.19";
            String[] terms = {"A 6", "B 12", "C 3"};
            String[] privacies = {"2021.05.02 A", "2021.07.01 B", "2022.02.19 C", "2022.02.20 C", "2022.02.19 C","2021.05.02 A", "2021.07.01 B", "2022.02.19 C", "2022.02.20 C","2021.05.02 A", "2021.07.01 B", "2022.02.19 C", "2022.02.20 C","2021.05.02 A", "2021.07.01 B", "2022.02.19 C", "2022.02.20 C"};
            List<Integer> answer = new ArrayList<>();
            int[] t_today = Arrays.stream(today.split("\\.")).mapToInt(Integer::parseInt).toArray();
            int s_today = (t_today[0]*12*28)+(t_today[1]*28)+t_today[2];
            Map<String, Integer> a = new HashMap<>();
            for (String t: terms) {
                Object[] z = Arrays.stream(t.split(" ")).toArray();
                a.put((String)z[0], s_today - (Integer.parseInt((String)z[1])) * 28);
            }
            for (int i=0; i < privacies.length; i++) {
                Object[] privace = Arrays.stream(privacies[i].split(" ")).toArray();
                int[] t_date = Arrays.stream(((String)privace[0]).split("\\.")).mapToInt(Integer::parseInt).toArray();
                int date = (t_date[0]*12*28)+(t_date[1]*28)+t_date[2];
                if (date <= a.get((String)privace[1])) answer.add(i+1);
            }
        }


}
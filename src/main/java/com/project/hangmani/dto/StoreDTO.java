package com.project.hangmani.dto;

import com.project.hangmani.domain.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class StoreDTO {
    @Getter
    public static class RequestStoreDTO {
        @NotBlank
        private String sido;
        @NotNull
        private String sigugun;

        public RequestStoreDTO() {
        }

        public RequestStoreDTO(String sido, String sigugun) {
            this.sido = sido;
            this.sigugun = sigugun;
        }
    }
    @Getter
    public static class ResponseStoreDTO {
        private List<Store> storeList;
        private Integer status;
        private String message;

        public ResponseStoreDTO() {
        }

        public ResponseStoreDTO(List<Store> storeList, Integer status, String message) {
            this.storeList = storeList;
            this.status = status;
            this.message = message;
        }
    }
}

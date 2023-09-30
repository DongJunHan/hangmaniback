package com.project.hangmani.store.model.dto;

import com.project.hangmani.file.model.dto.RequestSaveDTO;
import com.project.hangmani.store.model.entity.Store;
import com.project.hangmani.store.model.entity.StoreAttachment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestInsertDTO {
    @NotBlank
    private String storeName;
    @NotBlank
    private String storeAddress;
    @NotNull
    private Double storeLatitude;
    @NotNull
    private Double storeLongitude;
    private String storeBizNo;
    private String storeTelNum;
    private String storeMobileNum;
    private String storeOpenTime;
    private String storeCloseTime;
    private List<MultipartFile> attachFiles;

    public Store convertToEntity() {
        return Store.builder()
                .storeName(this.getStoreName())
                .storeAddress(this.getStoreAddress())
                .storeLatitude(this.getStoreLatitude())
                .storeLongitude(this.getStoreLongitude())
                .storeBizNo(this.getStoreBizNo())
                .storeTelNum(this.getStoreTelNum())
                .storeCloseTime(this.getStoreCloseTime())
                .storeOpenTime(this.getStoreOpenTime())
                .build();
    }

    public RequestSaveDTO convertSaveDTO() {
        return RequestSaveDTO.builder().attachFiles(attachFiles).build();
    }

    public RequestAttachmentDTO convertToAttachment(String storeUuid) {
        return RequestAttachmentDTO.builder()
                .attachFiles(this.getAttachFiles())
                .storeUuid(storeUuid)
                .build();
    }
}

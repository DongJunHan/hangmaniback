package com.project.hangmani.store.model.dto;

import com.project.hangmani.file.model.dto.RequestSaveDTO;
import com.project.hangmani.store.model.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private LocalDateTime storeOpenTime;
    private LocalDateTime storeCloseTime;
    private List<MultipartFile> attachFiles;
    @Builder
    private RequestInsertDTO(String storeName, String storeAddress, Double storeLatitude,
                             Double storeLongitude, String storeBizNo, String storeTelNum,
                             String storeMobileNum, LocalDateTime storeOpenTime,
                             LocalDateTime storeCloseTime, List<MultipartFile> attachFiles) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.storeBizNo = storeBizNo;
        this.storeTelNum = storeTelNum;
        this.storeMobileNum = storeMobileNum;
        this.storeOpenTime = storeOpenTime;
        this.storeCloseTime = storeCloseTime;
        this.attachFiles = attachFiles;
    }

    public Store of() {
        return Store.builder()
                .storeName(storeName)
                .storeAddress(storeAddress)
                .storeLatitude(storeLatitude)
                .storeLongitude(storeLongitude)
                .storeBizNo(storeBizNo)
                .storeTelNum(storeTelNum)
                .storeCloseTime(storeCloseTime)
                .storeOpenTime(storeOpenTime)
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

package com.project.hangmani.store.repository;

//import com.project.hangmani.file.model.dto.AttachmentDTO;
import com.project.hangmani.lottoType.model.dto.LottoTypeDTO;
import com.project.hangmani.store.model.dto.AttachmentDTO;
import com.project.hangmani.store.model.dto.RequestFilterDTO;
import com.project.hangmani.store.model.dto.StoreDTO;
import com.project.hangmani.store.model.entity.Store;
import com.project.hangmani.store.model.entity.StoreAttachment;

import java.util.List;

public interface StoreRepository {
    List<List<String>> getTableList();
    List<StoreDTO> getByArea(RequestFilterDTO dto);
    List<StoreDTO> getByCoordinates(RequestFilterDTO dto);

    List<StoreDTO> getByStoreNameAndCoordinates(Store store);
    List<StoreDTO> getWithWinHistoryByArea(RequestFilterDTO dto);
    List<StoreDTO> getWithWinHistoryByCoordinates(RequestFilterDTO dto);
    List<LottoTypeDTO> getLottoNameByUuid(String storeUuid);
    List<LottoTypeDTO> getLottoNameByArea(RequestFilterDTO dto);
    List<LottoTypeDTO> getLottoNameByCoordinates(RequestFilterDTO dto);
    List<AttachmentDTO> getAttachmentByArea(RequestFilterDTO dto);

    List<AttachmentDTO> getAttachmentByCoordinates(RequestFilterDTO dto);
    List<AttachmentDTO> getAttachmentByUuid(String storeUuid);
    List<StoreDTO> getByUuid(String storeUuid);
    String insert(Store store);

    int insertAttachFiles(List<StoreAttachment> attachFiles);
    int update(String StoreUuid, Store store);
    int delete(String StoreUuid);
//    int deleteByCoordinates(RequestDeleteDTO dto);
}

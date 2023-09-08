package com.project.hangmani.store.service;

import com.project.hangmani.lottoType.model.dto.LottoTypeDTO;
import com.project.hangmani.store.model.dto.AttachmentDTO;
import com.project.hangmani.store.model.dto.RequestFilterDTO;
import com.project.hangmani.store.model.dto.StoreDTO;

import java.util.List;

public interface StoreFilter {
    List<StoreDTO> getWithWinHistory(RequestFilterDTO dto);
    List<LottoTypeDTO> getLottoName(RequestFilterDTO dto);

    List<AttachmentDTO> getAttachment(RequestFilterDTO dto);
    String getType();
}

package com.project.hangmani.store.service;

import com.project.hangmani.lottoType.model.dto.LottoTypeDTO;
import com.project.hangmani.store.model.dto.AttachmentDTO;
import com.project.hangmani.store.model.dto.RequestFilterDTO;
import com.project.hangmani.store.model.dto.StoreDTO;
import com.project.hangmani.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class StoreFilterArea implements StoreFilter{
    private final StoreRepository storeRepository;
    @Override
    public List<StoreDTO> getWithWinHistory(RequestFilterDTO dto) {
        return storeRepository.getWithWinHistoryByArea(dto);
    }

    @Override
    public List<LottoTypeDTO> getLottoName(RequestFilterDTO dto) {
        return storeRepository.getLottoNameByArea(dto);
    }

    @Override
    public List<AttachmentDTO> getAttachment(RequestFilterDTO dto) {
        return storeRepository.getAttachmentByArea(dto);
    }

    @Override
    public String getType() {
        return "area";
    }

}

package com.project.hangmani.file.repository;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.store.model.dto.AttachmentDTO;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static com.project.hangmani.config.query.StoreQueryConst.insertStoreAttachment;

@Repository
@Slf4j
public class FileRepository {
    private JdbcTemplate template;
    private final String sql = "select * from store_attachment where storeuuid=?";

    public FileRepository(DataSource dataSource) {
        log.info("FileRepository={}", dataSource);
        this.template = new JdbcTemplate(dataSource);
    }

    public int insertAttachment(AttachmentDTO requestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int ret = template.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(insertStoreAttachment, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, requestDTO.getOriginalFileName());
            pstmt.setString(2, requestDTO.getSavedFileName());
            pstmt.setString(4, requestDTO.getStoreUuid());
            return pstmt;
        }, keyHolder);
        if (ret == 0)
            throw new FailInsertData();
        Number attachmentNo = keyHolder.getKey();
        if (attachmentNo == null) {
            throw new FailInsertData();
        }

        return attachmentNo.intValue();

    }

    public List<AttachmentDTO> getAttachmentsByUuid(String uuid) {
        return template.query(sql, new Object[]{uuid}, attachmentMapper());
    }
    private RowMapper<AttachmentDTO> attachmentMapper() {
        return (rs, rowNum) -> {
            return AttachmentDTO.builder()
                    .storeUuid(rs.getString("storeuuid"))
                    .originalFileName(rs.getString("original_file_name"))
                    .savedFileName(rs.getString("saved_file_name"))
                    .attachmentNo(rs.getInt("attachmentno"))
                    .build();
        };
    }
}

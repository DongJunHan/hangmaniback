package com.project.hangmani.repository;

import com.project.hangmani.config.PropertiesValues;
import com.project.hangmani.domain.StoreAttachment;
import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.exception.FailInsertData;
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
import java.util.Map;

import static com.project.hangmani.config.query.StoreQueryConst.insertStoreAttachment;

@Repository
@Slf4j
public class FileRepository {
    private JdbcTemplate template;
    private ConvertData convertData;
    private Util util;
    private final String sql = "select * from store_attachment where storeuuid=?";

    public FileRepository(DataSource dataSource, PropertiesValues propertiesValues) {
        log.info("FileRepository={}", dataSource);
        this.template = new JdbcTemplate(dataSource);
        this.convertData = new ConvertData(propertiesValues);
        this.util = new Util(propertiesValues);
    }

    public int insertAttachment(StoreAttachment requestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int ret = template.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(insertStoreAttachment, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, requestDTO.getOriginalFileName());
            pstmt.setString(2, requestDTO.getSavedFileName());
            pstmt.setLong(3, requestDTO.getFileSize());
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

    public List<StoreAttachment> getAttachmentsByUuid(String uuid) {
        return template.query(sql, new Object[]{uuid}, attachmentMapper());
    }
    private RowMapper<StoreAttachment> attachmentMapper() {
        return (rs, rowNum) -> {
            StoreAttachment storeAttachment = new StoreAttachment();
            storeAttachment.setStoreUuid(rs.getString("storeuuid"));
            storeAttachment.setFileSize(rs.getInt("file_size"));
            storeAttachment.setOriginalFileName(rs.getString("original_file_name"));
            storeAttachment.setSavedFileName(rs.getString("saved_file_name"));
            storeAttachment.setUploadDate(rs.getDate("upload_time"));
            return storeAttachment;
        };
    }
}

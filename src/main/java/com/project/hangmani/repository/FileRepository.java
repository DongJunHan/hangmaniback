package com.project.hangmani.repository;

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
    private final JdbcTemplate template;
    private final ConvertData convertData;
    private final Util util;
    private final String sql = "select * from store_attachment where storeuuid=?";

    public FileRepository(DataSource dataSource, Util util) {
        this.template = new JdbcTemplate(dataSource);
        this.convertData = new ConvertData();
        this.util = util;
    }

    public int insertAttachment(RequestStoreFileDTO requestDTO) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int ret = template.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(insertStoreAttachment, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, requestDTO.getOriginalFileName());
            pstmt.setString(2, requestDTO.getSavedFileName());
            pstmt.setLong(3, requestDTO.getFileSize());
            pstmt.setDate(4, requestDTO.getUploadTime());
            pstmt.setString(5, requestDTO.getStoreUUID());
            return pstmt;
        }, keyHolder);
        if (ret == 0)
            throw new FailInsertData();
        List<Map<String, Object>> keyList = keyHolder.getKeyList();
        if (keyList != null && keyList.size() > 0) {
            Number attachmentNo = (Number) keyList.get(0).get("attachment_no");
            return attachmentNo.intValue();
        }else{
            throw  new FailInsertData();
        }

    }

    public int insertAttachment() {
        return 0;
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

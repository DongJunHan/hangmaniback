package com.project.hangmani.repository;

import com.project.hangmani.dto.FileDTO.RequestStoreFileDTO;
import com.project.hangmani.exception.FailInsertData;
import com.project.hangmani.util.ConvertData;
import com.project.hangmani.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;

import static com.project.hangmani.config.query.StoreQueryConst.insertStoreAttachment;

@Repository
@Slf4j
public class FileRepository {
    private final JdbcTemplate template;
    private final ConvertData convertData;
    private final Util util;

    public FileRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.convertData = new ConvertData();
        this.util = new Util();
    }

    public int insertAttachment(RequestStoreFileDTO requestDTO) {
        //make rename file name
        Date uploadTime = this.convertData.getSqlDate();
        String renamedFile = this.util.getRenameWithDate(uploadTime, this.util.getRandomValue(),
                requestDTO.getOriginalFileName());
        //TODO file url 구하기
        String fileUrl = null;
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int ret = template.update(insertStoreAttachment, new Object[]{requestDTO.getOriginalFileName(), renamedFile,
                requestDTO.getFileSize(), uploadTime, fileUrl ,requestDTO.getStoreUUID()}, keyHolder);
        if (ret == 0)
            throw new FailInsertData();
        return keyHolder.getKey().intValue();
    }

    public int insertAttachment() {
        return 0;
    }
}

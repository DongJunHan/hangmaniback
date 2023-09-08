package com.project.hangmani.report.repository;

import com.project.hangmani.report.model.dto.ReportDTO;
import com.project.hangmani.report.model.entity.Report;
import com.project.hangmani.exception.FailInsertData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ReportRepositoryImpl implements ReportRepository{
    private JdbcTemplate template;
    private static final String INSERT_QUERY = "insert into REPORT(report_content, id, report_id) values(?,?,?)";
    private static final String SELECT_QUERY_NO =
            "select * from REPORT JOIN REPORT_TYPE ON REPORT.report_id=REPORT_TYPE.report_id where REPORT.report_no=?";
    private static final String SELECT_QUERY_ID =
            "SELECT * FROM REPORT JOIN REPORT_TYPE ON REPORT.report_id=REPORT_TYPE.report_id where REPORT.id=?";
    public ReportRepositoryImpl(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    public int add(Report report) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int ret = template.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, report.getReportContent());
            ps.setString(2, report.getId());
            ps.setInt(3, report.getReportID());
            return ps;
        }, keyHolder);
        if (ret == 0)
            throw new FailInsertData();
        Number attachmentNo = keyHolder.getKey();
        if (attachmentNo == null) {
            throw new FailInsertData();
        }

        return attachmentNo.intValue();
    }

    public List<ReportDTO> getByNo(int reportNo) {
        return template.query(SELECT_QUERY_NO, new Object[]{reportNo}, reportRowMapper());
    }
    public List<ReportDTO> getByUserId(String userId) {
        return template.query(SELECT_QUERY_ID, new Object[]{userId}, reportRowMapper());
    }

    private RowMapper<ReportDTO> reportRowMapper() {
        return (rs, rsNum) -> {
            ReportDTO report = new ReportDTO();
            report.setReportContent(rs.getString("report_content"));
            report.setReportID(rs.getInt("report_id"));
            report.setReportNo(rs.getInt("report_no"));
            report.setReportType(rs.getString("report_type"));
            report.setId(rs.getString("id"));
            report.setReportDate(rs.getDate("report_date"));
            return report;
        };
    }

}

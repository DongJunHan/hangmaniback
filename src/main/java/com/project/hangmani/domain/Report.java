package com.project.hangmani.domain;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Report {
    private int reportNo;
    private Date reportDate;
    private String reportContent;
    private String id;
    private int reportID;
    private String reportType;
}

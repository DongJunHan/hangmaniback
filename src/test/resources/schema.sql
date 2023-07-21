-- DROP TABLE IF EXISTS STORE;
create table IF NOT EXISTS STORE(
storeUuid       varchar(50),
storeName     varchar(100),
storeAddress varchar(200),
storeLatitude  varchar(20),
storeLongitude varchar(20),
storeBizNo      varchar(30),
storeTelNum   varchar(20),
storeMobileNum varchar(20),
storeOpenTime    varchar(30),
storeCloseTime   varchar(30),
storeisActivity    INT default 0,
storesido   varchar(50),
storesigugun  varchar(50),
PRIMARY KEY(storeuuid)
);
-- DROP TABLE IF EXISTS LOTTO_TYPE;
create table IF NOT EXISTS LOTTO_TYPE(
lottoid    int primary key,
lottoname varchar(50)
);
-- DROP TABLE IF EXISTS WIN_HISTORY;
create table IF NOT EXISTS WIN_HISTORY(
    storeuuid   varchar(50),
    lottoid        int,
    winround   int,
    winrank     int,
    INDEX win_history_index (storeuuid, lottoid, winround, winrank),
    foreign key (storeuuid) REFERENCES STORE(storeuuid),
    foreign key (lottoid) REFERENCES LOTTO_TYPE(lottoid)
);
-- DROP TABLE IF EXISTS BOARD;
create table IF NOT EXISTS BOARD(
 boardno int AUTO_INCREMENT primary key,
 title varchar(200) not null,
 content varchar(2000),
 boardwriter varchar(50),
 createat date default CURRENT_DATE,
 updateat date default CURRENT_DATE,
 isdelete boolean default 0
);
-- DROP TABLE IF EXISTS USER;
create table IF NOT EXISTS USER(
  email varchar(200),
  age  varchar(100),
  gender varchar(10),
  ID varchar(200) primary key
);
-- DROP TABLE IF EXISTS OAUTH;
create table IF NOT EXISTS OAUTH (
  ID varchar(200),
  oauthtype varchar(30),
  oauthid varchar(200) unique ,
  foreign key (ID) references USER(ID)
);
-- DROP TABLE IF EXISTS STORE_ATTACHMENT;
create table IF NOT EXISTS STORE_ATTACHMENT (
    attachment_no INT AUTO_INCREMENT PRIMARY KEY,
    original_file_name VARCHAR(100),
    saved_file_name VARCHAR(100),
    file_size BIGINT,
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    storeuuid varchar(50),
    foreign key (storeuuid) references STORE(storeuuid)
);
-- DROP TABLE IF EXISTS BOARD_ATTACHMENT;
CREATE TABLE IF NOT EXISTS BOARD_ATTACHMENT (
    attachment_no INT AUTO_INCREMENT PRIMARY KEY,
    original_file_name VARCHAR(100),
    saved_file_name VARCHAR(100),
    file_size BIGINT,
    upload_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    boardno int,
    foreign key (boardno) references BOARD(boardno)
);

-- DROP TABLE IF EXISTS LOTTO_TYPE_HANDLE;
create table IF NOT EXISTS LOTTO_TYPE_HANDLE (
lottoid int,
storeuuid varchar(50),
foreign key (storeuuid) references STORE(storeuuid),
foreign key (lottoid) references LOTTO_TYPE(lottoid)
);
-- DROP TABLE IF EXISTS REPORT_TYPE;
CREATE TABLE IF NOT EXISTS REPORT_TYPE (
  report_id int primary key,
  report_type varchar(50)
);
-- DROP TABLE IF EXISTS REPORT;
CREATE TABLE IF NOT EXISTS REPORT (
  report_no int primary key auto_increment,
  report_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  report_content varchar(150),
  report_id int,
  id varchar(200),
  foreign key (report_id) references REPORT_TYPE(report_id),
  foreign key (id) references USER(id)
);

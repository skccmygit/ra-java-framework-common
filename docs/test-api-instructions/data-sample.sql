use OCO;

DELETE FROM OCO50200;
DELETE FROM OCO20101;
DELETE FROM OCO50100;
DELETE FROM OCO10100;
DELETE FROM OCO40100;
DELETE FROM OCO40110;
DELETE FROM OCO40111;

ALTER TABLE OCO40100 AUTO_INCREMENT = 1;
ALTER TABLE OCO40110 AUTO_INCREMENT = 1;
ALTER TABLE OCO40111 AUTO_INCREMENT = 1;


-- Dept (OCO50200)
INSERT INTO OCO50200 (DEPTCD, LAST_CHNGR_ID, LAST_CHNG_DTMD, MGMT_DEPTCD, DEPT_NM, ENGSH_DEPT_NM, ENGSH_DEPT_ABBR_NM, BSSMACD, SUPER_DEPTCD, DEPT_GRADE_CD, DEPT_CRAT_DT, DEPT_ABOL_DT, FEMP_DEPTCD, WHTAX_BZPL_CD, SALS_DEPTCD, INV_DEPTCD, INVNT_WRHUS_CD, VAT_BZPL_CD, ZIPCD, BASIC_ADDR, DETIL_ADDR, EXTSN_NO, PHNO, FAX_NO, BZNO, BZMAN_ACQ_DT, TXOFC_CD, TXOFC_NM, COM_BSCND_NM, COM_ITM_NM, COM_CORP_NM, USE_YN, CCNTR_CD, RPRTT_NM) VALUES
('D001', 'system', NOW(), 'D000', 'Tech Support', 'Tech Support', 'TS', '01', null, '1', '20230414', '20230414', 'F001', '001', 'S001', 'INV001', 'WH001', 'VAT001', '100000', '123 Tech St', '456 Details Ave', '0010', '123-456-7890', '123-456-7891', '1234567890', '20230414', 'TX1', 'Tech Office', 'Tech Company', 'Item A', 'Corp A', 'Y', 'CN001', 'Report A'),
('D002', 'system', NOW(), 'D000', 'Sales Team', 'Sales Team', 'ST', '02', 'D001', '2', '20230414', '20230414', 'F002', '002', 'S002', 'INV002', 'WH002', 'VAT002', '200000', '789 Sales Rd', '101 Details St', '0011', '234-567-8901', '234-567-8902', '2345678901', '20230414', 'TX2', 'Sales Office', 'Sales Company', 'Item B', 'Corp B', 'Y', 'CN002', 'Report B'),
('D003', 'system', NOW(), 'D000', 'R&D', 'Research and Development', 'RD', '03', 'D002', '3', '20230414', '20230414', 'F003', '003', 'S003', 'INV003', 'WH003', 'VAT003', '300000', '456 Research Ave', '789 Details Blvd', '0012', '345-678-9012', '345-678-9013', '3456789012', '20230414', 'TX3', 'R&D Office', 'R&D Company', 'Item C', 'Corp C', 'Y', 'CN003', 'Report C');
('ROOT01', 'SYSTEM', NOW(), 'ROOT01', 'SK주식회사', 'SK Corporation', 'SK-CORP', '00', 'ROOT01', '0', '20250101', NULL, 'ROOT01', 'WHT001', 'SLS001', 'INV001', 'WH001', 'VAT001', '06164', '서울특별시 강남구 테헤란로 231', '센터필드 웨스트동 11층', '1230', '02-1234-5678', '02-1234-5679', '1234567890', '20250101', '101', '강남세무서', '서비스업', 'IT서비스', 'SK주식회사', 'Y', 'CC000', '대표이사'),
('IT0001', 'SYSTEM', NOW(), 'IT0001', 'IT본부', 'IT Division', 'IT-DIV', '01', 'ROOT01', '1', '20250101', NULL, 'IT0001', 'WHT001', 'SLS001', 'INV001', 'WH001', 'VAT001', '06164', '서울특별시 강남구 테헤란로 231', '센터필드 웨스트동 11층', '1234', '02-1234-5678', '02-1234-5679', '1234567890', '20250101', '101', '강남세무서', '서비스업', 'IT서비스', 'SK주식회사', 'Y', 'CC001', '김대표'),
('IT0002', 'SYSTEM', NOW(), 'IT0001', 'IT개발팀', 'IT Development Team', 'IT-DEV', '01', 'IT0001', '2', '20250101', NULL, 'IT0002', 'WHT001', 'SLS001', 'INV001', 'WH001', 'VAT001', '06164', '서울특별시 강남구 테헤란로 231', '센터필드 웨스트동 11층', '1235', '02-1234-5680', '02-1234-5681', '1234567890', '20250101', '101', '강남세무서', '서비스업', 'IT서비스', 'SK주식회사', 'Y', 'CC002', '이부장'),
('IT0003', 'SYSTEM', NOW(), 'IT0001', 'IT운영팀', 'IT Operation Team', 'IT-OPS', '01', 'IT0001', '2', '20250101', NULL, 'IT0003', 'WHT001', 'SLS001', 'INV001', 'WH001', 'VAT001', '06164', '서울특별시 강남구 테헤란로 231', '센터필드 웨스트동 11층', '1236', '02-1234-5682', '02-1234-5683', '1234567890', '20250101', '101', '강남세무서', '서비스업', 'IT서비스', 'SK주식회사', 'Y', 'CC003', '박부장'),
('HR0001', 'SYSTEM', NOW(), 'HR0001', '인사팀', 'HR Team', 'HR-TEAM', '02', 'ROOT01', '1', '20250101', NULL, 'HR0001', 'WHT001', 'SLS001', 'INV001', 'WH001', 'VAT001', '06164', '서울특별시 강남구 테헤란로 231', '센터필드 웨스트동 11층', '1237', '02-1234-5684', '02-1234-5685', '1234567890', '20250101', '101', '강남세무서', '서비스업', 'IT서비스', 'SK주식회사', 'Y', 'CC004', '최부장');

-- Common Code (OCO20100)
INSERT INTO OCO.OCO20100 (CMMN_CD, LAST_CHNGR_ID, LAST_CHNG_DTMD, CMMN_CD_NM, CMMN_CD_DESC) VALUES
('CHRG_TASK_GROUP_CD', 'SYSTEM',  NOW(), 'CHARGE TASK GROUP CODE', '담당업무그룹코드 (CHARGE TASK GROUP CODE)'),
('CRUD_CL_CD', 'SYSTEM',  NOW(), 'CRUD CLASSIFICATION CODE', '생성/조회/수정/삭제 분류 코드'),
('SCREN_CL_CD', 'SYSTEM',  NOW(), 'SCREEN CLASSIFICATION CODE', '화면 분류 코드'),
('SYSTM_CTGRY_CD','SYSTEM',  NOW(), 'SYSTEM CATEGORY CODE', 'SYSTEM CATEGORY CODE'),
('USER_ACTVY_TYPE_CD','SYSTEM',  NOW(), 'USER ACTIVITY TYPE CODE', '사용자 활동 유형 코드'),
('REOFO_CD', 'SYSTEM',  NOW(), 'ROLE OFFICE', 'ROLE OFFICE (역할/직위)');

-- Common Code Details (OCO20101)
INSERT INTO OCO20101 (CMMN_CD, CMMN_CD_VAL, CMMN_CD_VAL_NM, SORT_SEQN, LAST_CHNGR_ID, LAST_CHNG_DTMD) VALUES
('USERID_STS_CD', 'O0', '활성', 1, 'SYSTEM', NOW()),
('USERID_STS_CD', 'L0', '잠금', 2, 'SYSTEM', NOW()),
('REOFO_CD', 'M1', '관리자', 1, 'SYSTEM', NOW()),
('REOFO_CD', 'E1', '일반사용자', 2, 'SYSTEM', NOW());

-- Employee Basic Information (OCO50100)
INSERT INTO OCO50100 (EMPNO, LAST_CHNGR_ID, EMP_KRN_NM, EMP_ENGLNM, EMP_ENGFNM, DEPTCD, BTHDY, OWHM_PHNO, MPHNO, GROUP_ENTCP_DT, TCOM_ENTCP_DT, CURTP_OFORD_DT, CURTP_OFORD_CD, RESG_DT, COM_CL_CD, CLOFP_CD, REOFO_CD, JGP_CD, VCTN_CD, TEAMBR_CL_CD, CLOFP_NM, JGP_NM, VCTN_NM, EMAILADDR, BEFO_EMPNO) VALUES
('E100001', 'SYSTEM', '김민수', 'Kim', 'Minsoo', 'IT0001', '19900101', '02-1234-5678', '010-1111-2222', '20150101', '20150101', '20220101', 'A01', NULL, 'A', '001', '200', 'B01', '003', 'T01', '팀장', '개발', 'IT개발', 'minsoo.kim@company.com', NULL),
('E100002', 'SYSTEM', '이지연', 'Lee', 'Jiyeon', 'IT0002', '19920315', '02-1234-5679', '010-2222-3333', '20160301', '20160301', '20220301', 'A02', NULL, 'A', '002', '201', 'B02', '003', 'T02', '과장', '개발', 'IT개발', 'jiyeon.lee@company.com', NULL),
('E100003', 'SYSTEM', '박성호', 'Park', 'Sungho', 'IT0003', '19880520', '02-1234-5680', '010-3333-4444', '20140501', '20140501', '20210501', 'A03', NULL, 'A', '003', '202', 'B03', '003', 'T03', '차장', '운영', 'IT운영', 'sungho.park@company.com', NULL),
('E100004', 'SYSTEM', '최유진', 'Choi', 'Yujin', 'HR0001', '19950710', '02-1234-5681', '010-4444-5555', '20180701', '20180701', '20220701', 'A04', NULL, 'A', '004', '203', 'B04', '003', 'T04', '대리', '인사', '인사관리', 'yujin.choi@company.com', NULL),
('E100005', 'SYSTEM', '정태훈', 'Jung', 'Taehoon', 'ROOT01', '19850825', '02-1234-5682', '010-5555-6666', '20120825', '20120825', '20200825', 'A05', NULL, 'A', '005', '204', 'B05', '003', 'T05', '부장', '경영', '경영지원', 'taehoon.jung@company.com', NULL);

-- User Account Information (OCO10100)
-- raw password: Skcc1234!
INSERT INTO OCO10100 (USERID, USER_NM, CONN_PSSWD, PSSWD_EXPIR_DT, USER_CONT_PHNO, USER_EMAILADDR, DEPTCD, REOFO_CD, VCTN_CD, USER_GROUP_CD, INNER_USER_CL_CD, USER_IDENT_NO, FST_REG_DTMD, PSSWD_ERR_FRQY, USERID_STS_CD, USER_IPADDR, LAST_CHNGR_ID, LAST_CHNG_DTMD) VALUES
('SYSTEM', 'JohnS', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20250601', '01012345678', 'john@example.com', 'D001', '001', '001', 'A', '1', 'E0001', NOW(), 0, 'O', '192.168.0.1', 'system', NOW()),
('SYSTEM1', 'AliceB', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20250601', '01087654321', 'alice@example.com', 'D002', '002', '002', 'A', '1', 'E0002', NOW(), 0, 'O', '192.168.0.2', 'system', NOW()),
('SYSTEM2', 'BobK', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20250601', '01011112222', 'bob@example.com', 'D001', '001', '001', 'A', '2', NULL, NOW(), 1, 'L', '192.168.0.3', 'system', NOW()),
('SYSTEM3', 'CarolM', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20250601', '01033334444', 'carol@example.com', 'D003', '002', '002', 'B', '3', NULL, NOW(), 2, 'O', '192.168.0.4', 'system', NOW()),
('SYSTEM4', 'DavidL', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20250601', '01055556666', 'david@example.com', 'D004', '001', '001', 'C', '9', NULL, NOW(), 5, 'O', '192.168.0.5', 'system', NOW()),
('SYSTEM5', 'CarolM', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20250601', '01033334444', 'carol@example.com', 'D003', '002', '002', 'B', '3', NULL, NOW(), 2, 'D', '192.168.0.4', 'system', NOW());
-- System Administrator
('ADMIN00001', 'SYSTEM', CURRENT_TIMESTAMP, '시스템관리자', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20251231', '010-1234-5678', 'admin@company.com', 'IT0001', 'MGR', 'ADM', 'IT001', 'S001', 'A', '9', 'ID001', 'O', '2025-01-01 09:00:00', 0, '192.168.1.100'),
-- Regular Employee
('USER00001', 'SYSTEM', CURRENT_TIMESTAMP, '홍길동', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20251231', '010-2345-6789', 'hong.gd@company.com', 'HR0001', 'EMP', 'GEN', 'HR001', 'E001', 'U', '1', 'ID002', 'O', '2025-01-02 09:00:00', 0, '192.168.1.101'),
-- Manager
('USER00002', 'SYSTEM', CURRENT_TIMESTAMP, '김부장', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20251231', '010-3456-7890', 'kim.mgr@company.com', 'MK0001', 'MGR', 'MNG', 'MK001', 'M001', 'U', '2', 'ID003', 'O', '2025-01-03 09:00:00', 0, '192.168.1.102'),
-- Locked Account
('USER00003', 'SYSTEM', CURRENT_TIMESTAMP, '이잠김', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20251231', '010-4567-8901', 'lee.lock@company.com', 'IT0002', 'EMP', 'GEN', 'IT002', 'E002', 'U', '4', 'ID004', 'L', '2025-01-04 09:00:00', 5, '192.168.1.103'),
-- Pending Approval
('USER00004', 'SYSTEM', CURRENT_TIMESTAMP, '박대기', '$2a$10$xdKG7VrGkNgzxy7NwU6rpOzPtX5nub5SPuHO5YckBXZHb4K1kKp1e', '20251231', '010-5678-9012', 'park.wait@company.com', 'SA0001', 'EMP', 'GEN', 'SA001', 'E001', 'U', '3', 'ID005', 'W', '2025-01-05 09:00:00', 0, '192.168.1.104');

-- AppGroup
INSERT INTO OCO40100 (LAST_CHNGR_ID, LAST_CHNG_DTMD, APRO_TYPE_CL_CD, APRO_TASK_CL_CD, APRO_GROUP_CL_NM, APRO_GROUP_DESC) VALUES
('SYSTEM', '2025-05-13 15:20:00', 'A', 'COM', 'Transaction Group', 'Groups APIs related to core transactions.'),
('SYSTEM', '2025-05-13 15:22:30', 'R', 'ANL', 'Reporting Group', 'Groups APIs used for generating reports and analytics.'),
('SYSTEM', '2025-05-13 15:25:00', 'B', 'CSV', 'Data Import Group', 'Groups APIs for importing data from CSV files.'),
('SYSTEM', '2025-05-13 15:27:30', 'I', 'ISC', 'Integration Group', 'Groups APIs for communicating with external integration services.'),
('SYSTEM1', '2025-05-13 15:30:00', 'A', 'COM', 'User Management Group', 'Groups APIs related to managing user accounts.'),
('SYSTEM1', '2025-05-13 15:32:30', 'R', 'ANL', 'Dashboard APIs', 'Groups APIs for fetching data for dashboards.');

-- ApiInfo
INSERT INTO OCO40110 (LAST_CHNGR_ID, LAST_CHNG_DTMD, APRO_GROUP_ID, API_NM, API_DESC, API_LOC_URLADDR, HTT_METHOD_VAL, API_REQ_CNTNT, API_RESP_CNTNT, USE_YN) VALUES
('SYSTEM', '2025-05-13 14:10:00', 1, 'Create Order', 'API to create a new order.', '/api/order', 'POST', '{"customerId": "...", "items": [...]}', '{"orderId": "...", "status": "..."}', 'Y'),
('SYSTEM', '2025-05-13 14:15:30', 2, 'Get Sales Report', 'API to retrieve the latest sales report.', '/api/report/sales', 'GET', '{"fromDate": "YYYY-MM-DD", "toDate": "YYYY-MM-DD"}', '[{"date": "...", "revenue": ...}, ...]', 'Y'),
('SYSTEM', '2025-05-12 17:00:00', 1, 'Update User Status', 'API to update the status of a user.', '/api/user/{userId}/status', 'PUT', '{"status": "ACTIVE" | "INACTIVE"}', '{"userId": "...", "newStatus": "..."}', 'Y'),
('SYSTEM1', '2025-05-11 09:30:00', 4, 'Process Daily Data', 'API to trigger the daily data processing job.', '/api/batch/processDailyData', 'POST', '{}', '{"status": "STARTED", "jobId": "..."}', 'Y'),
('SYSTEM', '2025-05-13 14:20:45', 1, 'Authenticate User', 'API to authenticate a user and obtain a token.', '/api/auth/login', 'POST', '{"username": "...", "password": "..."}', '{"token": "...", "expiry": "..."}', 'Y'),
('SYSTEM', '2025-05-10 11:30:10', 1, 'Convert Currency', 'API to convert an amount from one currency to another.', '/api/util/currency/convert', 'GET', '{"from": "USD", "to": "EUR", "amount": 100}', '{"convertedAmount": 92.50, "rate": 0.925}', 'Y'),
('SYSTEM', '2025-05-13 14:12:00', 1, 'Get Order Details', 'API to retrieve details of a specific order.', '/api/order/{orderId}', 'GET', '{}', '{"orderId": "...", "items": [...], "total": ...}', 'Y');

-- ApiMonitor
INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID) VALUES
('2022-07-15 10:30:00', 'SYSTEM', 1);

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID, API_RESP_TIME, API_RESP_STS_VAL, ERR_CNTNT) VALUES
('2022-11-20 15:45:00', 'SYSTEM', 1, 600, 503, 'Timeout error when calling service B');

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID, API_RESP_TIME, API_RESP_STS_VAL) VALUES
('2023-03-01 09:00:00', 'SYSTEM', 1, 120, 200);

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID, API_RESP_TIME, API_RESP_STS_VAL, ERR_CNTNT, API_EXECT_USERID) VALUES
('2023-09-10 18:20:00', 'SYSTEM', 2, 450, 400, 'Invalid input parameters', 'USER007');

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID, API_RESP_TIME) VALUES
('2024-01-05 11:15:00', 'SYSTEM', 2, 80);

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID, API_RESP_TIME, API_RESP_STS_VAL, ERR_CNTNT, CONN_IPADDR) VALUES
('2024-06-22 14:00:00', 'SYSTEM', 2, 700, 504, 'Database error', '192.168.1.15');

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID, API_RESP_TIME, API_RESP_STS_VAL, API_EXECT_USERID, CONN_IPADDR) VALUES
('2025-05-13 12:10:00', 'SYSTEM1', 2, 200, 200, 'AGENT02', '203.0.113.50');

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID, API_RESP_TIME, ERR_CNTNT) VALUES
('2025-05-13 12:20:00', 'SYSTEM', 1, 550, 'Business logic processing error');

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID) VALUES
('2023-12-28 16:30:00', 'c', 1);

INSERT INTO OCO40111 (API_EXECT_START_DTMT, LAST_CHNGR_ID, API_ID, ERR_CNTNT) VALUES
('2024-09-03 08:45:00', 'SYSTEM', 1, 'Unknown error');

UPDATE OCO40110 SET APRO_GROUP_ID = (SELECT (APRO_GROUP_ID) FROM OCO40100 LIMIT 1);
UPDATE OCO40111 SET API_ID = (SELECT (API_ID) FROM OCO40110 LIMIT 1);
UPDATE OCO40111 SET API_EXECT_USERID = 'SYSTEM';

-- SystmConnPsbty (OCO10240)
INSERT INTO OCO10240 (LINKA_SYSTM_NM, LAST_CHNGR_ID, LAST_CHNG_DTMD, FST_REGR_ID, FST_REG_DTMD, CONN_PSBTY_YN) VALUES
('PAY_EXT_A', 'SYSTEM', '2025-05-14 09:35:00', 'SYSTEM', '2025-05-14 09:35:00', 'N'),
('REPORT_OLD', 'SYSTEM', '2025-05-14 09:37:30', 'SYSTEM', '2025-05-14 09:37:30', 'N'),
('REPORT_OLD', 'SYSTEM1', '2025-05-14 09:40:00', 'SYSTEM1', '2025-05-14 09:40:00', 'N'),
('PAY_EXT_A', 'SYSTEM', '2025-05-14 09:42:30', 'SYSTEM', '2025-05-14 09:42:30', 'N'),
('PAY_EXT_A', 'SYSTEM1', '2025-05-14 09:45:00', 'SYSTEM1', '2025-05-14 09:45:00', 'Y'),
('REPORT_OLD', 'SYSTEM', '2025-05-14 09:47:30', 'SYSTEM', '2025-05-14 09:47:30', 'Y');

-- MenuStatistics
INSERT INTO OCO.OCO10195 (SUMR_DT, MENU_ID, CONN_QTY, LAST_MODIFIED_BY) VALUES
('20250513', 'COM-01-05-00', 150, 'SYSTEM'),
('20250513', 'COM-01-05-01', 210, 'SYSTEM'),
('20250513', 'COM-01-05-02', 95, 'SYSTEM'),
('20250514', 'COM-01-05-00', 180, 'SYSTEM'),
('20250514', 'COM-01-05-01', 250, 'SYSTEM'),
('20250514', 'COM-01-05-02', 120, 'SYSTEM'),
('20250512', 'COM-01-05-00', 120, 'SYSTEM'),
('20250512', 'COM-01-05-01', 100, 'SYSTEM');

-- ApiStat (OCO40112)
INSERT INTO OCO.OCO40112 (API_EXECT_DT, API_ID, LAST_CHNGR_ID, LAST_CHNG_DTMD, API_EXECT_CCNT) VALUES
('20250513', 1, 'SYSTEM', NOW(), 15),
('20250513', 2, 'SYSTEM', NOW(), 22),
('20250514', 3, 'SYSTEM', NOW(), 30),
('20250514', 4, 'SYSTEM', NOW(), 10),
('20250515', 5, 'SYSTEM', NOW(), 18),
('20250515', 6, 'SYSTEM', NOW(), 5),
('20250515', 7, 'SYSTEM', NOW(), 5);

-- Bssmacd (OCO50300)
INSERT INTO OCO50300 (BSSMACD, LAST_CHNGR_ID, LAST_CHNG_DTMD, BSS_HQ_NM, USE_YN) VALUES
('A1', 'SYSTEM', NOW(), 'Alpha Headquarters', 'Y'),
('B2', 'SYSTEM', NOW(), 'Beta Headquarters', 'Y'),
('C3', 'SYSTEM', NOW(), 'Gamma Branch Office', 'N'),
('D4', 'SYSTEM', NOW(), 'Delta Main Office', 'Y'),
('E5', 'SYSTEM', NOW(), 'Epsilon Operations', 'N');

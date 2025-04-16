CREATE SCHEMA OIF;

CREATE TABLE OIF.OIF21300 (
    BSSMACD VARCHAR(2) NOT NULL,       -- BSSMACD, kiểu VARCHAR(2), không được null
    BSS_HQ_NM VARCHAR(50) NOT NULL,    -- BSS_HQ_NM, kiểu VARCHAR(50), không được null
    USE_YN CHAR(1) NOT NULL,           -- USE_YN, kiểu CHAR(1), không được null
    PRIMARY KEY (BSSMACD)              -- Khóa chính trên cột BSSMACD
);

CREATE TABLE OIF.OIF21200 (
    DEPTCD VARCHAR(8) NOT NULL,            -- Department Code, kiểu VARCHAR(8), không được null
    MGMT_DEPTCD VARCHAR(6),                -- Management Department Code, kiểu VARCHAR(6)
    DEPT_NM VARCHAR(50),                   -- Department Name, kiểu VARCHAR(50)
    ENGSH_DEPT_NM VARCHAR(50),             -- English Department Name, kiểu VARCHAR(50)
    ENGSH_DEPT_ABBR_NM VARCHAR(50),        -- English Department Abbreviation Name, kiểu VARCHAR(50)
    BSSMACD VARCHAR(2),                    -- BSSMACD, kiểu VARCHAR(2)
    SUPER_DEPTCD VARCHAR(6),               -- Super Department Code, kiểu VARCHAR(6)
    DEPT_GRADE_CD VARCHAR(1),              -- Department Grade Code, kiểu VARCHAR(1)
    DEPT_CRAT_DT VARCHAR(8),               -- Department Creation Date, kiểu VARCHAR(8)
    DEPT_ABOL_DT VARCHAR(8),               -- Department Abolishment Date, kiểu VARCHAR(8)
    FEMPD_DEPTCD VARCHAR(6),               -- Femp Department Code, kiểu VARCHAR(6)
    WHTAX_BZPL_CD VARCHAR(6),              -- Withholding Tax Business License Code, kiểu VARCHAR(6)
    SALS_DEPTCD VARCHAR(6),                -- Sales Department Code, kiểu VARCHAR(6)
    INV_DEPTCD VARCHAR(6),                 -- Inventory Department Code, kiểu VARCHAR(6)
    INVNT_WRHUS_CD VARCHAR(6),             -- Inventory Warehouse Code, kiểu VARCHAR(6)
    VAT_BZPL_CD VARCHAR(6),                -- VAT Business License Code, kiểu VARCHAR(6)
    ZIPCD VARCHAR(6),                      -- Zip Code, kiểu VARCHAR(6)
    BASIC_ADDR VARCHAR(200),               -- Basic Address, kiểu VARCHAR(200)
    DETIL_ADDR VARCHAR(200),               -- Detailed Address, kiểu VARCHAR(200)
    PHNO VARCHAR(14),                      -- Phone Number, kiểu VARCHAR(14)
    FAX_NO VARCHAR(14),                    -- Fax Number, kiểu VARCHAR(14)
    BZNO VARCHAR(10),                      -- Business License Number, kiểu VARCHAR(10)
    BZMAN_ACQ_DT VARCHAR(8),               -- Business Manager Acquisition Date, kiểu VARCHAR(8)
    TXOFC_CD VARCHAR(3),                   -- Tax Office Code, kiểu VARCHAR(3)
    TXOFC_NM VARCHAR(20),                  -- Tax Office Name, kiểu VARCHAR(20)
    COM_BSCND_NM VARCHAR(30),              -- Company Basic Name, kiểu VARCHAR(30)
    COM_ITM_NM VARCHAR(30),                -- Company Item Name, kiểu VARCHAR(30)
    COM_CORP_NM VARCHAR(50),               -- Company Corporation Name, kiểu VARCHAR(50)
    USE_YN CHAR(1),                        -- Use Y/N flag, kiểu CHAR(1)
    CCNTR_CD VARCHAR(10),                  -- Country Code, kiểu VARCHAR(10)
    RPRTT_NM VARCHAR(30),                  -- Reporter Name, kiểu VARCHAR(30)
    EXTSN_NO VARCHAR(4),                   -- Extension Number, kiểu VARCHAR(4)
    PRIMARY KEY (DEPTCD)                   -- Primary key on DEPTCD
);

CREATE TABLE OIF.OIF21100 (
    EMPNO VARCHAR(8) NOT NULL,
    EMP_KRN_NM VARCHAR(30) NOT NULL,
    EMP_ENGLNM VARCHAR(20) NOT NULL,
    EMP_ENGFNM VARCHAR(20),
    DEPTCD VARCHAR(6),
    BTHDY VARCHAR(8),
    OWHM_PHNO VARCHAR(14),
    MPHNO VARCHAR(14),
    GROUP_ENTCP_DT VARCHAR(8),
    TCOM_ENTCP_DT VARCHAR(8),
    CURTP_OFORD_DT VARCHAR(8),
    CURTP_OFORD_CD VARCHAR(3),
    RESG_DT VARCHAR(8),
    COM_CL_CD VARCHAR(1),
    CLOFP_CD VARCHAR(3),
    REOFO_CD VARCHAR(3),
    JGP_CD VARCHAR(1),
    VCTN_CD VARCHAR(3),
    TEAMBR_CL_CD VARCHAR(3),
    CLOFP_NM VARCHAR(20),
    JGP_NM VARCHAR(20),
    VCTN_NM VARCHAR(20),
    EMAILADDR VARCHAR(100),
    BEFO_EMPNO VARCHAR(8),
    PRIMARY KEY (EMPNO)
);

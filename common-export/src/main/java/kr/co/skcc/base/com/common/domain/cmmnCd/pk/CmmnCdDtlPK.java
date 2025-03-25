package kr.co.skcc.base.com.common.domain.cmmnCd.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmmnCdDtlPK implements Serializable {
    String cmmnCd;
    String cmmnCdVal;
}

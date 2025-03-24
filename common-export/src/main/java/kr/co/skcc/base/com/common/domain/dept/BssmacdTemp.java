package kr.co.skcc.base.com.common.domain.dept;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.skcc.base.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="OIF21300", catalog = "OIF")
public class BssmacdTemp extends BaseEntity {
    @Id
    @Column(name="BSSMACD", length = 2, nullable = false)
    private String bssmacd;

    @Column(name="BSS_HQ_NM", length = 50, nullable = false)
    private String bssHqNm;

    @Column(name="USE_YN", length = 1, nullable = false)
    private String useYn;

}

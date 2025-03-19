package kr.co.skcc.oss.com.common.domain.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.skcc.oss.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="OCO10240", catalog = "OCO")
public class SystmConnPsbty extends BaseEntity {

    @Id
    @Column(name="LINKA_SYSTM_NM", length = 10)
    private String linkaSystmNm;

    @Column(name="CONN_PSBTY_YN", length = 1)
    private String connPsbtyYn;

}

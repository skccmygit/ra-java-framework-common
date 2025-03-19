package kr.co.skcc.oss.com.common.domain.menu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.skcc.oss.com.common.api.dto.domainDto.ScrenDto;
import kr.co.skcc.oss.com.common.jpa.Apiable;
import kr.co.skcc.oss.com.common.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="OCO10220", catalog = "OCO")
public class Scren extends BaseEntity implements Apiable<ScrenDto> {

    @Id
    @Column(name="SCREN_ID", length=10, nullable=false)
    private String screnId;

    @Column(name="CHRG_TASK_GROUP_CD", length=3)
    private String chrgTaskGroupCd;

    @Column(name="SCREN_NM", length=100)
    private String screnNm;

    @Column(name="SCREN_DESC", length=100)
    private String screnDesc;

    @Column(name="SCREN_URLADDR", length=100)
    private String screnUrladdr;

    @Column(name="USE_YN", length=1)
    private String useYn;

    @Column(name="SCREN_CL_CD")
    private String screnClCd;

    @Column(name="SCREN_SIZE_CL_CD")
    private String screnSizeClCd;

    @Column(name="SCREN_WIDTH_SIZE")
    private Integer screnWidthSize;

    @Column(name="SCREN_VRTLN_SIZE")
    private Integer screnVrtlnSize;

    @Column(name="SCREN_START_TOP_CODN")
    private Integer screnStartTopCodn;

    @Column(name="SCREN_START_LEFT_CODN")
    private Integer screnStartLeftCodn;

//    @BatchSize(size = 100)
//    @OneToMany(mappedBy = "screnId",targetEntity = Bttn.class, fetch = FetchType.LAZY)
//    private List<Bttn> bttnList;

    @Override
    public ScrenDto toApi() {

        ScrenDto dto = new ScrenDto();
//        List<BttnDto> bttnListDto;
        BeanUtils.copyProperties(this,dto);

//        bttnListDto = new ArrayList<>();
//        if(this.bttnList != null) {
//            for (Bttn b : this.bttnList) {
//                bttnListDto.add(b.toApi());
//            }
//            dto.setBttnList(bttnListDto);
//        }
        return dto;
    }
}

package kr.co.skcc.base.com.common.domain.menu.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BttnPK implements Serializable {

    private String screnId;
    private String bttnId;
}



package com.skcc.ra.account.domain.loginCert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "UserAuth", timeToLive = 86400)
public class UserAuth {

    //사용자연락전화번호
//    @Column(name = "USERID", length = 10, nullable = false)
    @Id
    private String userid;

    //인증번호
//    @Column(name = "AUTH_NUMBER", length = 6, nullable = false)
    private String authNumber;

    private String useYn;

//    @Override
//    public UserAuthDto toApi() {
//        UserAuthDto userAuthDto = new UserAuthDto();
//        BeanUtils.copyProperties(this, userAuthDto);
//        return userAuthDto;
//    }
}
package kr.co.skcc.oss.com.common.jpa;

import kr.co.skcc.oss.com.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

//        HttpServletRequest request = RequestUtil.getHttpServletRequest();
//        String loginId = request.getHeader("ACCOUNT_ID");

        String loginId = RequestUtil.getLoginUserid();

        return Optional.ofNullable(loginId);
    }

}

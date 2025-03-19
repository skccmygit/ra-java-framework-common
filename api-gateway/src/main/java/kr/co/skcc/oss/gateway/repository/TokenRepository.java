package kr.co.skcc.oss.gateway.repository;

import kr.co.skcc.oss.gateway.domain.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * TokenRepository.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-04-26, 최초 작성
 */
@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
        Optional<Token> findByAccessTokenOrOldAccessToken(String accessToken, String oldAccessToken);
}

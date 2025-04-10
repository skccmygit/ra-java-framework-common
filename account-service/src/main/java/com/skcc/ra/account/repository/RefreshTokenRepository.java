package com.skcc.ra.account.repository;

import com.skcc.ra.account.domain.loginCert.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAccessToken(String accessToken);
    Optional<RefreshToken> findByOldAccessToken(String oldAccessToken);
}
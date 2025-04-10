package com.skcc.ra.account.service;

import com.skcc.ra.account.domain.loginCert.RefreshToken;

public interface RefreshTokenService {

    public void deleteByUserid(String userid);

    public RefreshToken createRefreshToken(String userId, String accessToken);

    public RefreshToken updateAccessToken(RefreshToken refreshToken);

    public boolean checkTokenValid(RefreshToken refreshToken);

}

package com.trail_race.race_application_command_service.security;

import com.trail_race.race_application_command_service.security.dto.JwtProperties;
import com.trail_race.race_application_command_service.exception.dao.ForbiddenException;
import com.trail_race.race_application_command_service.security.dto.AuthenticationRequest;
import com.trail_race.race_application_command_service.security.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${app.name}")
    private String appName;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String userEmail = authentication.getName();
        String accessToken = generateJwtToken(userEmail, jwtProperties.getAccessTokenExpirySeconds());
        String refreshToken = generateJwtToken(userEmail, jwtProperties.getRefreshTokenExpirySeconds());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Issues new access and refresh token from given refresh token
     */
    public AuthenticationResponse refreshToken(String refreshToken) {
        Jwt jwtRefreshToken = validateAndGetRefreshToken(refreshToken);
        String userEmail = jwtRefreshToken.getSubject();
        String newAccessToken = generateJwtToken(userEmail, jwtProperties.getAccessTokenExpirySeconds());
        String newRefreshToken = generateJwtToken(userEmail, jwtProperties.getRefreshTokenExpirySeconds());
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private String generateJwtToken(String email, Integer tokenDurationSeconds) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(appName)
                .issuedAt(now)
                .expiresAt(now.plus(tokenDurationSeconds, ChronoUnit.SECONDS))
                .subject(email)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    private Jwt validateAndGetRefreshToken(String refreshToken) {
        Jwt jwtRefreshToken = decoder.decode(refreshToken);
        if (isExpired(jwtRefreshToken)) {
            throw new ForbiddenException("Refresh token expired");
        }
        return jwtRefreshToken;
    }

    private boolean isExpired(Jwt jwt) {
        Instant expiresAt = jwt.getExpiresAt();
        return expiresAt == null || expiresAt.compareTo(Instant.now()) < 0;
    }
}

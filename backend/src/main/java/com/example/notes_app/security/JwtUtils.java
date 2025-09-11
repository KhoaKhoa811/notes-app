package com.example.notes_app.security;

import com.example.notes_app.enums.ErrorCode;
import com.example.notes_app.exception.impl.InvalidTokenSignatureException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${jwt.signed-key}")
    private String SIGNER_KEY;
    private final UserDetailsService userDetailsService;

    public String generateAccessToken(Authentication authentication) throws JOSEException {
        // get user information
        String email = authentication.getName();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        // create header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        // create JWT Claims
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .claim("scope", scope)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 3600000)) // one hour
                .build();
        // create signer
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        // create jws object
        JWSObject jwsObject = new JWSObject(header, payload);
        // sign jws object
        byte[] decodedKey = Base64.getDecoder().decode(SIGNER_KEY);
        jwsObject.sign(new MACSigner(decodedKey));
        // return String jwt
        return jwsObject.serialize();
    }

    public String generateRefreshToken(Authentication authentication) throws JOSEException {
        // get user information
        String email = authentication.getName();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        // create header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        // create JWT Claims
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .claim("scope", scope)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 2592000000L)) // one hour
                .build();
        // create signer
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        // create jws object
        JWSObject jwsObject = new JWSObject(header, payload);
        // sign jws object
        byte[] decodedKey = Base64.getDecoder().decode(SIGNER_KEY);
        jwsObject.sign(new MACSigner(decodedKey));
        // return String jwt
        return jwsObject.serialize();
    }

    public String getEmail(String token) {
        try {
            // Validate jwt sign
            if (isValidToken(token)) {
                // Parse JWT
                SignedJWT signedJWT = SignedJWT.parse(token);
                // Get claims from JWT
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
                return claims.getSubject();  // Return the subject (email in this case)
            } else {
                throw new InvalidTokenSignatureException(ErrorCode.JWT_INVALID);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while validating JWT", e);
        }
    }

    public boolean isValidToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            byte[] decodedKey = Base64.getDecoder().decode(SIGNER_KEY);
            JWSVerifier verifier = new MACVerifier(decodedKey);
            return signedJWT.verify(verifier);  // Verifies the JWT signature
        } catch (Exception e) {
            return false;  // If there's any issue parsing or verifying the token, return false
        }
    }

}

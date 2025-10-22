package com.example.SchoolApp.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	private String secretKey = "";
	
	
	public JWTService() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String generateToken(String username) {
		Map<String, Object> claim = new HashMap<>();
		return Jwts.builder()
				.claims()
				.add(claim)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+ (1000*60*60*60)))
				.and()
				.signWith(getKey())
				.compact();	
	}

	private Key getKey() {
		byte[] keyByte = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyByte);
	}


	public String extractUsername(String token) {
		
		return extractClaim(token, Claims::getSubject);
	}


	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}


	private Claims extractAllClaims(String token) {
		
		return Jwts.parser().verifyWith((SecretKey) getKey()).
				build().parseSignedClaims(token)
				.getPayload();
	}


	public boolean validateToken(String token, UserDetails user) {
		final String username = extractUsername(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
	}


	private boolean isTokenExpired(String token) {
		
		return extractExpiration(token).before(new Date());
	}


	private Date extractExpiration(String token) {
		
		return extractClaim(token, Claims::getExpiration);
	}
}

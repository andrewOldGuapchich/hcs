package com.andrew.authserver.service.add_serv

import com.andrew.authserver.entity.auth.Role
import com.andrew.authserver.entity.auth.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.SecretKey
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.NonNull

import java.security.Key

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date


@Service
open class JwtTokenService (
        @Value("\${jwt.token.secret.access}") jwtAccessSecret: String,
        @Value("\${jwt.token.duration}") durationMinute: Long
        ) {

        private val jwtAccessSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        private val duration: Long = durationMinute;

        //generate of access token
        fun generateAccessToken(user: User): String{
                val nowDate = LocalDateTime.now();
                val expireInstant = nowDate.plusMinutes(duration).atZone(ZoneId.systemDefault()).toInstant();
                val expireDate = Date.from(expireInstant);

                return Jwts.builder()
                        .setSubject(user.getUsername())
                        .setExpiration(expireDate)
                        .signWith(jwtAccessSecret)
                        .claim("roles", user.getRoles())
                        .claim("email", user.getEmail())
                        .compact();
        }

        fun validateAccessToken(@NonNull token: String): Boolean {
                return validateToken(token, jwtAccessSecret);
        }

        fun getUsername(token: String): String{
                return getAllClaimsFromToken(token).subject;
        }

        fun getRoles(token: String): List<Role> {
                val claims = getAllClaimsFromToken(token)

                val roles = claims.get("roles", List::class.java) as? List<*> ?: return emptyList()

                return roles.mapNotNull { it as? Role }
        }

        private fun validateToken(@NonNull token: String, @NonNull secret: Key): Boolean{
                return try{
                        Jwts.parserBuilder()
                                .setSigningKey(jwtAccessSecret)
                                .build()
                                .parseClaimsJws(token);
                        true;
                } catch (e: Exception){
                        false;
                }
        }

        private fun getAllClaimsFromToken(token: String): Claims{
                return Jwts.parser()
                        .setSigningKey(jwtAccessSecret)
                        .parseClaimsJws(token)
                        .body;
        }
}
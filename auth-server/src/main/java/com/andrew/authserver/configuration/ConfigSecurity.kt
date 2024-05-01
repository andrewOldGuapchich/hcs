package com.andrew.authserver.configuration

import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.tomcat.util.descriptor.web.SecurityCollection
import org.apache.tomcat.util.descriptor.web.SecurityConstraint
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


open class ConfigSecurity {
    @Bean
    open fun passwordEncoder(): BCryptPasswordEncoder{
        return BCryptPasswordEncoder();
    }

    @Bean
    @Throws(Exception::class)
    open fun filterChain(http: HttpSecurity) : SecurityFilterChain{
        return http
                .requiresChannel {
                    channel -> channel.anyRequest().requiresSecure()
                }
                .authorizeHttpRequests {
                    auth -> auth
                        .requestMatchers("/auth/user").permitAll()
                        .requestMatchers("/registration/**").permitAll()
                        .anyRequest().permitAll()
                }.build();
    }
    @Bean
    open fun servletContainer(): ServletWebServerFactory {
        val tomcat = object : TomcatServletWebServerFactory() {
            override fun postProcessContext(context: Context) {
                val securityConstraint = SecurityConstraint().apply {
                    userConstraint = "CONFIDENTIAL"
                }
                val collection = SecurityCollection().apply {
                    addPattern("/*")
                }
                securityConstraint.addCollection(collection)
                context.addConstraint(securityConstraint)
            }
        }
        tomcat.addAdditionalTomcatConnectors(getHttpConnector())
        return tomcat
    }

    private fun getHttpConnector(): Connector {
        return Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL).apply {
            scheme = "http"
            port = 8080
            redirectPort = 8443
        }
    }
}

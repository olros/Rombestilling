package ntnu.idatt2105.sercurity.config

import ntnu.idatt2105.sercurity.JwtUtil
import ntnu.idatt2105.sercurity.filter.JWTAuthenticationFilter
import ntnu.idatt2105.sercurity.filter.JWTUsernamePasswordAuthenticationFilter
import ntnu.idatt2105.sercurity.service.RefreshTokenService
import ntnu.idatt2105.user.model.RoleType
import ntnu.idatt2105.user.service.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import java.util.List
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurity(val refreshTokenService: RefreshTokenService,
                  private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                  private val jwtUtil: JwtUtil,
                  private val jwtConfig: JWTConfig) : WebSecurityConfigurerAdapter() {


    private val DOCS_WHITELIST = arrayOf(
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
    )

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        if (jwtConfig != null) {
            httpSecurity.cors().configurationSource { request: HttpServletRequest? ->
                val cors = CorsConfiguration()
                cors.allowedOrigins = List.of("*")
                cors.allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
                cors.allowedHeaders = List.of("*")
                cors
            }
                    .and()
                    .csrf()
                    .disable()
                    .authorizeRequests()
                    .antMatchers(*DOCS_WHITELIST).permitAll()
                    .antMatchers(HttpMethod.POST, jwtConfig.uri + "/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/auth/forgot-password/").permitAll()
                    .antMatchers(HttpMethod.POST, "/auth/reset-password/**").permitAll()
                    .antMatchers( "/**").hasAnyAuthority(RoleType.ADMIN)
                    .antMatchers(HttpMethod.POST, "/users/me/").hasAnyAuthority(RoleType.ADMIN, RoleType.USER)
                    .anyRequest()
                    .authenticated()
                    .and()
                    .exceptionHandling()
                        .authenticationEntryPoint { req: HttpServletRequest?, res: HttpServletResponse, e: AuthenticationException? ->
                        res.contentType = "application/json"
                        res.status = HttpServletResponse.SC_UNAUTHORIZED
                        res.outputStream.println("{ \"message\": \"Brukernavn eller passord er feil\"}")
                    }
                    .and()
                    .addFilter(JWTUsernamePasswordAuthenticationFilter(refreshTokenService, authenticationManager(), jwtConfig))
                    .addFilterAfter(JWTAuthenticationFilter(jwtConfig, jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(bCryptPasswordEncoder)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val source = UrlBasedCorsConfigurationSource()
        val corsConfiguration = CorsConfiguration().applyPermitDefaultValues()
        source.registerCorsConfiguration("/**", corsConfiguration)
        return source
    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        return UserDetailsServiceImpl()
    }
}
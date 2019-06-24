package com.fensibox.cms.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.common.ResultCode;
import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private RedisUtils redisUtils;

    public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        CustomUser user = new ObjectMapper().readValue(httpServletRequest.getInputStream(), CustomUser.class);
        Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
        authSet.add(new SimpleGrantedAuthority(String.valueOf(user.getRole())));
        //返回一个验证令牌
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        authSet
                )
        );
    }

    /**
     * 验证成功后调用
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {

        getToken(response, authResult.getName(), authResult.getAuthorities());
    }

    /**
     * 验证失败后调用
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseResult.fail(ResultCode.USERNAME_OR_PWD_ERROR)));
    }

    /**
     * 生成 JWT token
     *
     * @param response
     * @param userName
     * @param roles
     */
    private void getToken(HttpServletResponse response, String userName, Collection<?> roles) {
        try {
            String token = JWTUtil.generateToken(userName, null, roles);
            //将 JWT写入body
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setHeader("Access-Control-Expose-Headers", "Authentication-Info");
            response.setHeader("Authentication-Info", token);

            //保存在缓存中，用于后续刷新token
            redisUtils.putCache(userName, token);

            response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseResult.success()));
        } catch (Exception e) {
            logger.error("generate token failed", e);
            try {
                response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseResult.fail(ResultCode.SYS_ERROR)));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}

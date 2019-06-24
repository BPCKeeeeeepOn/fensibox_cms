package com.fensibox.cms.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.*;

public class JWTUtil {


    /**
     * 过期时间,单位毫秒
     */
    private static final long EXPIRATIONTIME = 2 * 60 * 60 * 1000;

    /**
     * 签名密钥
     */
    private static final String SECRET = "secret&dms#pwd";

    /**
     * Token前缀
     */
    private static final String TOKEN_PREFIX = "Bearer";

    /**
     * 生成token
     *
     * @param userName 用户名
     * @param expTime  过期时间(有效期)，单位毫秒
     * @param roles
     * @return
     */
    public static String generateToken(String userName, Long expTime, Collection<?> roles) {
        expTime = Objects.isNull(expTime) ? EXPIRATIONTIME : expTime;
        String token = Jwts.builder()
                //保存权限（角色）
                .claim("authorities", StringUtils.join(roles, ","))
                //用户名写入标题
                .setSubject(userName)
                //有效期设置
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                //签名设置
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return token;
    }

    /**
     * token校验
     *
     * @param token
     * @return
     */
    public static Authentication verifySign(String token) throws Exception {
        Claims claims = Jwts.parser()
                //验签
                .setSigningKey(SECRET)
                //去掉Bearer
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();

        //获取用户名
        String user = claims.getSubject();

        //得到权限(角色)
        if (Objects.isNull(claims.get("authorities"))) {
            //角色信息一定不能传null，不然会出错
            return user != null ? new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()) : null;
        } else {
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
            //返回验证令牌
            return user != null ? new UsernamePasswordAuthenticationToken(user, null, authorities) : null;
        }
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public static String refreshToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    //验签
                    .setSigningKey(SECRET)
                    //去掉 Bearer
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();

            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
            String userName = claims.getSubject();
            String newToken = generateToken(userName, null, authorities);
            return newToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package ms.cb.starter.filter;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.*;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import cn.authing.sdk.java.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import ms.cb.starter.properties.AuthingClientProperties;
import ms.cb.starter.utils.AuthingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: Declan
 * 超级过滤器token order:99
 **/
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AuthingClientProperties authingClientProperties;

    @Autowired
    AuthingUtils authingUtils;

//    @Bean
//    @ConditionalOnMissingBean
//    public AuthingUtils authingUtils() {
//        return new AuthingUtils();
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //用户从前端发起请求的请求头获取token
        log.info("Token filter...");
        String token = request.getHeader("accessToken");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        log.info("check has token...");
        AuthenticationClientOptions clientOptions = authingUtils.getAuthenticationClientOptionsByAccessToken(token);
        log.info("token is:"+ JsonUtils.serialize(clientOptions));
        AuthenticationClient client = null;
        try {
            client = new AuthenticationClient(clientOptions);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //用户资料
        GetProfileDto getProfileDto = new GetProfileDto();
        getProfileDto.setWithIdentities(true);
        UserSingleRespDto userSingleRespDto = client.getProfile(getProfileDto);
        UserDto userDto = userSingleRespDto.getData();
        //用户角色
        GetMyRoleListDto getMyRoleListDto = new GetMyRoleListDto();
        getMyRoleListDto.setNamespace(authingClientProperties.getClientId());
        RoleListRespDto roleListRespDto = client.getRoleList(getMyRoleListDto);
        List<SimpleGrantedAuthority> rolesList = Optional.ofNullable(roleListRespDto)
                .map(RoleListRespDto::getData)
                .orElse(Collections.emptyList())
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getCode()))
                .collect(Collectors.toList());
        //用户资源权限
        GetMyAuthorizedResourcesDto getMyAuthorizedResourcesDto = new GetMyAuthorizedResourcesDto();
        getMyAuthorizedResourcesDto.setNamespace(authingClientProperties.getClientId());
        AuthorizedResourcePaginatedRespDto authorizedResources = client.getAuthorizedResources(getMyAuthorizedResourcesDto);
        List<SimpleGrantedAuthority> resourceList = null;
        if (Objects.nonNull(authorizedResources) && authorizedResources.getData().getList().size() != 0) {
            resourceList = Optional.ofNullable(authorizedResources)
                    .map(AuthorizedResourcePaginatedRespDto::getData)
                    .map(AuthorizedResourcePagingDto::getList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(resource -> new SimpleGrantedAuthority(resource.getResourceCode().split(":")[0]))
                    .collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(resourceList) && !CollectionUtils.isEmpty(rolesList)) {
            rolesList.addAll(resourceList);
        }
        //封装Authentication对象存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto,token,rolesList);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}

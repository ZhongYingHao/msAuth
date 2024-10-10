package ms.cb.starter.utils;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.client.ManagementClient;
import cn.authing.sdk.java.dto.*;
import cn.authing.sdk.java.enums.AuthMethodEnum;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import cn.authing.sdk.java.model.ManagementClientOptions;
import cn.authing.sdk.java.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import ms.cb.starter.properties.AuthingClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Authing工具类
 * Created by Declan.
 */
@Slf4j
@Component
public class AuthingUtils {

    @Autowired
    private AuthingClientProperties authingClientProperties;
//    @Value("${authing.user-pool-id}")
//    private String userPoolId;
//
//    @Value("${authing.user-pool-secret}")
//    private String userPoolSecret;

    /**
     * 获取authing资源客户端连接
     */
    public AuthenticationClientOptions getAuthenticationClientOptions() {
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authingClientProperties.getClientId());
        clientOptions.setAppSecret(authingClientProperties.getClientSecret());
        clientOptions.setAppHost(authingClientProperties.getAppHost());
        return clientOptions;
    }

    /**
     * 获取authing资源客户端连接(token版本)，具体对token用户进行用户级别操作
     */
    public AuthenticationClientOptions getAuthenticationClientOptionsByAccessToken(String token) {
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authingClientProperties.getClientId());
        clientOptions.setAppSecret(authingClientProperties.getClientSecret());
        clientOptions.setAppHost(authingClientProperties.getAppHost());
        clientOptions.setAccessToken(token);
        return clientOptions;
    }

    /**
     * 获取authing资源客户端连接(使用用户凭证登录)，具体对token用户进行用户级别操作
     */
    public AuthenticationClientOptions getAuthenticationClientOptionsByCredentials() {
        AuthenticationClientOptions clientOptions = new AuthenticationClientOptions();
        clientOptions.setAppId(authingClientProperties.getClientId());
        clientOptions.setAppSecret(authingClientProperties.getClientSecret());
        clientOptions.setAppHost(authingClientProperties.getAppHost());
        clientOptions.setTokenEndPointAuthMethod(AuthMethodEnum.CLIENT_SECRET_BASIC.getValue());
        return clientOptions;
    }

    /**
     * 获取authing管理员客户端连接，代码级别对资源/用户等赋值可通过这里实现
     */
    public ManagementClientOptions getManagementClientOptions() {
        ManagementClientOptions managementClientOptions = new ManagementClientOptions();
        managementClientOptions.setAccessKeyId(authingClientProperties.getUserPoolId());
        managementClientOptions.setAccessKeySecret(authingClientProperties.getUserPoolSecret());
        return managementClientOptions;
    }

    /**
     *
     * @param code  需要赋值的code user
     * @param applicationId 应用id，不传默认为system
     * @param targetType 授权目标对象=> 0:用户,1:角色,2:分组,3:部门
     */
    public IsSuccessRespDto assignCodeByUserId(String code, String applicationId, Integer targetType) {
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        UserDto principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AssignRoleDto reqDto = new AssignRoleDto();
        reqDto.setCode(code);
        reqDto.setNamespace(applicationId);
        List<TargetDto> list = new ArrayList<>();
        TargetDto targetDto = new TargetDto();
        switch (targetType) {
            case 0:targetDto.setTargetType(TargetDto.TargetType.USER);break;
            case 1:targetDto.setTargetType(TargetDto.TargetType.ROLE);break;
            case 2:targetDto.setTargetType(TargetDto.TargetType.GROUP);break;
            case 3:targetDto.setTargetType(TargetDto.TargetType.DEPARTMENT);break;
            default:targetDto.setTargetType(TargetDto.TargetType.USER);break;
        }
        targetDto.setTargetIdentifier(principal.getUserId());
        list.add(targetDto);
        reqDto.setTargets(list);
        IsSuccessRespDto response = client.assignRole(reqDto);
        return response;
    }

    /**
     * 获取用户权限列表(角色代码列表)
     * @return
     */
    public List<GrantedAuthority> getUserRoleList() {
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        if (!authorities.isEmpty()) {
            return authorities.stream()
                    .map(authority -> (GrantedAuthority) authority)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取用户分组列表(用户组代码列表)
     * @return
     */
    public List<String> getUserGroupList() {
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        UserDto principal = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GetUserGroupsDto reqDto = new GetUserGroupsDto();
        reqDto.setUserId(principal.getUserId());
        GroupPaginatedRespDto response = client.getUserGroups(reqDto);
        if (response.getStatusCode() == 200) {
            return response.getData().getList().stream()
                    .map(obj -> obj.getCode())
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 用户绑定手机号，用户自主绑定手机号方法(需校验验证码等安全校验操作) user
     * 该方法仅限用于Authing提供验证码服务条件下
     * @param passCode  短信验证码
     * @param phoneNumber 手机号
     */
    public CommonResponseDto bindPhone(String passCode, String phoneNumber) throws IOException, ParseException {
        String token = (String)SecurityContextHolder.getContext().getAuthentication().getCredentials();
        AuthenticationClientOptions clientOptions = getAuthenticationClientOptionsByAccessToken(token);
        AuthenticationClient client = new AuthenticationClient(clientOptions);
        BindPhoneDto reqDto = new BindPhoneDto();
        reqDto.setPassCode(passCode);
        reqDto.setPhoneNumber(phoneNumber);
        CommonResponseDto response = client.bindPhone(reqDto);
        log.info("用户自主绑定手机号方法: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 用户绑定手机号，管理员绑定手机号方法(无需安全校验操作) admin
     * 该方法可用于自己校验验证码后，给用户绑定手机号
     * @param phoneNumber 手机号
     */
    public UserSingleRespDto bindPhoneByAdmin(String phoneNumber) {
        UserDto principal = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        UpdateUserReqDto reqDto = new UpdateUserReqDto();
        reqDto.setUserId(principal.getUserId());
        reqDto.setPhone(phoneNumber);
        UserSingleRespDto response = client.updateUser(reqDto);
        log.info("admin管理员绑定手机号方法: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 用户更新个人资料方法(无需安全校验操作) admin
     * @param reqDto 用户资料dto
     */
    public UserSingleRespDto updateUserByAdmin(UpdateUserReqDto reqDto) {
        UserDto principal = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        reqDto.setUserId(principal.getUserId());
        UserSingleRespDto response = client.updateUser(reqDto);
        log.info("admin用户更新个人资料方法: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 创建用户分组(无需安全校验操作) admin
     * @param reqDto
     */
    public GroupSingleRespDto createUserGroupByAdmin(CreateGroupReqDto reqDto) {
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        GroupSingleRespDto response = client.createGroup(reqDto);
        log.info("admin创建用户分组: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 获取用户分组成员列表(无需安全校验操作) admin
     * @param reqDto
     */
    public UserPaginatedRespDto getUserGroupMemberByAdmin(ListGroupMembersDto reqDto) {
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        UserPaginatedRespDto response = client.listGroupMembers(reqDto);
        log.info("admin获取用户分组成员列表: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 添加用户到分组成员列表(无需安全校验操作) admin
     * @param reqDto
     */
    public IsSuccessRespDto addGroupMembersByAdmin(AddGroupMembersReqDto reqDto) {
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        IsSuccessRespDto response = client.addGroupMembers(reqDto);
        log.info("admin添加用户到分组成员列表: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 创建用户到列表(无需安全校验操作) admin
     * @param reqDto
     */
    public UserSingleRespDto addUserByAdmin(CreateUserReqDto reqDto) {
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        UserSingleRespDto response = client.createUser(reqDto);
        log.info("admin创建用户到列表: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 获取用户组详情(无需安全校验操作) admin  目前用于三方客户
     * @param
     */
    public GroupSingleRespDto getGroupByAdmin(GetGroupDto reqDto) throws IOException, ParseException {
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        GroupSingleRespDto response = client.getGroup(reqDto);
        log.info("admin用户名密码登录: {}", JsonUtils.serialize(response));
        return response;
    }


    /**
     * 判断用户是否已存在列表(无需安全校验操作) admin
     * @param reqDto
     */
    public IsUserExistsRespDto existsUserByAdmin(IsUserExistsReqDto reqDto) {
        ManagementClientOptions clientOptions = getManagementClientOptions();
        ManagementClient client = new ManagementClient(clientOptions);
        IsUserExistsRespDto response = client.isUserExists(reqDto);
        log.info("admin判断用户是否已存在列表: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 用户名密码注册(无需安全校验操作) admin  目前用于三方客户
     * @param
     */
    public UserSingleRespDto signUpByUsernamePasswordByAdmin(String username, SignUpProfileDto signUpProfileDto) throws IOException, ParseException {
        AuthenticationClientOptions clientOptions = getAuthenticationClientOptions();
        AuthenticationClient client = new AuthenticationClient(clientOptions);
        UserSingleRespDto response = client.signUpByUsernamePassword(
                username,
                "hezuofang!@#user",
                signUpProfileDto,
                new SignUpOptionsDto()
        );
        log.info("admin用户名密码注册: {}", JsonUtils.serialize(response));
        return response;
    }

    /**
     * 用户名密码登录(无需安全校验操作) admin  目前用于三方客户
     * @param
     */
    public LoginTokenRespDto signInByAccountPasswordByAdmin(String username) throws IOException, ParseException {
        AuthenticationClientOptions clientOptions = getAuthenticationClientOptions();
        AuthenticationClient client = new AuthenticationClient(clientOptions);
        LoginTokenRespDto response = client.signInByAccountPassword(
                username,
                "hezuofang!@#user",
                new SignInOptionsDto());
        log.info("admin用户名密码登录: {}", JsonUtils.serialize(response));
        return response;
    }

}

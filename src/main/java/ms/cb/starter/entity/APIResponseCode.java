package ms.cb.starter.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum APIResponseCode {
    /* sales suitability data */
    DATA_FOUND_SUCCESS("200011", "successfully returned"),
    SALES_SUITABILITY_FOUND_NOT_FOUND("400011", "Sales suitability cloud not be found"),
    USER_MESSAGE_FOUND_SUCCESS("200012", "User message successfully returned"),
    USER_MESSAGE_FOUND_NOT_FOUND("400012", "User message cloud not be found"),
    USER_LOGIN_SUCCESS("200001", "Login successfully"),
    USER_GetResetPasswordLink_SUCCESS("200002", "Get Reset Password Link successfully"),
    USER_GetResetPasswordLink_FAILED("400002", "Get Reset Password Link failed"),
    USER_ForgotResetPassword_SUCCESS("200002", "Forgot Password successfully"),
    USER_ForgotResetPassword_FAILED("400002", "Forgot Password failed"),
    FILE_SENDING_SUCCESS("200021", "Files successfully sent"),
    DATA_UPDATE_SUCCESS("200031", "Data successfully updated"),
    DATA_UPDATE_PROCESSING("200032", "Data update is processing, please wait for completion"),
    ASYN_JOB_SENDING_SUCCESS("200041", "Job sent successfully, please check after 15 minutes"),
    ASYN_JOB_PROCESSING("200042", "Async job is processing, please wait for completion"),
    USER_LOGIN_FAILED("400404", "Login failed"),
    DATA_FOUND_FAILED("400001", "Data could not be found"),
    USER_ACCOUNT_LOGIN_FAILED("400006", "Account Login failed"),
    USER_PWD_LOGIN_FAILED("400007", "Password Login failed"),
    USER_MFA_LOGIN_FAILED("400008", "MFA Login failed"),
    INVALID_DATA_REQUEST("400003", "Invalid data request"),
    INVALID_TOKEN_REQUEST("400004", "Invalid token request"),
    UNAUTHORIZED_USER_REQUEST("400005", "User was not authorized"),
    DATA_UPDATE_FAILED("400031", "Data failed to update"),
    DUPLICATE_DATA_ERROR("400032", "Update data duplicated with source"),
    NONEXIST_DATA_ERROR("400033", "Update data not existed"),
    SEARCH_DATA_NOT_FOUND("400034", "Search data not found"),
    SIGNATURE_IS_FAIL("500001", "signature is fail"),
    TOKEN_ERROR("500003", "token error"),
    ACCESS_DENIED_ERROR("300001", "accessDenied error"),
    TOKEN_IS_NULL("500004", "token is null");

    private String appCode;
    private String message;

    public static APIResponseCode of(String appCode) {
        Optional<APIResponseCode> match = find(appCode);
        return match.orElseThrow(() -> new IllegalArgumentException("No matching app code for [" + appCode + "]"));
    }

    public static Optional<APIResponseCode> find(String appCode) {
        APIResponseCode[] responseCodes = values();
        return Arrays.stream(responseCodes)
                .filter(r -> r.getAppCode().equals(appCode))
                .findFirst();
    }
}

package ms.cb.starter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonPropertyOrder({"response_status", "hint"})
@Data
@Builder
@AllArgsConstructor
public final class ApiMetaStatusResponse {

    @JsonProperty("response_status")
    private String status;

    @JsonProperty("response_hint")
    private String hint;

    public ApiMetaStatusResponse(APIResponseCode apiResponseCode) {
        setStatus(apiResponseCode.getAppCode());
        setHint(apiResponseCode.getMessage());
    }

    public ApiMetaStatusResponse(APIResponseCode apiResponseCode,String hint) {
        setStatus(apiResponseCode.getAppCode());
        setHint(hint);
    }
}

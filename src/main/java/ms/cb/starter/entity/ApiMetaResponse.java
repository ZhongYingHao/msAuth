package ms.cb.starter.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"_meta"})
public class ApiMetaResponse {

    private ApiMetaStatusResponse meta;

    public ApiMetaResponse(ApiMetaStatusResponse meta) {
        this.meta = meta;
    }

    @JsonProperty("_meta")
    public final ApiMetaStatusResponse getMeta() {
        return meta;
    }

    public final void setMeta(ApiMetaStatusResponse meta) {
        this.meta = meta;
    }

}

package ms.cb.starter.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> extends ApiMetaResponse implements Serializable {
    @JsonProperty("pagination")
    private Pagination pagination;

    @JsonProperty("data")
    private final T object;

    public ApiResponse(ApiMetaStatusResponse meta, T object) {
        super(meta);
        this.object = object;
    }

    public ApiResponse(ApiMetaStatusResponse meta, Pagination pagination, T object) {
        super(meta);
        this.pagination = pagination;
        this.object = object;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse meta = new ApiResponse(new ApiMetaStatusResponse(APIResponseCode.DATA_FOUND_SUCCESS), null, data);
        return meta;
    }

    public static <T> ApiResponse<T> success(T data,Pagination pagination) {
        ApiResponse meta = new ApiResponse(new ApiMetaStatusResponse(APIResponseCode.DATA_FOUND_SUCCESS), pagination, data);
        return meta;
    }

    public static <T> ApiResponse<T> success() {
        ApiResponse meta = new ApiResponse(new ApiMetaStatusResponse(APIResponseCode.DATA_FOUND_SUCCESS), null, "");
        return meta;
    }

    public static <T> ApiResponse<T> fail(T data,String msg,Pagination pagination) {
        ApiResponse meta = new ApiResponse(new ApiMetaStatusResponse(APIResponseCode.DATA_FOUND_FAILED,msg), pagination, data);
        return meta;
    }

    public static <T> ApiResponse<T> fail(T data,String msg) {
        ApiResponse meta = new ApiResponse(new ApiMetaStatusResponse(APIResponseCode.DATA_FOUND_FAILED,msg), null, data);
        return meta;
    }

    public static <T> ApiResponse<T> fail(T data) {
        ApiResponse meta = new ApiResponse(new ApiMetaStatusResponse(APIResponseCode.DATA_FOUND_FAILED), null, data);
        return meta;
    }

    public static <T> ApiResponse<T> fail() {
        ApiResponse meta = new ApiResponse(new ApiMetaStatusResponse(APIResponseCode.DATA_FOUND_FAILED), null, "");
        return meta;
    }

    public static <T> ApiResponse<T> fail(APIResponseCode responseCode,String msg) {
        ApiResponse meta = new ApiResponse(new ApiMetaStatusResponse(responseCode,msg), null, "");
        return meta;
    }
}

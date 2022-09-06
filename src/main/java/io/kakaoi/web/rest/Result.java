package io.kakaoi.web.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.kakaoi.config.Constants;
import io.kakaoi.service.dto.PagingDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * 요청 한 것을 처리 후 데이터를 포함하여 응답하거나 오류를 응답하기 위한 Wrapper 클래스.
 * 상태 코드와 상태 메시지, 응답 데이터로 구성.
 * 응답 데이터는 오류 메시지이거나 내용이 없다면 null 일 수 있다.
 * <br>
 * <code>Result success = Result.ok(data);</code>
 * <br>
 * <code>Result fail = Result.error();</code>
 * <br>
 * 위와 같이 정적 생성 메소드를 통해 생성 할 수 있다.
 * 이전과 같이 생성자와 Setter 메서드로도 사용이 가능하지만 추천하진 않는다.
 * <br>
 * 응답 메시지는 아래와 같은 JSON 형태로 맵핑하여 출력한다.
 * <pre>
 * {
 *     "code": "0000",
 *     "message": "OK",
 *     "status": 200,
 *     "data": { // 여기는 실제 데이터가 들어가고 값, 객체, 배열, null 중 하나이다.
 *         "innerField": "innerData"
 *     }
 * }
 * </pre>
 */
@Schema(description = "요청한 결과를 응답할 때 상태코드와 데이터를 같이 보내기 위한 모델")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Constants.CommonCode commonCode;

    /** Code */
    @Schema(description = "응답 상태 코드", example = "0200")
    protected String code;

    /** Message */
    @Schema(description = "응답 메시지", example = "OK")
    protected String message;

    /** Status */
    @Schema(description = "HTTP Status", example = "200")
    protected int status;

    /** Data */
    @Schema(description = "응답 데이터 (nullable)")
    protected T data;

    /**
     * 매개변수가 없는 기본 생성자. (생성자 사용을 비추천, 정적 생성 메서드를 통해서 생성을 추천.)
     */
    public Result() { }

    /**
     * 요청 처리 응답 시 성공 메시지를 보내기 위한 Result 객체 정적 생성 메서드
     * Accepted 메소드는 비동기 처리 시 요청을 보냈단 응답을 보낸다.
     * @param data 응답 데이터
     * @return 'CommonCode.ACCEPTED'와 응답 데이터가 포함하여 생성된 Result 객체
     */
    public static <T> Result<T> accepted(@Nullable T data) {
        return of(Constants.CommonCode.ACCEPTED, data);
    }

    /**
     * 요청 처리 응답 시 성공 메시지를 보내기 위한 Result 객체 정적 생성 메서드
     * @return 'CommonCode.OK' 상태로 생성된 Result 객체
     */
    public static <T> Result<T> ok() {
        return of(Constants.CommonCode.OK, null);
    }

    /**
     * 요청 처리 응답 시 성공 메시지를 보내기 위한 Result 객체 정적 생성 메서드
     * @param data 응답 데이터
     * @return 'CommonCode.OK'와 응답 데이터가 포함하여 생성된 Result 객체
     */
    public static <T> Result<T> ok(@Nullable T data) {
        return of(Constants.CommonCode.OK, data);
    }

    /**
     * 요청 처리 응답 시 성공 메시지를 보내기 위한 Result 객체 정적 생성 메서드
     * @param data 응답 데이터
     * @return 'CommonCode.OK'와 응답 데이터가 포함하여 생성된 Result 객체
     */
    public static <T> Result<PagingDTO<T>> okWithPaging(Page<T> data) {
        return of(Constants.CommonCode.OK, new PagingDTO<>(data));
    }

    /**
     * 요청 처리 응답 시 500 에러를 반환하는 Result 객체
     * @return 'CommonCode.INTERNAL_SERVER_ERROR'가 포함하여 생성된 Result 객체 (data = null)
     */
    public static Result<?> error() {
        return of(Constants.CommonCode.INTERNAL_SERVER_ERROR);
    }

    /**
     * 요청 처리 응답 시 CommonCode를 필요한 것으로 커스텀하여 생성한 Result 객체 정적 생성 메서드
     * @param commonCode Constants.CommonCode Enum (응답 상태)
     * @return 매개변수에 포함하여 생성된 Result 객체 (data = null)
     */
    public static Result<?> of(Constants.CommonCode commonCode) {
        return of(commonCode, null);
    }

    /**
     * 요청 처리 응답 시 CommonCode와 data를 필요한 것으로 커스텀하여 생성한 Result 객체 정적 생성 메서드
     * @param commonCode Constants.CommonCode Enum (응답 상태)
     * @param data 응답 데이터
     * @return 매개변수에 포함하여 생성된 Result 객체
     */
    public static <T> Result<T> of(Constants.CommonCode commonCode, @Nullable T data) {
        return withMessage(commonCode, commonCode.getMessage(), data);
    }

    /**
     * 커스텀 메시지를 포함한 Result 객체 정적 생성 메서드
     * @param commonCode Constants.CommonCode Enum (응답 상태)
     * @param message 응답 메시지
     * @return 생성한 Result 객체
     */
    public static Result<?> withMessage(Constants.CommonCode commonCode, String message) {
        return withMessage(commonCode, message, null);
    }

    /**
     * 커스텀 메시지를 포함한 Result 객체 정적 생성 메서드
     * @param commonCode Constants.CommonCode Enum (응답 상태)
     * @param message 응답 메시지
     * @param data 응답 데이터
     * @param <T> Data Type
     * @return 생성한 Result 객체
     */
    public static <T> Result<T> withMessage(Constants.CommonCode commonCode, String message, @Nullable T data) {
        Result<T> result = new Result<>();
        result.commonCode = commonCode;
        result.code = commonCode.getCode();
        result.message = message;
        result.data = data;
        result.status = commonCode.getDefaultHttpStatus().value();
        return result;
    }

    /**
     * 문자열 형태의 응답 상태 코드를 가져온다.
     * @return 문자열 형태의 응답 상태 코드
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 응답 상태 메시지를 가져온다.
     * @return 응답 상태 메시지
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * HTTP Status 코드를 정수로 가져온다.
     * @return HTTP Status Code
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 응답 데이터를 가져온다. (null 일 수 있음)
     * @return 응답 데이터 (null 일 수 있음)
     */
    @Nullable
    public T getData() {
        return data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
            "commonCode=" + commonCode +
            ", code='" + code + '\'' +
            ", message='" + message + '\'' +
            ", data=" + data +
            '}';
    }
}

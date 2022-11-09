package net.jlxxw.robot.filter.core.vo.base;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

/**
 * base result
 *
 * @author chunyang.leng
 * @date 2022-03-24 3:27 PM
 */
@Schema(description = "base request result")
public class RequestResult<T> extends PageRequestResult implements Serializable {

    /**
     * success
     */
    @Schema(description = "is success")
    private boolean success;
    /**
     * message
     */
    @Schema(description = "failed message")
    private String message;
    /**
     * data value
     */
    @Schema(description = "data value")
    private T data;

    public RequestResult() {
    }

    /**
     * Construction
     *
     * @param resultCodeInterface interface
     */
    public RequestResult(ResultCodeInterface resultCodeInterface) {
        this.success = resultCodeInterface.success();
        this.message = resultCodeInterface.message();
    }

    /**
     * Construction
     *
     * @param resultCode interface
     * @param data       data
     */
    public RequestResult(ResultCodeInterface resultCode, T data) {
        this.success = resultCode.success();
        this.message = resultCode.message();
        this.data = data;
    }

    /**
     * Construction
     *
     * @param success success
     * @param message message
     */
    public RequestResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Construction
     *
     * @param success success
     * @param message message
     * @param data    data
     */
    public RequestResult(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * Construction
     *
     * @param success     success
     * @param message     message
     * @param data        data
     * @param total       total
     * @param currentPage currentPage
     */
    public RequestResult(boolean success, String message, T data, Long total, Long currentPage) {
        this.success = success;
        this.message = message;
        this.data = data;
        setTotal(total);
        setCurrentPageNum(currentPage);
    }

    /**
     * successfully
     *
     * @param <T> convert class
     * @return response data
     */
    public static <T> RequestResult<T> success() {
        return new RequestResult<>(ResultCode.SUCCESS);
    }

    /**
     * successfully
     *
     * @param data data
     * @param <T>  convert class
     * @return response data
     */
    public static <T> RequestResult<T> success(T data) {
        return new RequestResult<>(ResultCode.SUCCESS, data);
    }

    /**
     * successfully
     *
     * @param data    data
     * @param message message
     * @param <T>     convert class
     * @return response
     */
    public static <T> RequestResult<T> success(String message, T data) {
        return new RequestResult<>(ResultCode.SUCCESS.success(), message, data);
    }

    /**
     * failed
     *
     * @param resultCode interface
     * @param <T>        convert class
     * @return response
     */
    public static <T> RequestResult<T> failure(ResultCodeInterface resultCode) {
        return new RequestResult<>(resultCode.success(), resultCode.message());
    }

    /**
     * failed
     *
     * @param resultCode interface
     * @param data       data
     * @param <T>        convert class
     * @return response
     */
    public static <T> RequestResult<T> failure(ResultCodeInterface resultCode, T data) {
        return new RequestResult<>(resultCode.success(), resultCode.message(), data);
    }

    /**
     * failed
     *
     * @param message message
     * @param <T>     convert class
     * @return response
     */
    public static <T> RequestResult<T> failure(String message) {
        return new RequestResult<>(false, message);
    }

    /**
     * failed
     *
     * @param data    data
     * @param message message
     * @param <T>     convert class
     * @return 应答结果
     */
    public static <T> RequestResult<T> failure(String message, T data) {
        return new RequestResult<>(false, message, data);
    }

}

package net.jlxxw.robot.filter.web.advice;

import net.jlxxw.robot.filter.core.vo.base.RequestResult;
import net.jlxxw.robot.filter.core.vo.base.ResultCode;
import net.jlxxw.robot.filter.web.exception.ParamCheckException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chunyang.leng
 * @date 2022-11-09 5:59 PM
 */
@RestControllerAdvice
public class ParamCheckExceptionAdvice {


    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParamCheckException.class)
    public RequestResult<Object> paramExceptionHandler(ParamCheckException paramCheckException){
        return RequestResult.failure(paramCheckException.getMessage());
    }


}

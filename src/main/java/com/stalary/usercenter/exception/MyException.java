package com.stalary.usercenter.exception;


import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.data.Constant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Stalary
 * @description
 * @date 2018/01/04
 */
@Slf4j
public class MyException extends RuntimeException {

    @Getter
    private Integer code;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public MyException(ResultEnum resultEnum, Long id) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        log.warn(Constant.USER_LOG + Constant.SPLIT + Constant.USER + Constant.SPLIT + id + Constant.SPLIT + resultEnum.getMsg());
    }

}

package com.stalary.usercenter.exception;


import com.stalary.usercenter.data.ResultEnum;
import com.stalary.usercenter.utils.UCUtil;
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
        log.warn("user_log" + UCUtil.SPLIT + UCUtil.USER + UCUtil.SPLIT + id + UCUtil.SPLIT + resultEnum.getMsg());
    }

}

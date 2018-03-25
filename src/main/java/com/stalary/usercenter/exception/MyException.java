package com.stalary.usercenter.exception;


import com.stalary.usercenter.data.ResultEnum;
import lombok.Getter;

/**
 * @author Stalary
 * @description
 * @date 2018/01/04
 */
public class MyException extends RuntimeException {

    @Getter
    private Integer code;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

}

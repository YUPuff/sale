package com.example.sale.common;

/**
 * @ProjectName: project1
 * @Author: lyl
 * @Description:
 * @Date: 2023-02-07 15:13
 **/

public class BusinessException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    protected final String message;

    public BusinessException(String message)
    {
        this.message = message;
    }

    public BusinessException(String message, Throwable e)
    {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}

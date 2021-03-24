package com.aiolos.library.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Aiolos
 * @date 2021/3/19 6:24 上午
 */
@Data
@ApiModel(value = "用户注册登录对象")
public class RegisterLoginBO {

    @ApiModelProperty(value = "用于注册的手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[0-9]{11}$", message = "请输入合法的11位手机号")
    public String phone;

    @ApiModelProperty(value = "该手机号接收到的验证码", required = true)
    @NotBlank(message = "短信验证码不能为空")
    public String code;
}

package com.aiolos.library.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Aiolos
 * @date 2021/3/19 6:24 上午
 */
@Data
public class RegisterLoginBO {

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[0-9]{11}$", message = "请输入合法的11位手机号")
    public String phone;

    @NotBlank(message = "短信验证码不能为空")
    public String code;
}

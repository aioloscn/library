package com.aiolos.library.controller.user;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.config.MyServiceList;
import com.aiolos.library.pojo.bo.RegisterLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/19 4:41 下午
 */
@Api(value = "用户注册登录")
@FeignClient(value = MyServiceList.LIBRARY_USER)    // TODO 先不做降级处理
@RequestMapping("/user")
public interface UserControllerApi {

    @ApiOperation(value = "获取短信验证码", httpMethod = "POST")
    @PostMapping("/getSMSCode")
    CommonResponse getSMSCode(@RequestParam String phone, HttpServletRequest request);

    @ApiOperation(value = "一键注册登录接口", httpMethod = "POST")
    @PostMapping("/login")
    CommonResponse login(@Valid @RequestBody RegisterLoginBO registerLoginBO) throws CustomizeException;

    @ApiOperation(value = "查询个人基本信息", httpMethod = "GET")
    @GetMapping("/{id}")
    CommonResponse getById(@PathVariable("id") String id);

    @ApiOperation(value = "登出", httpMethod = "GET")
    @GetMapping("/logout")
    CommonResponse logout(@RequestHeader("token") String token);

    @ApiOperation(value = "根据用户主键集合查询相应的数据", httpMethod = "POST")
    @PostMapping("/searchBatchIds")
    CommonResponse searchBatchIds(@RequestBody List<String> ids);
}

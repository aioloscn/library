package com.aiolos.library.controller.book;

import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.config.MyServiceList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 5:51 上午
 */
@Api(value = "书籍信息相关接口")
@FeignClient(value = MyServiceList.LIBRARY_BOOK)
@RequestMapping("/book")
public interface BookControllerApi {

    @ApiOperation(value = "根据书籍主键查询书籍信息", httpMethod = "GET")
    @GetMapping("/{id}")
    CommonResponse getById(@PathVariable("id") String id);

    @ApiOperation(value = "根据书籍主键集合查询相应的数据", httpMethod = "POST")
    @PostMapping("/searchBatchIds")
    CommonResponse searchBatchIds(@RequestBody List<String> ids);
}


package com.aiolos.library.controller.book;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.common.utils.PagedResult;
import com.aiolos.library.config.MyServiceList;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.bo.BookInsertBO;
import com.aiolos.library.pojo.bo.BookUpdateBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 5:51 上午
 */
@Api(tags = "书籍信息相关接口")
@FeignClient(value = MyServiceList.LIBRARY_BOOK)
@RequestMapping("/book")
public interface BookControllerApi {

    @ApiOperation(value = "添加书籍", httpMethod = "POST")
    @PostMapping("/addBooks")
    CommonResponse addBooks(@ApiParam(value = "要新增的书籍集合") @Valid @RequestBody List<BookInsertBO> bookInsertBOs) throws CustomizeException;

    @ApiOperation(value = "分页获取全部书籍", httpMethod = "GET")
    @GetMapping("/getAllBooks")
    PagedResult getAllBooks(@ApiParam(value = "关键字") @RequestParam(required = false) String keyword,
                            @ApiParam(value = "书籍类型") @RequestParam(required = false) Integer category,
                            @ApiParam(value = "状态") @RequestParam(required = false) Integer status,
                            @ApiParam(value = "查询第几页") @RequestParam(required = false) Integer page,
                            @ApiParam(value = "每页显示的条数") @RequestParam(required = false) Integer pageSize,
                            @ApiParam(value = "排序") @RequestParam(required = false, defaultValue = "1") Integer sort);

    @ApiOperation(value = "分页获取全部书籍", httpMethod = "GET")
    @GetMapping("/list")
    CommonResponse list(@ApiParam(value = "关键字") @RequestParam(required = false) String keyword,
                               @ApiParam(value = "书籍类型") @RequestParam(required = false) Integer category,
                               @ApiParam(value = "状态") @RequestParam(required = false) Integer status,
                               @ApiParam(value = "查询第几页") @RequestParam(required = false) Integer page,
                               @ApiParam(value = "每页显示的条数") @RequestParam(required = false) Integer pageSize,
                               @ApiParam(value = "排序") @RequestParam(required = false, defaultValue = "1") Integer sort);

    @ApiOperation(value = "根据书籍主键查询书籍信息", httpMethod = "GET")
    @GetMapping("/get/{id}")
    CommonResponse getById(@ApiParam(value = "书籍主键", required = true) @PathVariable("id") Long id);

    @ApiOperation(value = "根据书籍主键集合查询书籍信息，用于其他模块远程调用", httpMethod = "GET")
    @GetMapping("/getBatchIds")
    List<Book> getBatchIds(@RequestBody List<Long> ids);

    @ApiOperation(value = "修改书籍", httpMethod = "PUT")
    @PutMapping("/updateBooks")
    CommonResponse updateBooks(@ApiParam(value = "要修改的书籍集合") @Valid @RequestBody List<BookUpdateBO> bookUpdateBOs) throws CustomizeException;

    @ApiOperation(value = "删除书籍", httpMethod = "DELETE")
    @DeleteMapping("/deleteBooks")
    CommonResponse deleteBooks(@ApiParam(value = "要删除的书籍主键集合") @RequestBody List<Long> ids) throws CustomizeException;

    @ApiOperation(value = "获取同类推荐书籍", httpMethod = "GET")
    @GetMapping("/getSimilarRecommended/{classification}")
    CommonResponse getSimilarRecommended(@ApiParam(value = "书籍分类", required = true) @PathVariable("classification") Integer classification);
}


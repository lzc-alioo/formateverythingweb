package com.alioo.format.web.controller.rest.test;

import com.alioo.format.domain.base.XxxDto;
import com.alioo.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: liudi
 * Date: 2018/7/12
 * Time: 下午5:25
 */
@Api(tags = {"rest测试"})
@RestController
@RequestMapping(value = "/rest/test")
public class RestTestController {




    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "GET（id=0时，返回404，其余正常）")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object get(@PathVariable(value = "id") Integer id) {
        if (id == 0) {
            throw new ResourceNotFoundException();
        }
        XxxDto xxxDto = new  XxxDto();
        return xxxDto;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ApiOperation(value = "POST 创建时用（name为空时，返回400，其余正常）")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void post(@RequestBody  XxxDto city) {
//        if (city.getName() == null || city.getName().length() < 1) {
//            throw new BadRequestException("name");
//        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "PUT 修改时用（name为空时，返回400，id=0时，返回404，其余正常）")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void put(@PathVariable(value = "id") Integer id, @RequestBody XxxDto xxxDto) {
        if (id == 0) {
            throw new ResourceNotFoundException();
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "DELETE （id=0时，返回404，其余正常）")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(value = "id") Integer id) {
        if (id == 0) {
            throw new ResourceNotFoundException();
        }
    }

}

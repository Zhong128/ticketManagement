package org.example.ticketmanagement.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.CityVO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cities")
@Tag(name = "公共查询接口/城市", description = "城市公共查询接口")
public class PublicCityController {

    @Autowired
    private CityService cityService;

    /**
     * 获取所有城市列表（公开接口）
     * GET /api/cities
     * 可选参数：status (0-禁用，1-启用)
     * 可选参数：hotLevel (0-普通，1-热门)
     */
    @Operation(summary = "获取城市列表", tags = {"公共查询接口/城市"})
    @GetMapping
    public Result<List<CityVO>> getAllCities(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer hotLevel) {

        List<CityVO> cities;

        if (status != null && hotLevel != null) {
            log.debug("查询城市列表：status={}, hotLevel={}", status, hotLevel);
            cities = cityService.getCitiesByStatus(status);
        } else if (status != null) {
            log.debug("根据状态查询城市列表，status: {}", status);
            cities = cityService.getCitiesByStatus(status);
        } else if (hotLevel != null) {
            log.debug("根据热门等级查询城市列表，hotLevel: {}", hotLevel);
            cities = cityService.getCitiesByHotLevel(hotLevel);
        } else {
            log.debug("查询所有城市列表");
            cities = cityService.getAllCities();
        }

        return Result.success("查询成功", cities);
    }

    /**
     * 根据名称查询城市（模糊查询）- 公开接口
     * GET /api/cities/search/name/{name}
     */
    @Operation(summary = "根据名称模糊查询城市", tags = {"公共查询接口/城市"})
    @GetMapping("/search/name/{name}")
    public Result<List<CityVO>> searchCitiesByName(@PathVariable String name) {
        log.debug("根据名称模糊查询城市: {}", name);

        //TODO 需要先扩展Service方法，这里暂时返回所有城市
        // 实际项目中需要实现模糊查询逻辑

        List<CityVO> cities = cityService.getAllCities();
        return Result.success("查询成功", cities);
    }
}

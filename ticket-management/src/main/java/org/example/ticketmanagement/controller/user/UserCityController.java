package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.CityVO;
import org.example.ticketmanagement.pojo.PageResult;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/cities")
@Tag(name = "客户端/城市", description = "城市查询相关接口")
public class UserCityController {

    @Autowired
    private CityService cityService;

    /**
     * 根据ID获取城市详情（用户端）
     * GET /api/user/cities/{id}
     */
    @Operation(summary = "获取城市详情", tags = {"客户端/城市"})
    @GetMapping("/{id}")
    public Result<CityVO> getCityById(@PathVariable Long id) {
        log.debug("查询城市详情，ID: {}", id);

        CityVO city = cityService.getCityById(id);
        if (city != null) {
            return Result.success("查询成功", city);
        } else {
            return Result.error("城市不存在");
        }
    }

    /**
     * 分页查询城市列表（用户端）
     * GET /api/user/cities/page
     */
    @Operation(summary = "分页查询城市列表", tags = {"客户端/城市"})
    @GetMapping("/page")
    public Result<PageResult<CityVO>> getCitiesByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer hotLevel) {

        log.debug("分页查询城市列表，page: {}, size: {}, status: {}, hotLevel: {}",
                page, size, status, hotLevel);

        // 构建查询参数
        org.example.ticketmanagement.dto.CityQueryDTO queryDTO = new org.example.ticketmanagement.dto.CityQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        queryDTO.setStatus(status);
        queryDTO.setHotLevel(hotLevel);

        // 调用统一的分页查询方法
        PageResult<CityVO> result = cityService.queryCities(queryDTO);
        return Result.success("查询成功", result);
    }

    /**
     * 根据状态查询城市列表（用户端）
     * GET /api/user/cities/status/{status}
     */
    @Operation(summary = "根据状态查询城市列表", tags = {"客户端/城市"})
    @GetMapping("/status/{status}")
    public Result<List<CityVO>> getCitiesByStatus(@PathVariable Integer status) {
        log.debug("根据状态查询城市列表，status: {}", status);

        List<CityVO> cities = cityService.getCitiesByStatus(status);
        return Result.success("查询成功", cities);
    }

    /**
     * 根据热门等级查询城市列表（用户端）
     * GET /api/user/cities/hot/{hotLevel}
     */
    @Operation(summary = "根据热门等级查询城市列表", tags = {"客户端/城市"})
    @GetMapping("/hot/{hotLevel}")
    public Result<List<CityVO>> getCitiesByHotLevel(@PathVariable Integer hotLevel) {
        log.debug("根据热门等级查询城市列表，hotLevel: {}", hotLevel);

        List<CityVO> cities = cityService.getCitiesByHotLevel(hotLevel);
        return Result.success("查询成功", cities);
    }
}

package org.example.ticketmanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.CityDTO;
import org.example.ticketmanagement.dto.CityQueryDTO;
import org.example.ticketmanagement.vo.CityVO;
import org.example.ticketmanagement.pojo.PageResult;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/cities")
@Validated
@Tag(name = "管理端/城市管理", description = "城市管理相关接口")
public class AdminCityController {

    @Autowired
    private CityService cityService;

    /**
     * 1. 新增城市
     * POST /api/admin/cities
     */
    @Operation(summary = "新增城市", tags = {"管理端/城市管理"})
    @PostMapping
    public Result<Void> addCity(@Valid @RequestBody CityDTO cityDTO) {
        log.info("收到新增城市请求: {}", cityDTO);

        boolean success = cityService.addCity(cityDTO);
        if (success) {
            return Result.success("城市添加成功");
        } else {
            return Result.error("城市添加失败，可能名称或代码已存在");
        }
    }

    /**
     * 2. 根据ID删除城市
     * DELETE /api/admin/cities/{id}
     */
    @Operation(summary = "删除城市", tags = {"管理端/城市管理"})
    @DeleteMapping("/{id}")
    public Result<Void> deleteCity(@PathVariable Long id) {
        log.info("收到删除城市请求，ID: {}", id);

        boolean success = cityService.deleteCity(id);
        if (success) {
            return Result.success("城市删除成功");
        } else {
            return Result.error("城市删除失败，可能城市不存在");
        }
    }

    /**
     * 3. 更新城市信息
     * PUT /api/admin/cities/{id}
     */
    @Operation(summary = "更新城市信息", tags = {"管理端/城市管理"})
    @PutMapping("/{id}")
    public Result<Void> updateCity(@PathVariable Long id,
                                   @Valid @RequestBody CityDTO cityDTO) {
        log.info("收到更新城市请求，ID: {}", id);

        boolean success = cityService.updateCity(id, cityDTO);
        if (success) {
            return Result.success("城市更新成功");
        } else {
            return Result.error("城市更新失败，可能城市不存在或名称/代码重复");
        }
    }

    /**
     * 4. 根据ID获取城市详情（管理端，可能包含更多字段）
     * GET /api/admin/cities/{id}
     */
    @Operation(summary = "查询城市详情（管理端）", tags = {"管理端/城市管理"})
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
     * 分页查询城市列表（支持多条件筛选）
     * GET /api/admin/cities/query
     */
    @Operation(summary = "分页查询城市列表（多条件筛选）", tags = {"管理端/城市管理"})
    @GetMapping("/query")
    public Result<PageResult<CityVO>> queryCities(@Valid CityQueryDTO queryDTO) {
        log.info("分页查询城市列表，参数: {}", queryDTO);

        try {
            PageResult<CityVO> result = cityService.queryCities(queryDTO);
            return Result.success("查询成功", result);
        } catch (Exception e) {
            log.error("分页查询城市列表失败", e);
            return Result.error("查询失败，请检查参数");
        }
    }
}

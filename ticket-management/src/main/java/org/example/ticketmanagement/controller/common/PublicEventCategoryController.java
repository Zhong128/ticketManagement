package org.example.ticketmanagement.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.EventCategoryVO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.EventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@Tag(name = "公共查询接口/演出分类", description = "演出分类公共查询接口")
public class PublicEventCategoryController {

    @Autowired
    private EventCategoryService eventCategoryService;

    /**
     * 获取全部分类列表（公共接口）
     * GET /api/categories
     * 可选参数：status (0-禁用，1-启用)
     */
    @Operation(summary = "获取分类列表", tags = {"公共查询接口/演出分类"})
    @GetMapping
    public Result<List<EventCategoryVO>> getAllCategories(
            @RequestParam(required = false) Integer status) {

        List<EventCategoryVO> categories;

        if (status != null) {
            log.debug("根据状态查询分类列表，status: {}", status);
            categories = eventCategoryService.getCategoriesByStatus(status);
        } else {
            log.debug("查询全部分类列表");
            categories = eventCategoryService.getAllCategories();
        }

        return Result.success("查询成功", categories);
    }
}

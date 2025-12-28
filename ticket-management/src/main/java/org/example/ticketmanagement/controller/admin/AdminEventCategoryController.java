package org.example.ticketmanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.EventCategoryDTO;
import org.example.ticketmanagement.dto.EventCategoryQueryDTO;
import org.example.ticketmanagement.vo.EventCategoryVO;
import org.example.ticketmanagement.pojo.PageResult;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.EventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/categories")
@Validated
@Tag(name = "管理端/演出分类管理", description = "演出分类管理相关接口")
public class AdminEventCategoryController {

    @Autowired
    private EventCategoryService eventCategoryService;

    /**
     * 新增演出分类
     * POST /api/admin/categories
     */
    @Operation(summary = "新增演出分类", tags = {"管理端/演出分类管理"})
    @PostMapping
    public Result<Void> addCategory(@Valid @RequestBody EventCategoryDTO eventCategoryDTO) {
        log.info("收到新增分类请求: {}", eventCategoryDTO);

        boolean success = eventCategoryService.addCategory(eventCategoryDTO);
        if (success) {
            return Result.success("分类添加成功");
        } else {
            return Result.error("分类添加失败，请稍后重试");
        }
    }

    /**
     * 根据ID删除分类
     * DELETE /api/admin/categories/{id}
     */
    @Operation(summary = "删除演出分类", tags = {"管理端/演出分类管理"})
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        log.info("收到删除分类请求，ID: {}", id);

        boolean success = eventCategoryService.deleteCategory(id);
        if (success) {
            return Result.success("分类删除成功");
        } else {
            return Result.error("分类删除失败，可能分类不存在");
        }
    }

    /**
     * 更新分类信息
     * PUT /api/admin/categories/{id}
     */
    @Operation(summary = "更新演出分类", tags = {"管理端/演出分类管理"})
    @PutMapping("/{id}")
    public Result<Void> updateCategory(@PathVariable Long id,
                                       @Valid @RequestBody EventCategoryDTO eventCategoryDTO) {
        log.info("收到更新分类请求，ID: {}", id);

        boolean success = eventCategoryService.updateCategory(id, eventCategoryDTO);
        if (success) {
            return Result.success("分类更新成功");
        } else {
            return Result.error("分类更新失败，可能分类不存在");
        }
    }

    /**
     * 根据ID获取分类详情（管理端）
     * GET /api/admin/categories/{id}
     */
    @Operation(summary = "查询分类详情（管理端）", tags = {"管理端/演出分类管理"})
    @GetMapping("/{id}")
    public Result<EventCategoryVO> getCategoryById(@PathVariable Long id) {
        log.debug("查询分类详情，ID: {}", id);

        EventCategoryVO category = eventCategoryService.getCategoryById(id);
        if (category != null) {
            return Result.success("查询成功", category);
        } else {
            return Result.error("分类不存在");
        }
    }

    /**
     * 分页查询分类列表（支持多条件筛选）
     * GET /api/admin/categories/query
     */
    @Operation(summary = "分页查询分类列表（多条件筛选）", tags = {"管理端/演出分类管理"})
    @GetMapping("/query")
    public Result<PageResult<EventCategoryVO>> queryCategories(@Valid EventCategoryQueryDTO queryDTO) {
        log.info("分页查询分类列表，参数: {}", queryDTO);

        try {
            PageResult<EventCategoryVO> result = eventCategoryService.queryCategories(queryDTO);
            return Result.success("查询成功", result);
        } catch (Exception e) {
            log.error("分页查询分类列表失败", e);
            return Result.error("查询失败，请检查参数");
        }
    }
}

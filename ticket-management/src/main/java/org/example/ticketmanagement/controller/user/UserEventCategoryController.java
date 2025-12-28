package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.EventCategoryVO;
import org.example.ticketmanagement.pojo.PageResult;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.EventCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/categories")
@Tag(name = "客户端/演出分类", description = "演出分类查询相关接口")
public class UserEventCategoryController {

    @Autowired
    private EventCategoryService eventCategoryService;

    /**
     * 根据ID获取分类详情（用户端）
     * GET /api/user/categories/{id}
     */
    @Operation(summary = "获取分类详情", tags = {"客户端/演出分类"})
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
     * 分页查询分类列表（用户端）
     * GET /api/user/categories/page
     */
    @Operation(summary = "分页查询分类列表", tags = {"客户端/演出分类"})
    @GetMapping("/page")
    public Result<PageResult<EventCategoryVO>> getCategoriesByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {

        log.debug("简单分页查询分类，page: {}, size: {}, status: {}", page, size, status);

        // 构建查询参数
        org.example.ticketmanagement.dto.EventCategoryQueryDTO queryDTO = new org.example.ticketmanagement.dto.EventCategoryQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        queryDTO.setStatus(status);

        // 调用统一的分页查询方法
        PageResult<EventCategoryVO> result = eventCategoryService.queryCategories(queryDTO);
        return Result.success("查询成功", result);
    }

    /**
     * 根据状态获取分类列表（用户端）
     * GET /api/user/categories/status/{status}
     */
    @Operation(summary = "根据状态获取分类列表", tags = {"客户端/演出分类"})
    @GetMapping("/status/{status}")
    public Result<List<EventCategoryVO>> getCategoriesByStatus(@PathVariable Integer status) {
        log.debug("根据状态查询分类列表，status: {}", status);

        List<EventCategoryVO> categories = eventCategoryService.getCategoriesByStatus(status);
        return Result.success("查询成功", categories);
    }
}

package org.example.ticketmanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.EventDTO;
import org.example.ticketmanagement.dto.EventQueryDTO;
import org.example.ticketmanagement.vo.EventVO;
import org.example.ticketmanagement.pojo.PageResult;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/events")
@Validated
@Tag(name = "管理端/演出管理", description = "演出管理相关接口")
public class AdminEventController {

    @Autowired
    private EventService eventService;

    /**
     * 1. 新增演出
     * POST /api/admin/events
     */
    @Operation(summary = "新增演出", tags = {"管理端/演出管理"})
    @PostMapping
    public Result<Void> addEvent(@Valid @RequestBody EventDTO eventDTO) {
        log.info("收到新增演出请求: {}", eventDTO.getName());

        boolean success = eventService.addEvent(eventDTO);
        if (success) {
            return Result.success("演出添加成功");
        } else {
            return Result.error("演出添加失败，请检查分类、城市是否存在，或时间逻辑是否正确");
        }
    }

    /**
     * 2. 根据ID删除演出
     * DELETE /api/admin/events/{id}
     */
    @Operation(summary = "删除演出", tags = {"管理端/演出管理"})
    @DeleteMapping("/{id}")
    public Result<Void> deleteEvent(@PathVariable Long id) {
        log.info("收到删除演出请求，ID: {}", id);

        boolean success = eventService.deleteEvent(id);
        if (success) {
            return Result.success("演出删除成功");
        } else {
            return Result.error("演出删除失败，可能演出不存在");
        }
    }

    /**
     * 3. 更新演出信息
     * PUT /api/admin/events/{id}
     */
    @Operation(summary = "更新演出信息", tags = {"管理端/演出管理"})
    @PutMapping("/{id}")
    public Result<Void> updateEvent(@PathVariable Long id,
                                    @Valid @RequestBody EventDTO eventDTO) {
        log.info("收到更新演出请求，ID: {}", id);

        boolean success = eventService.updateEvent(id, eventDTO);
        if (success) {
            return Result.success("演出更新成功");
        } else {
            return Result.error("演出更新失败，可能演出不存在或数据不合法");
        }
    }

    /**
     * 4. 根据ID获取演出详情（管理端）
     * GET /api/admin/events/{id}
     */
    @Operation(summary = "查询演出详情（管理端）", tags = {"管理端/演出管理"})
    @GetMapping("/{id}")
    public Result<EventVO> getEventById(@PathVariable Long id) {
        log.debug("查询演出详情，ID: {}", id);

        EventVO event = eventService.getEventById(id);
        if (event != null) {
            return Result.success("查询成功", event);
        } else {
            return Result.error("演出不存在");
        }
    }

    /**
     * 分页查询演出列表（支持多条件筛选）
     * GET /api/admin/events/query
     */
    @Operation(summary = "分页查询演出列表（多条件筛选）", tags = {"管理端/演出管理"})
    @GetMapping("/query")
    public Result<PageResult<EventVO>> queryEvents(@Valid EventQueryDTO queryDTO) {
        log.info("分页查询演出列表，参数: {}", queryDTO);

        try {
            PageResult<EventVO> result = eventService.queryEvents(queryDTO);
            return Result.success("查询成功", result);
        } catch (Exception e) {
            log.error("分页查询演出列表失败", e);
            return Result.error("查询失败，请检查参数");
        }
    }
}

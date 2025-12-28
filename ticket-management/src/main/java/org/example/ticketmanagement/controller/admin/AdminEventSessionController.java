package org.example.ticketmanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.EventSessionDTO;
import org.example.ticketmanagement.vo.EventSessionVO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.EventSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin/sessions")
@Validated
@Tag(name = "管理端/演出场次管理", description = "演出场次管理相关接口")
public class AdminEventSessionController {

    @Autowired
    private EventSessionService eventSessionService;

    /**
     * 1. 新增场次
     * POST /api/admin/sessions
     */
    @Operation(summary = "新增场次", tags = {"管理端/演出场次管理"})
    @PostMapping
    public Result<Void> addSession(@Valid @RequestBody EventSessionDTO eventSessionDTO) {
        log.info("收到新增场次请求: {}", eventSessionDTO.getSessionName());

        boolean success = eventSessionService.addSession(eventSessionDTO);
        if (success) {
            return Result.success("场次添加成功");
        } else {
            return Result.error("场次添加失败，请检查演出是否存在、时间是否冲突或是否在演出时间范围内");
        }
    }

    /**
     * 2. 根据ID删除场次
     * DELETE /api/admin/sessions/{id}
     */
    @Operation(summary = "删除场次", tags = {"管理端/演出场次管理"})
    @DeleteMapping("/{id}")
    public Result<Void> deleteSession(@PathVariable Long id) {
        log.info("收到删除场次请求，ID: {}", id);

        boolean success = eventSessionService.deleteSession(id);
        if (success) {
            return Result.success("场次删除成功");
        } else {
            return Result.error("场次删除失败，可能场次不存在");
        }
    }

    /**
     * 3. 更新场次信息
     * PUT /api/admin/sessions/{id}
     */
    @Operation(summary = "更新场次信息", tags = {"管理端/演出场次管理"})
    @PutMapping("/{id}")
    public Result<Void> updateSession(@PathVariable Long id,
                                      @Valid @RequestBody EventSessionDTO eventSessionDTO) {
        log.info("收到更新场次请求，ID: {}", id);

        boolean success = eventSessionService.updateSession(id, eventSessionDTO);
        if (success) {
            return Result.success("场次更新成功");
        } else {
            return Result.error("场次更新失败，可能场次不存在或数据不合法");
        }
    }

    /**
     * 4. 根据ID获取场次详情（管理端）
     * GET /api/admin/sessions/{id}
     */
    @Operation(summary = "查询场次详情（管理端）", tags = {"管理端/演出场次管理"})
    @GetMapping("/{id}")
    public Result<EventSessionVO> getSessionById(@PathVariable Long id) {
        log.debug("查询场次详情，ID: {}", id);

        EventSessionVO session = eventSessionService.getSessionById(id);
        if (session != null) {
            return Result.success("查询成功", session);
        } else {
            return Result.error("场次不存在");
        }
    }
}

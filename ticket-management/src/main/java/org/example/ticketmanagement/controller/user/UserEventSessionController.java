package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.EventSessionVO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.EventSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/sessions")
@Tag(name = "客户端/演出场次", description = "演出场次查询相关接口")
public class UserEventSessionController {

    @Autowired
    private EventSessionService eventSessionService;

    /**
     * 根据ID获取场次详情（用户端）
     * GET /api/user/sessions/{id}
     */
    @Operation(summary = "获取场次详情", tags = {"客户端/演出场次"})
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

    /**
     * 根据演出ID获取场次列表（用户端）
     * GET /api/user/sessions/event/{eventId}
     */
    @Operation(summary = "根据演出ID获取场次列表", tags = {"客户端/演出场次"})
    @GetMapping("/event/{eventId}")
    public Result<List<EventSessionVO>> getSessionsByEventId(@PathVariable Long eventId) {
        log.debug("根据演出ID查询场次，eventId: {}", eventId);

        List<EventSessionVO> sessions = eventSessionService.getSessionsByEventId(eventId);
        return Result.success("查询成功", sessions);
    }

    /**
     * 根据状态获取场次列表（用户端）
     * GET /api/user/sessions/status/{status}
     */
    @Operation(summary = "根据状态获取场次列表", tags = {"客户端/演出场次"})
    @GetMapping("/status/{status}")
    public Result<List<EventSessionVO>> getSessionsByStatus(@PathVariable Integer status) {
        log.debug("根据状态查询场次，status: {}", status);

        List<EventSessionVO> sessions = eventSessionService.getSessionsByStatus(status);
        return Result.success("查询成功", sessions);
    }
}

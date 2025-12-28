package org.example.ticketmanagement.controller.common;

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
@RequestMapping("/api/sessions")
@Tag(name = "公共查询接口/演出场次", description = "演出场次公共查询接口")
public class PublicEventSessionController {

    @Autowired
    private EventSessionService eventSessionService;

    /**
     * 获取所有场次列表（公共接口）
     * GET /api/sessions
     * 可选参数：
     * - status: 状态 (0-禁用，1-启用)
     * - eventId: 演出ID
     */
    @Operation(summary = "获取场次列表", tags = {"公共查询接口/演出场次"})
    @GetMapping
    public Result<List<EventSessionVO>> getAllSessions(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long eventId) {

        List<EventSessionVO> sessions;

        if (eventId != null && status != null) {
            log.debug("根据演出ID和状态查询场次，eventId: {}, status: {}", eventId, status);
            sessions = eventSessionService.getSessionsByEventIdAndStatus(eventId, status);
        } else if (eventId != null) {
            log.debug("根据演出ID查询场次，eventId: {}", eventId);
            sessions = eventSessionService.getSessionsByEventId(eventId);
        } else if (status != null) {
            log.debug("根据状态查询场次，status: {}", status);
            sessions = eventSessionService.getSessionsByStatus(status);
        } else {
            log.debug("查询所有场次列表");
            sessions = eventSessionService.getAllSessions();
        }

        return Result.success("查询成功", sessions);
    }

    /**
     * 获取即将开始的场次（公共接口）
     * GET /api/sessions/upcoming
     * 可选参数：limit (默认10条)
     */
    @Operation(summary = "获取即将开始的场次", tags = {"公共查询接口/演出场次"})
    @GetMapping("/upcoming")
    public Result<List<EventSessionVO>> getUpcomingSessions(
            @RequestParam(defaultValue = "10") Integer limit) {
        log.debug("获取即将开始的场次，limit: {}", limit);

        List<EventSessionVO> sessions = eventSessionService.getUpcomingSessions(limit);
        return Result.success("查询成功", sessions);
    }
}

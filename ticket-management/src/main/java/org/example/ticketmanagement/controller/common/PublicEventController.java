package org.example.ticketmanagement.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.EventVO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/events")
@Tag(name = "公共查询接口/演出", description = "演出公共查询接口")
public class PublicEventController {

    @Autowired
    private EventService eventService;

    /**
     * 获取演出列表（公共接口）
     * GET /api/events
     * 可选参数：
     * - status: 演出状态
     * - categoryId: 分类ID
     * - cityId: 城市ID
     * - artistName: 艺人名称（模糊查询）
     * - name: 演出名称（模糊查询）
     */
    @Operation(summary = "获取演出列表", tags = {"公共查询接口/演出"})
    @GetMapping
    public Result<List<EventVO>> getAllEvents(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) String artistName,
            @RequestParam(required = false) String name) {

        List<EventVO> events;

        if (artistName != null) {
            log.debug("根据艺人名称模糊查询演出，artistName: {}", artistName);
            events = eventService.searchEventsByArtistName(artistName);
        } else if (name != null) {
            log.debug("根据演出名称模糊查询演出，name: {}", name);
            events = eventService.searchEventsByName(name);
        } else if (categoryId != null && cityId != null) {
            log.debug("根据分类和城市查询演出，categoryId: {}, cityId: {}", categoryId, cityId);
            events = eventService.getEventsByCategoryAndCity(categoryId, cityId);
        } else if (categoryId != null) {
            log.debug("根据分类查询演出，categoryId: {}", categoryId);
            events = eventService.getEventsByCategoryId(categoryId);
        } else if (cityId != null) {
            log.debug("根据城市查询演出，cityId: {}", cityId);
            events = eventService.getEventsByCityId(cityId);
        } else if (status != null) {
            log.debug("根据状态查询演出，status: {}", status);
            events = eventService.getEventsByStatus(status);
        } else {
            log.debug("查询所有演出列表");
            events = eventService.getAllEvents();
        }

        return Result.success("查询成功", events);
    }

    /**
     * 获取热门演出列表（公共接口）
     * GET /api/events/hot
     * 参数：limit - 返回数量限制，默认10
     */
    @Operation(summary = "获取热门演出列表", tags = {"公共查询接口/演出"})
    @GetMapping("/hot")
    public Result<List<EventVO>> getHotEvents(
            @RequestParam(defaultValue = "10") Integer limit) {
        log.debug("获取热门演出，limit: {}", limit);

        List<EventVO> events = eventService.getHotEvents(limit);
        return Result.success("查询成功", events);
    }

    /**
     * 获取即将开售的演出列表（公共接口）
     * GET /api/events/upcoming
     * 参数：limit - 返回数量限制，默认10
     */
    @Operation(summary = "获取即将开售的演出列表", tags = {"公共查询接口/演出"})
    @GetMapping("/upcoming")
    public Result<List<EventVO>> getUpcomingEvents(
            @RequestParam(defaultValue = "10") Integer limit) {
        log.debug("获取即将开售的演出，limit: {}", limit);

        List<EventVO> events = eventService.getUpcomingEvents(limit);
        return Result.success("查询成功", events);
    }
}

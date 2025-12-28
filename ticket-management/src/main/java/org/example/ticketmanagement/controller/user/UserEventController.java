package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.EventVO;
import org.example.ticketmanagement.pojo.PageResult;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/events")
@Tag(name = "客户端/演出", description = "演出查询相关接口")
public class UserEventController {

    @Autowired
    private EventService eventService;

    /**
     * 根据ID获取演出详情（用户端）
     * GET /api/user/events/{id}
     */
    @Operation(summary = "获取演出详情", tags = {"客户端/演出"})
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
     * 分页查询演出列表（用户端）
     * GET /api/user/events/page
     */
    @Operation(summary = "分页查询演出列表", tags = {"客户端/演出"})
    @GetMapping("/page")
    public Result<PageResult<EventVO>> getEventsByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String artistName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) Integer status) {

        log.debug("分页查询演出列表，page: {}, size: {}, name: {}, artistName: {}, categoryId: {}, cityId: {}, status: {}",
                page, size, name, artistName, categoryId, cityId, status);

        // 构建查询参数
        org.example.ticketmanagement.dto.EventQueryDTO queryDTO = new org.example.ticketmanagement.dto.EventQueryDTO();
        queryDTO.setPage(page);
        queryDTO.setSize(size);
        queryDTO.setName(name);
        queryDTO.setArtistName(artistName);
        queryDTO.setCategoryId(categoryId);
        queryDTO.setCityId(cityId);
        queryDTO.setStatus(status);

        // 调用分页查询方法
        PageResult<EventVO> result = eventService.queryEvents(queryDTO);
        return Result.success("查询成功", result);
    }

    /**
     * 根据分类ID获取演出列表（用户端）
     * GET /api/user/events/category/{categoryId}
     */
    @Operation(summary = "根据分类获取演出列表", tags = {"客户端/演出"})
    @GetMapping("/category/{categoryId}")
    public Result<List<EventVO>> getEventsByCategoryId(@PathVariable Long categoryId) {
        log.debug("根据分类ID查询演出列表，categoryId: {}", categoryId);

        List<EventVO> events = eventService.getEventsByCategoryId(categoryId);
        return Result.success("查询成功", events);
    }

    /**
     * 根据城市ID获取演出列表（用户端）
     * GET /api/user/events/city/{cityId}
     */
    @Operation(summary = "根据城市获取演出列表", tags = {"客户端/演出"})
    @GetMapping("/city/{cityId}")
    public Result<List<EventVO>> getEventsByCityId(@PathVariable Long cityId) {
        log.debug("根据城市ID查询演出列表，cityId: {}", cityId);

        List<EventVO> events = eventService.getEventsByCityId(cityId);
        return Result.success("查询成功", events);
    }

    /**
     * 根据状态获取演出列表（用户端）
     * GET /api/user/events/status/{status}
     */
    @Operation(summary = "根据状态获取演出列表", tags = {"客户端/演出"})
    @GetMapping("/status/{status}")
    public Result<List<EventVO>> getEventsByStatus(@PathVariable Integer status) {
        log.debug("根据状态查询演出列表，status: {}", status);

        List<EventVO> events = eventService.getEventsByStatus(status);
        return Result.success("查询成功", events);
    }
}

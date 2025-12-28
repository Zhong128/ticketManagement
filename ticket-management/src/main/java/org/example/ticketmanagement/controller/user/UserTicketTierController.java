package org.example.ticketmanagement.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.TicketStockVO;
import org.example.ticketmanagement.vo.TicketTierVO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.TicketTierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user/ticket-tiers")
@Tag(name = "客户端/票档", description = "票档查询相关接口")
public class UserTicketTierController {

    @Autowired
    private TicketTierService ticketTierService;

    /**
     * 根据ID查询票档详情（用户端）
     * GET /api/user/ticket-tiers/{id}
     */
    @Operation(summary = "获取票档详情", tags = {"客户端/票档"})
    @GetMapping("/{id}")
    public Result<TicketTierVO> getTicketTierById(@PathVariable Long id) {
        log.debug("查询票档详情，ID: {}", id);

        TicketTierVO ticketTier = ticketTierService.getTicketTierById(id);
        if (ticketTier != null) {
            return Result.success("查询成功", ticketTier);
        } else {
            return Result.error("票档不存在");
        }
    }

    /**
     * 根据演出ID查询票档列表（用户端）
     * GET /api/user/ticket-tiers/event/{eventId}
     */
    @Operation(summary = "根据演出ID查询票档列表", tags = {"客户端/票档"})
    @GetMapping("/event/{eventId}")
    public Result<List<TicketTierVO>> getTicketTiersByEventId(@PathVariable Long eventId) {
        log.debug("根据演出ID查询票档，eventId: {}", eventId);

        List<TicketTierVO> ticketTiers = ticketTierService.getTicketTiersByEventId(eventId);
        return Result.success("查询成功", ticketTiers);
    }

    /**
     * 根据场次ID查询票档列表（用户端）
     * GET /api/user/ticket-tiers/session/{sessionId}
     */
    @Operation(summary = "根据场次ID查询票档列表", tags = {"客户端/票档"})
    @GetMapping("/session/{sessionId}")
    public Result<List<TicketTierVO>> getTicketTiersBySessionId(@PathVariable Long sessionId) {
        log.debug("根据场次ID查询票档，sessionId: {}", sessionId);

        List<TicketTierVO> ticketTiers = ticketTierService.getTicketTiersBySessionId(sessionId);
        return Result.success("查询成功", ticketTiers);
    }

    /**
     * 根据状态查询票档列表（用户端）
     * GET /api/user/ticket-tiers/status/{status}
     */
    @Operation(summary = "根据状态查询票档列表", tags = {"客户端/票档"})
    @GetMapping("/status/{status}")
    public Result<List<TicketTierVO>> getTicketTiersByStatus(@PathVariable Integer status) {
        log.debug("根据状态查询票档，status: {}", status);

        List<TicketTierVO> ticketTiers = ticketTierService.getTicketTiersByStatus(status);
        return Result.success("查询成功", ticketTiers);
    }

    /**
     * 获取票档库存信息（客户端）
     * GET /api/user/ticket-tiers/stock
     * 参数：
     * - eventId: 演出ID
     * - sessionId: 场次ID
     */
    @Operation(summary = "获取票档库存信息", tags = {"客户端/票档"})
    @GetMapping("/stock")
    public Result<TicketStockVO> getTicketStock(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long sessionId) {

        if (sessionId != null) {
            log.debug("获取场次票档库存信息，sessionId: {}", sessionId);
            TicketStockVO stockInfo = ticketTierService.getStockInfoBySessionId(sessionId);
            if (stockInfo != null) {
                return Result.success("查询成功", stockInfo);
            } else {
                return Result.error("场次不存在");
            }
        } else if (eventId != null) {
            log.debug("获取演出票档库存信息，eventId: {}", eventId);
            TicketStockVO stockInfo = ticketTierService.getStockInfoByEventId(eventId);
            if (stockInfo != null) {
                return Result.success("查询成功", stockInfo);
            } else {
                return Result.error("演出不存在");
            }
        } else {
            return Result.error("请提供演出ID或场次ID");
        }
    }

    /**
     * 检查是否已开票（客户端）
     * GET /api/user/ticket-tiers/on-sale
     * 参数：
     * - eventId: 演出ID
     * - sessionId: 场次ID
     */
    @Operation(summary = "检查是否已开票", tags = {"客户端/票档"})
    @GetMapping("/on-sale")
    public Result<Boolean> checkOnSale(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long sessionId) {

        if (sessionId != null) {
            log.debug("检查场次是否已开票，sessionId: {}", sessionId);
            boolean onSale = ticketTierService.isSessionOnSale(sessionId);
            return Result.success("查询成功", onSale);
        } else if (eventId != null) {
            log.debug("检查演出是否已开票，eventId: {}", eventId);
            boolean onSale = ticketTierService.isEventOnSale(eventId);
            return Result.success("查询成功", onSale);
        } else {
            return Result.error("请提供演出ID或场次ID");
        }
    }
}

package org.example.ticketmanagement.controller.common;

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
@RequestMapping("/api/ticket-tiers")
@Tag(name = "公共查询接口/票档", description = "票档公共查询接口")
public class PublicTicketTierController {

    @Autowired
    private TicketTierService ticketTierService;

    /**
     * 获取票档列表（公共接口）
     * GET /api/ticket-tiers
     * 可选参数：
     * - status: 状态 (0-禁用，1-启用)
     * - eventId: 演出ID
     * - sessionId: 场次ID
     * - minPrice: 最低价格
     * - maxPrice: 最高价格
     */
    @Operation(summary = "获取票档列表", tags = {"公共查询接口/票档"})
    @GetMapping
    public Result<List<TicketTierVO>> getAllTicketTiers(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {

        List<TicketTierVO> ticketTiers;

        if (eventId != null && status != null && sessionId != null) {
            // 根据演出ID、场次ID和状态查询
            log.debug("根据演出ID、场次ID和状态查询票档，eventId: {}, sessionId: {}, status: {}", eventId, sessionId, status);
            ticketTiers = ticketTierService.getTicketTiersBySessionIdAndStatus(sessionId, status);
        } else if (eventId != null && status != null) {
            log.debug("根据演出ID和状态查询票档，eventId: {}, status: {}", eventId, status);
            ticketTiers = ticketTierService.getTicketTiersByEventIdAndStatus(eventId, status);
        } else if (sessionId != null && status != null) {
            log.debug("根据场次ID和状态查询票档，sessionId: {}, status: {}", sessionId, status);
            ticketTiers = ticketTierService.getTicketTiersBySessionIdAndStatus(sessionId, status);
        } else if (minPrice != null && maxPrice != null) {
            log.debug("根据价格范围查询票档，minPrice: {}, maxPrice: {}", minPrice, maxPrice);
            // 默认查询启用状态的票档
            Integer queryStatus = (status != null) ? status : 1;
            ticketTiers = ticketTierService.getTicketTiersByPriceRange(minPrice, maxPrice, queryStatus);
        } else if (eventId != null) {
            log.debug("根据演出ID查询票档，eventId: {}", eventId);
            ticketTiers = ticketTierService.getTicketTiersByEventId(eventId);
        } else if (sessionId != null) {
            log.debug("根据场次ID查询票档，sessionId: {}", sessionId);
            ticketTiers = ticketTierService.getTicketTiersBySessionId(sessionId);
        } else if (status != null) {
            log.debug("根据状态查询票档，status: {}", status);
            ticketTiers = ticketTierService.getTicketTiersByStatus(status);
        } else {
            log.debug("查询所有票档列表");
            ticketTiers = ticketTierService.getAllTicketTiers();
        }

        return Result.success("查询成功", ticketTiers);
    }

    /**
     * 根据价格范围查询票档列表（公共接口）
     * GET /api/ticket-tiers/price-range
     * 参数：
     * - minPrice: 最低价格（必填）
     * - maxPrice: 最高价格（必填）
     * - status: 状态 (1-启用，默认1)
     */
    @Operation(summary = "根据价格范围查询票档列表", tags = {"公共查询接口/票档"})
    @GetMapping("/price-range")
    public Result<List<TicketTierVO>> getTicketTiersByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            @RequestParam(defaultValue = "1") Integer status) {
        log.debug("根据价格范围查询票档，minPrice: {}, maxPrice: {}, status: {}", minPrice, maxPrice, status);

        // 参数校验
        if (minPrice.compareTo(BigDecimal.ZERO) < 0 || maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            return Result.error("价格不能为负数");
        }
        if (minPrice.compareTo(maxPrice) > 0) {
            return Result.error("最低价格不能大于最高价格");
        }

        List<TicketTierVO> ticketTiers = ticketTierService.getTicketTiersByPriceRange(minPrice, maxPrice, status);
        return Result.success("查询成功", ticketTiers);
    }
}

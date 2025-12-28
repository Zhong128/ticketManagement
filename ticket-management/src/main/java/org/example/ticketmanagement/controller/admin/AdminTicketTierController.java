package org.example.ticketmanagement.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.dto.TicketTierDTO;
import org.example.ticketmanagement.vo.StockStatisticsVO;
import org.example.ticketmanagement.vo.TicketTierVO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.TicketTierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/ticket-tiers")
@Validated
@Tag(name = "管理端/票档管理", description = "票档管理相关接口")
public class AdminTicketTierController {

    @Autowired
    private TicketTierService ticketTierService;

    /**
     * 1. 新增票档
     * POST /api/admin/ticket-tiers
     */
    @Operation(summary = "新增票档", tags = {"管理端/票档管理"})
    @PostMapping
    public Result<Void> addTicketTier(@Valid @RequestBody TicketTierDTO ticketTierDTO) {
        log.info("收到新增票档请求: {}", ticketTierDTO.getTierName());

        boolean success = ticketTierService.addTicketTier(ticketTierDTO);
        if (success) {
            return Result.success("票档添加成功");
        } else {
            return Result.error("票档添加失败，请检查演出、场次是否存在，或价格/库存逻辑是否合理");
        }
    }

    /**
     * 2. 根据ID删除票档
     * DELETE /api/admin/ticket-tiers/{id}
     */
    @Operation(summary = "删除票档", tags = {"管理端/票档管理"})
    @DeleteMapping("/{id}")
    public Result<Void> deleteTicketTier(@PathVariable Long id) {
        log.info("收到删除票档请求，ID: {}", id);

        boolean success = ticketTierService.deleteTicketTier(id);
        if (success) {
            return Result.success("票档删除成功");
        } else {
            return Result.error("票档删除失败，可能票档不存在");
        }
    }

    /**
     * 3. 更新票档信息
     * PUT /api/admin/ticket-tiers/{id}
     */
    @Operation(summary = "更新票档信息", tags = {"管理端/票档管理"})
    @PutMapping("/{id}")
    public Result<Void> updateTicketTier(@PathVariable Long id,
                                         @Valid @RequestBody TicketTierDTO ticketTierDTO) {
        log.info("收到更新票档请求，ID: {}", id);

        boolean success = ticketTierService.updateTicketTier(id, ticketTierDTO);
        if (success) {
            return Result.success("票档更新成功");
        } else {
            return Result.error("票档更新失败，可能票档不存在或数据不合法");
        }
    }

    /**
     * 4. 根据ID查询票档详情（管理端）
     * GET /api/admin/ticket-tiers/{id}
     */
    @Operation(summary = "查询票档详情（管理端）", tags = {"管理端/票档管理"})
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
     * 根据演出ID查询票档列表（管理端）
     * GET /api/admin/ticket-tiers/event/{eventId}
     */
    @Operation(summary = "根据演出ID查询票档列表", tags = {"管理端/票档管理"})
    @GetMapping("/event/{eventId}")
    public Result<List<TicketTierVO>> getTicketTiersByEventId(@PathVariable Long eventId) {
        log.debug("根据演出ID查询票档，eventId: {}", eventId);

        List<TicketTierVO> ticketTiers = ticketTierService.getTicketTiersByEventId(eventId);
        return Result.success("查询成功", ticketTiers);
    }

    /**
     * 根据场次ID查询票档列表（管理端）
     * GET /api/admin/ticket-tiers/session/{sessionId}
     */
    @Operation(summary = "根据场次ID查询票档列表", tags = {"管理端/票档管理"})
    @GetMapping("/session/{sessionId}")
    public Result<List<TicketTierVO>> getTicketTiersBySessionId(@PathVariable Long sessionId) {
        log.debug("根据场次ID查询票档，sessionId: {}", sessionId);

        List<TicketTierVO> ticketTiers = ticketTierService.getTicketTiersBySessionId(sessionId);
        return Result.success("查询成功", ticketTiers);
    }

    /**
     * 根据演出ID和状态查询票档列表（管理端）
     * GET /api/admin/ticket-tiers/event/{eventId}/status/{status}
     */
    @Operation(summary = "根据演出ID和状态查询票档列表", tags = {"管理端/票档管理"})
    @GetMapping("/event/{eventId}/status/{status}")
    public Result<List<TicketTierVO>> getTicketTiersByEventIdAndStatus(
            @PathVariable Long eventId,
            @PathVariable Integer status) {
        log.debug("根据演出ID和状态查询票档，eventId: {}, status: {}", eventId, status);

        List<TicketTierVO> ticketTiers = ticketTierService.getTicketTiersByEventIdAndStatus(eventId, status);
        return Result.success("查询成功", ticketTiers);
    }

    /**
     * 根据场次ID和状态查询票档列表（管理端）
     * GET /api/admin/ticket-tiers/session/{sessionId}/status/{status}
     */
    @Operation(summary = "根据场次ID和状态查询票档列表", tags = {"管理端/票档管理"})
    @GetMapping("/session/{sessionId}/status/{status}")
    public Result<List<TicketTierVO>> getTicketTiersBySessionIdAndStatus(
            @PathVariable Long sessionId,
            @PathVariable Integer status) {
        log.debug("根据场次ID和状态查询票档，sessionId: {}, status: {}", sessionId, status);

        List<TicketTierVO> ticketTiers = ticketTierService.getTicketTiersBySessionIdAndStatus(sessionId, status);
        return Result.success("查询成功", ticketTiers);
    }


    /**
     * 获取库存统计信息（管理端）
     * GET /api/admin/ticket-tiers/stock-statistics
     * 可选参数：
     * - eventId: 演出ID
     * - sessionId: 场次ID
     * - tierId: 票档ID
     */
    @Operation(summary = "获取库存统计信息", tags = {"管理端/票档管理"})
    @GetMapping("/stock-statistics")
    public Result<StockStatisticsVO> getStockStatistics(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long sessionId,
            @RequestParam(required = false) Long tierId) {
        log.debug("获取库存统计，eventId: {}, sessionId: {}, tierId: {}", eventId, sessionId, tierId);

        StockStatisticsVO statistics = ticketTierService.getStockStatistics(eventId, sessionId, tierId);
        return Result.success("查询成功", statistics);
    }

    /**
     * 获取售罄票档列表（管理端）
     * GET /api/admin/ticket-tiers/sold-out
     * 可选参数：
     * - eventId: 演出ID
     * - sessionId: 场次ID
     */
    @Operation(summary = "获取售罄票档列表", tags = {"管理端/票档管理"})
    @GetMapping("/sold-out")
    public Result<List<StockStatisticsVO.TierStockDetail>> getSoldOutTiers(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long sessionId) {
        log.debug("获取售罄票档列表，eventId: {}, sessionId: {}", eventId, sessionId);

        // 获取统计信息
        StockStatisticsVO statistics = ticketTierService.getStockStatistics(eventId, sessionId, null);

        // 筛选售罄票档（availableStock = 0 且 status = 1）
        List<StockStatisticsVO.TierStockDetail> soldOutTiers = statistics.getTierDetails().stream()
                .filter(tier -> tier.getAvailableStock() == 0 && tier.getStatus() == 1)
                .collect(java.util.stream.Collectors.toList());

        return Result.success("查询成功", soldOutTiers);
    }
}

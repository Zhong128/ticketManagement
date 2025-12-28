// org/example/ticketmanagement/service/TicketTierService.java
package org.example.ticketmanagement.service;

import org.example.ticketmanagement.dto.TicketTierDTO;
import org.example.ticketmanagement.vo.StockStatisticsVO;
import org.example.ticketmanagement.vo.TicketStockVO;
import org.example.ticketmanagement.vo.TicketTierVO;

import java.math.BigDecimal;
import java.util.List;

public interface TicketTierService {

    /**
     * 新增票档
     * @param ticketTierDTO 票档数据
     * @return 新增成功返回 true，失败返回 false
     */
    boolean addTicketTier(TicketTierDTO ticketTierDTO);

    /**
     * 根据ID删除票档
     * @param id 票档ID
     * @return 删除成功返回 true，失败返回 false
     */
    boolean deleteTicketTier(Long id);

    /**
     * 更新票档信息
     * @param id 要更新的票档ID
     * @param ticketTierDTO 新的票档数据
     * @return 更新成功返回 true，失败返回 false
     */
    boolean updateTicketTier(Long id, TicketTierDTO ticketTierDTO);

    /**
     * 根据ID查询票档详情
     * @param id 票档ID
     * @return 票档详情视图对象，未找到返回 null
     */
    TicketTierVO getTicketTierById(Long id);

    /**
     * 查询所有票档列表
     * @return 票档视图对象列表
     */
    List<TicketTierVO> getAllTicketTiers();

    /**
     * 根据状态查询票档列表
     * @param status 状态 (0-禁用，1-启用)
     * @return 符合条件的票档列表
     */
    List<TicketTierVO> getTicketTiersByStatus(Integer status);

    /**
     * 根据演出ID查询票档列表
     * @param eventId 演出ID
     * @return 该演出的所有票档列表
     */
    List<TicketTierVO> getTicketTiersByEventId(Long eventId);

    /**
     * 根据场次ID查询票档列表
     * @param sessionId 场次ID
     * @return 该场次的所有票档列表
     */
    List<TicketTierVO> getTicketTiersBySessionId(Long sessionId);

    /**
     * 根据演出ID和状态查询票档列表
     * @param eventId 演出ID
     * @param status 状态 (0-禁用，1-启用)
     * @return 符合条件的票档列表
     */
    List<TicketTierVO> getTicketTiersByEventIdAndStatus(Long eventId, Integer status);

    /**
     * 根据场次ID和状态查询票档列表
     * @param sessionId 场次ID
     * @param status 状态 (0-禁用，1-启用)
     * @return 符合条件的票档列表
     */
    List<TicketTierVO> getTicketTiersBySessionIdAndStatus(Long sessionId, Integer status);

    /**
     * 根据价格范围查询票档列表
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @param status 状态 (1-启用)
     * @return 符合条件的票档列表
     */
    List<TicketTierVO> getTicketTiersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Integer status);
    /**
     * 获取票档库存信息（用户端）
     * @param sessionId 场次ID
     * @return 库存信息视图对象
     */
    TicketStockVO getStockInfoBySessionId(Long sessionId);

    /**
     * 获取票档库存信息（用户端）- 根据演出ID
     * @param eventId 演出ID
     * @return 库存信息视图对象
     */
    TicketStockVO getStockInfoByEventId(Long eventId);

    /**
     * 检查场次是否已开票
     * @param sessionId 场次ID
     * @return 是否已开票
     */
    boolean isSessionOnSale(Long sessionId);

    /**
     * 检查演出是否有任何票档已开票
     * @param eventId 演出ID
     * @return 是否已开票
     */
    boolean isEventOnSale(Long eventId);

    /**
     * 获取库存统计信息（管理端）
     * @param eventId 演出ID（可选）
     * @param sessionId 场次ID（可选）
     * @param tierId 票档ID（可选）
     * @return 库存统计视图对象
     */
    StockStatisticsVO getStockStatistics(Long eventId, Long sessionId, Long tierId);
}
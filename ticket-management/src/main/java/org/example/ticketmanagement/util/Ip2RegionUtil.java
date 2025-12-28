package org.example.ticketmanagement.util;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ip2region IP定位工具
 * 离线IP地址定位库，99.9%准确率，微秒级查询
 */
@Slf4j
@Component
public class Ip2RegionUtil {

    private Searcher searcher;
    private byte[] vIndex;

    // 缓存一些热门城市，减少数据库查询
    private static final ConcurrentHashMap<String, String> CITY_CACHE = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @PostConstruct
    public void init() {
        try {
            // 方式1：完全基于文件的查询（推荐，内存使用少）
            String dbPath = new ClassPathResource("ipdb/ip2region.xdb").getFile().getPath();
            searcher = Searcher.newWithFileOnly(dbPath);

            // 方式2：VectorIndex索引（更快，但需要额外内存）
            // loadVectorIndex(dbPath);

            log.info("ip2region初始化成功");
        } catch (Exception e) {
            log.error("ip2region初始化失败", e);
        }
    }

    /**
     * 使用VectorIndex索引（速度更快）
     */
    private void loadVectorIndex(String dbPath) throws Exception {
        // 1. 加载VectorIndex索引
        byte[] vIndex = Searcher.loadVectorIndexFromFile(dbPath);
        // 2. 使用全局VectorIndex创建带VectorIndex索引的查询对象
        searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
        this.vIndex = vIndex;
    }

    /**
     * 根据IP地址查询地理位置
     * @param ip IP地址
     * @return 地理位置信息
     */
    public String search(String ip) {
        if (searcher == null) {
            log.warn("ip2region未初始化");
            return null;
        }

        try {
            long startTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = (System.nanoTime() - startTime) / 1000; // 微秒

            log.debug("IP查询: {} -> {}，耗时: {}μs", ip, region, cost);
            return region;
        } catch (Exception e) {
            log.error("IP查询失败: {}", ip, e);
            return null;
        }
    }

    /**
     * 根据IP获取城市名称
     * @param ip IP地址
     * @return 城市名称（如：北京、上海）
     */
    public String getCityName(String ip) {
        // 先检查缓存
        String cacheKey = "city:" + ip;
        String cachedCity = CITY_CACHE.get(cacheKey);
        if (cachedCity != null) {
            return cachedCity;
        }

        String region = search(ip);
        if (region == null) {
            return null;
        }

        // 解析结果格式：国家|区域|省份|城市|ISP
        // 例如：中国|0|上海|上海市|电信
        String[] parts = region.split("\\|");
        if (parts.length >= 4) {
            String city = parts[3];
            // 去除"市"后缀
            if (city.endsWith("市")) {
                city = city.substring(0, city.length() - 1);
            }
            // 处理特殊城市
            if ("北京".equals(city) || "上海市".equals(city) || "天津".equals(city) || "重庆".equals(city)) {
                city = city.replace("市", "");
            }

            // 缓存结果（5分钟）
            CITY_CACHE.put(cacheKey, city);
            scheduler.schedule(() -> CITY_CACHE.remove(cacheKey), 5, TimeUnit.MINUTES);

            return city;
        }

        return null;
    }

    /**
     * 根据IP获取省份名称
     * @param ip IP地址
     * @return 省份名称
     */
    public String getProvinceName(String ip) {
        String region = search(ip);
        if (region == null) {
            return null;
        }

        String[] parts = region.split("\\|");
        if (parts.length >= 3) {
            return parts[2];
        }

        return null;
    }

    @PreDestroy
    public void destroy() {
        if (searcher != null) {
            try {
                searcher.close();
                log.info("ip2region资源已释放");
            } catch (IOException e) {
                log.error("ip2region资源释放失败", e);
            }
        }
        scheduler.shutdown();
    }
}

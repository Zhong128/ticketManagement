package org.example.ticketmanagement.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.ticketmanagement.vo.HomeRecommendVO;
import org.example.ticketmanagement.pojo.Result;
import org.example.ticketmanagement.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/home")
@Tag(name = "公共查询接口/首页", description = "首页公共推荐接口")
public class PublicHomeController {

    @Autowired
    private HomeService homeService;

    /**
     * 获取默认首页推荐（北京地区四大类）
     * GET /api/home/recommend/default
     */
    @Operation(summary = "获取默认首页推荐", tags = {"公共查询接口/首页"})
    @GetMapping("/recommend/default")
    public Result<HomeRecommendVO> getDefaultHomeRecommend(
            @RequestParam(defaultValue = "4") Integer limit) {

        log.info("获取默认首页推荐，limit: {}", limit);

        try {
            HomeRecommendVO result = homeService.getDefaultHomeRecommend(limit);
            return Result.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取默认首页推荐失败", e);
            return Result.error("获取默认首页推荐失败");
        }
    }
}

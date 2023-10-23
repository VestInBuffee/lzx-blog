package com.lzx.job;


import com.lzx.constants.SystemConstants;
import com.lzx.domain.entity.Article;
import com.lzx.service.ArticleService;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

/**
 * @author lzx
 */
@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void updateViewCount(){
        //从redis中获取viewCount
        Map<String, Integer> map = redisCache.getCacheMap(
                SystemConstants.ARTICLE_VIEWCOUNT_REDIS_KEY);
        //转换成article的collection
        List<Article> articles = map.entrySet().stream().
                map(entry -> new Article(Long.parseLong(entry.getKey()) ,
                        entry.getValue().longValue())).
                collect(Collectors.toList());
        //更新数据库
         articleService.updateBatchById(articles);
    }
}

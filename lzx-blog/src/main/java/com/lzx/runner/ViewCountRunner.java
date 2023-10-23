package com.lzx.runner;

import com.lzx.constants.SystemConstants;
import com.lzx.domain.entity.Article;
import com.lzx.mapper.ArticleMapper;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;
    
    @Override
    public void run(String... args) throws Exception {
        // TODO
        //从数据库获取Article数据
        List<Article> articles = articleMapper.selectList(null);
        //提取Id，viewCount列，存储到map中
        Map<String, Integer> map = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(),
                        article -> article.getViewCount().intValue()));
        //存入redis
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEWCOUNT_REDIS_KEY, map);
        setTagArticleCountMap();
    }
    private void setTagArticleCountMap(){
        //从获取所有文章的tagId
    }
}

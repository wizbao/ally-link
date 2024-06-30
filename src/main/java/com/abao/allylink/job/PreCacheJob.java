package com.abao.allylink.job;

import com.abao.allylink.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.abao.allylink.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description 缓存预热任务
 * @Author zhangweibao
 * @Date 2024/6/29 23:42
 * @Version 1.0
 **/
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    @Scheduled(cron = "0 24 19 * * ?")
    private void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("ally-link:precachejob:docache:lock");
        boolean isLock = lock.tryLock();
        if (isLock) {
            try {
                log.info("getLock:{}", Thread.currentThread().getId());
                log.info("PreCacheJob[preUserCache]预热用户缓存 start");
                String key = "ally-link:user:recommend";
                Page<User> page = userService.page(Page.of(1, 100), null);
                Gson gson = new Gson();
                try {
                    // 需要考虑缓存预热时机和过期时间
                    // 若缓存过期时间设置太短，预热后在第一个用户访问之前缓存就已经失效了，还是会影响用户体验
                    stringRedisTemplate.opsForValue().set(key, gson.toJson(page), 10, TimeUnit.MINUTES);
                } catch (Exception e) {
                    log.error("PreCacheJob[preUserCache]用户缓存预热 fail", e);
                }
            } catch (Exception e) {
                log.error("doCacheRecommendUser error", e);
            } finally {
                // 只能释放自己的锁
                if (lock.isHeldByCurrentThread()) {
                    log.info("unLock:{}", Thread.currentThread().getId());
                    lock.unlock();
                }
            }
        }
    }
}

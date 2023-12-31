package io.ihankun.framework.cache.key;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import io.ihankun.framework.cache.config.RedisConfigProperties;
import io.ihankun.framework.common.context.DomainContext;
import io.ihankun.framework.common.exception.BusinessException;
import io.ihankun.framework.common.utils.spring.SpringHelpers;

import java.util.List;

import static io.ihankun.framework.cache.comm.CacheErrorCodeEnum.DOMAIN_NOT_FIND;

/**
 * @author hankun
 */
public abstract class AbstractCacheKey {

    protected static final String SPLIT = ":";

    /**
     * 获取key的统一前缀
     *
     * @param originKey 原始业务组装的Key
     */
    protected String formatKey(String originKey) {

        RedisConfigProperties config = SpringHelpers.context().getBean(RedisConfigProperties.class);
        //未开启状态，则不进行前缀设置
        if (!config.isDomainPrefixEnable()) {
            return originKey;
        }

        //忽略规则不为空,且匹配存在忽略的key值
        List<String> ignoreDomainPrefixKeys = config.getIgnoreDomainPrefixKeys();
        if (CollectionUtil.isNotEmpty(ignoreDomainPrefixKeys) && ignoreDomainPrefixKeys.contains(originKey)) {
            return originKey;
        }

        //获取域名,如果域名为空，则报错处理
        String domain = DomainContext.get();
        if (StrUtil.isEmpty(domain)) {
            throw BusinessException.build(DOMAIN_NOT_FIND, originKey);
        }

        return domain + SPLIT + originKey;
    }
}

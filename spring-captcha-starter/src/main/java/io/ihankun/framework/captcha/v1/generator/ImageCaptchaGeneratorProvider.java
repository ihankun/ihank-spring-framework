package io.ihankun.framework.captcha.v1.generator;


import io.ihankun.framework.captcha.v1.resource.ImageCaptchaResourceManager;

/**
 * @Author: hankun
 * @date 2022/5/19 14:45
 * @Description ImageCaptchaGenerator 提供者
 */
public interface ImageCaptchaGeneratorProvider {

    /**
     * 生成/获取 ImageCaptchaGenerator
     *
     * @param resourceManager resourceManager
     * @param imageTransform imageTransform
     * @return ImageCaptchaGenerator
     */
    ImageCaptchaGenerator get(ImageCaptchaResourceManager resourceManager, ImageTransform imageTransform);

    /**
     * 验证码类型
     *
     * @return String
     */
    default String getType() {
        return "unknown";
    }
}

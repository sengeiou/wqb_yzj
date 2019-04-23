
package com.wqb.security.app.validate.code.impl;

import com.wqb.security.core.validate.code.ValidateCode;
import com.wqb.security.core.validate.code.ValidateCodeException;
import com.wqb.security.core.validate.code.ValidateCodeRepository;
import com.wqb.security.core.validate.code.ValidateCodeType;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis的验证码存取器，避免由于没有session导致无法存取验证码的问题
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ValidateCodeRepository#save(org.
	 * springframework.web.context.request.ServletWebRequest,
	 * ValidateCode,
	 * ValidateCodeType)
	 */
	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
        long second = code.getExpireTime().toEpochSecond(ZoneOffset.of("+8")) -
                LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        redisTemplate.opsForValue().set(buildKey(request, type), code, second, TimeUnit.SECONDS);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ValidateCodeRepository#get(org.
	 * springframework.web.context.request.ServletWebRequest,
	 * ValidateCodeType)
	 */
	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType type) {
		Object value = redisTemplate.opsForValue().get(buildKey(request, type));
		if (value == null) {
			return null;
		}
		return (ValidateCode) value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ValidateCodeRepository#remove(org.
	 * springframework.web.context.request.ServletWebRequest,
	 * ValidateCodeType)
	 */
	@Override
	public void remove(ServletWebRequest request, ValidateCodeType type) {
		redisTemplate.delete(buildKey(request, type));
	}

	/**
	 * @param request
	 * @param type
	 * @return
	 */
	private String buildKey(ServletWebRequest request, ValidateCodeType type) {
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        return "code:" + type.toString().toLowerCase() + ":" + deviceId;
	}

}

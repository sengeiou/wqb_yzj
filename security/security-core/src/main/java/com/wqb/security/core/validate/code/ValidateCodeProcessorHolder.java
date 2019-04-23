
package com.wqb.security.core.validate.code;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 校验码处理器管理器
 */
@Component
public class ValidateCodeProcessorHolder {

	@Autowired
	private Map<String, ValidateCodeProcessor> validateCodeProcessors;

	/**
	 * @param type
	 * @return
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
		return findValidateCodeProcessor(type.toString().toLowerCase());
	}

	/**
	 * @param type
	 * @return
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        String processorName = validateCodeProcessors.keySet().stream()
                .filter(name -> StringUtils.startsWith(name, type))
                .findFirst()
                .orElse(null);
		ValidateCodeProcessor processor = validateCodeProcessors.get(processorName);
		if (processor == null) {
			throw new ValidateCodeException("验证码处理器" + processorName + "不存在");
		}
		return processor;
	}

}

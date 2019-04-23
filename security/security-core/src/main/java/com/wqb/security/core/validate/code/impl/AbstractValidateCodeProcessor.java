
package com.wqb.security.core.validate.code.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqb.security.core.support.Response;
import com.wqb.security.core.validate.code.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * 抽象的图片验证码处理器
 *
 * @author Shoven
 */
public abstract class AbstractValidateCodeProcessor<T extends ValidateCode> implements ValidateCodeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractValidateCodeProcessor.class);

	/**
	 * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
	 */
	@Autowired
	private Map<String, ValidateCodeGenerator> validateCodeGenerators;

	@Autowired
	private ValidateCodeRepository validateCodeRepository;

	@Autowired
    private ObjectMapper objectMapper;

	@Override
	public void create(ServletWebRequest request) {
        try {
            T validateCode = generate(request);
            save(request, validateCode);
            send(request, validateCode);
        } catch (ValidateCodeException e) {
            responseMessage(request, e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseMessage(request, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    protected void responseMessage(ServletWebRequest request, String message, int status) {
        try {
            Response result = new Response(message).setStatus(status);
            HttpServletResponse response = request.getResponse();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(status);
            response.getWriter().write(objectMapper.writeValueAsString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

	/**
	 * 生成校验码
	 *
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private T generate(ServletWebRequest request) {
		String type = getValidateCodeType(request).toString().toLowerCase();
        String generatorName = validateCodeGenerators.keySet().stream()
                .filter(name -> StringUtils.startsWith(name, type))
                .findFirst()
                .orElse(null);
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
        if (validateCodeGenerator == null) {
            throw new ValidateCodeException("没有 "+ generatorName +" 验证码生成器");
        }
		return (T) validateCodeGenerator.generate(request);
	}

	/**
	 * 保存校验码
	 *
	 * @param request
	 * @param validateCode
	 */
	private void save(ServletWebRequest request, T validateCode) {
		ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
		validateCodeRepository.save(request, code, getValidateCodeType(request));
	}

	/**
	 * 发送校验码，由子类实现
	 *
	 * @param request
	 * @param validateCode
	 * @throws Exception
	 */
	protected abstract void send(ServletWebRequest request, T validateCode) throws Exception;

	/**
	 * 根据请求的url获取校验码的类型
	 *
	 * @param request
	 * @return
	 */
	private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
		String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
		return ValidateCodeType.valueOf(type.toUpperCase());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void validate(ServletWebRequest request) {

		ValidateCodeType codeType = getValidateCodeType(request);

		T codeInSession = (T) validateCodeRepository.get(request, codeType);

		String codeInRequest;
		try {
			codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeType.getLabel());
		} catch (ServletRequestBindingException e) {
			throw new RuntimeException("获取验证码的值失败");
		}

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException("请输入" + codeType.getName() + "验证码");
		}

		if (codeInSession == null || codeInSession.isExpried()) {
			validateCodeRepository.remove(request, codeType);
			throw new ValidateCodeException(codeType.getName() + "验证码无效");
		}

		if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
			throw new ValidateCodeException(codeType.getName() + "验证码错误");
		}

		validateCodeRepository.remove(request, codeType);

	}

}

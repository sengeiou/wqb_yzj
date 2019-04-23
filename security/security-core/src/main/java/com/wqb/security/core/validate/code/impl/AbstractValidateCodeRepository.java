package com.wqb.security.core.validate.code.impl;

import com.wqb.security.core.validate.code.ValidateCodeRepository;
import com.wqb.security.core.validate.code.ValidateCodeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author XW
 * @since 2019-04-20 14:44
 */
public abstract class AbstractValidateCodeRepository implements ValidateCodeRepository {

    @Override
    public String getIdentity(ServletWebRequest request, ValidateCodeType type) {
        if (type != ValidateCodeType.IMAGE) {
            String mobile = request.getParameter("mobile");
            if (StringUtils.isBlank(mobile)) {
                throw new IllegalArgumentException("请传入手机号");
            }
            return mobile;
        }
        return "";
    }
}

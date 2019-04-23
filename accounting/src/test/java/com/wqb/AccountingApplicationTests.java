package com.wqb;

import com.wqb.services.voucher.VoucherItemService;
import com.wqb.services.voucher.VoucherRuleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountingApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(AccountingApplicationTests.class);


    @Autowired
    private VoucherRuleService voucherRuleService;

    @Autowired
    private VoucherItemService voucherItemService;

	@Test
	public void testSpringData() {


    }
}

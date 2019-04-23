package com.wqb.services.tax;

import com.wqb.domains.Account;
import com.wqb.domains.User;
import com.wqb.domains.tax.Ticket;
import org.springframework.web.multipart.MultipartFile;

public interface TaxService {
    Ticket getVatTicke(byte[] ticketImage, User user, Account account);  //获取增值税发票

    Ticket getVatTicke(MultipartFile ticketImage, User user, Account account);

    Ticket getVatTicke(String ticketId, User user, Account account);  //获取增值税发票
}

package com.example.RentCar.service;

import com.example.RentCar.entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRentalConfirmation(Rental rental) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(rental.getCustomerEmail());
        helper.setSubject("Xác nhận đặt xe - " + rental.getCar().getName());

        String content = "<h3>Xin chào " + rental.getCustomerName() + ",</h3>"
                + "<p>Cảm ơn bạn đã đặt xe <strong>" + rental.getCar().getName() + "</strong>.</p>"
                + "<p><b>Thông tin thuê:</b></p>"
                + "<ul>"
                + "<li>Hình thức thuê: " + rental.getRentalType() + "</li>"
                + "<li>Thời gian nhận xe: " + rental.getPickupDateTime() + "</li>"
                + "<li>Thời gian trả xe: " + rental.getDropoffDateTime() + "</li>"
                + "<li>Giá thuê: " + rental.getRentalPrice() + " VND</li>"
                + "<li>Tiền cọc: " + rental.getDepositAmount() + " VND</li>"
                + "<li>Tổng thanh toán: " + rental.getTotalPayment() + " VND</li>"
                + "</ul>"
                + "<p>Khi kết thúc thuê, bạn sẽ được hoàn lại <b>" + rental.getDepositAmount() + " VND</b>.</p>"
                + "<p>Chúc bạn có chuyến đi vui vẻ!</p>";

        helper.setText(content, true);
        mailSender.send(message);
    }
    public void sendStatusUpdateEmail(Rental rental, String confirmUrl) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(rental.getCustomerEmail());
        helper.setSubject("Xác nhận nhận xe - " + rental.getCar().getName());

        String content = "<h3>Xin chào " + rental.getCustomerName() + ",</h3>"
                + "<p>Bạn đã đặt xe <strong>" + rental.getCar().getName() + "</strong>.</p>"
                + "<p>Vui lòng bấm vào liên kết sau để xác nhận đã nhận xe:</p>"
                + "<p><a href='" + confirmUrl + "'>Xác nhận đã nhận xe</a></p>";

        helper.setText(content, true);
        mailSender.send(message);
    }
}

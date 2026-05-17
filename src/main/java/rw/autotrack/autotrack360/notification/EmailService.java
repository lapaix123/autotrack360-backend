package rw.autotrack.autotrack360.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@autotrack360.com}")
    private String from;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String subject, String body) {
        if (!emailEnabled || to == null || to.isBlank()) {
            log.info("[EMAIL-DEMO] To: {} | Subject: {} | Body: {}", to, subject, body);
            return;
        }
        if (mailSender == null) {
            log.warn("[EMAIL] JavaMailSender not configured. Enable app.email.enabled=true and set SMTP properties.");
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(from);
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            log.info("[EMAIL] Sent to {}: {}", to, subject);
        } catch (Exception e) {
            log.error("[EMAIL] Failed to send to {}: {}", to, e.getMessage());
        }
    }

    public void sendVehicleArrived(String to, String customerName, String vehicleMake,
                                   String vehicleModel, String trackingNumber, String destination) {
        String subject = "Your Vehicle Has Arrived at Port — AutoTrack360";
        String body = String.format(
            "Dear %s,\n\n" +
            "Great news! Your vehicle has arrived at the port.\n\n" +
            "Vehicle: %s %s\n" +
            "Tracking Number: %s\n" +
            "Destination Port: %s\n\n" +
            "Our team will contact you shortly to arrange final delivery.\n\n" +
            "Thank you for choosing AutoTrack360.\n\n" +
            "Best regards,\nAutoTrack360 Logistics Team",
            customerName, vehicleMake, vehicleModel, trackingNumber, destination
        );
        send(to, subject, body);
    }

    public void sendOrderConfirmation(String to, String customerName, String vehicleMake,
                                      String vehicleModel, String saleId, String amount) {
        String subject = "Order Confirmed — AutoTrack360";
        String body = String.format(
            "Dear %s,\n\n" +
            "Your order has been confirmed.\n\n" +
            "Vehicle: %s %s\n" +
            "Order ID: #%s\n" +
            "Amount: $%s\n\n" +
            "You will receive updates as your vehicle progresses through shipment.\n\n" +
            "Best regards,\nAutoTrack360 Sales Team",
            customerName, vehicleMake, vehicleModel, saleId, amount
        );
        send(to, subject, body);
    }
}

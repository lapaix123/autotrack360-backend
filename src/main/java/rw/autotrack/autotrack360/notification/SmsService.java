package rw.autotrack.autotrack360.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    @Value("${app.sms.enabled:false}")
    private boolean smsEnabled;

    @Value("${app.sms.provider:console}")
    private String provider;

    @Value("${app.sms.api-key:}")
    private String apiKey;

    @Value("${app.sms.sender-id:AutoTrack360}")
    private String senderId;

    /**
     * Send an SMS. In demo mode (app.sms.enabled=false) it logs to console.
     * Set app.sms.enabled=true and configure provider credentials for production.
     */
    public void send(String phone, String message) {
        if (!smsEnabled) {
            log.info("[SMS-DEMO] To: {} | Message: {}", phone, message);
            return;
        }
        // Plug in real provider here (e.g. Twilio, Africa's Talking, etc.)
        log.info("[SMS-{}] Sending to {}: {}", provider.toUpperCase(), phone, message);
        // TODO: implement HTTP call to provider using apiKey and senderId
    }

    public void sendSaleConfirmation(String phone, String customerName, String vehicleMake,
                                     String vehicleModel, String saleId) {
        String msg = String.format(
                "Dear %s, your purchase of %s %s has been confirmed. Sale ID: %s. Thank you - AutoTrack360",
                customerName, vehicleMake, vehicleModel, saleId);
        send(phone, msg);
    }

    public void sendShipmentUpdate(String phone, String customerName, String trackingNumber,
                                   String status, String destination) {
        String msg = String.format(
                "Dear %s, your shipment [%s] to %s is now: %s. Track at AutoTrack360.",
                customerName, trackingNumber, destination, status);
        send(phone, msg);
    }

    public void sendPasswordReset(String phone, String otp) {
        String msg = String.format(
                "Your AutoTrack360 password reset OTP is: %s. Valid for 15 minutes. Do not share.", otp);
        send(phone, msg);
    }

    public void sendWelcome(String phone, String name) {
        String msg = String.format(
                "Welcome to AutoTrack360, %s! Your account has been created successfully.", name);
        send(phone, msg);
    }
}

package com.smarthealth.healthcarebackend.service;

import com.smarthealth.healthcarebackend.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendAppointmentReminder(String to, Appointment appointment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Appointment Reminder");
        message.setText(String.format("Dear %s, your appointment with Dr. %s is scheduled on %s.",
                appointment.getPatient().getUser().getFullName(),
                appointment.getDoctor().getUser().getFullName(),
                appointment.getAppointmentDateTime().toString()));
        mailSender.send(message);
    }

    public void sendBillNotification(String to, double amount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Bill Notification");
        message.setText(String.format("You have a bill of $%.2f. Please make payment.", amount));
        mailSender.send(message);
    }
}
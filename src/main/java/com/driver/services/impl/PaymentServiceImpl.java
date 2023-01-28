package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Payment payment = reservation.getPayment();

        int reservationPrice = reservation.getNumberOfHours() * reservation.getSpot().getPricePerHour();

        try{
            mode = mode.toUpperCase();
            if(mode == PaymentMode.CASH.toString()){
                payment.setPaymentMode(PaymentMode.CASH);
            }else if(mode == PaymentMode.CARD.toString()){
                payment.setPaymentMode(PaymentMode.CARD);
            }else if(mode == PaymentMode.UPI.toString()){
                payment.setPaymentMode(PaymentMode.UPI);
            }else {
                throw new Exception("Payment mode not detected");
            }
        }catch (Exception e){
            throw new Exception("Payment mode not detected");
        }

        if(amountSent < reservationPrice){
            throw new Exception("Insufficient Amount");
        }

        payment.setPaymentCompleted(true);
        payment.setReservation(reservation);
        paymentRepository2.save(payment);

        return payment;
    }
}

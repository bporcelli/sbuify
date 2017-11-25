package com.cse308.sbuify.label.payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;


    /**
     *
     * @param paymentId
     * @return HTTP.OK if successful, HTTP.NOTFOUND, if already paid or payment found is null
     */

    @PostMapping(path = "/api/royalty-payments/{id}/pay")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> markedPayed(@PathVariable("id") String paymentId){

        Optional<Payment> paymentFound = paymentRepository.findById(Integer.valueOf(paymentId));

        if (!paymentFound.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Payment payment = paymentFound.get();

        payment.setStatus(PaymentStatus.PAID);

        paymentRepository.save(payment);

        return new ResponseEntity<>(HttpStatus.OK);


    }
}

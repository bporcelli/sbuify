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
@RequestMapping(path = "/api/royalty-payments")
@PreAuthorize("hasRole('ADMIN')")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Mark an individual payment as paid.
     *
     * @param id Payment ID.
     */
    @PostMapping(path = "/{id}/pay")
    public ResponseEntity<?> markPaid(@PathVariable Integer id){
        // todo: expand to allow multiple payments to be paid simultaneously
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        if (!optionalPayment.isPresent()) {  // invalid payment ID
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Payment payment = optionalPayment.get();
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

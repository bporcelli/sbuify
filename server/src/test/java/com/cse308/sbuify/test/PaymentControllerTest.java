package com.cse308.sbuify.test;

import com.cse308.sbuify.label.Label;
import com.cse308.sbuify.label.LabelRepository;
import com.cse308.sbuify.label.payment.Payment;
import com.cse308.sbuify.label.payment.PaymentPeriod;
import com.cse308.sbuify.label.payment.PaymentPeriodRepository;
import com.cse308.sbuify.label.payment.PaymentRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentControllerTest extends AuthenticatedTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;

    @Autowired
    private LabelRepository labelRepository;

    /**
     * Test: marking a royalty payment as paid.
     */
    @Test
    public void markPaid(){
        // create payment period
        PaymentPeriod paymentPeriod = new PaymentPeriod();

        paymentPeriod.setName("Q1 2017");
        paymentPeriod.setStart(LocalDateTime.of(2017, 1, 1, 0, 0, 0));
        paymentPeriod.setEnd(LocalDateTime.of(2017, 3, 31, 0, 0, 0));

        paymentPeriod = paymentPeriodRepository.save(paymentPeriod);

        // create unpaid payment for period
        Optional<Label> optionalLabel = labelRepository.findById(1);
        assertTrue(optionalLabel.isPresent());

        Label label = optionalLabel.get();
        Payment payment = new Payment(new BigDecimal(1000), paymentPeriod, label);
        payment = paymentRepository.save(payment);

        // mark payment as paid
        Map<String,String> params = new HashMap<>();
        params.put("id", payment.getId().toString());
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/royalty-payments/{id}/pay", null, Void.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com";
    }

    @Override
    public String getPassword() {
        return "a";
    }
}

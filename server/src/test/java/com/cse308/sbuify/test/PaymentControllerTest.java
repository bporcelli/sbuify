package com.cse308.sbuify.test;

import com.cse308.sbuify.label.Label;
import com.cse308.sbuify.label.LabelRepository;
import com.cse308.sbuify.label.payment.Payment;
import com.cse308.sbuify.label.payment.PaymentPeriod;
import com.cse308.sbuify.label.payment.PaymentPeriodRepository;
import com.cse308.sbuify.label.payment.PaymentRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.apache.http.protocol.HTTP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerTest extends AuthenticatedTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;

    @Autowired
    private LabelRepository labelRepository;


    @Test
    public void markedPayed(){

        PaymentPeriod paymentPeriod = new PaymentPeriod();
        paymentPeriod.setName("Example Loan");
        paymentPeriod.setStart(LocalDateTime.now());
        paymentPeriod.setEnd(LocalDateTime.now());

        paymentPeriodRepository.save(paymentPeriod);

        Label label = labelRepository.findById(1).get();

        BigDecimal bigDecimal = new BigDecimal(1000);


        Iterable<PaymentPeriod> allPeriodPayments = paymentPeriodRepository.findAll();

        Iterator<PaymentPeriod> paymentPeriodIterator = allPeriodPayments.iterator();

        paymentPeriod = paymentPeriodIterator.next();

        Payment payment = new Payment(bigDecimal, paymentPeriod, label);

        paymentRepository.save(payment);

        Iterable<Payment> allPayments = paymentRepository.findAll();

        Iterator<Payment> paymentIterator = allPayments.iterator();

        payment = paymentIterator.next();

        Map<String,String> params = new HashMap<>();
        params.put("id", payment.getId().toString());
        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/api/royalty-payments/{id}/pay", null, Void.class, params);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com";
    }

    @Override
    public String getPassword() {
        return "b";
    }
}

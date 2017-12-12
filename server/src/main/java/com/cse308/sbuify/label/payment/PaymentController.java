package com.cse308.sbuify.label.payment;

import com.cse308.sbuify.customer.LibraryProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/royalty-payments")
@PreAuthorize("hasRole('ADMIN')")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentPeriodRepository periodRepository;

    private final Integer ITEMS_PER_PAGE;

    @Autowired
    public PaymentController(LibraryProperties properties) {
        ITEMS_PER_PAGE = properties.getItemPerPage();
    }

    /**
     * Mark an individual payment as paid.
     *
     * @param id Payment ID.
     */
    @PostMapping(path = "/{id}/pay")
    public ResponseEntity<?> markPaid(@PathVariable Integer id) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        if (!optionalPayment.isPresent()) {  // invalid payment ID
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Payment payment = optionalPayment.get();
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get a list of royalty payment periods available for selection.
     * @return a 200 response containing a list of payment periods on success.
     */
    @GetMapping(path = "/periods")
    public @ResponseBody List<PaymentPeriod> getPeriods() {
        return periodRepository.findAllByOrderByEndDesc();
    }

    /**
     * Get a list of royalty payments filtered by status and payment period.
     * @param page Page index.
     * @param status Optional payment status to filter by (default: null).
     * @param period Optional ID of payment period to filter by (default: null).
     * @return a 200 response containing a list of payments.
     */
    @GetMapping
    public @ResponseBody List<Payment> getPayments(
        @RequestParam Integer page,
        @RequestParam(required = false) PaymentStatus status,
        @RequestParam(required = false) Integer period
    ) {
        PageRequest pageRequest = PageRequest.of(page, ITEMS_PER_PAGE);

        Page<Payment> payments;

        if (status != null && period != null) {
            payments = paymentRepository.findAllByStatusAndPeriod_Id(status, period, pageRequest);
        } else if (status != null && period == null) {
            payments = paymentRepository.findAllByStatus(status, pageRequest);
        } else if (status == null && period != null) {
            payments = paymentRepository.findAllByPeriod_Id(period, pageRequest);
        } else {
            payments = paymentRepository.findAll(pageRequest);
        }

        List<Payment> paymentList = new ArrayList<>();

        for (Payment payment: payments) {
            paymentList.add(payment);
        }

        return paymentList;
    }
}

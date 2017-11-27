package com.cse308.sbuify.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/cron")
public class RoyaltyController {
//
//    private static final String FREE_RATE = null;
//    private static final String PREMIUM_RATE = null;
//    @Autowired
//    private RoyaltyRepository royaltyRepo;
//
//    /**
//     * Compute royalties due for the previous quarter.
//     */
//    @PostMapping(path = "/compute-royalty")
//    public ResponseEntity<?> computeRoyalty() {
//
//        LocalDateTime start = getStartTime();
//        LocalDateTime end = getEndTime();
//
//        ResultSet rs = compute(start, end);
//
//        List<RoyaltyPayment> rpList = getRoyaltyPayments(start, end, rs);
//
//        royaltyRepo.saveAll(rpList);
//
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    private List<RoyaltyPayment> getRoyaltyPayments(LocalDateTime start, LocalDateTime end, ResultSet rs) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    private ResultSet compute(LocalDateTime start, LocalDateTime end) {
//        // TODO: Custom query calls
//
////        return rs;
//        return null;
//    }
//
//    private LocalDateTime getStartTime() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    private LocalDateTime getEndTime() {
//        // TODO Auto-generated method stub
//        return null;
//    }
}

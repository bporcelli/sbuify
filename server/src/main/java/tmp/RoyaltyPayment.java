package tmp;

import javax.persistence.*;

import com.cse308.sbuify.domain.RecordLabel;
import com.cse308.sbuify.enums.PaymentStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class RoyaltyPayment implements Serializable {
    @Id
    private Integer id;
    private Double amount;
    private LocalDateTime startPeriod;
    private LocalDateTime endPeriod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private RecordLabel payee;


}

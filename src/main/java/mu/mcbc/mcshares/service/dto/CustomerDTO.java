package mu.mcbc.mcshares.service.dto;

import java.time.Instant;
import javax.persistence.Column;
import mu.mcbc.mcshares.enums.CustomerType;

public class CustomerDTO {

    private String id;
    private String name;
    private Instant dob;
    private CustomerType custType;
    private Long numShares;
    private Double sharePrice;
    private Double balance;
}

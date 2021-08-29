package mu.mcbc.mcshares.service.dto;

import java.math.BigDecimal;
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

    public CustomerDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public CustomerDTO(String id, String name, Instant dob, CustomerType custType, Long numShares, Double sharePrice, Double balance) {
        super();
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.custType = custType;
        this.numShares = numShares;
        this.sharePrice = sharePrice;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDob() {
        return dob;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public CustomerType getCustType() {
        return custType;
    }

    public void setCustType(CustomerType custType) {
        this.custType = custType;
    }

    public Long getNumShares() {
        return numShares;
    }

    public void setNumShares(Long numShares) {
        this.numShares = numShares;
    }

    public Double getSharePrice() {
        return sharePrice;
    }

    public void setSharePrice(Double sharePrice) {
        this.sharePrice = sharePrice;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return (
            "CustomerDTO [id=" +
            id +
            ", name=" +
            name +
            ", dob=" +
            dob +
            ", custType=" +
            custType +
            ", numShares=" +
            numShares +
            ", sharePrice=" +
            sharePrice +
            ", balance=" +
            balance +
            "]"
        );
    }
}

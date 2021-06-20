package mu.mcbc.mcshares.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import mu.mcbc.mcshares.enums.CustomerType;

/**
 * A Shares.
 */
@Entity
@Table(name = "shares")
public class Shares implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 1, message = "Number of shares should be greater than 0")
    @Column(name = "num_shares")
    private Long numShares;

    @Min(value = 1, message = "Price should be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price should be up to 2 decimal places")
    @Column(name = "share_price")
    private BigDecimal sharePrice;

    @OneToOne(mappedBy = "share")
    @JsonBackReference
    private Customer customer;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    private Double balance;

    public Shares() {
        // TODO Auto-generated constructor stub
    }

    public Shares(Long id, Long numShares, BigDecimal sharePrice) {
        super();
        this.id = id;
        this.numShares = numShares;
        this.sharePrice = sharePrice;
    }

    public Shares(Long numShares, BigDecimal sharePrice) {
        this.numShares = numShares;
        this.sharePrice = sharePrice;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shares id(Long id) {
        this.id = id;
        return this;
    }

    public Long getNumShares() {
        return this.numShares;
    }

    public Shares numShares(Long numShares) {
        this.numShares = numShares;
        return this;
    }

    public void setNumShares(Long numShares) {
        this.numShares = numShares;
    }

    public BigDecimal getSharePrice() {
        return this.sharePrice;
    }

    public Shares sharePrice(BigDecimal sharePrice) {
        this.sharePrice = sharePrice;
        return this;
    }

    public void setSharePrice(BigDecimal sharePrice) {
        this.sharePrice = sharePrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public BigDecimal getBalance() {
        return sharePrice.multiply(BigDecimal.valueOf(numShares));
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shares)) {
            return false;
        }
        return id != null && id.equals(((Shares) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Shares{" +
            "id=" + getId() +
            ", numShares=" + getNumShares() +
            ", sharePrice=" + getSharePrice() +
            "}";
    }
}

package mu.mcbc.mcshares.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import mu.mcbc.mcshares.enums.CustomerType;

/**
 * A Corporate.
 */
@Entity
@Table(name = "corporate")
@PrimaryKeyJoinColumn(name = "id")
public class Corporate extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "date_incorp")
    private Instant dateIncorp;

    @Column(name = "reg_no")
    private String regNo;

    public Corporate() {
        // TODO Auto-generated constructor stub
        this.custType = CustomerType.CORPORATE;
    }

    public Corporate(String id, String name, String phone, Instant dateIncorp, String regNo) {
        super(id, name, phone);
        this.dateIncorp = dateIncorp;
        this.regNo = regNo;
        this.custType = CustomerType.CORPORATE;
        // TODO Auto-generated constructor stub
    }

    public Instant getDateIncorp() {
        return this.dateIncorp;
    }

    public Corporate dateIncorp(Instant dateIncorp) {
        this.dateIncorp = dateIncorp;
        return this;
    }

    public void setDateIncorp(Instant dateIncorp) {
        this.dateIncorp = dateIncorp;
    }

    public String getRegNo() {
        return this.regNo;
    }

    public Corporate regNo(String regNo) {
        this.regNo = regNo;
        return this;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Corporate)) {
            return false;
        }
        return id != null && id.equals(((Corporate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Corporate{" +
            "id=" + getId() +
            ", dateIncorp='" + getDateIncorp() + "'" +
            ", regNo='" + getRegNo() + "'" +
            "}";
    }
}

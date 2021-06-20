package mu.mcbc.mcshares.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import mu.mcbc.mcshares.enums.CustomerType;

/**
 * A Individual.
 */
@Entity
@Table(name = "individual")
@PrimaryKeyJoinColumn(name = "id")
public class Individual extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dob")
    private Instant dob;

    public Individual() {
        super();
        this.custType = CustomerType.INDIVIDUAL;
    }

    public Individual(String id, String name, String phone, Instant dob) {
        super(id, name, phone);
        this.dob = dob;
        this.custType = CustomerType.INDIVIDUAL;
        // TODO Auto-generated constructor stub
    }

    public Instant getDob() {
        return this.dob;
    }

    public Individual dob(Instant dob) {
        this.dob = dob;
        return this;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Individual)) {
            return false;
        }
        return id != null && id.equals(((Individual) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Individual{" +
            "id=" + getId() +
            ", dob='" + getDob() + "'" +
            "}";
    }
}

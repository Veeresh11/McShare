package mu.mcbc.mcshares.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import javax.persistence.*;
import mu.mcbc.mcshares.enums.CustomerType;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Inheritance(strategy = InheritanceType.JOINED)
@Indexed
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    protected String id;

    @Column(name = "name")
    @Field(termVector = TermVector.YES)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    protected CustomerType custType;

    public Customer() {
        // TODO Auto-generated constructor stub
    }

    public Customer(String id, String name, String phone) {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(unique = true)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(unique = true)
    private Shares share;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Customer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    protected Address getAddress() {
        return this.address;
    }

    protected Customer address(Address address) {
        this.setAddress(address);
        return this;
    }

    protected void setAddress(Address address) {
        this.address = address;
    }

    public Shares getShare() {
        return this.share;
    }

    protected Customer share(Shares shares) {
        this.setShare(shares);
        return this;
    }

    protected void setShare(Shares shares) {
        this.share = shares;
    }

    public CustomerType getCustType() {
        return custType;
    }

    public void setCustType(CustomerType custType) {
        this.custType = custType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}

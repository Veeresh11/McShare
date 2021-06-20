package mu.mcbc.mcshares.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "adress_1")
    private String adress1;

    @Column(name = "address_2")
    private String address2;

    @Column(name = "town")
    private String town;

    @Column(name = "country")
    private String country;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    private Customer customer;

    public Address() {
        // TODO Auto-generated constructor stub
    }

    public Address(Long id, String adress1, String address2, String town, String country) {
        super();
        this.id = id;
        this.adress1 = adress1;
        this.address2 = address2;
        this.town = town;
        this.country = country;
    }

    public Address(String adress1, String address2, String town, String country) {
        this.adress1 = adress1;
        this.address2 = address2;
        this.town = town;
        this.country = country;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address id(Long id) {
        this.id = id;
        return this;
    }

    public String getAdress1() {
        return this.adress1;
    }

    public Address adress1(String adress1) {
        this.adress1 = adress1;
        return this;
    }

    public void setAdress1(String adress1) {
        this.adress1 = adress1;
    }

    public String getAddress2() {
        return this.address2;
    }

    public Address address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getTown() {
        return this.town;
    }

    public Address town(String town) {
        this.town = town;
        return this;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return this.country;
    }

    public Address country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", adress1='" + getAdress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", town='" + getTown() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}

package mu.mcbc.mcshares.domain;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import mu.mcbc.mcshares.enums.CustomerType;

public class XmlCustomer {

    String custId;
    CustomerType custType;
    Date dob;
    Date dateIncorp;
    String regNo;
    String addr1;
    String addr2;
    String town;
    String country;
    String name;
    String phone;
    Long noOfShares;
    BigDecimal price;

    public XmlCustomer() {
        // TODO Auto-generated constructor stub
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public CustomerType getCustType() {
        return custType;
    }

    public void setCustType(CustomerType custType) {
        this.custType = custType;
    }

    public void setCustType(String custType) {
        if (custType.toUpperCase().equals("INDIVIDUAL")) {
            this.custType = CustomerType.INDIVIDUAL;
        } else if (custType.toUpperCase().equals("CORPORATE")) {
            this.custType = CustomerType.CORPORATE;
        }
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(String dobStr) {
        if (dobStr == "") {
            this.dob = null;
            return;
        }
        try {
            this.dob = new SimpleDateFormat("dd/MM/yyyy").parse(dobStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Date getDateIncorp() {
        return dateIncorp;
    }

    public void setDateIncorp(String dateIncorpStr) {
        if (dateIncorpStr == "") {
            this.dateIncorp = null;
            return;
        }
        try {
            this.dateIncorp = new SimpleDateFormat("dd/MM/yyyy").parse(dateIncorpStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getNoOfShares() {
        return noOfShares;
    }

    public void setNoOfShares(Long noOfShares) {
        this.noOfShares = noOfShares;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return (
            "XmlCustomer [custId=" +
            custId +
            ", CustType=" +
            custType +
            ", dob=" +
            dob +
            ", dateIncorp=" +
            dateIncorp +
            ", regNo=" +
            regNo +
            ", addr1=" +
            addr1 +
            ", addr2=" +
            addr2 +
            ", town=" +
            town +
            ", country=" +
            country +
            ", name=" +
            name +
            ", phone=" +
            phone +
            ", noOfShares=" +
            noOfShares +
            ", price=" +
            price +
            "]"
        );
    }
}

package mu.mcbc.mcshares.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import mu.mcbc.mcshares.enums.CustomerType;

public class XmlFileClass {

    Instant docDate;
    String docRef;
    List<XmlCustomer> xmlCustomer;

    public XmlFileClass() {
        // TODO Auto-generated constructor stub
    }

    public XmlFileClass(Instant docDate, String docRef, List<XmlCustomer> xmlCustomer) {
        super();
        this.docDate = docDate;
        this.docRef = docRef;
        this.xmlCustomer = xmlCustomer;
    }

    public Instant getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDateStr) {
        this.docDate =
            LocalDateTime
                .parse(docDateStr, DateTimeFormatter.ofPattern("dd/MM/uuuu hh:mm:ss a", Locale.US))
                .atZone(ZoneId.of("America/Toronto"))
                .toInstant();
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }

    public List<XmlCustomer> getXmlCustomer() {
        return xmlCustomer;
    }

    public void setXmlCustomer(List<XmlCustomer> xmlCustomer) {
        this.xmlCustomer = xmlCustomer;
    }

    public List<Corporate> getAllCorporate() {
        List<Corporate> corporates = new ArrayList<Corporate>();

        for (XmlCustomer cust : xmlCustomer) {
            if (cust.custType == CustomerType.CORPORATE) {
                Corporate corp = new Corporate(cust.custId, cust.name, cust.phone, cust.dateIncorp.toInstant(), cust.regNo);
                Address addr = new Address(cust.addr1, cust.addr2, cust.town, cust.country);
                Shares share = new Shares(cust.noOfShares, cust.price);
                corp.setAddress(addr);
                corp.setShare(share);

                corporates.add(corp);
            }
        }
        return corporates;
    }

    public List<Individual> getAllIndividual() {
        List<Individual> individuals = new ArrayList<Individual>();

        for (XmlCustomer cust : xmlCustomer) {
            if (cust.custType == CustomerType.INDIVIDUAL) {
                System.out.println("----> " + cust.toString());
                Individual ind = new Individual(cust.custId, cust.name, cust.phone, cust.dob.toInstant());
                Address addr = new Address(cust.addr1, cust.addr2, cust.town, cust.country);
                Shares share = new Shares(cust.noOfShares, cust.price);
                ind.setAddress(addr);
                ind.setShare(share);

                individuals.add(ind);
            }
        }
        return individuals;
    }

    @Override
    public String toString() {
        return "XmlFileClass [docDate=" + docDate + ", docRef=" + docRef + ", xmlCustomer=" + xmlCustomer + "]";
    }
}

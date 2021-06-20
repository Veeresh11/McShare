package mu.mcbc.mcshares.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import mu.mcbc.mcshares.domain.XmlCustomer;
import mu.mcbc.mcshares.domain.XmlFileClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class ReadXmlService {

    private final Logger log = LoggerFactory.getLogger(ReadXmlService.class);

    public XmlFileClass readXml(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + ".xml";

        File savedFile = new File("src/main/resources/" + fileName);
        log.info("Saving xml file: ", fileName);

        try (OutputStream os = new FileOutputStream(savedFile)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(savedFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            XmlFileClass xmlFileObj = new XmlFileClass();

            xmlFileObj.setDocDate(doc.getElementsByTagName("Doc_Date").item(0).getTextContent());
            System.out.println(xmlFileObj.getDocDate().toString());

            xmlFileObj.setDocRef(doc.getElementsByTagName("Doc_Ref").item(0).getTextContent());
            System.out.println(xmlFileObj.getDocRef());

            NodeList nodeList = doc.getElementsByTagName("DataItem_Customer");
            // now XML is loaded as Document in memory, lets convert it to Object List

            List<XmlCustomer> lstCust = new ArrayList<XmlCustomer>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                lstCust.add(getCust(nodeList.item(i)));
            }
            // lets print User list information
            for (XmlCustomer emp : lstCust) {
                System.out.println(emp.toString());
            }
            xmlFileObj.setXmlCustomer(lstCust);
            System.out.println(xmlFileObj.toString());
            return xmlFileObj;
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    private static XmlCustomer getCust(Node node) {
        // XMLReaderDOM domReader = new XMLReaderDOM();
        XmlCustomer cust = new XmlCustomer();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            cust.setCustId(getTagValue("customer_id", element));
            cust.setCustType(getTagValue("Customer_Type", element));
            cust.setDob(getTagValue("Date_Of_Birth", element));
            cust.setDateIncorp(getTagValue("Date_Incorp", element));
            cust.setRegNo(getTagValue("Registration_No", element)); //If there is no typo in xml file skip setting reg no when customer type = individual

            cust.setAddr1(getTagValue("Address_Line1", element));
            cust.setAddr2(getTagValue("Address_Line2", element));
            cust.setTown(getTagValue("Town_City", element));
            cust.setCountry(getTagValue("Country", element));

            cust.setName(getTagValue("Contact_Name", element));
            cust.setPhone(getTagValue("Contact_Number", element));

            cust.setNoOfShares(Long.parseLong(getTagValue("Num_Shares", element)));
            cust.setPrice(new BigDecimal(getTagValue("Share_Price", element)));
        }
        return cust;
    }

    //	private static String getTagValue(String tag, Element element) {
    //        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
    //        Node node = (Node) nodeList.item(0);
    //        return node.getNodeValue();
    //    }

    private static String getTagValue(String tag, Element element) {
        System.out.println(
            "element: " + element + " Tag: " + tag + " content: " + element.getElementsByTagName(tag).item(0).getTextContent()
        );
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }
}

package mu.mcbc.mcshares.web.rest;

import com.netflix.discovery.converters.Auto;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import mu.mcbc.mcshares.domain.Corporate;
import mu.mcbc.mcshares.domain.Customer;
import mu.mcbc.mcshares.domain.Individual;
import mu.mcbc.mcshares.domain.Shares;
import mu.mcbc.mcshares.domain.XmlFileClass;
import mu.mcbc.mcshares.repository.CorporateRepository;
import mu.mcbc.mcshares.repository.CustomerRepository;
import mu.mcbc.mcshares.repository.IndividualRepository;
import mu.mcbc.mcshares.service.CorporateService;
import mu.mcbc.mcshares.service.IndividualService;
import mu.mcbc.mcshares.service.dto.CustomerDTO;
import mu.mcbc.mcshares.service.impl.ReadXmlService;
import mu.mcbc.mcshares.service.impl.XmlDataValidationService;
import mu.mcbc.mcshares.service.mapper.CustomerMapper;
import mu.mcbc.mcshares.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing {@link mu.mcbc.mcshares.domain.Customer}.
 */
@RestController
@RequestMapping("/api")
public class CustomerUploadResource {

    private final Logger log = LoggerFactory.getLogger(CustomerUploadResource.class);

    private static final String ENTITY_NAME = "CustomerUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorporateService corporateService;

    private final CorporateRepository corporateRepository;

    private final IndividualService individualService;

    private final IndividualRepository individualRepository;

    private final ReadXmlService readXmlService;

    private final XmlDataValidationService xmlDataValidService;

    private final CustomerRepository customerRepo;

    private final CustomerMapper customerMapper;

    public CustomerUploadResource(
        CorporateService corporateService,
        CorporateRepository corporateRepository,
        IndividualService individualService,
        IndividualRepository individualRepository,
        ReadXmlService readXmlService,
        XmlDataValidationService xmlDataValidService,
        CustomerRepository customerRepo,
        CustomerMapper customerMapper
    ) {
        this.corporateService = corporateService;
        this.corporateRepository = corporateRepository;
        this.individualService = individualService;
        this.individualRepository = individualRepository;
        this.readXmlService = readXmlService;
        this.xmlDataValidService = xmlDataValidService;
        this.customerRepo = customerRepo;
        this.customerMapper = customerMapper;
    }

    @RequestMapping(value = "/custSearch")
    public ResponseEntity<List<Customer>> search(@RequestParam(value = "search", required = true) String q) {
        List<Customer> searchResults = null;
        try {
            searchResults = customerRepo.search(q);
            return new ResponseEntity<List<Customer>>(searchResults, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<List<Customer>>(new ArrayList<Customer>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/custExport")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<Customer> listCustomers = customerRepo.findAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = { "User ID", "Name", "Phone", "Type" };
        String[] nameMapping = { "id", "name", "phone", "custType" };

        csvWriter.writeHeader(csvHeader);

        for (Customer cust : listCustomers) {
            csvWriter.write(cust, nameMapping);
        }

        csvWriter.close();
    }

    @PostMapping("/custUpload")
    public ResponseEntity<String> uploadCustomers(@RequestParam(value = "xmlFile") MultipartFile xmlFile) throws URISyntaxException {
        log.debug("REST request to save from xml file");
        if (xmlFile == null) {
            throw new BadRequestAlertException("Invalid xml file", ENTITY_NAME, "invalidxml");
        }

        XmlFileClass xmlFileObj = readXmlService.readXml(xmlFile);

        List<Individual> individuals = xmlFileObj.getAllIndividual();
        List<Corporate> corporates = xmlFileObj.getAllCorporate();

        //TOTRY: Create custom validators for all. Use multifield validator
        List<Corporate> validCorporates = xmlDataValidService.validateCorporates(corporates);
        List<Individual> validIndividuals = xmlDataValidService.validateIndividuals(individuals);

        List<Corporate> resultCorporate = corporateService.saveAll(validCorporates);
        List<Individual> resultIndividual = individualService.saveAll(validIndividuals);

        if (validCorporates.size() == corporates.size() && validIndividuals.size() == individuals.size()) {
            return new ResponseEntity<>("Xml data saved!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Valid xml data saved!", HttpStatus.OK);
        }
    }

    @GetMapping("/custdto/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(customerMapper.customerToDto(customerRepo.findById(id).get()), HttpStatus.OK);
    }
}

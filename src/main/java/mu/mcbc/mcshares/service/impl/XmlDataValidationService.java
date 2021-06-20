package mu.mcbc.mcshares.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import mu.mcbc.mcshares.domain.Corporate;
import mu.mcbc.mcshares.domain.ErrorLog;
import mu.mcbc.mcshares.domain.Individual;
import mu.mcbc.mcshares.domain.Shares;
import mu.mcbc.mcshares.service.ErrorLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XmlDataValidationService {

    private final Logger log = LoggerFactory.getLogger(XmlDataValidationService.class);

    @Autowired
    ErrorLogService errorLogService;

    public List<Corporate> validateCorporates(List<Corporate> lstCorp) {
        List<Corporate> lstNewCorp = new ArrayList<Corporate>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        for (Corporate cp : lstCorp) {
            Set<ConstraintViolation<Shares>> violations = validator.validate(cp.getShare());

            if (violations.size() == 0) {
                lstNewCorp.add(cp);
            }

            for (ConstraintViolation<Shares> violation : violations) {
                System.out.println("Logging Corporate error");
                log.error("Corporate shares validation error: " + violation.getMessage() + " Data:  " + violation.getRootBean().toString());
                errorLogService.save(
                    new ErrorLog(
                        "Corporate shares validation error: " + violation.getMessage() + " Data:  " + violation.getRootBean().toString(),
                        Instant.now()
                    )
                );
            }
        }

        return lstNewCorp;
    }

    public List<Individual> validateIndividuals(List<Individual> lstInd) {
        List<Individual> lstNewInd = new ArrayList<Individual>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        for (Individual ind : lstInd) {
            Set<ConstraintViolation<Shares>> violations = validator.validate(ind.getShare());

            if (violations.size() == 0) {
                lstNewInd.add(ind);
            }

            for (ConstraintViolation<Shares> violation : violations) {
                System.out.println("Logging Corporate error");
                log.error("Corporate shares validation error: " + violation.getMessage() + " Data:  " + violation.getRootBean().toString());
            }
        }

        return lstNewInd;
    }
}

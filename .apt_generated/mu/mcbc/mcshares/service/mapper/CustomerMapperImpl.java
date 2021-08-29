package mu.mcbc.mcshares.service.mapper;

import javax.annotation.Generated;
import mu.mcbc.mcshares.domain.Customer;
import mu.mcbc.mcshares.service.dto.CustomerDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-07-08T14:02:38+0400",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.19.0.v20190903-0936, environment: Java 1.8.0_281 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDTO customerToDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId( customer.getId() );
        customerDTO.setName( customer.getName() );
        customerDTO.setCustType( customer.getCustType() );

        customerDTO.setNumShares( customer.getShare().getNumShares() );
        customerDTO.setBalance( customer.getShare().getBalance().doubleValue() );
        customerDTO.setSharePrice( customer.getShare().getSharePrice().doubleValue() );

        return customerDTO;
    }
}

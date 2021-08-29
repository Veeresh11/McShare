package mu.mcbc.mcshares.service.mapper;

import mu.mcbc.mcshares.domain.Corporate;
import mu.mcbc.mcshares.domain.Customer;
import mu.mcbc.mcshares.domain.Individual;
import mu.mcbc.mcshares.enums.CustomerType;
import mu.mcbc.mcshares.repository.CorporateRepository;
import mu.mcbc.mcshares.service.dto.CustomerDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "numShares", expression = "java(customer.getShare().getNumShares())")
    @Mapping(target = "balance", expression = "java(customer.getShare().getBalance().doubleValue())")
    @Mapping(target = "sharePrice", expression = "java(customer.getShare().getSharePrice().doubleValue())")
    CustomerDTO customerToDto(Customer customer);
    //Try @InheritComfiguration
    //https://mapstruct.org/documentation/stable/reference/html/#_reusing_mapping_configurations
    //https://stackoverflow.com/questions/52929073/use-the-mapper-for-the-base-class-to-map-the-child-class

    //	@AfterMapping
    //	default void after(@MappingTarget Customer customer, CustomerDTO customerDTO) {
    //		if(customer.getCustType().equals(CustomerType.INDIVIDUAL)) {
    //			Individual ind = (Individual) customer;
    //			customerDTO.setDob(ind.getDob());
    //		}
    //
    //		if(customer.getCustType().equals(CustomerType.CORPORATE)) {
    //			Corporate corp = (Corporate) customer;
    //			customerDTO.setDob(corp.getDateIncorp());
    //		}
    //
    //	}
}

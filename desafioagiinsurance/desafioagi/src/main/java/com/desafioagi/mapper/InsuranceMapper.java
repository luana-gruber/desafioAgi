package com.desafioagi.mapper;

import com.desafioagi.dto.InsuranceResponseDTO;
import com.desafioagi.model.InsuranceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InsuranceMapper {

    @Mapping(target = "idCustomer", source = "idCustomer")
    @Mapping(target = "plan", source = "plan")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "planValue", source = "planValue")
    InsuranceResponseDTO toInsuranceResponseDTO(InsuranceEntity entity);

}



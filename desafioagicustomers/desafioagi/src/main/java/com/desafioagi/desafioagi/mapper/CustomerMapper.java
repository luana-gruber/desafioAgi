package com.desafioagi.desafioagi.mapper;

import com.desafioagi.desafioagi.dto.AddressResponseDTO;
import com.desafioagi.desafioagi.dto.CustomerResponseDTO;
import com.desafioagi.desafioagi.model.AddressEntity;
import com.desafioagi.desafioagi.model.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id_cliente", source = "idCustomer")
    @Mapping(target = "nome_completo", source = "name")
    @Mapping(target = "telefone", source = "phone")
    @Mapping(target = "endereco", source = "addressEntity")
    CustomerResponseDTO toCustomerResponseDTO(CustomerEntity customerEntity);

    @Mapping(target = "rua", source = "street")
    @Mapping(target = "numero", source = "number")
    @Mapping(target = "cidade", source = "city")
    @Mapping(target = "estado", source = "state")
    @Mapping(target = "cep", source = "zipCode")
    @Mapping(target = "pais", source = "country")
    AddressResponseDTO toAddressDTO(AddressEntity addressEntity);
}


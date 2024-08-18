package com.desafioagi.desafioagi.mapper;

import com.desafioagi.desafioagi.dto.AddressResponseDTO;
import com.desafioagi.desafioagi.dto.CustomerResponseDTO;
import com.desafioagi.desafioagi.model.AddressEntity;
import com.desafioagi.desafioagi.model.CustomerEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-17T15:49:07-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerResponseDTO toCustomerResponseDTO(CustomerEntity customerEntity) {
        if ( customerEntity == null ) {
            return null;
        }

        String id_cliente = null;
        String nome_completo = null;
        String telefone = null;
        AddressResponseDTO endereco = null;

        id_cliente = customerEntity.getIdCustomer();
        nome_completo = customerEntity.getName();
        telefone = customerEntity.getPhone();
        endereco = toAddressDTO( customerEntity.getAddressEntity() );

        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO( id_cliente, nome_completo, telefone, endereco );

        return customerResponseDTO;
    }

    @Override
    public AddressResponseDTO toAddressDTO(AddressEntity addressEntity) {
        if ( addressEntity == null ) {
            return null;
        }

        String rua = null;
        String numero = null;
        String cidade = null;
        String estado = null;
        String cep = null;
        String pais = null;

        rua = addressEntity.getStreet();
        numero = addressEntity.getNumber();
        cidade = addressEntity.getCity();
        estado = addressEntity.getState();
        cep = addressEntity.getZipCode();
        pais = addressEntity.getCountry();

        AddressResponseDTO addressResponseDTO = new AddressResponseDTO( rua, numero, cidade, estado, cep, pais );

        return addressResponseDTO;
    }
}

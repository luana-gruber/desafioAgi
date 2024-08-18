package com.desafioagi.desafioagi.dto;


public record CustomerResponseDTO(String id_cliente,
                                  String nome_completo,
                                  String telefone,
                                  AddressResponseDTO endereco) {
}

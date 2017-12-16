package com.jcsastre.picobank.dto;

import com.jcsastre.picobank.entity.Client;
import com.jcsastre.picobank.entity.Operation;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.Function;

@Value
public class ResponseGetClientDto {

    private Integer status;
    private Data data;

    @Value
    public static class Data {

        private Client client;

        @Value
        public static class Client {

            private String email;
            private Integer balanceInCents;
            List<Operation> operations;
        }
    }

    public static Function<Client, ResponseGetClientDto> asGetClientDto = c -> {
        final Data.Client client = new Data.Client(c.getEmail(), c.getBalanceInCents(), c.getOperations());
        final Data data = new Data(client);
        return new ResponseGetClientDto(HttpStatus.OK.value(), data);
    };
}

package com.camel.taxi.Messages;

import com.ms3_inc.tavros.extensions.rest.OperationResult;

public class Messages {

    public Messages(){
    }

    public final OperationResult.Message NOT_FOUND_ERROR =
            OperationResult.MessageBuilder
                    .error("NotFoundError", "Record not found")
                    .withCode("404")
                    .build();

    public final OperationResult.Message CONFLICT_ERROR =
            OperationResult.MessageBuilder
                    .error("ConflictError", "Record already exists")
                    .withCode("409")
                    .build();

    public final OperationResult.Message BAD_REQUEST_ERROR =
            OperationResult.MessageBuilder
                    .error("BadRequestError", "Bad Request")
                    .withCode("400")
                    .build();
}

package br.com.sysmap.application.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Created by ecellani on 03/06/17.
 */
@Data
@ToString
@NoArgsConstructor
@JsonInclude(NON_NULL)
public class ResponseError implements Serializable {

    public ResponseError(String responseStatus, String message, List<ResponseErrorMessage> messages) {
        this.message = message;
        this.messages = messages;
        this.responseStatus = responseStatus;
    }

    public ResponseError(String responseStatus, String message) {
        this(responseStatus, message, null);
    }

    private String message;
    private String responseStatus;
    private List<ResponseErrorMessage> messages;
}

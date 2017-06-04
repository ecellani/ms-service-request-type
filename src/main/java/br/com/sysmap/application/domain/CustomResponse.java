package br.com.sysmap.application.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
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
public class CustomResponse implements Serializable {

    private Boolean success;
    private List<ServiceRequestType> payload;
    private ResponseError error;

    public CustomResponse(Boolean success, List<ServiceRequestType> payload, ResponseError error) {
        this.error = error;
        this.success = success;
        this.payload = payload;
    }

    public CustomResponse(Boolean success, List<ServiceRequestType> payload) {
        this(success, payload, null);
    }

    public CustomResponse(Boolean success, ResponseError error) {
        this(success, null, error);
    }
}

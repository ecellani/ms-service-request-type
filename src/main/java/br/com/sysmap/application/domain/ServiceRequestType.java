package br.com.sysmap.application.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;

/**
 * Created by ecellani on 01/06/17.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
@XmlAccessorType(FIELD)
@XmlRootElement(name = "sear")
@ApiModel(description = "Represents the service request type object")
public class ServiceRequestType {

    @XmlElement(name = "serviceId")
    @ApiModelProperty(value = "The serviceId of the service request type", required = true)
    private String serviceId;

    @XmlElement(name = "channel")
    @ApiModelProperty(value = "The channel of the service request type", required = true)
    private String channel;

    @ApiModelProperty(value = "The id of the service request type", required = true)
    private String id;

    @ApiModelProperty(value = "The description of the service request type", required = true)
    private String description;
}

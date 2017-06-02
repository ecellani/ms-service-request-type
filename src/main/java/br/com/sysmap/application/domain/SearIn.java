package br.com.sysmap.application.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

/**
 * Created by ecellani on 01/06/17.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "sear")
@XmlAccessorType(FIELD)
@ApiModel(description = "Represents SEAR In")
public class SearIn {

    @XmlElement(name = "serviceId")
    @ApiModelProperty(value = "The serviceId of the SEAR", required = true)
    private Integer serviceId;

    @XmlElement(name = "channel")
    @ApiModelProperty(value = "The ID of the SEAR", required = true)
    private String channel;
}

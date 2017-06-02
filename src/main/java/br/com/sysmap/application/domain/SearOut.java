package br.com.sysmap.application.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by ecellani on 01/06/17.
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Represents SEAR Out")
public class SearOut {

    @ApiModelProperty(value = "The ID of the SEAR", required = true)
    private Integer id;

    @ApiModelProperty(value = "The description of the SEAR", required = true)
    private String description;
}

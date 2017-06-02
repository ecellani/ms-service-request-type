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
@ApiModel(description = "Represents an user of the system X")
public class User {

    @ApiModelProperty(value = "The ID of the user X", required = true)
    private Integer id;

    @ApiModelProperty(value = "The name of the user X", required = true)
    private String name;
}

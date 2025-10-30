package com.jhaprashant1108.SwitchBoard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitchModel implements Serializable {

    private String switchId;

    private String switchName;

    private String switchDescription;

    private String applicationName;

    private String environmentName;

    private boolean status;

    private Map<String, Map<String, Integer>> meteredStatus;

    private String createdBy;

    private String createdAt;

    private String updatedBy;

    private String updatedAt;

}

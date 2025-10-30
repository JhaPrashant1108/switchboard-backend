package com.jhaprashant1108.SwitchBoard.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageModel implements Serializable {

    private String switchName;

    private SwitchModel switchDetails;

}

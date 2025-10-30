package com.jhaprashant1108.SwitchBoard.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchSwitchRequestDto implements Serializable {

    private List<String> switchNames;


}

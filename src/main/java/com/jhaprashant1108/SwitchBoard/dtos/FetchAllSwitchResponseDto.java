package com.jhaprashant1108.SwitchBoard.dtos;

import com.jhaprashant1108.SwitchBoard.models.SwitchModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchAllSwitchResponseDto implements Serializable {

    private List<SwitchModel> switchModels;

    private int totalCount;

}

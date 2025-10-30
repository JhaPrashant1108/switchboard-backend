package com.jhaprashant1108.SwitchBoard.mappers;


import com.jhaprashant1108.SwitchBoard.entities.SwitchEntity;
import com.jhaprashant1108.SwitchBoard.models.SwitchModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SwitchMapper {

    SwitchEntity toEntity(SwitchModel switchModel);

    SwitchModel toModel(SwitchEntity switchEntity);


}

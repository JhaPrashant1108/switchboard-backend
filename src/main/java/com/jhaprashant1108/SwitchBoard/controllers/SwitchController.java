package com.jhaprashant1108.SwitchBoard.controllers;

import com.jhaprashant1108.SwitchBoard.config.AppConstants;
import com.jhaprashant1108.SwitchBoard.dtos.FetchAllSwitchResponseDto;
import com.jhaprashant1108.SwitchBoard.dtos.FetchSwitchRequestDto;
import com.jhaprashant1108.SwitchBoard.dtos.FetchSwitchResponseDto;
import com.jhaprashant1108.SwitchBoard.models.SwitchModel;
import com.jhaprashant1108.SwitchBoard.repositories.redis.SwitchRedisRepo;
import com.jhaprashant1108.SwitchBoard.services.SwitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstants.Api.SWITCH_BASE_PATH)
public class SwitchController {

    @Autowired
    SwitchRedisRepo switchRedisRepo;

    @Autowired
    SwitchService switchService;

    @PostMapping(AppConstants.Api.Endpoints.CREATE_SWITCH)
    public SwitchModel createSwitch(@RequestBody SwitchModel switchModel){
        return switchService.createSwitch(switchModel);
    }

    @GetMapping(AppConstants.Api.Endpoints.READ_SWITCH)
    public SwitchModel readSwitch(@RequestHeader String switchId){
        return switchService.readSwitch(switchId);
    }

    @GetMapping(AppConstants.Api.Endpoints.FETCH_ALL_SWITCHES)
    public FetchAllSwitchResponseDto fetchAllSwitch(@RequestHeader String environmentName , @RequestHeader String applicationName){
        return switchService.fetchAllSwitch(environmentName , applicationName);
    }

    @PostMapping(AppConstants.Api.Endpoints.FETCH_SWITCH)
    public FetchSwitchResponseDto fetchSwitch(@RequestBody FetchSwitchRequestDto fetchSwitchRequest ,
                                              @RequestHeader String applicationName ,
                                              @RequestHeader String environmentName){
        return switchService.fetchSwitch(fetchSwitchRequest , applicationName , environmentName);
    }


    @PutMapping(AppConstants.Api.Endpoints.UPDATE_SWITCH)
    public SwitchModel updateSwitch(@RequestBody SwitchModel switchModel){
        return switchService.updateSwitch(switchModel);
    }

    @DeleteMapping(AppConstants.Api.Endpoints.DELETE_SWITCH)
    public SwitchModel deleteSwitch(@RequestHeader String switchId){
        switchService.deleteSwitch(switchId);
        return null;
    }



}

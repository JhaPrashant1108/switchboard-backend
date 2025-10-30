package com.jhaprashant1108.SwitchBoard.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhaprashant1108.SwitchBoard.components.SwitchPublisher;
import com.jhaprashant1108.SwitchBoard.dtos.FetchAllSwitchResponseDto;
import com.jhaprashant1108.SwitchBoard.dtos.FetchSwitchRequestDto;
import com.jhaprashant1108.SwitchBoard.dtos.FetchSwitchResponseDto;
import com.jhaprashant1108.SwitchBoard.entities.SwitchEntity;
import com.jhaprashant1108.SwitchBoard.mappers.SwitchMapper;
import com.jhaprashant1108.SwitchBoard.models.MessageModel;
import com.jhaprashant1108.SwitchBoard.models.SwitchModel;
import com.jhaprashant1108.SwitchBoard.repositories.postgres.SwitchPostgresRepo;
import com.jhaprashant1108.SwitchBoard.repositories.redis.SwitchRedisRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.jhaprashant1108.SwitchBoard.utils.CommonUtil.getRedisKeyParts;

@Slf4j
@Service
public class SwitchService {

    @Autowired
    SwitchRedisRepo switchRedisRepo;

    @Autowired
    SwitchPostgresRepo switchPostgresRepo;

    @Autowired
    SwitchMapper switchMapper;

    @Autowired
    SwitchPublisher switchPublisher;

    private static final ObjectMapper objectMapper = new ObjectMapper();



    public SwitchModel createSwitch(SwitchModel switchModel){

        switchRedisRepo.createSwitch(switchModel);
        switchPostgresRepo.save(switchMapper.toEntity(switchModel));
        switchPublisher.send(new MessageModel(switchModel.getSwitchName() , switchModel));
        return switchModel;
    }

    public SwitchModel readSwitch(String switchId) {

        try {
            SwitchEntity switchEntity = switchPostgresRepo.getReferenceById(switchId);
            return switchMapper.toModel(switchEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SwitchModel updateSwitch(SwitchModel switchModel) {
        String meteredStatus = null;
        if(null != switchModel.getMeteredStatus()){
            try {
                meteredStatus = objectMapper.writeValueAsString(switchModel.getMeteredStatus());
            } catch (Exception e){
                log.error("Failed to serialize meteredStatus map {}" ,e.getMessage());
            }

        }

        switchRedisRepo.updateSwitch(switchModel);
        switchPostgresRepo.updateStatusAndAuditByEnvAndAppName(switchModel.getSwitchDescription() , switchModel.isStatus() , switchModel.getUpdatedBy() , switchModel.getUpdatedAt() , meteredStatus,switchModel.getSwitchId());
        switchPublisher.send(new MessageModel(switchModel.getSwitchName() , switchModel));
        return switchModel;
    }



    public FetchSwitchResponseDto fetchSwitch(FetchSwitchRequestDto fetchSwitchRequest , String applicationName , String environmentName) {

        FetchSwitchResponseDto fetchSwitchResponse = new FetchSwitchResponseDto();
        Map<String , SwitchModel> switchDetails = new HashMap<>();
        if(!fetchSwitchRequest.getSwitchNames().isEmpty()){
            for (String switchName : fetchSwitchRequest.getSwitchNames()) {
                String redisKey = getRedisKeyParts(switchName , applicationName , environmentName);
                SwitchModel switchModel = switchRedisRepo.readSwitch(redisKey);
                switchDetails.put(switchName , switchModel);
            }
        }
        fetchSwitchResponse.setSwitchDetails(switchDetails);
        return fetchSwitchResponse;
    }

    public FetchAllSwitchResponseDto fetchAllSwitch(String environmentName , String applicationName) {

        FetchAllSwitchResponseDto fetchAllSwitchResponse =  new FetchAllSwitchResponseDto();
        fetchAllSwitchResponse.setSwitchModels(switchPostgresRepo.findAll().stream().map(switchMapper::toModel).toList());

        return fetchAllSwitchResponse;
    }


    public void deleteSwitch(String switchId) {

        try {
            SwitchEntity switchEntity = switchPostgresRepo.getReferenceById(switchId);
            String redisKey = getRedisKeyParts(switchEntity.getSwitchName() , switchEntity.getApplicationName() , switchEntity.getEnvironmentName());
            switchPostgresRepo.delete(switchEntity);
            switchRedisRepo.deleteSwitch(redisKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}

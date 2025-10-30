package com.jhaprashant1108.SwitchBoard.utils;

import com.jhaprashant1108.SwitchBoard.models.SwitchModel;

public class CommonUtil {



    public static final String getRedisKey(SwitchModel switchModel){
        return switchModel.getEnvironmentName() + "." + switchModel.getApplicationName() + "." + switchModel.getSwitchName();
    }


    public static final String getRedisKeyParts(String switchName , String applicationName , String environmentName){
        return environmentName + "." + applicationName + "." + switchName;
    }



}

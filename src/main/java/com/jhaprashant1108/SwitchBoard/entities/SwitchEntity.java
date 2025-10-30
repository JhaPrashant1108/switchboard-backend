package com.jhaprashant1108.SwitchBoard.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhaprashant1108.SwitchBoard.config.AppConstants;
import com.jhaprashant1108.SwitchBoard.config.ErrorMessages;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Data
@Entity
@Table(
        name = AppConstants.Database.SwitchTable.TABLE_NAME,
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"environment_name", "switch_name"})
        }
)
public class SwitchEntity {

    @Id
    @Column(name = "switch_id" , length = AppConstants.Database.SwitchTable.SWITCH_ID_MAX_LENGTH , nullable = false)
    private String switchId;

    @Column(name = "switch_name" , nullable = false)
    private String switchName;

    @Column(name = "switch_description")
    private String switchDescription;

    @Column(name = "application_name" , nullable = false)
    private String applicationName;

    @Column(name = "environment_name" , nullable = false)
    private String environmentName;

    @Column(name = "status" , columnDefinition = "BOOLEAN")
    private boolean status;

    @Column(name = "metered_status", columnDefinition = "TEXT")
    private String meteredStatusJson;

    @Column(name = "created_by" , length = AppConstants.Database.SwitchTable.USER_FIELD_MAX_LENGTH)
    private String createdBy;

    @Column(name = "created_at" , length = AppConstants.Database.SwitchTable.TIMESTAMP_FIELD_MAX_LENGTH)
    private String createdAt;

    @Column(name = "updated_by" , length = AppConstants.Database.SwitchTable.USER_FIELD_MAX_LENGTH)
    private String updatedBy;

    @Column(name = "updated_at" , length = AppConstants.Database.SwitchTable.TIMESTAMP_FIELD_MAX_LENGTH)
    private String updatedAt;

    @Transient
    private Map<String, Map<String, Integer>> meteredStatus;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PrePersist
    @PreUpdate
    private void serializeData() {
        if (meteredStatus != null) {
            try {
                meteredStatusJson = objectMapper.writeValueAsString(meteredStatus);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(ErrorMessages.Serialization.SERIALIZATION_ERROR, e);
            }
        }
    }

    @PostLoad
    private void deserializeData() {
        if (meteredStatusJson != null) {
            try {
                meteredStatus = objectMapper.readValue(meteredStatusJson, new TypeReference<>() {});
            } catch (JsonProcessingException e) {
                throw new RuntimeException(ErrorMessages.Serialization.DESERIALIZATION_ERROR, e);
            }
        }
    }



}

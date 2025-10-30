package com.jhaprashant1108.SwitchBoard.repositories.postgres;

import com.jhaprashant1108.SwitchBoard.entities.SwitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SwitchPostgresRepo extends JpaRepository<SwitchEntity, String> {


    @Modifying
    @Transactional
    @Query("""
        UPDATE SwitchEntity s
        SET s.switchDescription = :switchDescription,
            s.status = :status,
            s.updatedBy = :updatedBy,
            s.updatedAt = :updatedAt,
            s.meteredStatusJson = :meteredStatusJson
        WHERE s.switchId = :switchId
    """)
    int updateStatusAndAuditByEnvAndAppName(@Param("switchDescription") String switchDescription,
                                            @Param("status") boolean status,
                                            @Param("updatedBy") String updatedBy,
                                            @Param("updatedAt") String updatedAt,
                                            @Param("meteredStatusJson") String meteredStatusJson,
                                            @Param("switchId") String switchId);


//    List<SwitchEntity> findAllbyEnvandAppName();

}

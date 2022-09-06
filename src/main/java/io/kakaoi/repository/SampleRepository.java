package io.kakaoi.repository;

import io.kakaoi.domain.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SampleRepository extends JpaRepository<SampleEntity, String>, JpaSpecificationExecutor<SampleEntity> {

    @Query(
        "SELECT sample " +
        "FROM   SampleEntity sample " +
        "WHERE  sample.id = :sampleId " +
        "  AND  sample.project.id = :projectId "
    )
    Optional<SampleEntity> findByIdAndProjectId(
        @Param("sampleId") String sampleId,
        @Param("projectId") String projectId
    );

    @Query(
        "SELECT CASE WHEN COUNT(sample) > 0 THEN true ELSE false END " +
        "FROM   SampleEntity sample " +
        "WHERE  sample.id = :sampleId " +
        "  AND  sample.project.id = :projectId "
    )
    boolean existsSampleByIdAndProjectId(
        @Param("sampleId") String sampleId,
        @Param("projectId") String projectId
    );

}

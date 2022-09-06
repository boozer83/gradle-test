package io.kakaoi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 프로젝트
 */
@Entity
@Table(name = "T_PROJECT")
public class ProjectEntity extends AbstractAuditingEntity {

    /**
     * id
     */
    @Id
    @Column(name = "ID", columnDefinition="VARCHAR(100)", nullable=false, length = 100)
    private String id;

    /**
     * 이름
     */
    @Column(name = "PROJECT_NAME", nullable = false, length = 100)
    private String projectName;

    /**
     * 도메인 이름
     */
    @Column(name = "DOMAIN_NAME", nullable = false, length = 100)
    private String domainName;

    /**
     * 도메인 ID
     */
    @Column(name = "DOMAIN_ID", nullable = false, length = 100)
    private String domainId;

    /**
     * 레파지토리 목록
     */
    @JsonIgnore
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<SampleEntity> samples = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public List<SampleEntity> getSamples() {
        return samples;
    }

    public void setSamples(List<SampleEntity> samples) {
        this.samples = samples;
    }
}

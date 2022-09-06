package io.kakaoi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 샘플 테이블
 */
@Entity
@Table(name = "T_SAMPLE")
public class SampleEntity extends AbstractAuditingEntity {

    /**
     * id
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ID", columnDefinition="VARCHAR(100)", nullable=false, length = 100)
    private String id;

    /**
     *
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private ProjectEntity project;

    /**
     * 이름
     */
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    /**
     * 설명
     */
    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

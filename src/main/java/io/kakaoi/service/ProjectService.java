package io.kakaoi.service;

import io.kakaoi.domain.ProjectEntity;
import io.kakaoi.repository.ProjectRepository;
import io.kakaoi.service.dto.IAMDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectEntity createProject(IAMDTO.ProjectSummery project) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(project.getId());
        projectEntity.setProjectName(project.getName());
        projectEntity.setDomainId(project.getDomain().getId());
        projectEntity.setDomainName(project.getDomain().getName());
        return projectRepository.save(projectEntity);
    }

    public ProjectEntity getProject(String projectId) {
        return projectRepository.findById(projectId)
            .orElse(null);
    }

}

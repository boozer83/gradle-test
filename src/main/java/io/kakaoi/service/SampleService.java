package io.kakaoi.service;

import io.kakaoi.config.Constants;
import io.kakaoi.domain.ProjectEntity;
import io.kakaoi.domain.SampleEntity;
import io.kakaoi.exception.BusinessException;
import io.kakaoi.repository.SampleRepository;
import io.kakaoi.service.dto.IAMDTO;
import io.kakaoi.service.dto.SampleDTO;
import io.kakaoi.service.predicate.SampleFilterPredicateFactory;
import io.kakaoi.util.SecurityUtils;
import io.kakaoi.web.rest.vm.FilterVM;
import io.kakaoi.web.rest.vm.SampleVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SampleFilterPredicateFactory sampleFilterPredicateFactory;

    public Page<SampleDTO.Info> getInfoPage(FilterVM filterVM, Pageable pageable) {
        String projectId = SecurityUtils.getCurrentUserProjectId();
        Specification<SampleEntity> spec = sampleFilterPredicateFactory.makeWithProject(projectId, filterVM);

        return sampleRepository.findAll(spec, pageable)
            .map(this::mappingSampleInfo);
    }

    public SampleDTO.Info getInfo(String sampleId) {
        String projectId = SecurityUtils.getCurrentUserProjectId();

        return sampleRepository.findByIdAndProjectId(sampleId, projectId)
            .map(this::mappingSampleInfo)
            .orElseThrow(NoSuchElementException::new);
    }

    public SampleDTO.Info createSample(SampleVM.Apply sampleVM) {
        String projectId = SecurityUtils.getCurrentUserProjectId();

        ProjectEntity projectEntity = projectService.getProject(projectId);
        if (projectEntity == null) {
            projectEntity = SecurityUtils.getCurrentUserLogin()
                    .map(IAMDTO.Info::getProject)
                    .map(projectService::createProject)
                    .orElseThrow(() -> new BusinessException(Constants.CommonCode.UNAUTHORIZED));
        }

        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setName(sampleVM.getName());
        sampleEntity.setDescription(sampleVM.getDescription());
        sampleEntity.setProject(projectEntity);

        SampleEntity saved = sampleRepository.save(sampleEntity);
        return this.mappingSampleInfo(saved);
    }

    public SampleDTO.Info updateSample(String sampleId, SampleVM.Apply sampleVM) {
        String projectId = SecurityUtils.getCurrentUserProjectId();

        SampleEntity sampleEntity = sampleRepository.findByIdAndProjectId(sampleId, projectId)
            .orElseThrow(NoSuchElementException::new);

        sampleEntity.setName(sampleVM.getName());
        sampleEntity.setDescription(sampleVM.getDescription());

        SampleEntity saved = sampleRepository.save(sampleEntity);
        return this.mappingSampleInfo(saved);
    }

    public void deleteSample(String sampleId) {
        String projectId = SecurityUtils.getCurrentUserProjectId();

        SampleEntity sampleEntity = sampleRepository.findByIdAndProjectId(sampleId, projectId)
            .orElseThrow(NoSuchElementException::new);

        sampleRepository.delete(sampleEntity);
    }

    private SampleDTO.Info mappingSampleInfo(SampleEntity entity) {
        SampleDTO.Info info = new SampleDTO.Info();
        info.setId(entity.getId());
        info.setName(entity.getName());
        info.setDescription(entity.getDescription());
        info.setCreatedBy(entity.getCreatedBy());
        info.setCreatedDt(entity.getCreatedDt());
        info.setUpdatedBy(entity.getUpdatedBy());
        info.setUpdatedDt(entity.getUpdatedDt());
        return info;
    }

}

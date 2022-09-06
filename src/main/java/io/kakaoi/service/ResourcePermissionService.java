package io.kakaoi.service;

import io.kakaoi.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourcePermissionService {

    @Autowired
    private SampleRepository sampleRepository;

    public boolean isAccessSample(String sampleId, String projectId) {
        return sampleRepository.existsSampleByIdAndProjectId(sampleId, projectId);
    }

}

package io.kakaoi.service.iam.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IamProjectPage extends IamAbstractPage<IamProject> {

    private final List<IamProject> projects;

    @JsonCreator
    public IamProjectPage(
        @JsonProperty("offset") Long offset,
        @JsonProperty("size") Long size,
        @JsonProperty("total") Long total,
        @JsonProperty("projects") List<IamProject> projects
    ) {
        super(offset, size, total);
        this.projects = projects;
    }

    public List<IamProject> getProjects() {
        return projects;
    }

    @Override
    public List<IamProject> getElements() {
        return this.getProjects();
    }

}

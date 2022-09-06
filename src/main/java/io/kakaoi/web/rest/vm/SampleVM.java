package io.kakaoi.web.rest.vm;

import io.kakaoi.web.rest.vm.vaild.SampleNamingRule;

import javax.validation.constraints.Size;

public final class SampleVM {

    public static class Apply {

        @SampleNamingRule
        private String name;

        @Size(max = 255)
        private String description;

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

    private SampleVM() {}
}

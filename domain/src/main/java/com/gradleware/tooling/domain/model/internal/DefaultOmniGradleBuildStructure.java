package com.gradleware.tooling.domain.model.internal;

import com.gradleware.tooling.domain.model.BasicGradleProjectFields;
import com.gradleware.tooling.domain.model.OmniGradleBuildStructure;
import com.gradleware.tooling.domain.model.generic.DefaultHierarchicalDomainObject;
import com.gradleware.tooling.domain.model.generic.HierarchicalDomainObject;
import org.gradle.tooling.model.gradle.BasicGradleProject;
import org.gradle.tooling.model.gradle.GradleBuild;

import java.io.File;

/**
 * Default implementation of the {@link OmniGradleBuildStructure} interface.
 */
public final class DefaultOmniGradleBuildStructure implements OmniGradleBuildStructure {

    private final HierarchicalDomainObject<BasicGradleProjectFields> rootProject;

    private DefaultOmniGradleBuildStructure(HierarchicalDomainObject<BasicGradleProjectFields> rootProject) {
        this.rootProject = rootProject;
    }

    @Override
    public HierarchicalDomainObject<BasicGradleProjectFields> getRootProject() {
        return this.rootProject;
    }

    public static DefaultOmniGradleBuildStructure from(GradleBuild gradleBuild) {
        BasicGradleProject rootProject = gradleBuild.getRootProject();
        DefaultHierarchicalDomainObject<BasicGradleProjectFields> basicGradleProject = convert(rootProject);

        return new DefaultOmniGradleBuildStructure(basicGradleProject);
    }

    private static DefaultHierarchicalDomainObject<BasicGradleProjectFields> convert(BasicGradleProject project) {
        DefaultHierarchicalDomainObject<BasicGradleProjectFields> basicGradleProject = new DefaultHierarchicalDomainObject<BasicGradleProjectFields>();
        basicGradleProject.put(BasicGradleProjectFields.NAME, project.getName());
        basicGradleProject.put(BasicGradleProjectFields.PATH, project.getPath());
        basicGradleProject.put(BasicGradleProjectFields.PROJECT_DIRECTORY, getProjectDirectory(project));

        for (BasicGradleProject child : project.getChildren()) {
            DefaultHierarchicalDomainObject<BasicGradleProjectFields> basicGradleProjectChild = convert(child);
            basicGradleProject.addChild(basicGradleProjectChild);
        }

        return basicGradleProject;
    }

    /**
     * BasicGradleProject#getProjectDirectory is only available in Gradle versions >= 1.8.
     *
     * @param project the project model
     * @return the project directory or null if not available on the project model
     */
    private static File getProjectDirectory(BasicGradleProject project) {
        try {
            return project.getProjectDirectory();
        } catch (Exception e) {
            return null;
        }
    }

}
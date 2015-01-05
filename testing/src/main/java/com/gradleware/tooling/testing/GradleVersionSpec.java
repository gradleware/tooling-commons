package com.gradleware.tooling.testing;

import com.google.common.base.Preconditions;
import org.gradle.api.specs.Spec;
import org.gradle.api.specs.Specs;
import org.gradle.util.GradleVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * A specification that matches against Gradle version patterns.
 */
public final class GradleVersionSpec {

    private static final String CURRENT = "current";
    private static final String NOT_CURRENT = "!current";
    private static final String EQUALS = "=";
    private static final String GREATER_THAN_OR_EQUALS = ">=";
    private static final String GREATER_THAN = ">";
    private static final String SMALLER_THAN_OR_EQUALS = "<=";
    private static final String SMALLER_THAN = "<";

    private GradleVersionSpec() {
    }

    /**
     * Creates a spec from the given version constraint.
     *
     * @param constraint the version constraint, must not be null
     * @return the spec representing the version constraint, never null
     */
    public static Spec<GradleVersion> toSpec(String constraint) {
        Preconditions.checkNotNull(constraint);

        String trimmed = constraint.trim();

        // exclusive patterns
        if (trimmed.equals(CURRENT)) {
            final GradleVersion current = GradleVersion.current();
            return new Spec<GradleVersion>() {
                @Override
                public boolean isSatisfiedBy(GradleVersion element) {
                    return element.equals(current);
                }
            };
        }
        if (trimmed.equals(NOT_CURRENT)) {
            final GradleVersion current = GradleVersion.current();
            return new Spec<GradleVersion>() {
                @Override
                public boolean isSatisfiedBy(GradleVersion element) {
                    return !element.equals(current);
                }
            };
        }
        if (trimmed.startsWith(EQUALS)) {
            final GradleVersion target = GradleVersion.version(trimmed.substring(1)).getBaseVersion();
            return new Spec<GradleVersion>() {
                @Override
                public boolean isSatisfiedBy(GradleVersion element) {
                    return element.getBaseVersion().equals(target);
                }
            };
        }

        // AND-combined patterns
        List<Spec<GradleVersion>> specs = new ArrayList<Spec<GradleVersion>>();
        String[] patterns = trimmed.split("\\s+");
        for (String value : patterns) {
            if (value.startsWith(GREATER_THAN_OR_EQUALS)) {
                final GradleVersion minVersion = GradleVersion.version(value.substring(2));
                specs.add(new Spec<GradleVersion>() {
                    @Override
                    public boolean isSatisfiedBy(GradleVersion element) {
                        return element.getBaseVersion().compareTo(minVersion) >= 0;
                    }
                });
            } else if (value.startsWith(GREATER_THAN)) {
                final GradleVersion minVersion = GradleVersion.version(value.substring(1));
                specs.add(new Spec<GradleVersion>() {
                    @Override
                    public boolean isSatisfiedBy(GradleVersion element) {
                        return element.getBaseVersion().compareTo(minVersion) > 0;
                    }
                });
            } else if (value.startsWith(SMALLER_THAN_OR_EQUALS)) {
                final GradleVersion maxVersion = GradleVersion.version(value.substring(2));
                specs.add(new Spec<GradleVersion>() {
                    @Override
                    public boolean isSatisfiedBy(GradleVersion element) {
                        return element.getBaseVersion().compareTo(maxVersion) <= 0;
                    }
                });
            } else if (value.startsWith(SMALLER_THAN)) {
                final GradleVersion maxVersion = GradleVersion.version(value.substring(1));
                specs.add(new Spec<GradleVersion>() {
                    @Override
                    public boolean isSatisfiedBy(GradleVersion element) {
                        return element.getBaseVersion().compareTo(maxVersion) < 0;
                    }
                });
            } else {
                throw new RuntimeException(String.format("Unsupported version range '%s' specified in constraint '%s'. Supported formats: '>=nnn' or '<=nnn' or space-separate patterns", value, constraint));
            }
        }

        return Specs.and(specs);
    }

}

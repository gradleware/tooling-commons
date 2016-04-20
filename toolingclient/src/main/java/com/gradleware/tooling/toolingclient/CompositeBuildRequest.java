/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gradleware.tooling.toolingclient;


import org.gradle.tooling.connection.ModelResults;

/**
 * A special request type which is issued against a composition of Gradle builds.
 * <p/>
 * Instances of {@code CompositeRequest} are not thread-safe.
 *
 * @author Stefan Oehme
 * @param <T> the result type
 */
public interface CompositeBuildRequest<T> extends Request<ModelResults<T>> {

    /**
     * Specifies the builds which will participate in the request.
     *
     * @param buildIdentifiers the descriptors of the Gradle builds which will participate in the request
     * @return this
     */
    CompositeBuildRequest<T> participants(GradleBuildIdentifier... buildIdentifiers);

    /**
     * Specifies additional builds which will participate in the request.
     *
     * @param buildIdentifiers the descriptor of the Gradle builds which will participate in the request
     * @return this
     */
    CompositeBuildRequest<T> addParticipants(GradleBuildIdentifier... buildIdentifiers);

}

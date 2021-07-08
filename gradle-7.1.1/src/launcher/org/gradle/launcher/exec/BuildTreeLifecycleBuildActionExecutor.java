/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.launcher.exec;

import org.gradle.internal.buildtree.BuildActionRunner;
import org.gradle.internal.buildtree.BuildTreeState;
import org.gradle.internal.buildtree.BuildTreeModelControllerServices;
import org.gradle.internal.invocation.BuildAction;
import org.gradle.internal.session.BuildSessionActionExecutor;
import org.gradle.internal.session.BuildSessionContext;

/**
 * A {@link BuildActionExecuter} responsible for establishing the build tree for a single invocation of a {@link BuildAction}.
 */
public class BuildTreeLifecycleBuildActionExecutor implements BuildSessionActionExecutor {
    private final BuildTreeModelControllerServices buildTreeModelControllerServices;

    public BuildTreeLifecycleBuildActionExecutor(BuildTreeModelControllerServices buildTreeModelControllerServices) {
        this.buildTreeModelControllerServices = buildTreeModelControllerServices;
    }

    @Override
    public BuildActionRunner.Result execute(BuildAction action, BuildSessionContext buildSession) {
        BuildTreeModelControllerServices.Supplier modelServices = buildTreeModelControllerServices.servicesForBuildTree(action.isRunTasks(), action.isCreateModel(), action.getStartParameter());
        try (BuildTreeState buildTree = new BuildTreeState(buildSession.getServices(), modelServices)) {
            return buildTree.run(context -> context.execute(action));
        }
    }
}

/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.screens.datasource.management.client.explorer.project;

import java.util.ArrayList;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.guvnor.common.services.project.model.Project;
import org.guvnor.structure.organizationalunit.OrganizationalUnit;
import org.guvnor.structure.repositories.Repository;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.kie.workbench.common.screens.datasource.management.client.explorer.common.DefExplorerContent;

@Dependent
@Templated
public class ProjectDataSourceExplorerViewImpl
        implements ProjectDataSourceExplorerView,
                   IsElement {

    @Inject
    @DataField("project-selector")
    private ProjectSelector projectSelector;

    @Inject
    @DataField("datasource-explorer-container")
    private Div container;

    private Presenter presenter;

    public ProjectDataSourceExplorerViewImpl() {
    }

    @Override
    public void init(final Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadContent(final Collection<OrganizationalUnit> organizationalUnits,
                            final OrganizationalUnit activeOrganizationalUnit,
                            final Collection<Repository> repositories,
                            final Repository activeRepository,
                            final Collection<Project> projects,
                            final Project activeProject) {

        projectSelector.loadOptions(organizationalUnits,
                                    activeOrganizationalUnit,
                                    repositories,
                                    activeRepository,
                                    projects,
                                    activeProject);
    }

    @Override
    public void clear() {
        loadContent(new ArrayList<>(),
                    null,
                    new ArrayList<>(),
                    null,
                    new ArrayList<>(),
                    null);
    }

    @Override
    public void addProjectSelectorHandler(final ProjectSelectorHandler handler) {
        projectSelector.addProjectSelectorHandler(handler);
    }

    @Override
    public void setDataSourceDefExplorer(final DefExplorerContent defExplorerContent) {
        container.appendChild(defExplorerContent.getElement());
    }
}
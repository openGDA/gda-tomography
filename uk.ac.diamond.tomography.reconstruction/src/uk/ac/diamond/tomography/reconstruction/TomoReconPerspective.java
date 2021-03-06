/*-
 * Copyright © 2012 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.diamond.tomography.reconstruction;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import uk.ac.diamond.tomography.reconstruction.views.CenterOfRotationView;
import uk.ac.diamond.tomography.reconstruction.views.NexusNavigator;
import uk.ac.diamond.tomography.reconstruction.views.ParameterView;
import uk.ac.diamond.tomography.reconstruction.views.ProjectionsView;
import uk.ac.diamond.tomography.reconstruction.views.ReconResultsView;

public class TomoReconPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {

		layout.addView(NexusNavigator.ID, IPageLayout.LEFT, 0.3f, IPageLayout.ID_EDITOR_AREA);
		layout.addView("uk.ac.diamond.scisoft.analysis.rcp.plotView1", IPageLayout.RIGHT, 0.5f,
				IPageLayout.ID_EDITOR_AREA);
		layout.addView(ProjectionsView.ID, IPageLayout.BOTTOM, 0.2f, NexusNavigator.ID);
		IFolderLayout parameterCenterOfRotationFolder = layout.createFolder("ParameterCenterOfRotation",
				IPageLayout.BOTTOM, 0.6f, ProjectionsView.ID);
		parameterCenterOfRotationFolder.addView(ParameterView.ID);
		parameterCenterOfRotationFolder.addView(CenterOfRotationView.ID);
		layout.addPlaceholder("org.dawb.workbench.plotting.views.toolPageView.1D_and_2D", IPageLayout.RIGHT, 0.7f,
				"uk.ac.diamond.scisoft.analysis.rcp.plotView1");
		layout.addView(IPageLayout.ID_PROGRESS_VIEW, IPageLayout.RIGHT, 0.7f,
				"org.dawb.workbench.plotting.views.toolPageView.1D_and_2D");

		layout.addView(ReconResultsView.ID, IPageLayout.BOTTOM, 0.7f, IPageLayout.ID_PROGRESS_VIEW);

		layout.setEditorAreaVisible(false);

		layout.addShowViewShortcut("uk.ac.diamond.scisoft.analysis.rcp.plotView1");
		layout.addShowViewShortcut(NexusNavigator.ID);
		layout.addShowViewShortcut(ProjectionsView.ID);
		layout.addShowViewShortcut(ParameterView.ID);
		layout.addShowViewShortcut(CenterOfRotationView.ID);
		layout.addShowViewShortcut(ReconResultsView.ID);
		layout.addShowViewShortcut(IPageLayout.ID_PROGRESS_VIEW);

	}

}

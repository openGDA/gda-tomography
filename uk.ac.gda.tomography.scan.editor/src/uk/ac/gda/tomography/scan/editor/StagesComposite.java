/*-
 * Copyright © 2019 Diamond Light Source Ltd.
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

package uk.ac.gda.tomography.scan.editor;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.gda.tomography.model.AcquisitionConfiguration;
import uk.ac.gda.tomography.ui.StageType;
import uk.ac.gda.tomography.ui.controller.TomographyParametersAcquisitionController;
import uk.ac.gda.tomography.ui.controller.TomographyParametersAcquisitionController.Positions;
import uk.ac.gda.ui.tool.ClientMessages;
import uk.ac.gda.ui.tool.ClientSWTElements;

/**
 * Creates a drop-down list in the parent Composite to switch between different stages. using a {@link TomographyParametersAcquisitionController} to updates the
 * associated {@link AcquisitionConfiguration#getDevices()} model.
 *
 * @author Maurizio Nagni
 */
public class StagesComposite {

	private final Composite parent;
	private Combo stagesCombo;
	private Button outOfBeam;
	private Composite stageComposite;

	private StageType stageType;

	private final TomographyParametersAcquisitionController controller;

	private static final Logger logger = LoggerFactory.getLogger(StagesComposite.class);

	private StagesComposite(Composite parent, TomographyParametersAcquisitionController controller) {
		this.parent = parent;
		this.controller = controller;
	}

	public static final StagesComposite buildModeComposite(Composite parent, TomographyParametersAcquisitionController controller) {
		StagesComposite pc = new StagesComposite(parent, controller);
		pc.buildStageComposite();
		return pc;
	}

	public StageType getStageType() {
		return stageType;
	}

	private Composite getParent() {
		return parent;
	}

	/**
	 */
	private void buildStageComposite() {
		ClientSWTElements.createLabel(getParent(), SWT.NONE, ClientMessages.STAGE, new Point(2, 1));
		stagesCombo = ClientSWTElements.createCombo(getParent(), SWT.READ_ONLY, getTypes(), ClientMessages.STAGE_TP);
		stageComposite = ClientSWTElements.createComposite(getParent(), SWT.NONE, 1);
		setStageType(StageType.DEFAULT);

		comboStageSelectionListener();

		outOfBeam = ClientSWTElements.createButton(getParent(), SWT.PUSH, ClientMessages.OUT_OF_BEAM, ClientMessages.OUT_OF_BEAM_TP);
		attachOutOfBeamSelectionListener();
	}

	private void comboStageSelectionListener() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Combo source = Combo.class.cast(e.getSource());
				if (source.getSelectionIndex() > -1) {
					filterPerspectiveLabel(getTypes()[source.getSelectionIndex()]).findFirst().ifPresent(p -> setStageType(p));
				}
			}
		};
		stagesCombo.addSelectionListener(listener);
	}

	private void attachOutOfBeamSelectionListener() {
		SelectionListener listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.savePosition(Positions.OUT_OF_BEAM);
			}
		};
		outOfBeam.addSelectionListener(listener);
	}

	private Stream<StageType> filterPerspectiveLabel(final String perspectiveLabel) {
		return Arrays.stream(StageType.values()).filter(p -> p.getLabel().equals(perspectiveLabel));
	}

	private void setStageType(StageType stageType) {
		this.stageType = stageType;
		stagesCombo.setText(stageType.getLabel());
		Arrays.stream(stageComposite.getChildren()).forEach(Control::dispose);
		this.stageType.getStage().getUI(stageComposite);
		stageComposite.layout(true);
	}

	private String[] getTypes() {
		return Arrays.stream(StageType.values()).map(StageType::getLabel).collect(Collectors.toList()).toArray(new String[0]);
	}
}
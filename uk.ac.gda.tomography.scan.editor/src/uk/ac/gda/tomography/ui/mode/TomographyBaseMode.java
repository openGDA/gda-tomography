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

package uk.ac.gda.tomography.ui.mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gda.configuration.properties.LocalProperties;
import gda.device.DeviceException;
import gda.device.IScannableMotor;
import gda.rcp.views.StageCompositeDefinition;
import gda.rcp.views.StageCompositeFactory;
import gda.rcp.views.TabCompositeFactory;
import gda.rcp.views.TabCompositeFactoryImpl;
import gda.rcp.views.TabFolderCompositeFactory;
import uk.ac.gda.client.composites.FinderHelper;
import uk.ac.gda.client.exception.IncompleteModeException;
import uk.ac.gda.tomography.base.TomographyMode;
import uk.ac.gda.tomography.controller.TomoIncompleteModeException;
import uk.ac.gda.tomography.model.DevicePosition;
import uk.ac.gda.ui.tool.ClientMessages;
import uk.ac.gda.ui.tool.ClientMessagesUtility;
import uk.ac.gda.ui.tool.ClientSWTElements;

public abstract class TomographyBaseMode implements TomographyMode {

	private Composite stageControls;
	private final Stage stage;

	// this works for any number of elements:
	private final Map<TomographyDevices, String> devicesMap = new EnumMap<>(TomographyDevices.class);
	private final Map<TomographyDevices, IScannableMotor> motors = new EnumMap<>(TomographyDevices.class);
	private final Map<String, String> metadata = new HashMap<>();

	protected static final Logger logger = LoggerFactory.getLogger(TomographyBaseMode.class);

	protected TomographyBaseMode(Stage stage) {
		super();
		this.stage = stage;
	}

	@Override
	public Map<TomographyDevices, IScannableMotor> getMotors() throws TomoIncompleteModeException {
		if (motors.isEmpty()) {
			loadDevices();
		}
		return Collections.unmodifiableMap(motors);
	}

	@Override
	public Stage getStage() {
		return stage;
	}

	@Override
	public Map<String, String> getMetadata() {
		if (metadata.isEmpty()) {
			loadDevices();
		}
		return metadata;
	}

	@Override
	public Set<DevicePosition<Double>> getMotorsPosition() {
		Set<DevicePosition<Double>> positions = new HashSet<>();
		Map<TomographyDevices, IScannableMotor> intMotors;
		try {
			intMotors = getMotors();
		} catch (TomoIncompleteModeException e) {
			logger.error("TODO put description of error here", e);
			return Collections.unmodifiableSet(positions);
		}
		for (Entry<TomographyDevices, IScannableMotor> entry : intMotors.entrySet()) {
			try {
				Double position = (Double)entry.getValue().getPosition();
				positions.add(new DevicePosition(entry.getKey().name(), position));
			} catch (DeviceException e) {
				logger.error("TODO put description of error here", e);
			}
		}
		return Collections.unmodifiableSet(positions);
	}

	public Map<TomographyDevices, String> getDevicesMap() {
		if (devicesMap.isEmpty()) {
			populateDevicesMap();
		}
		return Collections.unmodifiableMap(devicesMap);
	}

	public Composite getUI(Composite parent) {
		return getStageControls(parent);
	}

	void addToDevicesMap(TomographyDevices device, String propertyName) {
		devicesMap.put(device, propertyName);
	}

	private void loadDevices() {
		for (Entry<TomographyDevices, String> entry : getDevicesMap().entrySet()) {
			if (entry.getKey().name().startsWith("MOTOR")) {
				loadMotor(entry);
			}
			if (entry.getKey().name().startsWith("MALCOLM")) {
				loadMalcolm(entry);
			}
		}
	}

	private void loadMotor(Entry<TomographyDevices, String> entry) {
			try {
				FinderHelper.getIScannableMotor(getBeanId(entry)).ifPresent(c -> motors.put(entry.getKey(), c));
			} catch (IncompleteModeException e) {
				logger.error("Cannot load motor", e);
			}
	}

	private void loadMalcolm(Entry<TomographyDevices, String> entry) {
		try {
			metadata.put(entry.getKey().name(), getBeanId(entry));
		} catch (IncompleteModeException e) {
			logger.error("Error", e);
		}
	}

	private String getBeanId(Entry<TomographyDevices, String> entry) throws IncompleteModeException {
		return Optional.ofNullable(LocalProperties.get(entry.getValue())).orElseThrow(IncompleteModeException::new);
	}

	private Composite getStageControls(Composite parent) {
		if (Objects.isNull(stageControls) || stageControls.isDisposed()) {
			stageControls = ClientSWTElements.createComposite(parent, SWT.NONE, 4);
			createStageControls(SWT.NONE, SWT.BORDER);
			stageControls.pack();
		}
		return stageControls;
	}

	private Composite getStageControls() {
		return stageControls;
	}

	protected final TabCompositeFactory createStageMotorsCompositeFactory(StageCompositeDefinition[] motors, ClientMessages message) {
		TabCompositeFactoryImpl group = new TabCompositeFactoryImpl();
		StageCompositeFactory scf = new StageCompositeFactory();
		group.setCompositeFactory(scf);
		group.setLabel(ClientMessagesUtility.getMessage(message));
		scf.setStageCompositeDefinitions(motors);
		return group;
	}

	protected StageCompositeDefinition createMotorElement(TomographyDevices device, ClientMessages label) throws TomoIncompleteModeException {
		StageCompositeDefinition scd = new StageCompositeDefinition();
		scd.setScannable(getMotors().get(device));
		if (Objects.isNull(scd.getScannable())) {
			throw new TomoIncompleteModeException(String.format("Device %s not found", device));
		}
		scd.setStepSize(1);
		scd.setDecimalPlaces(0);
		scd.setLabel(ClientMessagesUtility.getMessage(label));
		return scd;
	}

	protected final void createStageControls(int labelStyle, int textStyle) {
		TabFolderCompositeFactory motorTabs = new TabFolderCompositeFactory();
		motorTabs.setFactories(getTabsFactories());
		motorTabs.createComposite(getStageControls(), SWT.NONE);
	}

	protected class TabCompositionBuilder {
		private List<TabCompositeFactory> tabComposite = new ArrayList<>();

		public TabCompositionBuilder assemble(StageCompositeDefinition[] stageComposite, ClientMessages label) {
			if (Objects.nonNull(stageComposite)) {
				tabComposite.add(createStageMotorsCompositeFactory(stageComposite, label));
			}
			return this;
		}

		public TabCompositeFactory[] build() {
			return tabComposite.toArray(new TabCompositeFactory[0]);
		}
	}

	protected class StageCompositeDefinitionBuilder {
		private List<StageCompositeDefinition> composite = new ArrayList<>();

		public StageCompositeDefinitionBuilder assemble(TomographyDevices device, ClientMessages label) {
			StageCompositeDefinition scd;
			try {
				scd = createMotorElement(device, label);
			} catch (TomoIncompleteModeException e) {
				return this;
			}
			if (Objects.nonNull(scd)) {
				composite.add(scd);
			}
			return this;
		}

		public StageCompositeDefinition[] build() {
			return composite.toArray(new StageCompositeDefinition[0]);
		}
	}

	protected abstract void populateDevicesMap();

	protected abstract TabCompositeFactory[] getTabsFactories();
}

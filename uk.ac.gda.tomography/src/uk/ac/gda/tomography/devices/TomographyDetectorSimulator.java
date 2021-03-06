/*-
 * Copyright © 2011 Diamond Light Source Ltd.
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

package uk.ac.gda.tomography.devices;

import java.awt.Point;
import java.awt.Rectangle;

import gda.device.DeviceException;
import gda.factory.FindableBase;
import uk.ac.gda.api.remoting.ServiceInterface;

@ServiceInterface(ITomographyDetector.class)
public class TomographyDetectorSimulator extends FindableBase implements ITomographyDetector {

	@Override
	public void setExposureTime(double collectionTime) throws Exception {

	}

	@Override
	public void acquireMJpeg(Double acqTime, int binX, int binY, Double scale, Double offset) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setZoomRoiStart(Point roiStart) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setupZoomMJpeg(Rectangle roi, Point bin) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Integer getRoi1BinX() throws Exception {
		return 1;
	}

	@Override
	public Integer getRoi2BinX() throws Exception {
		return 1;
	}

	@Override
	public void enableFlatField() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableFlatField() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTiffFilePath() throws Exception {
		return null;
	}

	@Override
	public String getTiffFileName() throws Exception {
		return null;
	}

	@Override
	public String getTiffFileTemplate() throws Exception {
		return null;
	}

	@Override
	public void setTiffFileNumber(int fileNumber) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String demandRaw(Double acqTime, String demandRawFilePath, String demandRawFileName, Boolean isHdf,
			Boolean isFlatFieldCorrectionRequired, Boolean demandWhileStreaming) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String takeFlat(double expTime, int numberOfImages, String fileLocation, String fileName,
			String filePathTemplate) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTiffImageFileName() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String takeDark(int numberOfImages, double acqTime, String fileLocation, String fileName,
			String filePathTemplate) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setHdfFormat(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetFileFormat() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isHdfFormat() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetAll() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetAfterTiltToInitialValues() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRoi1ScalingDivisor(double divisor) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDetectorName() {
		return null;
	}

	@Override
	public boolean isAcquiring() throws DeviceException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setExternalTriggered(Boolean val) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initDetector() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableDarkSubtraction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enableDarkSubtraction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setupForTilt(int minY, int maxY, int minX, int maxX) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public double getProc1Scale() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setProc1Scale(double newScale) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProcScale(double factor) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOffsetAndScale(double offset, double scale) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void resumeAcquisition() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setupHistoStatCollection(int binSize) throws Exception {
		// TODO Auto-generated method stub

	}

}

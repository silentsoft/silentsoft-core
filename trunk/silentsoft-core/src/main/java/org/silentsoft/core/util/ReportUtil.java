package org.silentsoft.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportUtil.class);
	
	public enum ReportCase {
		BUILD,
		DEPLOY,
		RECOVERY,
		TESTRESULT,
		AUTHENTICATE
	};

	public enum ReportContent {
		fctName,
		hostIp,
		version,
		masterPath,
		backupPath,
		srcJsonPath,
		srcZipPath,
		tarJsonPath,
		tarZipPath,
		tarSecPath,
		ntpPath,
		successYn,
		errorMsg,
		backupTime,
		buildTime,
		deployTime,
		recoveryTime,
		testTime,
		authenticateTime
	};
	
	private CSVUtil report;
	String[] headers = null;
	
	public ReportUtil(ReportCase reportCase, String reportPath) {
		report = new CSVUtil();
		
		switch (reportCase) {
		case BUILD:
			headers = new String[]{
				ReportContent.fctName.toString(),
				ReportContent.version.toString(),
				ReportContent.srcZipPath.toString(),
				ReportContent.tarZipPath.toString(),
				ReportContent.ntpPath.toString(),
				ReportContent.successYn.toString(),
				ReportContent.errorMsg.toString(),
				ReportContent.buildTime.toString()
			};
			break;
		case DEPLOY:
			headers = new String[]{
				ReportContent.fctName.toString(),
				ReportContent.hostIp.toString(),
				ReportContent.version.toString(),
				ReportContent.masterPath.toString(),
				ReportContent.backupPath.toString(),
				ReportContent.backupTime.toString(),
				ReportContent.srcJsonPath.toString(),
				ReportContent.srcZipPath.toString(),
				ReportContent.tarJsonPath.toString(),
				ReportContent.tarZipPath.toString(),
				ReportContent.successYn.toString(),
				ReportContent.errorMsg.toString(),
				ReportContent.deployTime.toString()
			};
			break;
		case RECOVERY:
			headers = new String[]{
				ReportContent.fctName.toString(),
				ReportContent.hostIp.toString(),
				ReportContent.backupPath.toString(),
				ReportContent.masterPath.toString(),
				ReportContent.successYn.toString(),
				ReportContent.errorMsg.toString(),
				ReportContent.recoveryTime.toString()
			};
			break;
		case TESTRESULT:
			headers = new String[]{
				ReportContent.fctName.toString(),
				ReportContent.hostIp.toString(),
				ReportContent.version.toString(),
				ReportContent.tarZipPath.toString(),
				ReportContent.successYn.toString(),
				ReportContent.errorMsg.toString(),
				ReportContent.testTime.toString()
			};
			break;
		case AUTHENTICATE:
			headers = new String[]{
				ReportContent.hostIp.toString(),
				ReportContent.tarSecPath.toString(),
				ReportContent.successYn.toString(),
				ReportContent.errorMsg.toString(),
				ReportContent.authenticateTime.toString()
			};
			break;
		}
		
		report.make(reportPath, headers);
		report.save();
	}
	
	public boolean write(String[] contents) {
		int nConentCnt = contents.length;
		int nHeaderCnt = headers.length;
		if (nHeaderCnt != nConentCnt) {
			LOGGER.error("Report header size<{}> and content size<{}> is mismatch !",
					new Object[]{nHeaderCnt, nConentCnt});
			return false;
		}
		
		int nRowIdx = report.getDataSet().getRowCount();
		report.getDataSet().insertRow(nRowIdx);
		
		for(int i=0; i<nHeaderCnt; i++) {
			report.getDataSet().setDataByName(headers[i], nRowIdx, contents[i]);
		}
		
		report.save();
		
		return true;
	}
}

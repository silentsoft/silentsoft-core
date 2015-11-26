package org.silentsoft.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.silentsoft.core.CommonConst;
import org.silentsoft.io.data.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CSVUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtil.class);
	
	private String path;
	
	private DataSet dataSet;
	
	public DataSet getDataSet() {
		return this.dataSet;
	}
	
	/**
	 * if file is exist and load successfully return true. nor false.
	 * in this case, you need to use make method.
	 * @param path : path of csv
	 */
	public boolean load(String path){
		this.path = path;

		if (isLoadable()) {
			this.dataSet = new DataSet(getHeaders());
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(this.path));
				
				int nRowCount = 0;
				int contentsCount = 0;
				boolean isHeadLine = true;
				String[] contents = null;
				
				String str = "";
				while ((str=br.readLine()) != null) {
					if (isHeadLine) {
						isHeadLine = false;
						continue;
					}

					this.dataSet.insertRow(nRowCount);
					contents = str.split(CommonConst.COMMA);
					contentsCount = contents.length;
					for (int i=0; i<contentsCount; i++) {
						this.dataSet.setData(nRowCount, i, contents[i]);
					}
					
					nRowCount++;
				}
				
				br.close();
			} catch (IOException e) {
				LOGGER.error("I got catch IOException <{}>", e);
			}
		} else {
			LOGGER.error("could not load the csv file <{}>", new Object[]{path});
			return false;
		}
		
		return true;
	}
	
	/**
	 * save changed information at dataset to csv file.
	 */
	public void save() {
		File file = new File(this.path);
		
		try {
			if (file.exists()) {
				file.delete();
			}
			
			file.createNewFile();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.path));
			StringBuffer sb = new StringBuffer();
			
			int nRowCnt = this.dataSet.getRowCount();
			int nColCnt = this.dataSet.getColumnCount();
			
			//to write header info
			for (int i=0; i<nColCnt; i++) {
				String endOfComma = (i>=nColCnt-1) ? CommonConst.NULL_STR : CommonConst.COMMA;
				String endOfEnter = (i>=nColCnt-1) ? CommonConst.ENTER : CommonConst.NULL_STR;
				sb.append(this.dataSet.getColumnName(i) + endOfComma + endOfEnter);
			}
			
			//to write contents
			for (int i=0; i<nRowCnt; i++) {
				for (int j=0; j<nColCnt; j++) {
					String endOfComma = (j>=nColCnt-1) ? CommonConst.NULL_STR : CommonConst.COMMA;
					String endOfEnter = (j>=nColCnt-1) ? CommonConst.ENTER : CommonConst.NULL_STR;
					sb.append(this.dataSet.getData(i, j) + endOfComma + endOfEnter);
				}
			}
			
			bw.write(sb.toString());
			bw.close();
		} catch (IOException e) {
			LOGGER.error("I got catch IOException <{}>", e);
		}
	}
	
	/**
	 * if make first time, you need to use this method.
	 * if file is already exist, then delete and make again automatically
	 * @param path 		  : path of csv
	 * @param header	  : column of csv
	 */
	public void make(String path, String[] header) {
		this.path = path;
		this.dataSet = new DataSet(header);
			
		save();
	}
	
	private boolean isLoadable() {
		File file = new File(this.path);
		
		if (!file.exists()) {
			return false;
		} else if (file.length() <= 0) {
			return false;
		}
		
		return true;
	}
	
	private String[] getHeaders() {
		String[] headers = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.path));
			
			String str = "";
			while ((str=br.readLine()) != null) {
				headers = str.split(CommonConst.COMMA);
				break;
			}
			
			br.close();
		} catch (IOException e) {
			LOGGER.error("I got catch IOException <{}>", e);
		}
		
		return headers;
	}
	
}

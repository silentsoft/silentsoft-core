package org.silentsoft.core.util;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class INIUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(INIUtil.class);
	
	private String path;
	
	/**
	 * param1 : String : name of section
	 * param2 : String : name of key
	 * param3 : Object : data of key
	 */
	private Map<String, Map<String, Object>> iniUtil;
	
	public INIUtil(String path) {
		this.path = path;
		iniUtil = new LinkedHashMap<String, Map<String, Object>>();
		
		if (isExists()) {
			try {
				String str = "";
				String section = "";
				String[] array;
				BufferedReader br = new BufferedReader(new FileReader(this.path));
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				
				while ((str=br.readLine()) != null) {
					if (str.startsWith("[") && str.endsWith("]")) {
						if (str.length() > 2) {
							section = str.substring(1, str.length()-1);
							data = new LinkedHashMap<String, Object>();
						}
					} else {
						array = str.split("=");
						if (array.length != 2) continue;
						data.put(array[0], array[1]);
						iniUtil.put(section, data);
					}
				}
				
				br.close();
			} catch (IOException e) {
				LOGGER.error("I got catch IOException <{}>", e);
			}
		}
	}
	
	public boolean isExists() {
		return new File(this.path).exists();
	}
	
	public String getData(String section, String key) {
		String data = "";

		try {
			data = (String) iniUtil.get(section).get(key);
		} catch (Exception e) {
			;
		}
		
		return data;
	}
	
	public void setData(String section, String key, Object data) {
		if (iniUtil.get(section) != null) {
			iniUtil.get(section).put(key, data);
		} else {
			Map<String, Object> newData = new LinkedHashMap<String, Object>();
			newData.put(key, data);
			iniUtil.put(section, newData);
		}
		
		try {
			Boolean isFirst = true;
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.path));
			String secName, keyName, enterStr;
			StringBuffer sBuffer = new StringBuffer();
			
			Iterator<String> sectionItr = iniUtil.keySet().iterator();
			while (sectionItr.hasNext()) {
				if (!isFirst) sBuffer.append("\r\n");
				
				secName = sectionItr.next();
				sBuffer.append("["+secName+"]\r\n");
				isFirst = false;
				
				Iterator<String> keyItr = iniUtil.get(secName).keySet().iterator();
				while (keyItr.hasNext()) {
					keyName = keyItr.next();
					enterStr = (!sectionItr.hasNext() && !keyItr.hasNext() ? "" : "\r\n");
					sBuffer.append(keyName+"="+iniUtil.get(secName).get(keyName)+enterStr);
				}
			}
			
			bw.write(sBuffer.toString());
			bw.close();
		} catch (IOException e) {
			LOGGER.error("I got catch IOException <{}>", e);
		}
	}
}

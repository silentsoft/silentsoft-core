package org.silentsoft.core.item;

import java.util.ArrayList;

import org.silentsoft.core.pojo.FilePOJO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonFormat(shape = Shape.OBJECT)
@JsonIgnoreProperties(value = { "empty", "length" })
public class StoreItem extends ArrayList<FilePOJO> {
	
	private static final long serialVersionUID = 2792868376739341803L;

	private String tag;

	private String sender;

	public StoreItem() {
	}

	public StoreItem(String tag) {
		setTag(tag);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public long getLength() {
		return this.stream().mapToLong(FilePOJO::getLength).sum();
	}

	public ArrayList<FilePOJO> getValues() {
		return new ArrayList<FilePOJO>(this);
	}

	/**
	 * Do not use this method manually. This method is designed for json parsing.
	 * @param values
	 * @author silentsoft
	 */
	public void setValues(ArrayList<FilePOJO> values) {
		this.clear();
		this.addAll(values);
	}
}


package org.linlinjava.litemall.admin.vo;

import java.util.List;

public class CatVo {
	private Integer value = null;
	private String label = null;
	@SuppressWarnings("rawtypes")
	private List children = null;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@SuppressWarnings("rawtypes")
	public List getChildren() {
		return children;
	}

	@SuppressWarnings("rawtypes")
	public void setChildren(List children) {
		this.children = children;
	}
}
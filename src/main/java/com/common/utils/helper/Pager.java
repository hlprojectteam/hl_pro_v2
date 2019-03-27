package com.common.utils.helper;

import java.util.List;

@SuppressWarnings({ "rawtypes", "serial" })
public class Pager implements java.io.Serializable {
    
	private long pageCount; //总页数
	private long rowCount; //总条数
    private List pageList; //列表
  
    public Pager(long pageCount,long rowCount,List pageList){  
    	this.setPageCount(pageCount);
    	this.setRowCount(rowCount);
    	this.setPageList(pageList);
     }      
    
	public long getPageCount() {
		return pageCount;
	}
	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}
	public long getRowCount() {
		return rowCount;
	}
	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
	public List getPageList() {
		return pageList;
	}
	public void setPageList(List pageList) {
		this.pageList = pageList;
	}


}

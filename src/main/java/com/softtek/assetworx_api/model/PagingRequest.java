package com.softtek.assetworx_api.model;

import java.util.ArrayList;
import java.util.List;

public class PagingRequest {

    private int start;
    
    private int length;
    
    private int draw;
    
    private List<Order> order;
    
    private List<Column> columns;
    
    private Search search;
    
    private String entity;
    
    private String type;
    
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public List<Order> getOrder() {
		return order;
	}
	public void setOrder(List<Order> order) {
		this.order = order;
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	public void addColumn(Column column) {
		if(this.columns == null) {
			this.columns = new ArrayList<Column>();
		}
		this.columns.add(column);
	}
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
    

}

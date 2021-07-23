package com.softtek.assetworx_api.model;

public class Column {

    private String data;
    
    private String name;
    
    private Boolean searchable;
    
    private Boolean orderable;
    
    private Search search;
    
    private Boolean visible;
    
    private String type;
    
    
    
	public Column(String data, String name, Boolean searchable, Boolean orderable, Search search, Boolean visible) {
		super();
		this.data = data;
		this.name = name;
		this.searchable = searchable;
		this.orderable = orderable;
		this.search = search;
		this.visible = visible;
	}
	
	public Column() {
		super();
	}

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getSearchable() {
		return searchable;
	}
	public void setSearchable(Boolean searchable) {
		this.searchable = searchable;
	}
	public Boolean getOrderable() {
		return orderable;
	}
	public void setOrderable(Boolean orderable) {
		this.orderable = orderable;
	}
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public static Column createColumn(String value, SearchOperation searchOperation, String data, String name) {
		Search search = new Search(value, "", searchOperation);
		Column column = new Column(data, name, true, true, search, false);
		return column;
	}
	
    
}

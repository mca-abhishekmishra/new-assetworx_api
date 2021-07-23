package com.softtek.assetworx_api.model;


public class Search {

    private String value;
    
    private String regexp;
    
    private SearchOperation searchOperation;
    
    
    
	public Search() {
		super();
	}
	public Search(String value, String regexp, SearchOperation searchOperation) {
		super();
		this.value = value;
		this.regexp = regexp;
		this.searchOperation = searchOperation;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRegexp() {
		return regexp;
	}
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}
	public SearchOperation getSearchOperation() {
		return searchOperation;
	}
	public void setSearchOperation(SearchOperation searchOperation) {
		this.searchOperation = searchOperation;
	}
	
	
    
    
}

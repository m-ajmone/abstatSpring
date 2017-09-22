package com.summarization.experiments;

import java.util.Set;
import java.util.TreeSet;

public class Concept {
	
	private String URI;
	private boolean isInternal;
	private TreeSet<Integer> depths_internal;    //it considers depth based on paths that only envolves internal concepts
	private TreeSet<Integer> depths;             // it considers depth based on every path to 
	private String color; 
	
	public Concept(String URI, boolean isInternal){
		this.URI = URI;
		this.isInternal = isInternal;
		depths_internal = new TreeSet<Integer>();
		depths = new TreeSet<Integer>();
		color = "white";
	}
	
	public Concept(String URI){
		this.URI = URI;
		depths_internal = new TreeSet<Integer>();
		depths = new TreeSet<Integer>();
		color = "white";
	}
	
	public String getURI(){ return URI; }
	public void setURI(String arg) { URI = arg; } 
	
	public boolean isInternal(){ return isInternal; }
	
	public TreeSet<Integer> getDepthsInternal(){ return depths_internal; }
	public void addDepthsInternal(Set<Integer> arg) { depths_internal.addAll(arg); }
	
	public TreeSet<Integer> getDepths(){ return depths; }
	public void addDepths(Set<Integer> arg) { depths.addAll(arg); }
	
	public String getColor(){ return color; }
	public void setColor(String arg){ color = arg; }
	
	public String toString(){
		return URI ;//+ "$$" + depths_internal + depths ;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((URI == null) ? 0 : URI.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Concept other = (Concept) obj;
		if (URI == null) {
			if (other.URI != null)
				return false;
		} else if (!URI.equals(other.URI))
			return false;
		return true;
	}

}
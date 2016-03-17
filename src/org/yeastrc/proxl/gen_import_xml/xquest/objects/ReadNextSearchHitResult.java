package org.yeastrc.proxl.gen_import_xml.xquest.objects;

import java.util.Map;

public class ReadNextSearchHitResult {


	private Map<String,String> spectrumSearchContents;
	private Map<String,String> searchHitContents;
	
	
	public Map<String, String> getSpectrumSearchContents() {
		return spectrumSearchContents;
	}
	public void setSpectrumSearchContents(Map<String, String> spectrumSearchContents) {
		this.spectrumSearchContents = spectrumSearchContents;
	}
	public Map<String, String> getSearchHitContents() {
		return searchHitContents;
	}
	public void setSearchHitContents(Map<String, String> searchHitContents) {
		this.searchHitContents = searchHitContents;
	}
}

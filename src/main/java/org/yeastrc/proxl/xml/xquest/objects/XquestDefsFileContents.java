package org.yeastrc.proxl.xml.xquest.objects;

import java.io.File;
import java.util.List;

import org.yeastrc.proxl_import.api.xml_dto.ConfigurationFile;
import org.yeastrc.proxl_import.api.xml_dto.StaticModification;

/**
 * 
 *
 */
public class XquestDefsFileContents {
	

	private String fastaFilename;
	private File fastaFile;


	private List<StaticModification> xquestStaticMods;
	private ConfigurationFile xquestDefsConfigurationFile;


	public String getFastaFilename() {
		return fastaFilename;
	}


	public void setFastaFilename(String fastaFilename) {
		this.fastaFilename = fastaFilename;
	}


	public List<StaticModification> getXquestStaticMods() {
		return xquestStaticMods;
	}


	public void setXquestStaticMods(List<StaticModification> xquestStaticMods) {
		this.xquestStaticMods = xquestStaticMods;
	}



	public ConfigurationFile getXquestDefsConfigurationFile() {
		return xquestDefsConfigurationFile;
	}


	public void setXquestDefsConfigurationFile(
			ConfigurationFile xquestDefsConfigurationFile) {
		this.xquestDefsConfigurationFile = xquestDefsConfigurationFile;
	}


	public File getFastaFile() {
		return fastaFile;
	}
	public void setFastaFile(File fastaFile) {
		this.fastaFile = fastaFile;
	} 
	

	
	

}

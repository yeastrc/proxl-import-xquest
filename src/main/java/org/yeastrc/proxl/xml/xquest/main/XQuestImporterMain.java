package org.yeastrc.proxl.xml.xquest.main;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.proxl.xml.xquest.builder.MatchedProteinsBuilder;
import org.yeastrc.proxl.xml.xquest.exceptions.ProxlGenXMLDataException;
import org.yeastrc.proxl.xml.xquest.objects.XquestDefsFileContents;
import org.yeastrc.proxl.xml.xquest.objects.XquestXprophDefsFileContents;
import org.yeastrc.proxl.xml.xquest.readers.XquestDefsFileReader;
import org.yeastrc.proxl.xml.xquest.readers.XquestXprophDefsFileReader;
import org.yeastrc.proxl.xml.xquest.search_program_info_population.AddSearchProgramAndAnnotationTypeRecords;
import org.yeastrc.proxl_import.api.xml_dto.ConfigurationFile;
import org.yeastrc.proxl_import.api.xml_dto.ConfigurationFiles;
import org.yeastrc.proxl_import.api.xml_dto.DecoyLabel;
import org.yeastrc.proxl_import.api.xml_dto.DecoyLabels;
import org.yeastrc.proxl_import.api.xml_dto.Linker;
import org.yeastrc.proxl_import.api.xml_dto.Linkers;
import org.yeastrc.proxl_import.api.xml_dto.ProxlInput;
import org.yeastrc.proxl_import.create_import_file_from_java_objects.main.CreateImportFileFromJavaObjectsMain;

/**
 * 
 *
 */
public class XQuestImporterMain {

	private static final Logger log = Logger.getLogger(XQuestImporterMain.class);
	
	/**
	 * private constructor
	 */
	private XQuestImporterMain(){}
	
	public static XQuestImporterMain getInstance() {
		
		return new XQuestImporterMain();
	}
	

	public void doGenFile(String linkerName, String decoyPrefix, File resultsDirectory, File outputFile) throws Exception {

		//  The object graph that will be serialized to generate the import XML file 
		
		ProxlInput proxlInputRoot = new ProxlInput();
		

		XquestDefsFileContents xquestDefsFileContents =
				XquestDefsFileReader.getInstance().getXquestDefsFileContents( resultsDirectory );
        
		
		XquestXprophDefsFileContents xquestXprophDefsFileContents =
				XquestXprophDefsFileReader.getInstance().getXquestXprophDefsFileContents( resultsDirectory );
		

		ConfigurationFiles configurationFiles = new ConfigurationFiles();
		proxlInputRoot.setConfigurationFiles( configurationFiles );
		
		List<ConfigurationFile> configurationFileList = configurationFiles.getConfigurationFile();
		
		configurationFileList.add( xquestDefsFileContents.getXquestDefsConfigurationFile() );
		
		configurationFileList.add( xquestXprophDefsFileContents.getXquestXprophDefsConfigurationFile() );
		
		

		String xquestVersion = ProcessXQuestMainFile.getInstance().getXquestVersionNumber( resultsDirectory /* xquestDataDir */ );



		AddSearchProgramAndAnnotationTypeRecords.getInstance()
		.addSearchProgramAndAnnotationTypeRecords( proxlInputRoot, xquestVersion );
		
		
		File fastaFileObj = xquestDefsFileContents.getFastaFile();
		
		if ( ! fastaFileObj.exists() ) {
			
			String msg = "fasta file from XQuest Defs file does not exist: " + fastaFileObj.getCanonicalPath(); 
			System.err.println( msg );
			throw new ProxlGenXMLDataException(msg);
		}
		
		
		System.out.println( "FASTA file from defs file: " + fastaFileObj.getCanonicalPath() );
		
		
		String fastaFilename = xquestDefsFileContents.getFastaFilename();
		
		proxlInputRoot.setFastaFilename( fastaFilename );
		
		
		
		if ( StringUtils.isNotEmpty( decoyPrefix ) ) {

			DecoyLabels decoyLabels = new DecoyLabels();
			proxlInputRoot.setDecoyLabels( decoyLabels );

			List<DecoyLabel> decoyLabelList = decoyLabels.getDecoyLabel();

			DecoyLabel decoyLabel = new DecoyLabel();
			decoyLabelList.add( decoyLabel );

			decoyLabel.setPrefix( decoyPrefix );
		}
		

		Linkers linkers = new Linkers();
		proxlInputRoot.setLinkers( linkers );

		List<Linker> linkerList = linkers.getLinker();
		Linker linker = new Linker();
		linkerList.add( linker );
		linker.setName( linkerName );

		try {
		
			ProcessXQuestMainFile.getInstance().importXquestData( resultsDirectory, decoyPrefix, proxlInputRoot );
			
			Set<String> decoyIdentificationStringFromConfFileSet = new HashSet<>();
			
			if ( StringUtils.isNotEmpty( decoyPrefix ) ) {
				decoyIdentificationStringFromConfFileSet.add( decoyPrefix );
			}

			MatchedProteinsBuilder.getInstance().buildMatchedProteins( proxlInputRoot, fastaFileObj, decoyIdentificationStringFromConfFileSet );
			
			try {
			
				CreateImportFileFromJavaObjectsMain.getInstance().createImportFileFromJavaObjectsMain( outputFile, proxlInputRoot );
				
			} catch ( Exception e ) {
				
				String msg = "Error writing output file: " + e.toString();
				log.error( msg, e );
				
				throw new Exception(msg, e);
			}

			
			
			
		} catch ( Exception e ) {
			
			System.out.println( "Exception in processing" );
			System.err.println( "Exception in processing" );
			
			e.printStackTrace( System.out );
			e.printStackTrace( System.err );
			
			
			throw e;
		}
		
		
	}
}

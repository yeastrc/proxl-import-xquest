package org.yeastrc.proxl.xml.xquest.readers;

import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.yeastrc.proxl.xml.xquest.constants.SearchProgramConstants;
import org.yeastrc.proxl.xml.xquest.constants.XquestFilenameConstants;
import org.yeastrc.proxl.xml.xquest.objects.XquestXprophDefsFileContents;
import org.yeastrc.proxl_import.api.xml_dto.ConfigurationFile;

public class XquestXprophDefsFileReader {

	private static Logger log = Logger.getLogger(XquestXprophDefsFileReader.class);
			
	
	
	//  Keys in the Def file
	
	// prevent direct instantiation of XMLFileReader
	private XquestXprophDefsFileReader() {
	}

	/**
	 * get an instance of XquestDefsFileReader
	 * @return an instance of XquestDefsFileReader
	 * @throws Exception 
	 */
	public static XquestXprophDefsFileReader getInstance( ) throws Exception {
		
		XquestXprophDefsFileReader reader = new XquestXprophDefsFileReader();
		
		return reader;
	}

	
	/**
	 * @param resultsPathFile
	 * @return
	 * @throws Exception
	 */
	public XquestXprophDefsFileContents getXquestXprophDefsFileContents(File resultsPathFile ) throws Exception {
		

		File defsFile = new File( resultsPathFile, XquestFilenameConstants.XQUEST_XPROPH_DEF_FILENAME );
		

		
		long xquestXprophDefsFileSize = defsFile.length();
		
		if ( xquestXprophDefsFileSize > Integer.MAX_VALUE ) {
			
			String msg = "Xquest Xproph defs file is larger than Integer.MAX_VALUE so unable to read into byte[] (file: " + defsFile.getAbsolutePath() + ") .";
			log.error( msg );
			throw new Exception( msg );
		}
		
		

		//  Get contents of Xquest Xproph defs file as bytes
		
		
		
		byte[] xquestXprophDefsFileBytes = new byte[ (int) xquestXprophDefsFileSize ];
		
		FileInputStream xquestXprophDefsFileFileInputStream = null;
		
		try {
			
			xquestXprophDefsFileFileInputStream = new FileInputStream( defsFile );
			
			
			int bytesRead = xquestXprophDefsFileFileInputStream.read( xquestXprophDefsFileBytes );
			
			if ( bytesRead != xquestXprophDefsFileSize ) {
			
				String msg = "ERROR: bytesRead != xquestXprophDefsFileSize: Xquest Xproph defs file: " + defsFile.getAbsolutePath();

				log.error( msg );

				throw new Exception(msg);
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "ERROR: Reading into byte[]: Xquest Xproph defs file: " + defsFile.getAbsolutePath();
			
			log.error( msg, e );
			
			throw e;
			
		} finally {
			
			if ( xquestXprophDefsFileFileInputStream != null ) {
				xquestXprophDefsFileFileInputStream.close();
			}
		}
		
		
		
		ConfigurationFile xquestXprophDefsConfigurationFile = new ConfigurationFile();
		
		xquestXprophDefsConfigurationFile.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
		xquestXprophDefsConfigurationFile.setFileName( defsFile.getName() );
		xquestXprophDefsConfigurationFile.setFileContent( xquestXprophDefsFileBytes );
		

		XquestXprophDefsFileContents xquestXprophDefsFileContents = new XquestXprophDefsFileContents();
		
		xquestXprophDefsFileContents.setXquestXprophDefsConfigurationFile( xquestXprophDefsConfigurationFile );
		
		
		return xquestXprophDefsFileContents;
	}
	
	

}

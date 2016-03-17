package org.yeastrc.proxl.gen_import_xml.xquest.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.SearchProgramConstants;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.XquestFilenameConstants;
import org.yeastrc.proxl.gen_import_xml.xquest.objects.XquestDefsFileContents;
import org.yeastrc.proxl_import.api.xml_dto.ConfigurationFile;
import org.yeastrc.proxl_import.api.xml_dto.StaticModification;

public class XquestDefsFileReader {

	private static Logger log = Logger.getLogger(XquestDefsFileReader.class);
			
	
	//  Keys in the Def file
	
	/**
	 * fasta filename
	 */
	private static final String FASTA_FILE_CONFIG_KEY = "database";
	
	private static final String MODIFICATIONS_FIXED_START_CONFIG_KEY = "modifications fixed";
	

	// prevent direct instantiation of XMLFileReader
	private XquestDefsFileReader() {
	}

	/**
	 * get an instance of XquestDefsFileReader
	 * @return an instance of XquestDefsFileReader
	 * @throws Exception 
	 */
	public static XquestDefsFileReader getInstance( ) throws Exception {
		
		XquestDefsFileReader reader = new XquestDefsFileReader();
		
		return reader;
	}

	
	/**
	 * @param resultsPathFile
	 * @return
	 * @throws Exception
	 */
	public XquestDefsFileContents getXquestDefsFileContents( File resultsPathFile ) throws Exception {
		

		File defsFile = new File( resultsPathFile, XquestFilenameConstants.XQUEST_DEF_FILENAME );
		

		XquestDefsFileContents xquestDefsFileContents = new XquestDefsFileContents();
		

		List<StaticModification> xquestStaticMods = new ArrayList<>();
		xquestDefsFileContents.setXquestStaticMods( xquestStaticMods );
		
		
		String fastaFilenamePossiblyWithPath = null;
		
			
		if ( ! defsFile.exists() ) {

			String msg = "Xquest defs file does not exist (file: " + defsFile.getAbsolutePath() + ") .";
			log.error( msg );
			throw new Exception( msg );
		}

		long xquestDefsFileSize = defsFile.length();
		
		if ( xquestDefsFileSize > Integer.MAX_VALUE ) {
			
			String msg = "Xquest defs file is larger than Integer.MAX_VALUE so unable to read into byte[] (file: " + defsFile.getAbsolutePath() + ") .";
			log.error( msg );
			throw new Exception( msg );
		}
		
		

		
		
		BufferedReader reader = null;
		
		try {
			
			reader = new BufferedReader( new FileReader( defsFile ) ); 

			String line = null;
			
			boolean inModificationsFixed = false;
			
			
			while ( ( line = reader.readLine() ) != null ) {
				
				LineParsed lineParsed = parseLine( line );
				
				if ( lineParsed == null ) {
					
					inModificationsFixed = false;  // exit on empty line
					
					continue;  // skip to next line
				}
				
				if ( FASTA_FILE_CONFIG_KEY.equals( lineParsed.getKey() ) ) {

					fastaFilenamePossiblyWithPath = lineParsed.getValue();
				}
				
				//  above where set so don't process line with text MODIFICATIONS_FIXED_START_CONFIG_KEY
				if ( inModificationsFixed ) {
					
					
					StaticModification staticMod = getXquestStaticMod( lineParsed, line );
					
					if ( staticMod != null ) {
					
						xquestStaticMods.add( staticMod );
					}
				}
	
				
				
				if ( line.startsWith( MODIFICATIONS_FIXED_START_CONFIG_KEY ) ) {
					
					inModificationsFixed = true;
				}
			}
			
			
			validateStaticMods( xquestStaticMods, defsFile );
			
			

			if ( fastaFilenamePossiblyWithPath == null ) {
				
				String msg = "Xquest Defs file does not contain fasta filename key '" + FASTA_FILE_CONFIG_KEY 
						+ "' ( Xquest Defs file: " + defsFile.getAbsolutePath() + ") .";
				log.error( msg );
				throw new Exception( msg );
			}
			
			if ( fastaFilenamePossiblyWithPath.isEmpty() ) {
				
				String msg = "Xquest Defs file does not contain a value for fasta filename key '" + FASTA_FILE_CONFIG_KEY 
						+ "' ( Xquest Defs file: " + defsFile.getAbsolutePath() + ") .";
				log.error( msg );
				throw new Exception( msg );
			}
			
			File fastaFileObj = new File( fastaFilenamePossiblyWithPath );
			
			xquestDefsFileContents.setFastaFilename( fastaFileObj.getName() );
			
			
			
		} catch ( Exception e ) {
			
			String msg = "Xquest Defs file: " + defsFile.getAbsolutePath();
			
			log.error( msg, e );
			
			throw e;
			
		} finally {
			
			if ( reader != null ) {
				reader.close();
			}
		}
		
		
		

		//  Get contents of Xquest Defs file as bytes
		
		
		
		
		byte[] xquestDefsFileBytes = new byte[ (int) xquestDefsFileSize ];
		
		FileInputStream xquestDefsFileFileInputStream = null;
		
		try {
			
			xquestDefsFileFileInputStream = new FileInputStream( defsFile );
			
			
			int bytesRead = xquestDefsFileFileInputStream.read( xquestDefsFileBytes );
			
			if ( bytesRead != xquestDefsFileSize ) {
			
				String msg = "ERROR: bytesRead != xquestDefsFileSize: Xquest Defs file: " + defsFile.getAbsolutePath();

				log.error( msg );

				throw new Exception(msg);
			}
			
			
		} catch ( Exception e ) {
			
			String msg = "ERROR: Reading into byte[]: Xquest Defs file: " + defsFile.getAbsolutePath();
			
			log.error( msg, e );
			
			throw e;
			
		} finally {
			
			if ( xquestDefsFileFileInputStream != null ) {
				xquestDefsFileFileInputStream.close();
			}
		}
		

		ConfigurationFile xquestDefsConfigurationFile = new ConfigurationFile();
		
		xquestDefsConfigurationFile.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
		
		xquestDefsConfigurationFile.setFileName( defsFile.getName() );
		
		xquestDefsConfigurationFile.setFileContent( xquestDefsFileBytes );
		
		xquestDefsFileContents.setXquestDefsConfigurationFile( xquestDefsConfigurationFile );
		
		
		
		return xquestDefsFileContents;
	}
	
	
	
	
	/**
	 * @param lineParsed
	 * @param line
	 * @return
	 * @throws Exception 
	 */
	private StaticModification getXquestStaticMod( LineParsed lineParsed, String line ) throws Exception {
		
		
		String residue = lineParsed.getKey();
		
		if ( StringUtils.isEmpty( residue ) ) {
			
			return null;  //  EARLY EXIT   skip entries with residue is empty
		}
		
		String massString = lineParsed.getValue();
		
		if ( "0".equals( massString ) ) {
			
			return null;  //  EARLY EXIT   skip entries with mass of zero
		}
		
		
		BigDecimal mass = null;
		
		try {
			
			mass = new BigDecimal( massString );
			
		} catch ( Exception e ) {
			
			
			String msg = "Config key '" + MODIFICATIONS_FIXED_START_CONFIG_KEY + "' mass is not parsable to BigDecimal.  mass: " 
					+ massString + ", line: " + line;
			
			log.error( msg, e );
			
			throw new Exception(msg, e);
			
		}
		
		if ( BigDecimal.ZERO.equals( mass ) ) {
			
			return null;  //  EARLY EXIT   skip entries with mass of zero
		}
		
		
		StaticModification staticModification = new StaticModification();
		
		staticModification.setAminoAcid( residue );
		staticModification.setMassChange( mass );
		
		return staticModification;
	}
	

	/**
	 * Validate same residue is not in the list more than once.
	 * @param staticMods
	 * @throws Exception 
	 */
	private void validateStaticMods( List<StaticModification> staticMods, File xquestDefsFile ) throws Exception {
		
		Set<String> residueSet = new HashSet<>();
		
		
		for ( StaticModification staticModDTO : staticMods ) {
			
			String residue = staticModDTO.getAminoAcid();
			
			if ( residueSet.contains(residue)) {
				
				
				String msg = "ERROR: Duplicate residue entries for config key '" 
						+ MODIFICATIONS_FIXED_START_CONFIG_KEY + "' , Xquest defs file: " + xquestDefsFile.getAbsolutePath();
				
				log.error( msg );
				
				throw new Exception(msg);
			}
			
			residueSet.add( residue );
		}
		
	}
	
	
	
	
	/**
	 * @param line
	 * @return
	 */
	private LineParsed parseLine( String line ) {
		
		//  Remove comment part of line, starts at "#" in any position on the line
		
		int commentCharPos = line.indexOf( '#' );
		
		if ( commentCharPos != -1 ) {
			line = line.substring(0, commentCharPos);
		}
		
		line = line.trim();
		
		if ( line.length() == 0 ) {
			
			// Nothing left on the line, so return null 
			return null;
		}
		
		//  Split into key and value
		
		String[] lineSplit = line.split( "\\s+", 2 );


		String key = lineSplit[ 0 ];
		
		String value = "";
		
		if ( lineSplit.length == 2 ) {
						
			value = lineSplit[ 1 ];
		}
		
		key = key.trim();
		value = value.trim();
		
		LineParsed lineParsed = new LineParsed();
		
		lineParsed.setKey( key );
		lineParsed.setValue( value );
		
		return lineParsed;
	}
	
	
	private class LineParsed {
		
		String key;
		String value;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		
	}
}

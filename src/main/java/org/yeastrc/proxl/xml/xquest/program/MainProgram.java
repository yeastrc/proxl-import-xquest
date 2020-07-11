package org.yeastrc.proxl.xml.xquest.program;

import java.io.File;
import picocli.CommandLine;
import org.yeastrc.proxl.xml.xquest.main.XQuestImporterMain;

/**
 * 
 *
 */
public class MainProgram implements Runnable {

	@CommandLine.Option(names = { "-l", "--linker" }, required = true, description = "The name of the linker. E.g., dss, bs3, bs3, etc.")
	private String linkerName;

	@CommandLine.Option(names = { "-r", "--results-directory" }, required = true, description = "Full or relative path to xQuest results directory.")
	private File resultsDirectory;

	@CommandLine.Option(names = { "-d", "--decoy-prefix" }, required = false, description = "[Optional] If present, will override the 'decoy_filter' setting in xQuest config file.")
	private String decoyPrefix;

	@CommandLine.Option(names = { "-o", "--out" }, required = true, description = "Full path to use for the " +
			"ProXL XML output file (e.g., ./xquest.proxl.xml).")
	private File outFile;


	@Override
	public void run() {

		boolean successfulGenImportXMLFile = false;
		

		try {

			if( !resultsDirectory.exists() ) {
				System.err.println( "Could not find file: " + resultsDirectory.getAbsolutePath() );
				System.exit( 1 );
			}

			if( !resultsDirectory.isDirectory()) {
				System.err.println( "Must specify a directory, got: " + resultsDirectory.getAbsolutePath() );
				System.exit( 1 );
			}


	        System.out.println( "Performing Proxl Gen import XML file for parameters:" );
	        
	        System.out.println( "output filename: " + outFile.getAbsolutePath() );
	        System.out.print( "linker:" + linkerName );
	        System.out.println( "path to xquest results files: " + resultsDirectory.getAbsolutePath() );
	        System.out.println( "Decoy prefix: " + decoyPrefix );
	        
			XQuestImporterMain.getInstance().doGenFile( 
					linkerName,
					decoyPrefix,
					resultsDirectory,
					outFile
			);


			
			System.out.println( "" );
			System.out.println( "--------------------------------------" );
			System.out.println( "" );

	        System.out.println( "Completed Proxl Gen XML for Import for parameters:" );

			successfulGenImportXMLFile = true;
			

			
		} catch ( Exception e ) {
			
			System.out.println( "Exception in processing" );
			System.err.println( "Exception in processing" );
			
			e.printStackTrace( System.out );
			e.printStackTrace( System.err );

			System.exit(1);

		}
	    
	    if ( successfulGenImportXMLFile ) {

	    	System.out.println( "" );
	    	System.out.println( "--------------------------------------" );
	    	System.out.println( "" );
	    	System.out.println( "Done Generating Proxl Import XML data." );

	    	System.out.println( "" );
	    	System.out.println( "--------------------------------------" );
	    	System.out.println( "" );
	    }

	}

	public static void main( String[] args ) {

		//printRuntimeInfo();

		CommandLine.run(new MainProgram(), args);
	}

}

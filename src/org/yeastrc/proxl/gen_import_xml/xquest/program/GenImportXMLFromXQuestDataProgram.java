package org.yeastrc.proxl.gen_import_xml.xquest.program;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;





//import org.apache.log4j.Logger;









import org.apache.commons.lang3.StringUtils;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.DecoyInXquestProt1Prot2Constants;
import org.yeastrc.proxl.gen_import_xml.xquest.main.XQuestImporterMain;




/**
 * 
 *
 */
public class GenImportXMLFromXQuestDataProgram {

//	private static final Logger log = Logger.getLogger( GenImportXMLFromXQuestDataProgram.class );

	private static final int PROGRAM_EXIT_CODE_DEFAULT_NO_SYTEM_EXIT_CALLED = 0;

	private static final int PROGRAM_EXIT_CODE_INVALID_INPUT = 1;

	private static final int PROGRAM_EXIT_CODE_HELP = 1;
	
	private static final String FOR_HELP_STRING = "For help, run without any parameters, -h, or --help";
	
	//  Must start with "/" since not in any Java package
	private static final String HELP_TEXT_FILE = "/help_output_proxl_gen_import_xml_XQuest.txt";

	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		boolean successfulGenImportXMLFile = false;
		
		int programExitCode = PROGRAM_EXIT_CODE_DEFAULT_NO_SYTEM_EXIT_CALLED;
		
		
		try {


			if ( args.length == 0 ) {
				
	            printHelp();
	            
	            System.exit( PROGRAM_EXIT_CODE_HELP );
			}

			
			
			CmdLineParser cmdLineParser = new CmdLineParser();
	        
			CmdLineParser.Option outputFilenameOpt = cmdLineParser.addStringOption( 'o', "output-file" );
			CmdLineParser.Option linkerOpt = cmdLineParser.addStringOption( 'l', "linker" );	
			CmdLineParser.Option resultsPathOpt = cmdLineParser.addStringOption( 'r', "results-path" );	

			CmdLineParser.Option nameOpt = cmdLineParser.addStringOption( 'n', "name" );	
			
			CmdLineParser.Option proteinNameDecoyPrefixCommandLineOpt = cmdLineParser.addStringOption( 'Z', "decoy-prefix" );
			

			CmdLineParser.Option helpOpt = cmdLineParser.addBooleanOption('h', "help"); 

	        // parse command line options
	        try { cmdLineParser.parse(args); }
	        catch (IllegalOptionValueException e) {
	            System.err.println(e.getMessage());

				System.err.println( "" );
				System.err.println( FOR_HELP_STRING );
				
				System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
	        }
	        catch (UnknownOptionException e) {
	            System.err.println(e.getMessage());

				System.err.println( "" );
				System.err.println( FOR_HELP_STRING );
				
				System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
	        }
	        
	        Boolean help = (Boolean) cmdLineParser.getOptionValue(helpOpt, Boolean.FALSE);
	        if(help) {

	            printHelp();
	            
	            System.exit( PROGRAM_EXIT_CODE_HELP );
	        }
	        
	        String outputFilename = (String)cmdLineParser.getOptionValue( outputFilenameOpt );
	        
			@SuppressWarnings("rawtypes")
			Vector linkerNameStringsVector = cmdLineParser.getOptionValues( linkerOpt );

			
	        String resultsPath = (String)cmdLineParser.getOptionValue( resultsPathOpt );

	        String searchName = (String)cmdLineParser.getOptionValue( nameOpt );

	        
	        //  Decoy 
	        
	        String proteinNameDecoyPrefixOnCommandLine = (String)cmdLineParser.getOptionValue( proteinNameDecoyPrefixCommandLineOpt );
	        
	        
	        String proteinNameDecoyPrefix = DecoyInXquestProt1Prot2Constants.XQUEST_DECOY_DETECT_STRING_IN_PROT1_PROT2;

	        if ( StringUtils.isNotEmpty( proteinNameDecoyPrefixOnCommandLine ) ) {
	        	
	        	proteinNameDecoyPrefix = proteinNameDecoyPrefixOnCommandLine;
	        	
	        }
	        
	        
	        
	        
	        
			String[] remainingArgs = cmdLineParser.getRemainingArgs();
			
			if( remainingArgs.length > 0 ) {
				System.err.println( "Got unexpected number of arguments." );

				System.err.println( "" );
				System.err.println( FOR_HELP_STRING );
				
				System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
			}

	        if( linkerNameStringsVector == null || ( linkerNameStringsVector.isEmpty() ) ) {
		        
	        	System.err.println( "Must specify at least one linker using -l" );
	        	System.err.println( "" );
				System.err.println( FOR_HELP_STRING );
				
				System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
	        }

	        
	        if( resultsPath == null || resultsPath.equals( "" ) ) {
	        	System.err.println( "Must specify a path to xquest results files using -r\n" );

				System.err.println( "" );
				System.err.println( FOR_HELP_STRING );
				
				System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
	        }

	        
	        File resultsPathFile = new File( resultsPath );
	        
	        if ( ! resultsPathFile.exists() ) {
	        	
	        	System.err.println( "The path to xquest results files using -r does not exist: " + resultsPathFile );

				System.err.println( "" );
				System.err.println( FOR_HELP_STRING );
				
				System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
	        }
	        
	        
	        if ( ! resultsPathFile.isDirectory() ) {
	        	
	        	System.err.println( "The path to xquest results files using -r is not a directory: " + resultsPathFile );

				System.err.println( "" );
				System.err.println( FOR_HELP_STRING );
				
				System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
	        }
	        
	        List<String> linkerNamesStringsList = new ArrayList<>(  );

	        for ( Object linkerNameStringObject : linkerNameStringsVector ) {
	        	
	        	if ( ! (  linkerNameStringObject instanceof String ) ) {

		        	System.err.println( "Internal ERROR:  linkerNameStringObject is not a String object\n" );
	        		System.err.println( "" );
		        	System.err.println( FOR_HELP_STRING );
					
					System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
	        	}
	        	
	        	String linkerNameString = (String) linkerNameStringObject;

	        	if( linkerNameString == null || linkerNameString.equals( "" ) ) {

	        		System.err.println( "Internal ERROR:  linkerNameString is empty or null." );
	        		System.err.println( "" );
		        	System.err.println( FOR_HELP_STRING );
					
					System.exit( PROGRAM_EXIT_CODE_INVALID_INPUT );
	        	}
	        	
	        	linkerNamesStringsList.add( linkerNameString );
	        }
	        
	        

	        System.out.println( "Performing Proxl Gen import XML file for parameters:" );
	        
	        System.out.println( "output filename: " + outputFilename );

	        System.out.print( "linker"  );
	        
	        if ( linkerNamesStringsList.size() > 1 ) {

	        	System.out.print( "s"  );
	        }

	        System.out.print( ": "  );
	        
	        boolean firstLinkerBefore = true;

	        for ( String linkerNameString : linkerNamesStringsList ) {
	        	
	        	if ( firstLinkerBefore ) {
	        		
	        		firstLinkerBefore = false;
	        	} else {
	        		
	        		System.out.print( ", " ) ;
	        	}
	        	
	        	System.out.print( linkerNameString );
	        }
	        
	        System.out.println( "" );
	        
	        System.out.println( "path to xquest results files: " + resultsPathFile.getAbsolutePath() );
	        
	        if ( StringUtils.isNotEmpty( proteinNameDecoyPrefixOnCommandLine ) ) {
	        	
	        	System.out.println( "Decoy prefix on command line: " + proteinNameDecoyPrefixOnCommandLine );
	        	
	        } else {
	        		        	
	        	System.out.println( "No Decoy prefix on command line so using default: " + proteinNameDecoyPrefix );
				
	        }
	        
	        
	        File outputFile = new File( outputFilename );
	        
	        
			XQuestImporterMain.getInstance().doGenFile( 
					linkerNamesStringsList, 
					searchName, 
					proteinNameDecoyPrefix, 
					resultsPathFile, 
					outputFile );


			
			System.out.println( "" );
			System.out.println( "--------------------------------------" );
			System.out.println( "" );

	        System.out.println( "Completed Proxl Gen XML for Import for parameters:" );

	        System.out.println( "output filename: " + outputFilename );

	        System.out.print( "linker"  );
	        
	        if ( linkerNamesStringsList.size() > 1 ) {

	        	System.out.print( "s"  );
	        }

	        System.out.print( ": "  );
	        
	        boolean firstLinkerAfter = true;

	        for ( String linkerNameString : linkerNamesStringsList ) {
	        	
	        	if ( firstLinkerAfter ) {
	        		
	        		firstLinkerAfter = false;
	        	} else {
	        		
	        		System.out.print( ", " ) ;
	        	}
	        	
	        	System.out.print( linkerNameString );
	        }
	        
	        System.out.println( "" );
	        System.out.println( "path to xquest results files: " + resultsPathFile.getAbsolutePath() );

			successfulGenImportXMLFile = true;
			

			
		} catch ( Exception e ) {
			
			System.out.println( "Exception in processing" );
			System.err.println( "Exception in processing" );
			
			e.printStackTrace( System.out );
			e.printStackTrace( System.err );
			
			
			throw e;
			
		} finally {
			
			

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

		
		if ( programExitCode != PROGRAM_EXIT_CODE_DEFAULT_NO_SYTEM_EXIT_CALLED ) {
			
			System.exit( programExitCode );
		}
	            
			
	}
	

	private static void printHelp() throws Exception {
		
		try( BufferedReader br = 
				new BufferedReader( 
						new InputStreamReader( 
								GenImportXMLFromXQuestDataProgram.class
								.getResourceAsStream( HELP_TEXT_FILE ) ) ) ) {
			
			String line = null;
			while ( ( line = br.readLine() ) != null )
				System.out.println( line );				
			
		} catch ( Exception e ) {
			System.out.println( "Error printing help." );
		}
		
				
	}

}

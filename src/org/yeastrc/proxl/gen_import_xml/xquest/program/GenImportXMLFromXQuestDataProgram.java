package org.yeastrc.proxl.gen_import_xml.xquest.program;

import java.io.File;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

import org.apache.log4j.Logger;
import org.yeastrc.proxl.gen_import_xml.xquest.exceptions.PrintHelpOnlyException;
import org.yeastrc.proxl.gen_import_xml.xquest.main.XQuestImporterMain;




/**
 * 
 *
 */
public class GenImportXMLFromXQuestDataProgram {

	private static final Logger log = Logger.getLogger( GenImportXMLFromXQuestDataProgram.class );

	private static final int PROGRAM_EXIT_CODE_DEFAULT_NO_SYTEM_EXIT_CALLED = 0;
	

	private static final String PROTEIN_NAME_DECOY_PREFIX_CMD_LINE_PARAM_STRING  = "protein_name_decoy_prefix";
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		boolean successfulGenImportXMLFile = false;
		
		int programExitCode = PROGRAM_EXIT_CODE_DEFAULT_NO_SYTEM_EXIT_CALLED;
		
		
		try {

			
			
			
			CmdLineParser cmdLineParser = new CmdLineParser();
	        
			CmdLineParser.Option outputFilenameOpt = cmdLineParser.addStringOption( 'o', "output_file" );
			CmdLineParser.Option linkerOpt = cmdLineParser.addStringOption( 'l', "linker" );	
			CmdLineParser.Option resultsPathOpt = cmdLineParser.addStringOption( 'r', "resultsPath" );	

			CmdLineParser.Option nameOpt = cmdLineParser.addStringOption( 'n', "name" );	
			
			CmdLineParser.Option proteinNameDecoyPrefixCommandLineOpt = cmdLineParser.addStringOption( 'Z', PROTEIN_NAME_DECOY_PREFIX_CMD_LINE_PARAM_STRING );
			

			CmdLineParser.Option helpOpt = cmdLineParser.addBooleanOption('h', "help"); 

	        // parse command line options
	        try { cmdLineParser.parse(args); }
	        catch (IllegalOptionValueException e) {
	            System.err.println(e.getMessage());
	            
	            
				programExitCode = 1;
	            throw new PrintHelpOnlyException();
//	            System.exit( 0 );
	        }
	        catch (UnknownOptionException e) {
	            System.err.println(e.getMessage());
	            
				programExitCode = 1;
	            throw new PrintHelpOnlyException();
//	            System.exit( 0 );
	        }
	        
	        Boolean help = (Boolean) cmdLineParser.getOptionValue(helpOpt, Boolean.FALSE);
	        if(help) {
	        	
				programExitCode = 1;
	            throw new PrintHelpOnlyException();
	        }
	        
	        String outputFilename = (String)cmdLineParser.getOptionValue( outputFilenameOpt );
	        String linkerNameString = (String)cmdLineParser.getOptionValue( linkerOpt );
	        String resultsPath = (String)cmdLineParser.getOptionValue( resultsPathOpt );

	        String searchName = (String)cmdLineParser.getOptionValue( nameOpt );
	        
	        String proteinNameDecoyPrefix = (String)cmdLineParser.getOptionValue( proteinNameDecoyPrefixCommandLineOpt );
	        
	        
			String[] remainingArgs = cmdLineParser.getRemainingArgs();
			
			if( remainingArgs.length > 0 ) {
				System.err.println( "Got unexpected number of arguments.\n" );
				printHelp();
				programExitCode = 1;
				throw new PrintHelpOnlyException();
			}

	        if( linkerNameString == null || linkerNameString.equals( "" ) ) {
	        	System.err.println( "Must specify a linker using -l\n" );
	        	printHelp();
				programExitCode = 1;
				throw new PrintHelpOnlyException();
	        }
	        
	        if( resultsPath == null || resultsPath.equals( "" ) ) {
	        	System.err.println( "Must specify a path to xquest results files using -r\n" );
	        	printHelp();
				programExitCode = 1;
				throw new PrintHelpOnlyException();
	        }

	        
	        File resultsPathFile = new File( resultsPath );
	        
	        if ( ! resultsPathFile.exists() ) {
	        	
	        	System.err.println( "The path to xquest results files using -r does not exist: " + resultsPathFile );
	        	System.err.println( "" );
	        	printHelp();
				programExitCode = 1;
				throw new PrintHelpOnlyException();
	        }
	        
	        
	        if ( ! resultsPathFile.isDirectory() ) {
	        	
	        	System.err.println( "The path to xquest results files using -r is not a directory: " + resultsPathFile );
	        	System.err.println( "" );
	        	printHelp();
				programExitCode = 1;
				throw new PrintHelpOnlyException();
	        }
	        
	        

	        System.out.println( "Performing Proxl Gen import XML file for parameters:" );
	        
	        System.out.println( "output filename: " + outputFilename );
	        System.out.println( "linker: " + linkerNameString );
	        System.out.println( "path to xquest results files: " + resultsPathFile.getAbsolutePath() );
	        
	        File outputFile = new File( outputFilename );
	        
	        
			XQuestImporterMain.getInstance().doGenFile( 
					linkerNameString, 
					searchName, 
					proteinNameDecoyPrefix, 
					resultsPathFile, 
					outputFile );


			
			System.out.println( "" );
			System.out.println( "--------------------------------------" );
			System.out.println( "" );

	        System.out.println( "Completed Proxl Gen XML for Import for parameters:" );

	        System.out.println( "output filename: " + outputFilename );
	        System.out.println( "linker: " + linkerNameString );
	        System.out.println( "path to xquest results files: " + resultsPathFile.getAbsolutePath() );

			successfulGenImportXMLFile = true;
			
		} catch ( PrintHelpOnlyException e ) {
			
			//  land here when only need to print the help
			
            printHelp();

			
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
		
		String line = "Usage: <run jar script> -o output_filename -l linker -r path_to_results_files ";

		System.err.println( line );
		
				
	}

}

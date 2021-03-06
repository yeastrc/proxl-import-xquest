package org.yeastrc.proxl.xml.xquest.main;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.yeastrc.proxl.xml.xquest.constants.AnnotationType_Constants;
import org.yeastrc.proxl.xml.xquest.constants.MZXML_File_Suffix_Constants;
import org.yeastrc.proxl.xml.xquest.constants.ScanTypeAllowedConstants;
import org.yeastrc.proxl.xml.xquest.constants.SearchProgramConstants;
import org.yeastrc.proxl.xml.xquest.constants.XquestFilenameConstants;
import org.yeastrc.proxl.xml.xquest.enums.XQuestLinkTypes;
import org.yeastrc.proxl.xml.xquest.exceptions.ProxlGenXMLDataException;
import org.yeastrc.proxl.xml.xquest.objects.ReadNextSearchHitResult;
import org.yeastrc.proxl.xml.xquest.readers.XquestXMLReader;
import org.yeastrc.proxl.xml.xquest.utils.GetProxlLinkTypeFromXquestLinkType;
import org.yeastrc.proxl.xml.xquest.utils.XquestImportUtils;
import org.yeastrc.proxl_import.api.xml_dto.DescriptivePsmAnnotation;
import org.yeastrc.proxl_import.api.xml_dto.DescriptivePsmAnnotations;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotation;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotations;
import org.yeastrc.proxl_import.api.xml_dto.LinkType;
import org.yeastrc.proxl_import.api.xml_dto.LinkedPosition;
import org.yeastrc.proxl_import.api.xml_dto.LinkedPositions;
import org.yeastrc.proxl_import.api.xml_dto.Modification;
import org.yeastrc.proxl_import.api.xml_dto.Modifications;
import org.yeastrc.proxl_import.api.xml_dto.Peptide;
import org.yeastrc.proxl_import.api.xml_dto.Peptides;
import org.yeastrc.proxl_import.api.xml_dto.ProxlInput;
import org.yeastrc.proxl_import.api.xml_dto.Psm;
import org.yeastrc.proxl_import.api.xml_dto.Psms;
import org.yeastrc.proxl_import.api.xml_dto.ReportedPeptide;
import org.yeastrc.proxl_import.api.xml_dto.ReportedPeptides;


/**
 * 
 * Process Xquest file
 */
public class ProcessXQuestMainFile {

	private static final Logger log = Logger.getLogger(ProcessXQuestMainFile.class);
	
	/**
	 * private constructor
	 */
	private ProcessXQuestMainFile(){}
	
	public static ProcessXQuestMainFile getInstance() {
		
		return new ProcessXQuestMainFile();
	}
	
	
	private List<String> fatalErrors = new ArrayList<>();
	
	
	public List<String> getFatalErrors() {
		return fatalErrors;
	}
	
	
	/**
	 * @param xquestDataDir
	 * @return
	 * @throws 
	 */
	public String getXquestVersionNumber( File xquestDataDir ) throws Exception {
		
		
		String xquestVersion = null;

		File xquestFile = new File( xquestDataDir, XquestFilenameConstants.XQUEST_DATA_FILENAME );

		XquestXMLReader xquestXMLReader = null;
		
		try {

			xquestXMLReader = XquestXMLReader.getInstance( xquestFile );
			
			xquestVersion = xquestXMLReader.getXquestVersion();

		} finally {
			
			if ( xquestXMLReader != null ) {
				
				xquestXMLReader.close();
			}
		}
		
		return xquestVersion;
	}
	

	/**
	 * @param xquestDataDir
	 * @param proteinNameDecoyPrefix
	 * @param proxlInputRoot
	 * @return
	 * @throws Exception
	 */
	public Set<String> importXquestData( 

			File xquestDataDir,
			
			String proteinNameDecoyPrefix,
			
			ProxlInput proxlInputRoot
			
			) throws Exception {
		

		Set<String> proteinNameStrings = new HashSet<>();
		
		
		File xquestFile = new File( xquestDataDir, XquestFilenameConstants.XQUEST_DATA_FILENAME );

		if ( ! xquestFile.exists() ) {
			
			String msg = "Expected Xquest file does not exist: " + xquestFile.getAbsolutePath();
			
			log.error( msg );
			System.out.println( msg );
			System.err.println( msg );
			
			throw new ProxlGenXMLDataException(msg);
		}
		

		XquestXMLReader xquestXMLReader = null;
		
		try {

			xquestXMLReader = XquestXMLReader.getInstance( xquestFile );

			
			System.out.println( "Processing Xquest file " + xquestFile.getAbsolutePath() );


			//  Save the XquestFileDTO and Xquest Conf file contents


			processXquestRecords( 
					proxlInputRoot,
					proteinNameStrings,
					proteinNameDecoyPrefix,
					xquestFile, 
					xquestXMLReader );

		} finally {
			
			if ( xquestXMLReader != null ) {
				
				xquestXMLReader.close();
			}
		}

		return proteinNameStrings;
		
	}
	
	
	/**
	 * @param proxlInputRoot
	 * @param proteinNameStrings
	 * @param proteinNameDecoyPrefix
	 * @param xquestFile
	 * @param xquestXMLReader
	 * @throws Exception
	 */
	private void processXquestRecords(

			ProxlInput proxlInputRoot,
			Set<String> proteinNameStrings,
			
			String proteinNameDecoyPrefix,
			
			File xquestFile,
			XquestXMLReader xquestXMLReader
			)
	
			throws Exception {
		
		

		Map<String, ReportedPeptide> proxlInputReportedPeptideMap = new HashMap<>();
		
		//  Process records in xquest file
		
		int xquestRecordCount = 0;
		int xquestProcessedPSMRecordCount = 0;

		
		while (true) {

			ReadNextSearchHitResult readNextSearchHitResult;

			try {
				readNextSearchHitResult = xquestXMLReader.readNextSearchHit();

			} catch ( Exception e ) {

				String msg = "Error reading Xquest file (file: " + xquestFile.getAbsolutePath() + ") .";

				log.error( msg, e );

				throw e;
			}

			if ( readNextSearchHitResult == null ) {

				break;  //  EARLY EXIT from LOOOP
			}

			xquestRecordCount++;
			
			Map<String,String> spectrumSearchContents = readNextSearchHitResult.getSpectrumSearchContents();
			Map<String,String> searchHitContents = readNextSearchHitResult.getSearchHitContents();

			
			//  Only allow attribute 'scanType' to have value 'light_light'
			
			String scanTypeValue = spectrumSearchContents.get( ScanTypeAllowedConstants.SCAN_TYPE_ATTRIBUTE_NAME );

			if ( ! ScanTypeAllowedConstants.SCAN_TYPE_ATTRIBUTE_ALLOWED_VALUE.equals( scanTypeValue ) ) {
				
				String msg = "ERROR: For attribute '" + ScanTypeAllowedConstants.SCAN_TYPE_ATTRIBUTE_NAME
						+ "' the only supported value is '"
						+ ScanTypeAllowedConstants.SCAN_TYPE_ATTRIBUTE_ALLOWED_VALUE
						+ "'.  The value in the file is: " + scanTypeValue;
				log.error( msg );
				throw new ProxlGenXMLDataException( msg );
			}
			
			

			String structure = searchHitContents.get( "structure" );
			String topology = searchHitContents.get( "topology");
			
			String xquestId = searchHitContents.get( "id" );

			
			
			//  Skip decoys
			
			//    Check attributes prot1 and prot2.
			//    If all proteins in either attribute contain "decoy", skip the PSM
			
			String xQuestProt1 = searchHitContents.get( "prot1" );
			String xQuestProt2 = searchHitContents.get( "prot2" );
			
			String[] xQuestProteins = null;
			
			if ( StringUtils.isNotEmpty( xQuestProt2 ) ) {
				
				xQuestProteins = new String[ 2 ];
				xQuestProteins[ 1 ] = xQuestProt2;
			
			} else {
				xQuestProteins = new String[ 1 ];
			}
			
			xQuestProteins[ 0 ] = xQuestProt1;
			

			
			if ( StringUtils.isNotEmpty( proteinNameDecoyPrefix ) ) {


				boolean skipPSMforDecoys = false;

				for ( String xQuestProtein1or2 : xQuestProteins ) {

					String[] xQuestProtein1or2Split = xQuestProtein1or2.split( "," );

					boolean allDecoysForProtein = true;

					for ( String xQuestProteinSingle : xQuestProtein1or2Split ) {

						if ( ! xQuestProteinSingle.contains( proteinNameDecoyPrefix ) ) {

							//  Not decoy found

							allDecoysForProtein = false;
							break;

						}
					}

					if ( allDecoysForProtein ) {

						skipPSMforDecoys = true;

						break;
					}
				}

				if ( skipPSMforDecoys ) {

					//  At least one of the proteins has all proteins that contain 'decoy' so skip this PSM


					System.out.println( "Skipping Xquest record since at least one protein is all 'decoy'"
							+ " for xquestId ('id'): " + xquestId
							+ " structure: " + structure + ", topology: " + topology );



					continue;  ///  EARLY CONTINUE
				}


			}

					
			
    		Psm outputPsm = new Psm();
    		
			

			System.out.println( "Processing Xquest record for xquestId ('id'): " + xquestId
					+ " structure: " + structure + ", topology: " + topology );
			
			
			
			XQuestLinkTypes xquestLinkType = null;
			
			String xquestLinkTypeString = searchHitContents.get( "type" );
			
			try {

				xquestLinkType = GetProxlLinkTypeFromXquestLinkType.getProxlLinkTypeFromXquestLinkType( xquestLinkTypeString );
				
			} catch ( Exception e ) {
				
				String msg = "Failed to get link type for type: |" + xquestLinkTypeString
						+ "|, structure: |" + structure
						+ "|, topology: |" + topology + "|.";
				log.error( msg );
				throw new ProxlGenXMLDataException(msg, e);
			}
			
			
			
			
			String spectrum = spectrumSearchContents.get( "spectrum" );
					
					// spectrum="Q_2013_1016_RJ_08.23795.23795.2_Q_2013_1016_RJ_08.23795.23795.2">

			String[] spectrumSplit = spectrum.split( "\\.", 3 );
			

			String scanFileMain = spectrumSplit[ 0 ];
			
			String scanFilename = scanFileMain + MZXML_File_Suffix_Constants.MZXML_FILE_SUFFIX;
					
			
			String scanNumberString = spectrumSplit[ 1 ];
			
			int scanNumber = -1;
			
			try {
				
				scanNumber = Integer.parseInt( scanNumberString );
			
			} catch ( Exception e ) {
				
				String msg = "Failed to parse scanNumberString: " + scanNumberString + " for spectrum: " + spectrum;
				
				log.error( msg );
				
				throw new ProxlGenXMLDataException(msg, e);
			}

			

			String fdrString = searchHitContents.get( "fdr" );
			String searchHitRankString = searchHitContents.get( "search_hit_rank" );
			String scoreString = searchHitContents.get( "score" );
			
			String chargeString = searchHitContents.get( "charge" );
			

			BigDecimal fdrBigDecimal = null;
			BigDecimal searchHitRankBigDecimal = null;
			BigDecimal scoreBigDecimal = null;
			
			try {
			
				fdrBigDecimal = new BigDecimal( fdrString );
			
				
			} catch ( Exception e ) {
				
				String msg = "Skipping Hit, Unable to parse fdr to a big decimal.  fdr: |" + fdrString 
						+ "|, xquestId: " + xquestId;
				
				log.error( msg, e );
				
    			continue;  //  !!!!!!!!!!!!!!  EARLY CONTINUE to next entry of loop
			}

			try {
			
				searchHitRankBigDecimal = new BigDecimal( searchHitRankString );
			
				
			} catch ( Exception e ) {
				
				String msg = "Skipping Hit, Unable to parse search_hit_rank to a big decimal.  searchHitRank: |" + searchHitRankString 
						+ "|, xquestId: " + xquestId;
				
				log.error( msg, e );
				
    			continue;  //  !!!!!!!!!!!!!!  EARLY CONTINUE to next entry of loop
			}
			

			try {
			
				scoreBigDecimal = new BigDecimal( scoreString );
			
				
			} catch ( Exception e ) {
				
				String msg = "Skipping Hit, Unable to parse score to a big decimal.  score: |" + scoreString 
						+ "|, xquestId: " + xquestId;
				
				log.error( msg, e );
				
    			continue;  //  !!!!!!!!!!!!!!  EARLY CONTINUE to next entry of loop
			}
			
			
			
			try {
				
				int chargeInt = Integer.parseInt( chargeString );
				
				outputPsm.setPrecursorCharge( BigInteger.valueOf( chargeInt ) );
			
			} catch ( Exception e ) {
				
				String msg = "Unable to parse charge to a int to put in psm.charge.  charge: " + chargeString + ", spectrum: " + spectrum;;
				
				log.error( msg, e );
				
				throw new ProxlGenXMLDataException( msg, e );
				
			}
			
			
			String xlinkposition = searchHitContents.get( "xlinkposition" );
					
			// used for monolinks as well as crosslink and looplink
			String xlinkermass = searchHitContents.get( "xlinkermass" ); 
			
			
			
			
			outputPsm.setScanFileName( scanFilename );
			outputPsm.setScanNumber( BigInteger.valueOf( scanNumber ) );

			FilterablePsmAnnotations filterablePsmAnnotations = new FilterablePsmAnnotations();
			outputPsm.setFilterablePsmAnnotations( filterablePsmAnnotations );

			List<FilterablePsmAnnotation> filterablePsmAnnotationList =
					filterablePsmAnnotations.getFilterablePsmAnnotation();
			
			////////////////

			//  Annotations
			
			//  FDR
			
			{
				FilterablePsmAnnotation filterablePsmAnnotation = new FilterablePsmAnnotation();
				filterablePsmAnnotationList.add( filterablePsmAnnotation );

				filterablePsmAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
				filterablePsmAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_FDR );
				filterablePsmAnnotation.setValue( fdrBigDecimal );
			}

			//  Rank
			
			{
				FilterablePsmAnnotation filterablePsmAnnotation = new FilterablePsmAnnotation();
				filterablePsmAnnotationList.add( filterablePsmAnnotation );

				filterablePsmAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
				filterablePsmAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_RANK );
				filterablePsmAnnotation.setValue( searchHitRankBigDecimal );
			}

			//  Score
			
			{
				FilterablePsmAnnotation filterablePsmAnnotation = new FilterablePsmAnnotation();
				filterablePsmAnnotationList.add( filterablePsmAnnotation );

				filterablePsmAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
				filterablePsmAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_XQUEST_SCORE );
				filterablePsmAnnotation.setValue( scoreBigDecimal );
			}
			

			DescriptivePsmAnnotations descriptivePsmAnnotations = new DescriptivePsmAnnotations();
			outputPsm.setDescriptivePsmAnnotations( descriptivePsmAnnotations );

			List<DescriptivePsmAnnotation> descriptivePsmAnnotationList =
					descriptivePsmAnnotations.getDescriptivePsmAnnotation();
			
			
			String obsMass = spectrumSearchContents.get( "Mr_precursor" ); 
			String calcMass = searchHitContents.get( "Mr" ); 
			
			//  Obs. mass
			
			{
				DescriptivePsmAnnotation descriptivePsmAnnotation = new DescriptivePsmAnnotation();
				descriptivePsmAnnotationList.add( descriptivePsmAnnotation );
				
				descriptivePsmAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_OBS_MASS );
				descriptivePsmAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
				descriptivePsmAnnotation.setValue( obsMass );
			}

			//  Calc. mass
			
			{
				DescriptivePsmAnnotation descriptivePsmAnnotation = new DescriptivePsmAnnotation();
				descriptivePsmAnnotationList.add( descriptivePsmAnnotation );
				
				descriptivePsmAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_CALC_MASS );
				descriptivePsmAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
				descriptivePsmAnnotation.setValue( calcMass );
			}
			
			////////////////
    		
			xquestProcessedPSMRecordCount++;

			BigDecimal linkerMass = null;

    	    try {
    	    	
    	    	linkerMass = new BigDecimal( xlinkermass );
    	    	
    	    	
    	    	
    	    } catch( Exception e ) {

				String msg = "Unable to parse xlinkermass to a Big Decimal.  xlinkermass: " + xlinkermass;
				
				log.error( msg, e );
				
				throw new ProxlGenXMLDataException( msg, e );
    	    }
    	    
    	    
    	    if ( xquestLinkType == XQuestLinkTypes.CROSSLINK
    	    		|| xquestLinkType == XQuestLinkTypes.LOOPLINK) {
    	    
    	    	outputPsm.setLinkerMass( linkerMass );
    	    }
    	    

			String xquestReportedPeptide = null;


			if ( xquestLinkType == XQuestLinkTypes.CROSSLINK ) {

				xquestReportedPeptide = structure + "-" + topology;
				
			} else if ( xquestLinkType == XQuestLinkTypes.LOOPLINK ) {

				xquestReportedPeptide = structure + "-" + topology;
				
			} else if ( xquestLinkType == XQuestLinkTypes.MONOLINK ) {

				xquestReportedPeptide = structure + "-" + topology + "-" + linkerMass;
				
			} else {
				
				String msg = "Unexpected XQuestLinkTypes: " + xquestLinkType;
				log.error( msg );
				throw new ProxlGenXMLDataException(msg);
			}
			
    	    
    	    
			
			ReportedPeptide reportedPeptideForXQuestRecord =
					proxlInputReportedPeptideMap.get( xquestReportedPeptide );
			
			if ( reportedPeptideForXQuestRecord == null ) {
				
			
				if ( xquestLinkType == XQuestLinkTypes.CROSSLINK ) {
					
					reportedPeptideForXQuestRecord =
							processCrosslinkCreateReportedPeptide( structure, xlinkposition );
				
				} else if ( xquestLinkType == XQuestLinkTypes.LOOPLINK ) {

					reportedPeptideForXQuestRecord =
							processLooplinkCreateReportedPeptide( structure, xlinkposition );

				} else if ( xquestLinkType == XQuestLinkTypes.MONOLINK ) {

					reportedPeptideForXQuestRecord =
							processMonolinkCreateReportedPeptide( structure, xlinkposition, linkerMass );
					
				} else {
					
					String msg = "Unexpected XQuestLinkTypes: " + xquestLinkType;
					log.error( msg );
					throw new ProxlGenXMLDataException(msg);
				}
				
				reportedPeptideForXQuestRecord.setReportedPeptideString( xquestReportedPeptide );
				
				
				
				Psms psms = new Psms();
				
				reportedPeptideForXQuestRecord.setPsms( psms );
//				reportedPeptideForXQuestRecord.setReportedPeptideAnnotations(  );

				proxlInputReportedPeptideMap.put( xquestReportedPeptide, reportedPeptideForXQuestRecord );
			}
			
			
			Psms psms = reportedPeptideForXQuestRecord.getPsms();
			
			List<Psm> psmList = psms.getPsm();
			
			psmList.add( outputPsm );
			
		}
		
		

		ReportedPeptides reportedPeptides = new ReportedPeptides();
		proxlInputRoot.setReportedPeptides( reportedPeptides );

		List<ReportedPeptide> proxlInputReportedPeptideList = reportedPeptides.getReportedPeptide();

		for ( Map.Entry<String, ReportedPeptide> entry : proxlInputReportedPeptideMap.entrySet()  ) {
			
			proxlInputReportedPeptideList.add( entry.getValue() );
			
		}

		
		log.info( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
		
		log.info( "xquestRecordCount: " + xquestRecordCount );
		log.info( "xquestProcessedPSMRecordCount: " + xquestProcessedPSMRecordCount );
				
		log.info( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
		
		
	}

	
	/**
	 * @param structure
	 * @param xlinkposition
	 * @return
	 * @throws Exception
	 */
	private ReportedPeptide processCrosslinkCreateReportedPeptide( 
			
			String structure,
			String xlinkposition ) throws Exception {
		
		ReportedPeptide reportedPeptideForXQuestRecord = new ReportedPeptide();

		Peptides peptides = new Peptides();
		reportedPeptideForXQuestRecord.setPeptides( peptides );
		
		List<Peptide> peptideList = peptides.getPeptide();
		
		
		String[] structureSplit = structure.split( "-" );
		
		if ( structureSplit.length != 2 ) {
			
			String msg = "For Crosslink, structure split on '-' does not have 2 parts.";
			log.error( msg );
			throw new ProxlGenXMLDataException( msg );
		}
		
		String seq1 = structureSplit[ 0 ];
		String seq2 = structureSplit[ 1 ];
		
		
		if ( StringUtils.isEmpty( seq1 ) ) {
			
			String msg = "For Crosslink, structure split on '-' part 1 is empty.";
			log.error( msg );
			throw new ProxlGenXMLDataException( msg );
		}
		

		if ( StringUtils.isEmpty( seq2 ) ) {
			
			String msg = "For Crosslink, structure split on '-' part 2 is empty.";
			log.error( msg );
			throw new ProxlGenXMLDataException( msg );
		}
	
	    String[] xlinkpositionSplit = xlinkposition.split( "," );
	    
		if ( xlinkpositionSplit.length != 2 ) {
			
			String msg = "processCrosslinkCreateReportedPeptide(...): Expected two link position per cross link peptide, xlinkpositionSplit.length: " 
					+ xlinkpositionSplit.length
					+ ", xlinkposition: " + xlinkposition;
			
			log.error( msg );
			
			throw new ProxlGenXMLDataException( msg );
		}
		
		int[] linkPositionsInts = new int[ 2 ];

		for ( int index = 0; index < xlinkpositionSplit.length; index++ ) {

			String linkPositionString = xlinkpositionSplit[ index ];
			
			try {
				
				int linkPositionInt = Integer.parseInt( linkPositionString );
				
				linkPositionsInts[ index ] = linkPositionInt;
				
			} catch ( Exception e ) {
				
				String msg = "Failed to parse link position '" + linkPositionString 
						+ "' in structure: " + structure;
				log.error( msg );
				throw new ProxlGenXMLDataException(msg);
			}
		}


		for ( int index = 0; index < structureSplit.length; index++ ) {
			
			int[] linkPositionsIntsForProcessSinglePeptide = { linkPositionsInts[ index ] };

			Peptide peptide =
					processSinglePeptide( 
							structureSplit[ index ], 
							linkPositionsIntsForProcessSinglePeptide,
							null /* monolinkPosition */,
							null /* monolinkMass */ );
			
			peptideList.add( peptide );
		}		
    	
		reportedPeptideForXQuestRecord.setType( LinkType.CROSSLINK );
		
    	return reportedPeptideForXQuestRecord;

	}
	
	
	

	/**
	 * @param structure
	 * @param xlinkposition
	 * @return
	 * @throws Exception
	 */
	private ReportedPeptide processLooplinkCreateReportedPeptide( 
			
			String structure,
			String xlinkposition ) throws Exception {
		
		ReportedPeptide reportedPeptideForXQuestRecord = new ReportedPeptide();

		Peptides peptides = new Peptides();
		reportedPeptideForXQuestRecord.setPeptides( peptides );
		
		List<Peptide> peptideList = peptides.getPeptide();
		
		
		
		String[] structureSplit = structure.split( "-" );

		if ( structureSplit.length != 1 ) {
			
			String msg = "For Looplink, structure split on '-' does not have 1 part.";
			log.error( msg );
			throw new ProxlGenXMLDataException( msg );
		}
		
		
		String seq1 = structureSplit[ 0 ];
		
		
		if ( StringUtils.isEmpty( seq1 ) ) {
			
			String msg = "For Looplink, structure split on '-' part 1 is empty.";
			log.error( msg );
			throw new ProxlGenXMLDataException( msg );
		}
		
	
	    String[] xlinkpositionSplit = xlinkposition.split( "," );
	    
		if ( xlinkpositionSplit.length != 2 ) {
			
			String msg = "processLooplinkCreateReportedPeptide(...): Expected two link position per loop link peptide, xlinkpositionSplit.length: " 
					+ xlinkpositionSplit.length
					+ ", xlinkposition: " + xlinkposition;
			
			log.error( msg );
			
			throw new ProxlGenXMLDataException( msg );
		}
		
		int[] linkPositionsInts = new int[ 2 ];

		for ( int index = 0; index < xlinkpositionSplit.length; index++ ) {

			String linkPositionString = xlinkpositionSplit[ index ];
			
			try {
				
				int linkPositionInt = Integer.parseInt( linkPositionString );
				
				linkPositionsInts[ index ] = linkPositionInt;
				
			} catch ( Exception e ) {
				
				String msg = "Failed to parse link position '" + linkPositionString 
						+ "' in structure: " + structure;
				log.error( msg, e );
				throw new ProxlGenXMLDataException(msg);
			}
		}


		Peptide peptide =
				processSinglePeptide( 
						structureSplit[ 0 ], 
						linkPositionsInts,
						null /* monolinkPosition */,
						null /* monolinkMass */  );

		peptideList.add( peptide );
		
		reportedPeptideForXQuestRecord.setType( LinkType.LOOPLINK );
		
    	return reportedPeptideForXQuestRecord;

	}
	

	/**
	 * @param structure
	 * @param xlinkposition
	 * @return
	 * @throws Exception
	 */
	private ReportedPeptide processMonolinkCreateReportedPeptide( 
			
			String structure,
			String xlinkposition,
			BigDecimal linkerMass
			) throws Exception {
		
		
		BigDecimal monolinkMass = linkerMass;
		
		ReportedPeptide reportedPeptideForXQuestRecord = new ReportedPeptide();

		Peptides peptides = new Peptides();
		reportedPeptideForXQuestRecord.setPeptides( peptides );
		
		List<Peptide> peptideList = peptides.getPeptide();
		
		
		String[] structureSplit = structure.split( "-" );

		if ( structureSplit.length != 1 ) {
			
			String msg = "For Monolink, structure split on '-' does not have 1 part.";
			log.error( msg );
			throw new ProxlGenXMLDataException( msg );
		}
		
		
		String seq1 = structureSplit[ 0 ];
		
		
		if ( StringUtils.isEmpty( seq1 ) ) {
			
			String msg = "For Monolink, structure split on '-' part 1 is empty.";
			log.error( msg );
			throw new ProxlGenXMLDataException( msg );
		}
		
	
	    String[] xlinkpositionSplit = xlinkposition.split( "," );
	    
		if ( xlinkpositionSplit.length != 1 ) {
			
			String msg = "processMonolinkCreateReportedPeptide(...): Expected one link position per mono link peptide, xlinkpositionSplit.length: " 
					+ xlinkpositionSplit.length
					+ ", xlinkposition: " + xlinkposition;
			
			log.error( msg );
			
			throw new ProxlGenXMLDataException( msg );
		}
		
		int[] linkPositionsInts = new int[ 1 ];

		String linkPositionString = xlinkpositionSplit[ 0 ];

		try {

			int linkPositionInt = Integer.parseInt( linkPositionString );

			linkPositionsInts[ 0 ] = linkPositionInt;

		} catch ( Exception e ) {

			String msg = "Failed to parse link position '" + linkPositionString 
					+ "' in structure: " + structure;
			log.error( msg, e );
			throw new ProxlGenXMLDataException(msg);
		}

		Peptide peptide =
				processSinglePeptide( 
						structureSplit[ 0 ], 
						null, 					// linkPositionsInts 
						linkPositionsInts[ 0 ], // monolinkPosition  
						monolinkMass 			// monolinkMass  
						);

		peptideList.add( peptide );
		
    	
		reportedPeptideForXQuestRecord.setType( LinkType.UNLINKED );
		
    	return reportedPeptideForXQuestRecord;

	}
	
	
	
	
	/**
	 * @param peptideString
	 * @param linkPositionsInts
	 * @param monolinkPosition
	 * @return
	 * @throws Exception
	 */
	private Peptide processSinglePeptide( 
			String peptideString, 
			int[] linkPositionsInts,	// null for monolink and unlinked
			Integer monolinkPosition, 	// null for other than monolink
			BigDecimal monolinkMass 	// null for other than monolink
			) throws Exception {
		

		Peptide peptide = new Peptide();
		
	    String peptideStringNaked = XquestImportUtils.getPeptideWithDynamicModificationsRemoved( peptideString );
		
		peptide.setSequence( peptideStringNaked );

		if ( linkPositionsInts != null && linkPositionsInts.length > 0 ) {

			LinkedPositions linkedPositions = new LinkedPositions();
			peptide.setLinkedPositions( linkedPositions );

			List<LinkedPosition> linkedPositionList = linkedPositions.getLinkedPosition();

			for ( int linkPositionInt : linkPositionsInts ) {

				LinkedPosition linkedPosition = new LinkedPosition();
				linkedPositionList.add( linkedPosition );
				
				linkedPosition.setPosition( BigInteger.valueOf( linkPositionInt ) );
			}
		}

		
    	Map<Integer,Collection<BigDecimal>> modLocations = XquestImportUtils.getDynamicModsForOneSequence( peptideString );
		
    	if ( modLocations != null && ( ! modLocations.isEmpty() ) ) {

    		Modifications modifications = new Modifications();
    		peptide.setModifications( modifications );

    		List<Modification> modificationList = modifications.getModification();

    		for ( Map.Entry<Integer,Collection<BigDecimal>> modLocationsPerPositionEntry : modLocations.entrySet() ) {

    			int position = modLocationsPerPositionEntry.getKey();
    			
    			for ( BigDecimal modificationMass : modLocationsPerPositionEntry.getValue() ){

    				Modification modification = new Modification();
    				modificationList.add( modification );
    				
    				modification.setPosition( BigInteger.valueOf( position ) );
    				modification.setMass( modificationMass );
    				
    				modification.setIsMonolink( false );
    			}
    		}
    	}

		if ( monolinkPosition != null && monolinkMass != null ) {

			Modifications modifications = peptide.getModifications();
			
			if ( modifications == null ) {

				modifications = new Modifications();
				peptide.setModifications( modifications );
			}

    		List<Modification> modificationList = modifications.getModification();

			Modification modification = new Modification();
			modificationList.add( modification );
			
			modification.setPosition( BigInteger.valueOf( monolinkPosition ) );
			modification.setMass( monolinkMass );
			
			
			modification.setIsMonolink( true );
		}
    	
    	return peptide;
	}
	

}

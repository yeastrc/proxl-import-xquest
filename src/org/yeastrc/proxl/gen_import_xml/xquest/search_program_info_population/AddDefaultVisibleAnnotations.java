package org.yeastrc.proxl.gen_import_xml.xquest.search_program_info_population;

import java.util.List;



//import org.apache.log4j.Logger;


import org.yeastrc.proxl.gen_import_xml.xquest.constants.AnnotationType_Constants;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.SearchProgramConstants;
import org.yeastrc.proxl_import.api.xml_dto.DefaultVisibleAnnotations;
import org.yeastrc.proxl_import.api.xml_dto.SearchAnnotation;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgramInfo;
import org.yeastrc.proxl_import.api.xml_dto.VisiblePsmAnnotations;

/**
 * 
 *
 */
public class AddDefaultVisibleAnnotations {
	
//	private static final Logger log = Logger.getLogger( AddDefaultVisibleAnnotations.class );
	

	/**
	 * private constructor
	 */
	private AddDefaultVisibleAnnotations() { }

	public static AddDefaultVisibleAnnotations getInstance() {
		return new AddDefaultVisibleAnnotations();
	}
	
	/**
	 * Add Default Visible Annotations for Reported Peptides and PSMs 
	 * @param searchProgramInfo
	 */
	public void addDefaultVisibleAnnotations( SearchProgramInfo searchProgramInfo ) {
		
		DefaultVisibleAnnotations defaultVisibleAnnotations = new DefaultVisibleAnnotations();
		searchProgramInfo.setDefaultVisibleAnnotations( defaultVisibleAnnotations );
		
		{
			VisiblePsmAnnotations visiblePsmAnnotations = new VisiblePsmAnnotations();
			defaultVisibleAnnotations.setVisiblePsmAnnotations( visiblePsmAnnotations );

			List<SearchAnnotation> visiblePsmAnnotationsSearchAnnotationList = visiblePsmAnnotations.getSearchAnnotation();

			{
				SearchAnnotation searchAnnotation = new SearchAnnotation();
				visiblePsmAnnotationsSearchAnnotationList.add( searchAnnotation );

				searchAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_FDR );
				searchAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
			}

			{
				SearchAnnotation searchAnnotation = new SearchAnnotation();
				visiblePsmAnnotationsSearchAnnotationList.add( searchAnnotation );

				searchAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_RANK );
				searchAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
			}

			{
				SearchAnnotation searchAnnotation = new SearchAnnotation();
				visiblePsmAnnotationsSearchAnnotationList.add( searchAnnotation );

				searchAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_XQUEST_SCORE );
				searchAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
			}

			{
				SearchAnnotation searchAnnotation = new SearchAnnotation();
				visiblePsmAnnotationsSearchAnnotationList.add( searchAnnotation );

				searchAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_OBS_MASS );
				searchAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
			}

			{
				SearchAnnotation searchAnnotation = new SearchAnnotation();
				visiblePsmAnnotationsSearchAnnotationList.add( searchAnnotation );

				searchAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_CALC_MASS );
				searchAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
			}
		}
		
//		{
//			VisibleReportedPeptideAnnotations visibleReportedPeptideAnnotations = new VisibleReportedPeptideAnnotations();
//			defaultVisibleAnnotations.setVisibleReportedPeptideAnnotations( visibleReportedPeptideAnnotations );
//			
//			List<SearchAnnotation> visibleReportedPeptideAnnotationsSearchAnnotationList = visibleReportedPeptideAnnotations.getSearchAnnotation();
//			
//
//			{
//				SearchAnnotation searchAnnotation = new SearchAnnotation();
//				visibleReportedPeptideAnnotationsSearchAnnotationList.add( searchAnnotation );
//
//				searchAnnotation.setAnnotationName( XXXXXX );
//				searchAnnotation.setSearchProgram( XXXXXX );
//			}
//
//		}
		
	}
	
}

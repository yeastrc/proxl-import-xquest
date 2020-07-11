package org.yeastrc.proxl.xml.xquest.search_program_info_population;

import java.util.List;






import org.yeastrc.proxl.xml.xquest.constants.AnnotationType_Constants;
import org.yeastrc.proxl.xml.xquest.constants.SearchProgramConstants;
//import org.apache.log4j.Logger;
import org.yeastrc.proxl_import.api.xml_dto.AnnotationSortOrder;
import org.yeastrc.proxl_import.api.xml_dto.PsmAnnotationSortOrder;
import org.yeastrc.proxl_import.api.xml_dto.SearchAnnotation;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgramInfo;

/**
 * 
 *
 */
public class AddAnnotationSortOrder {
	
//	private static final Logger log = Logger.getLogger( AddAnnotationSortOrder.class );
	

	/**
	 * private constructor
	 */
	private AddAnnotationSortOrder() { }

	public static AddAnnotationSortOrder getInstance() {
		return new AddAnnotationSortOrder();
	}
	
	/**
	 * Add Annotation Sort Order for Reported Peptides and PSMs 
	 * @param searchProgramInfo
	 */
	public void addAnnotationSortOrder( SearchProgramInfo searchProgramInfo ) {
		
		AnnotationSortOrder annotationSortOrder = new AnnotationSortOrder();
		searchProgramInfo.setAnnotationSortOrder( annotationSortOrder );
		
		{
			PsmAnnotationSortOrder psmAnnotationSortOrder = new PsmAnnotationSortOrder();
			annotationSortOrder.setPsmAnnotationSortOrder( psmAnnotationSortOrder );

			List<SearchAnnotation> psmAnnotationSortOrderSearchAnnotationList = psmAnnotationSortOrder.getSearchAnnotation();

			{
				SearchAnnotation searchAnnotation = new SearchAnnotation();
				psmAnnotationSortOrderSearchAnnotationList.add( searchAnnotation );

				searchAnnotation.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_FDR );
				searchAnnotation.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
			}

		}
//		
//		{
//			ReportedPeptideAnnotationSortOrder reportedPeptideAnnotationSortOrder = new ReportedPeptideAnnotationSortOrder();
//			annotationSortOrder.setReportedPeptideAnnotationSortOrder( reportedPeptideAnnotationSortOrder );
//			
//			List<SearchAnnotation> reportedPeptideAnnotationSortOrderSearchAnnotationList = reportedPeptideAnnotationSortOrder.getSearchAnnotation();
//			
//
//			{
//				SearchAnnotation searchAnnotation = new SearchAnnotation();
//				reportedPeptideAnnotationSortOrderSearchAnnotationList.add( searchAnnotation );
//
//				searchAnnotation.setAnnotationName( XXXXXX );
//				searchAnnotation.setSearchProgram( XXXXXX );
//			}
//
//		}
		
	}
	
}

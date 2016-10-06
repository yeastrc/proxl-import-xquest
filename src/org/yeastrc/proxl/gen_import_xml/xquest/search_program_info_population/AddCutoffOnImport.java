package org.yeastrc.proxl.gen_import_xml.xquest.search_program_info_population;

import java.math.BigDecimal;
import java.util.List;

import org.yeastrc.proxl.gen_import_xml.xquest.constants.AnnotationType_Constants;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.SearchProgramConstants;
import org.yeastrc.proxl_import.api.xml_dto.AnnotationCutoffsOnImport;
import org.yeastrc.proxl_import.api.xml_dto.PsmAnnotationCutoffsOnImport;
import org.yeastrc.proxl_import.api.xml_dto.SearchAnnotationCutoff;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgramInfo;



public class AddCutoffOnImport {


	/**
	 * private constructor
	 */
	private AddCutoffOnImport() { }

	public static AddCutoffOnImport getInstance() {
		return new AddCutoffOnImport();
	}

	public void addCutoffOnImport( BigDecimal fdrCutoffOnImport, BigDecimal rankCutoffOnImport, SearchProgramInfo searchProgramInfo ) {
		

		if ( fdrCutoffOnImport != null || rankCutoffOnImport != null ) {

			AnnotationCutoffsOnImport annotationCutoffsOnImport = new AnnotationCutoffsOnImport();
			
			searchProgramInfo.setAnnotationCutoffsOnImport( annotationCutoffsOnImport );
			
			PsmAnnotationCutoffsOnImport psmAnnotationCutoffsOnImport = new PsmAnnotationCutoffsOnImport();
			annotationCutoffsOnImport.setPsmAnnotationCutoffsOnImport( psmAnnotationCutoffsOnImport );

			List<SearchAnnotationCutoff> searchAnnotationCutoffList = psmAnnotationCutoffsOnImport.getSearchAnnotationCutoff();

		
			if ( fdrCutoffOnImport != null ) {

				SearchAnnotationCutoff searchAnnotationCutoff = new SearchAnnotationCutoff();
				searchAnnotationCutoffList.add(searchAnnotationCutoff);

				searchAnnotationCutoff.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
				searchAnnotationCutoff.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_FDR );
				searchAnnotationCutoff.setCutoffValue( fdrCutoffOnImport );
			}

			if ( rankCutoffOnImport != null ) {

				SearchAnnotationCutoff searchAnnotationCutoff = new SearchAnnotationCutoff();
				searchAnnotationCutoffList.add(searchAnnotationCutoff);

				searchAnnotationCutoff.setSearchProgram( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
				searchAnnotationCutoff.setAnnotationName( AnnotationType_Constants.ANNOTATION_NAME_RANK );
				searchAnnotationCutoff.setCutoffValue( rankCutoffOnImport );
			}
			
		}
	}
}

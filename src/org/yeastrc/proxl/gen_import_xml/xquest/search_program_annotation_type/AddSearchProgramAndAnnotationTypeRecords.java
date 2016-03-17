package org.yeastrc.proxl.gen_import_xml.xquest.search_program_annotation_type;

import java.util.List;

import org.yeastrc.proxl.gen_import_xml.xquest.annotation_default_visible.AddDefaultVisibleAnnotations;
import org.yeastrc.proxl.gen_import_xml.xquest.annotation_sort_order.AddAnnotationSortOrder;
//import org.apache.log4j.Logger;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.AnnotationNames_Constants;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.DefaultFilterValueConstants;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.SearchProgramConstants;
import org.yeastrc.proxl_import.api.xml_dto.FilterDirectionType;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotationType;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.ProxlInput;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram.PsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgramInfo;
import org.yeastrc.proxl_import.api.xml_dto.SearchPrograms;

/**
 * 
 *
 */
public class AddSearchProgramAndAnnotationTypeRecords {

//	private static final Logger log = Logger.getLogger(AddSearchProgramAndAnnotationTypeRecords.class);
	
	/**
	 * private constructor
	 */
	private AddSearchProgramAndAnnotationTypeRecords(){}
	
	public static AddSearchProgramAndAnnotationTypeRecords getInstance() {
		
		return new AddSearchProgramAndAnnotationTypeRecords();
	}
	
	
	public void addSearchProgramAndAnnotationTypeRecords( ProxlInput proxlInputRoot, String xquestVersion ) {
		
		SearchProgramInfo searchProgramInfo = new SearchProgramInfo();
		proxlInputRoot.setSearchProgramInfo( searchProgramInfo );
		
		SearchPrograms searchPrograms = new SearchPrograms();
		searchProgramInfo.setSearchPrograms( searchPrograms );
		
		List<SearchProgram> searchProgramList = searchPrograms.getSearchProgram();
		SearchProgram searchProgram = new SearchProgram();
		searchProgramList.add( searchProgram );
		
		searchProgram.setName( SearchProgramConstants.SEARCH_PROGRAM_NAME_XQUEST );
		searchProgram.setDisplayName( SearchProgramConstants.SEARCH_PROGRAM_DISPLAY_NAME_XQUEST );
		searchProgram.setDescription( SearchProgramConstants.SEARCH_PROGRAM_DESCRIPTION_XQUEST );
		
		searchProgram.setVersion( xquestVersion );
		
		addAnnotationTypeData( searchProgram );
		
		
		AddDefaultVisibleAnnotations.getInstance().addDefaultVisibleAnnotations( searchProgramInfo );
		
		AddAnnotationSortOrder.getInstance().addAnnotationSortOrder( searchProgramInfo );
		
	}
	
	
	/**
	 * @param searchProgram
	 */
	private void addAnnotationTypeData( SearchProgram searchProgram ) {
		
		PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
		searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
		
		FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
		psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
//		psmAnnotationTypes.setDescriptivePsmAnnotationTypes(  );
		
		
		List<FilterablePsmAnnotationType> filterablePsmAnnotationTypeList = 
				filterablePsmAnnotationTypes.getFilterablePsmAnnotationType();
		
		{
			FilterablePsmAnnotationType filterablePsmAnnotationType = new FilterablePsmAnnotationType();
			
			filterablePsmAnnotationType.setName( AnnotationNames_Constants.ANNOTATION_NAME_FDR );
			filterablePsmAnnotationType.setDescription( AnnotationNames_Constants.ANNOTATION_DESCRIPTION_FDR );
			
			filterablePsmAnnotationType.setFilterDirection( FilterDirectionType.BELOW );
			
			filterablePsmAnnotationType.setDefaultFilter( true );
			
			filterablePsmAnnotationType.setDefaultFilterValue( DefaultFilterValueConstants.PSM_FDR_DEFAULT );
			
			filterablePsmAnnotationTypeList.add( filterablePsmAnnotationType );
			
		}
		
	}
	
}

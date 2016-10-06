package org.yeastrc.proxl.gen_import_xml.xquest.search_program_info_population;

import java.math.BigDecimal;
import java.util.List;

//import org.apache.log4j.Logger;







import org.yeastrc.proxl.gen_import_xml.xquest.constants.AnnotationType_Constants;
import org.yeastrc.proxl.gen_import_xml.xquest.constants.SearchProgramConstants;
import org.yeastrc.proxl_import.api.xml_dto.DescriptivePsmAnnotationType;
import org.yeastrc.proxl_import.api.xml_dto.DescriptivePsmAnnotationTypes;
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
	
	
	public void addSearchProgramAndAnnotationTypeRecords( 
			
			ProxlInput proxlInputRoot, 
			String xquestVersion, 
			BigDecimal fdrCutoffOnImport, 
			BigDecimal rankCutoffOnImport ) {
		
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
		
		AddCutoffOnImport.getInstance().addCutoffOnImport( fdrCutoffOnImport, rankCutoffOnImport, searchProgramInfo );
		
	}
	
	
	/**
	 * @param searchProgram
	 */
	private void addAnnotationTypeData( SearchProgram searchProgram ) {
		
		PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
		searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
		
		FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
		psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
		
		
		List<FilterablePsmAnnotationType> filterablePsmAnnotationTypeList = 
				filterablePsmAnnotationTypes.getFilterablePsmAnnotationType();
		
		{
			//  FDR
			
			FilterablePsmAnnotationType filterablePsmAnnotationType = new FilterablePsmAnnotationType();
			
			filterablePsmAnnotationType.setName( AnnotationType_Constants.ANNOTATION_NAME_FDR );
			filterablePsmAnnotationType.setDescription( AnnotationType_Constants.ANNOTATION_DESCRIPTION_FDR );
			
			filterablePsmAnnotationType.setFilterDirection( FilterDirectionType.BELOW );
			
			filterablePsmAnnotationType.setDefaultFilter( true );
			
			filterablePsmAnnotationType.setDefaultFilterValue( AnnotationType_Constants.PSM_FDR_DEFAULT );
			
			filterablePsmAnnotationTypeList.add( filterablePsmAnnotationType );
			
		}

		{
			//  Rank
			
			FilterablePsmAnnotationType filterablePsmAnnotationType = new FilterablePsmAnnotationType();
			
			filterablePsmAnnotationType.setName( AnnotationType_Constants.ANNOTATION_NAME_RANK );
			filterablePsmAnnotationType.setDescription( AnnotationType_Constants.ANNOTATION_DESCRIPTION_RANK );
			
			filterablePsmAnnotationType.setFilterDirection( FilterDirectionType.BELOW );
			
			filterablePsmAnnotationType.setDefaultFilter( true );
			
			filterablePsmAnnotationType.setDefaultFilterValue( AnnotationType_Constants.PSM_RANK_DEFAULT );
			
			filterablePsmAnnotationTypeList.add( filterablePsmAnnotationType );
			
		}
		{
			// Score
			
			FilterablePsmAnnotationType filterablePsmAnnotationType = new FilterablePsmAnnotationType();
			
			filterablePsmAnnotationType.setName( AnnotationType_Constants.ANNOTATION_NAME_XQUEST_SCORE );
			filterablePsmAnnotationType.setDescription( AnnotationType_Constants.ANNOTATION_DESCRIPTION_XQUEST_SCORE );
			
			filterablePsmAnnotationType.setFilterDirection( FilterDirectionType.ABOVE );
			
			filterablePsmAnnotationType.setDefaultFilter( false );
			
			filterablePsmAnnotationTypeList.add( filterablePsmAnnotationType );
			
		}
		
		DescriptivePsmAnnotationTypes descriptivePsmAnnotationTypes = new DescriptivePsmAnnotationTypes();
		
		psmAnnotationTypes.setDescriptivePsmAnnotationTypes( descriptivePsmAnnotationTypes );
		
		List<DescriptivePsmAnnotationType> descriptivePsmAnnotationTypeList = descriptivePsmAnnotationTypes.getDescriptivePsmAnnotationType();

		{
			//  Obs. mass

			DescriptivePsmAnnotationType descriptivePsmAnnotationType = new DescriptivePsmAnnotationType();
			
			descriptivePsmAnnotationType.setName( AnnotationType_Constants.ANNOTATION_NAME_OBS_MASS );
			descriptivePsmAnnotationType.setDescription( AnnotationType_Constants.ANNOTATION_DESCRIPTION_OBS_MASS );
			
			descriptivePsmAnnotationTypeList.add( descriptivePsmAnnotationType );
		}

		{
			//  Calc. mass

			DescriptivePsmAnnotationType descriptivePsmAnnotationType = new DescriptivePsmAnnotationType();
			
			descriptivePsmAnnotationType.setName( AnnotationType_Constants.ANNOTATION_NAME_CALC_MASS );
			descriptivePsmAnnotationType.setDescription( AnnotationType_Constants.ANNOTATION_DESCRIPTION_CALC_MASS );
			
			descriptivePsmAnnotationTypeList.add( descriptivePsmAnnotationType );
		}
		
	}
	
}

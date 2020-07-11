package org.yeastrc.proxl.xml.xquest.constants;

import java.math.BigDecimal;

public class AnnotationType_Constants {

	//  PSM
	
	//  Filterable
	
	//  FDR: Smaller better
	
	public static final String ANNOTATION_NAME_FDR = "FDR";
	public static final String ANNOTATION_DESCRIPTION_FDR = "False Discovery Rate";
	
	//  Rank: Smaller better

	public static final String ANNOTATION_NAME_RANK = "Rank";
	public static final String ANNOTATION_DESCRIPTION_RANK = "PSM Rank for Scan";

	//  Score: Bigger better
	
	public static final String ANNOTATION_NAME_XQUEST_SCORE = "Score";
	public static final String ANNOTATION_DESCRIPTION_XQUEST_SCORE = "XQuest score";
	

	//  Descriptive

	public static final String ANNOTATION_NAME_OBS_MASS = "Obs. mass";
	public static final String ANNOTATION_DESCRIPTION_OBS_MASS = "Mass of precursor";

	public static final String ANNOTATION_NAME_CALC_MASS = "Calc. mass";
	public static final String ANNOTATION_DESCRIPTION_CALC_MASS = "Calculated mass for PSM";

	// NO:  Charge, Obs. m/z
	
	
	//  Default Filter Cutoffs

	public static final BigDecimal PSM_FDR_DEFAULT = new BigDecimal( "0.01" );
	

	public static final BigDecimal PSM_RANK_DEFAULT = new BigDecimal( "1" );
}

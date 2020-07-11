package org.yeastrc.proxl.xml.xquest.utils;

import org.yeastrc.proxl.xml.xquest.enums.XQuestLinkTypes;


public class GetProxlLinkTypeFromXquestLinkType {

	public static XQuestLinkTypes getProxlLinkTypeFromXquestLinkType( String xquestLinkType ) throws Exception {
		
		
		if ( "xlink".equals(xquestLinkType) ) {
			
			return XQuestLinkTypes.CROSSLINK;
			
		} else if ( "monolink".equals(xquestLinkType) ) {
			
			return XQuestLinkTypes.MONOLINK;  //  Saved in psm table as TYPE_UNLINKED
			
		} else if ( "intralink".equals(xquestLinkType) ) {
			
			return XQuestLinkTypes.LOOPLINK;
			
		} else {
			
			throw new Exception( "Unknown xquestLinkType value: " + xquestLinkType );
			
		}
		
		
		
	}
}

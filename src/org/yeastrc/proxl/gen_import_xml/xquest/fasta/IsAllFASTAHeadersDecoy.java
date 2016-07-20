package org.yeastrc.proxl.gen_import_xml.xquest.fasta;

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;
import org.yeastrc.fasta.FASTAHeader;

/**
 * 
 *
 */
public class IsAllFASTAHeadersDecoy {

	private static final Logger log = Logger.getLogger( IsAllFASTAHeadersDecoy.class );

	/**
	 * private constructor
	 */
	private IsAllFASTAHeadersDecoy(){}
	
	public static IsAllFASTAHeadersDecoy getInstance(  )  { 
		
		return new IsAllFASTAHeadersDecoy();
	}

	/**
	 * @param kojakPsmDataObject
	 * @param proxlInputRoot
	 * @return
	 */
	public boolean isAllFASTAHeadersDecoy( Set<FASTAHeader> headers, Collection<String> decoyIdentificationStringFromConfFileList ) {
		
		boolean allDecoysForProtein = true;
		
		for ( FASTAHeader header : headers ) {

			for ( String decoyIdentificationString : decoyIdentificationStringFromConfFileList ) {

				if ( ! header.getName().contains( decoyIdentificationString ) ) {

					//  Not decoy found

					allDecoysForProtein = false;
					break;

				}
			}
			
			if ( ! allDecoysForProtein ) {
				
				break;
			}
		}
	
	
		return allDecoysForProtein;
		
		
	}

}

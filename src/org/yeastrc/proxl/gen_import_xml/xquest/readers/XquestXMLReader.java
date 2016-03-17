package org.yeastrc.proxl.gen_import_xml.xquest.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;
import org.yeastrc.proxl.gen_import_xml.xquest.objects.ReadNextSearchHitResult;



public class XquestXMLReader {

	private static Logger log = Logger.getLogger(XquestXMLReader.class);
	

	//  These element tags are in the xml 

	private static final String XQUEST_MERGER_XML_TAG = "xquest_merger";
	private static final String XQUEST_RESULTS_XML_TAG = "xquest_results";

	private static final String SPECTRUM_SEARCH_XML_TAG = "spectrum_search";
	private static final String SEARCH_HIT_XML_TAG = "search_hit";
	

	//  The "version" attribute on the  "xquest_merger" element
	
	private static final String XQUEST_MERGER_VERSION_ATTRIBUTE_XML_TAG = "version";
	
	
	private File XMLFile;
//	private boolean atEnd;
	
	private XMLEventReader eventReader;
	
	
	private Map<String,String> xquestMergerContents = null;
	private Map<String,String> xquestResultsContents = null;

	private String xquestVersion = null;
	

	public String getXquestVersion() {
		return xquestVersion;
	}

	public Map<String, String> getXquestMergerContents() {
		return xquestMergerContents;
	}

	public Map<String, String> getXquestResultsContents() {
		return xquestResultsContents;
	}

	// prevent direct instantiation of XMLFileReader
	private XquestXMLReader() {
//		this.atEnd = false;
		this.eventReader = null;
	}

	/**
	 * get an instance of XquestXMLReader
	 * @return an instance of XquestXMLReader
	 * @throws Exception 
	 */
	public static XquestXMLReader getInstance(File file ) throws Exception {
		
		XquestXMLReader reader = new XquestXMLReader();
		
		reader.XMLFile = file;
		
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		// Setup a new eventReader
		InputStream in = new FileInputStream( reader.XMLFile );


		reader.eventReader = inputFactory.createXMLEventReader(in);

		reader.readMergerAndResultsData();

		return reader;
	}

	/**
	 * @throws XMLStreamException
	 */
	public void close() throws XMLStreamException {
		
		if ( eventReader != null ) {
			
			eventReader.close();
		}
	}
	
	
	private void readMergerAndResultsData() throws Exception {
		
		while ( true ) {

			XMLEvent event = null;
			
			try {
			
				event = eventReader.nextEvent();
			
			} catch ( java.util.NoSuchElementException e ) {
				
				break;
			}
			
			if ( event == null ) {
				
				break;
			}

			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();

				String elementName = startElement.getName().getLocalPart();

				
				// If we have a xquest_merger element
				if ( XQUEST_MERGER_XML_TAG.equals( elementName ) ) {


					xquestMergerContents = getAttributes( startElement );
					
					xquestVersion = xquestMergerContents.get( XQUEST_MERGER_VERSION_ATTRIBUTE_XML_TAG );
					

					// If we have a xquest_results element
				} else if ( XQUEST_RESULTS_XML_TAG.equals( elementName ) ) {
					
					xquestResultsContents = getAttributes( startElement );
					
					break;  //  EARLY EXIT FROM LOOP After get xquest_results contents
				

					// If we have a spectrum_search element, we have gone too far
				} else if ( SPECTRUM_SEARCH_XML_TAG.equals( elementName ) ) {

					String msg = "ERROR reading xquest file: " + XMLFile 
							+ ".  Found tag '" + SPECTRUM_SEARCH_XML_TAG + "' while looking for tags "
							+ "'" + XQUEST_MERGER_XML_TAG + "' and '" + XQUEST_RESULTS_XML_TAG + "'.";
				
					log.error( msg );
					
					throw new Exception( msg );
				}

			}

		}
		
		
	}
	
	
	
	
	/**
	 * @return
	 * @throws XMLStreamException
	 */
	public ReadNextSearchHitResult readNextSearchHit(  ) throws XMLStreamException {
		
		
		Map<String,String> spectrumSearchContents = null;
		Map<String,String> searchHitContents = null;


		while ( true ) {

			XMLEvent event = null;
			
			try {
			
				event = eventReader.nextEvent();
			
			} catch ( java.util.NoSuchElementException e ) {
				
				break;
			}
			
			if ( event == null ) {
				
				break;
			}

			if (event.isStartElement()) {
				StartElement startElement = event.asStartElement();

				String elementName = startElement.getName().getLocalPart();

				
				// If we have a spectrum_search element
				if ( SPECTRUM_SEARCH_XML_TAG.equals( elementName ) ) {


					spectrumSearchContents = getAttributes( startElement );

					// If we have a search_hit element
				} else if ( SEARCH_HIT_XML_TAG.equals( elementName ) ) {
					
					if ( searchHitContents == null ) {
					
						//  only get first hit
						searchHitContents = getAttributes( startElement );
					}
				}

			} else if (event.isEndElement()) {

				EndElement endElement = event.asEndElement();

				String elementName = endElement.getName().getLocalPart();

				// If we have a END 'spectrum_search' element
				if ( SPECTRUM_SEARCH_XML_TAG.equals(elementName) ) {
					
					
					if ( searchHitContents != null ) {
						
						//  found a search hit for this spectrum search so exit and return it.
						
						break;
					}
				}
			}
		}
		

		if ( spectrumSearchContents == null || searchHitContents == null ) {
			
			return null;  //  no data found
		}
		
		ReadNextSearchHitResult readNextSearchHitResult = new ReadNextSearchHitResult();
		
		readNextSearchHitResult.setSpectrumSearchContents( spectrumSearchContents );
		readNextSearchHitResult.setSearchHitContents( searchHitContents );
		
		//  reset for next read
		
		spectrumSearchContents = null;
		searchHitContents = null;
		
		return readNextSearchHitResult;
	}
	


	/**
	 * @param startElement
	 */
	private Map<String,String> getAttributes( StartElement startElement )  {

		// We read the attributes from this tag
		@SuppressWarnings("unchecked")
		Iterator<Attribute> attributes = startElement.getAttributes();


		Map<String,String> attributeValues = new HashMap<String, String>();


		while (attributes.hasNext()) {
			
			Attribute attribute = attributes.next();

			String attrName = attribute.getName().getLocalPart();

			String attrValue = attribute.getValue();

			attributeValues.put( attrName, attrValue );
		}

		return attributeValues;
		
	}
}

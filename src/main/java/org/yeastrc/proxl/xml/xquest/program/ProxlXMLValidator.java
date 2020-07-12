package org.yeastrc.proxl.xml.xquest.program;

import org.yeastrc.proxl_import.api.xml_dto.ProxlInput;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ProxlXMLValidator {

    public static void validateProxlXML(File proxlXMLFile) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(ProxlInput.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ProxlInput proxlInput = (ProxlInput)jaxbUnmarshaller.unmarshal( proxlXMLFile );

    }

}

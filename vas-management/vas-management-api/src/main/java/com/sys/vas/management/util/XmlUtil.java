package com.sys.vas.management.util;

import com.sys.vas.management.dto.ExportXmlDto;
import com.sys.vas.management.dto.entity.*;
import org.springframework.core.io.ByteArrayResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class XmlUtil {
    public static ExportXmlDto generate(ServiceEntity serviceEntity) throws ParserConfigurationException, IOException, TransformerException {
        if (serviceEntity == null) {
            return null;
        }
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();

        Element root = createRoot(doc, serviceEntity);
        doc.appendChild(root);
        ByteArrayResource array = build(doc);
        return new ExportXmlDto(array, serviceEntity.getName());
    }

    private static ByteArrayResource build(Document document) throws IOException, TransformerException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(outputStream);
            transformer.transform(domSource, streamResult);;
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    private static Element createRoot(Document doc, ServiceEntity data) {
        Element ele = doc.createElement("service");

        Element name = doc.createElement("name");
        name.setTextContent(data.getName());
        ele.appendChild(name);

        Element desc = doc.createElement("desc");
        desc.setTextContent(data.getDescription());
        ele.appendChild(desc);

        Element disable = doc.createElement("disable");
        disable.setTextContent(String.valueOf(data.getDisable()));
        ele.appendChild(disable);

        Element disableSms = doc.createElement("disableSms");
        disableSms.setTextContent(data.getDisableSms());
        ele.appendChild(disableSms);

        Element createdDate = doc.createElement("createdDate");
        createdDate.setTextContent(data.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        ele.appendChild(createdDate);

        ele.appendChild(createActions(doc, data.getActions()));
        return ele;
    }

    private static Element createActions(Document doc, Set<ActionEntity> actionEntities) {
        Element ele = doc.createElement("actions");
        actionEntities.forEach(e -> {
            Element action = doc.createElement("action");

            Element description = doc.createElement("description");
            description.setTextContent(e.getDescription());
            action.appendChild(description);

            action.appendChild(getApi(doc, e.getApi()));
            action.appendChild(keywords(doc, e.getKeywords()));
            ele.appendChild(action);
        });
        return ele;
    }

    private static Element getApi(Document doc, ApiEntity apiEntity) {
        Element ele = doc.createElement("api");

        Element name = doc.createElement("name");
        name.setTextContent(apiEntity.getName());
        ele.appendChild(name);

        Element desc = doc.createElement("desc");
        desc.setTextContent(apiEntity.getDescription());
        ele.appendChild(desc);

        Element version = doc.createElement("version");
        version.setTextContent(apiEntity.getVersion());
        ele.appendChild(version);

        Element xml = doc.createElement("xml");
        xml.setTextContent(apiEntity.getXml());
        ele.appendChild(xml);

        ele.appendChild(csResponses(doc, apiEntity.getCxResponses()));

        return ele;
    }

    private static Element keywords(Document doc, Set<KeywordEntity> keywords) {
        Element ele = doc.createElement("keywords");
        keywords.forEach(e -> {
            Element keyword = doc.createElement("keyword");

            Element firstKey = doc.createElement("firstKey");
            firstKey.setTextContent(e.getFirstKey());
            keyword.appendChild(firstKey);

            Element regEx = doc.createElement("regEx");
            regEx.setTextContent(e.getRegEx());
            keyword.appendChild(regEx);

            ele.appendChild(keyword);
        });

        return ele;
    }

    private static Element csResponses(Document doc, Set<CxResponseEntity> cxResponses) {
        Element ele = doc.createElement("cs_responses");
        cxResponses.forEach(e -> {
            Element cs_response = doc.createElement("cs_response");

            Element sms = doc.createElement("sms");
            sms.setTextContent(e.getSms());
            cs_response.appendChild(sms);

            Element resCode = doc.createElement("resCode");
            resCode.setTextContent(String.valueOf(e.getResCode()));
            cs_response.appendChild(resCode);

            Element resDesc = doc.createElement("resDesc");
            resDesc.setTextContent(String.valueOf(e.getResDesc()));
            cs_response.appendChild(resDesc);

            ele.appendChild(cs_response);
        });

        return ele;
    }

}

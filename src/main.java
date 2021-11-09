import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

public class main {

    public static void my_xpath(String file) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
        docFact.setNamespaceAware(true);
        DocumentBuilder builder = docFact.newDocumentBuilder();
        Document doc = builder.parse(file);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

//        String exp = "//NETWORK/VARIABLE["+



    }
}
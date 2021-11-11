import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class main {
    private static HashMap<String, NetNode> net = new HashMap<>();

    public static void my_xpath(String file) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
        docFact.setNamespaceAware(true);
        DocumentBuilder builder = docFact.newDocumentBuilder();
        Document doc = builder.parse(file);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

        XPathExpression expr = xpath.compile("//NETWORK");
        var_xpath(xpath, doc);
        def_xpath(xpath, doc);

    }

    private static void var_xpath(XPath xPath, Document doc) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xPath.compile("//VARIABLE").evaluate(doc, XPathConstants.NODESET); //from string to nodeset
        for (int i=1; i<= nodeList.getLength(); i++){ //nodeset --> NetNode --> net
            NodeList tmpName = (NodeList) xPath.compile(String.format("/NETWORK/VARIABLE[%d]/NAME", i)).evaluate(doc, XPathConstants.NODESET);
            NodeList tmpOutcome = (NodeList) xPath.compile(String.format("/NETWORK/VARIABLE[%d]/OUTCOME", i)).evaluate(doc, XPathConstants.NODESET);
            String name = tmpName.item(0).getTextContent();
            ArrayList<String> outcomes = new ArrayList<>();
            for (int j = 0; j < tmpOutcome.getLength(); j++) {
                outcomes.add(tmpOutcome.item(j).getTextContent());
            }

            NetNode netNode = new NetNode(name, outcomes);
            net.put(name, netNode);
        }

    }

    private static void def_xpath(XPath xPath, Document doc) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xPath.compile("//DEFINITION").evaluate(doc, XPathConstants.NODESET);
        for (int i=1; i<= nodeList.getLength(); i++){
            NodeList name = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/FOR", i)).evaluate(doc, XPathConstants.NODESET);
            NodeList tmpGiven = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/GIVEN", i)).evaluate(doc, XPathConstants.NODESET);
            NodeList tmpTable = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/TABLE", i)).evaluate(doc, XPathConstants.NODESET);
            String n = name.item(0).getTextContent();
            NetNode curr = net.get(n);
            for (int j=0; j< tmpGiven.getLength(); j++){
                curr.Parents.add(net.get(tmpGiven.item(j).getTextContent()));
                net.get(tmpGiven.item(j).getTextContent()).Children.add(curr);

            }
            String table = tmpTable.item(0).getTextContent();
            String[] t = table.split(" ");
            ArrayList<Double> arr = new ArrayList<>();
            for (String s : t) {
                double d =Double.parseDouble(s);
                arr.add(d);
            }
            curr.setCpt(arr);
            System.out.println(curr.getName()+": " +curr.getCpt().getTable());
        }
    }

    public static void main(String[] args) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        my_xpath("C:\\Users\\PC\\IdeaProjects\\AI_algo_ex\\alarm_net.xml");
    }
}
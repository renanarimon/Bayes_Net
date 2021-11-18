import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Net {
    private HashMap<String, NetNode> bayesNet;
    private ArrayList<String> QueryBayesBall;
    private ArrayList<String> QueryEliminate;

    public Net() {
        this.bayesNet = new HashMap<String, NetNode>();
        this.QueryBayesBall = new ArrayList<String>();
        this.QueryEliminate = new ArrayList<String>();

    }

    public HashMap<String, NetNode> getBayesNet() {
        return bayesNet;
    }
    public ArrayList<String> getQueryBayesBall() {
        return QueryBayesBall;
    }

    public ArrayList<String> getQueryEliminate() {
        return QueryEliminate;
    }

    public void readFromFile(String file_path) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        File f = new File(file_path);
        Scanner myReader = new Scanner(f);
        String xml = myReader.nextLine();
        this.my_xpath(xml);
        while (myReader.hasNextLine()){
            String str = myReader.nextLine();
            if (str.charAt(1)!='('){ // bayes ball Q
                this.QueryBayesBall.add(str);
            }
            else {
                this.QueryEliminate.add(str);
            }

        }
        myReader.close();
    }

    public void my_xpath(String file) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
        docFact.setNamespaceAware(true);
        DocumentBuilder builder = docFact.newDocumentBuilder();
        Document doc = builder.parse(file);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();

        XPathExpression expr = xpath.compile("//NETWORK");
        this.var_xpath(xpath, doc);
        this.def_xpath(xpath, doc);

    }

    private void var_xpath(XPath xPath, Document doc) throws XPathExpressionException {
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
            this.bayesNet.put(name, netNode);
        }

    }

    private void def_xpath(XPath xPath, Document doc) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xPath.compile("//DEFINITION").evaluate(doc, XPathConstants.NODESET);
        for (int i=1; i<= nodeList.getLength(); i++){
            NodeList name = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/FOR", i)).evaluate(doc, XPathConstants.NODESET);
            NodeList tmpGiven = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/GIVEN", i)).evaluate(doc, XPathConstants.NODESET);
            NodeList tmpTable = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/TABLE", i)).evaluate(doc, XPathConstants.NODESET);
            String n = name.item(0).getTextContent();
            NetNode curr = this.bayesNet.get(n);
            for (int j=0; j< tmpGiven.getLength(); j++){

//                System.out.println(curr);
                curr.Parents.add(tmpGiven.item(j).getTextContent());
                this.bayesNet.get(tmpGiven.item(j).getTextContent()).Children.add(curr.getName());

            }
            String table = tmpTable.item(0).getTextContent();
            String[] t = table.split(" ");
            ArrayList<Double> arr = new ArrayList<>();
            for (String s : t) {
                double d =Double.parseDouble(s);
                arr.add(d);
            }
            curr.setCpt(arr);
        }
    }

    public String toString(){
        String st="";
        for(NetNode n: getBayesNet().values()){
            st += n.toString() + "\n";
        }
        return st;
    }

}
/*
 * @project AI_algo_ex
 * @author Renana Rimon
 */
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Net {
    private HashMap<String, NetNode> bayesNet;
    private ArrayList<String> QueryBayesBall;
    private ArrayList<String> QueryEliminate;

    public Net() {
        this.bayesNet = new HashMap<>();
        this.QueryBayesBall = new ArrayList<>();
        this.QueryEliminate = new ArrayList<>();

    }

    /*
    copy constructor
     */
    public Net(Net other){
        this.bayesNet = new HashMap<String, NetNode>();
        for (String s: other.bayesNet.keySet()){
            NetNode n = new NetNode(other.bayesNet.get(s));
            this.bayesNet.put(s, n);
        }
        this.QueryBayesBall = new ArrayList<>();
        this.QueryBayesBall.addAll(other.QueryBayesBall);
        this.QueryEliminate = new ArrayList<>();
        this.QueryEliminate.addAll(other.QueryEliminate);

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

    /*
    read from txt file:
        * create net
        * QueryBayesBall
        * QueryEliminate
     */
    public void readFromFile(String file_path) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        File f = new File(file_path);
        Scanner myReader = new Scanner(f);
        String xml = myReader.nextLine();
        this.my_xpath(xml);
        while (myReader.hasNextLine()) {
            String str = myReader.nextLine();
            if (str.charAt(1) != '(') { // bayes ball Q
                this.QueryBayesBall.add(str);
            } else {
                this.QueryEliminate.add(str);
            }

        }
        myReader.close();
    }

  /*
  create XPATH to NETWORK in xml
   */
    public void my_xpath(String file) throws IOException, SAXException, ParserConfigurationException, XPathExpressionException {
        DocumentBuilderFactory docFact = DocumentBuilderFactory.newInstance();
        docFact.setNamespaceAware(true);
        DocumentBuilder builder = docFact.newDocumentBuilder();
        try {
            Document doc = builder.parse(file);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            XPathExpression expr = xpath.compile("//NETWORK");
            this.var_xpath(xpath, doc);
            this.def_xpath(xpath, doc);
        }catch (Exception e){
            System.err.println("file doesn't found");
        }

    }

    /**
     * create NetNode by VARIABLE from xml
     * @param xPath
     * @param doc
     * @throws XPathExpressionException
     */
    private void var_xpath(XPath xPath, Document doc) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xPath.compile("//VARIABLE").evaluate(doc, XPathConstants.NODESET); //from string to nodeset
        for (int i = 1; i <= nodeList.getLength(); i++) { //nodeset --> NetNode --> net
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

    /**
     * insert DEFINITION to NetNode from xml
     * @param xPath
     * @param doc
     * @throws XPathExpressionException
     */
    private void def_xpath(XPath xPath, Document doc) throws XPathExpressionException {
        NodeList nodeList = (NodeList) xPath.compile("//DEFINITION").evaluate(doc, XPathConstants.NODESET);
        for (int i = 1; i <= nodeList.getLength(); i++) {
            NodeList name = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/FOR", i)).evaluate(doc, XPathConstants.NODESET);
            NodeList tmpGiven = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/GIVEN", i)).evaluate(doc, XPathConstants.NODESET);
            NodeList tmpTable = (NodeList) xPath.compile(String.format("/NETWORK/DEFINITION[%d]/TABLE", i)).evaluate(doc, XPathConstants.NODESET);
            String n = name.item(0).getTextContent();
            NetNode curr = this.bayesNet.get(n);
            for (int j = 0; j < tmpGiven.getLength(); j++) {
                curr.Parents.add(tmpGiven.item(j).getTextContent());
                this.bayesNet.get(tmpGiven.item(j).getTextContent()).Children.add(curr.getName());
            }

            //CPT
            String table = tmpTable.item(0).getTextContent();
            String[] t = table.split(" ");
            ArrayList<Double> arr = new ArrayList<>();
            for (String s : t) {
                double d = Double.parseDouble(s);
                arr.add(d);
            }


            int j=0;
            while (j<arr.size()){
                if (curr.Parents.size()>0){
                    rec("", j, 0, curr, arr);
                    break;
                }
                for (String s: curr.getOutcomes()){
                    curr.getCpt().add(s, arr.get(j++));
                }
            }
            curr.getCpt().Given.addAll(curr.Parents);
        }
    }

    /**
     * A recursive function that adds the values to the CPT according to the exact string key.
     * @param st = string of outcomes
     * @param j = index on arr
     * @param k = index on parents
     * @param curr = NetNode
     * @param arr = values for table (Double)
     * @return int j = index on arr
     */
    private int rec(String st,int j, int k, NetNode curr, ArrayList<Double>arr) {
        if (k == curr.Parents.size()-1) {
            for (String s: this.bayesNet.get(curr.Parents.get(k)).getOutcomes()){
                for (String s1 : curr.getOutcomes()) {
                    if (!Objects.equals(st, "")){
                        curr.getCpt().add(st+ s+"-" + s1, arr.get(j++));
                    }else {
                        curr.getCpt().add(st + s+"-" + s1, arr.get(j++));
                    }

                }
            }
            return j;
        }
        for (String s: this.bayesNet.get(curr.Parents.get(k)).getOutcomes()){
            j = rec(st+s+"-", j, k+1, curr, arr);
        }
        return j;
    }


    public String toString() {
        String st = "";
        for (NetNode n : getBayesNet().values()) {
            st += n.toString() + "\n";
        }
        return st;
    }

}
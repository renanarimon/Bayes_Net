import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {


    public static void main(String[] args) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        Net net = new Net();
        net.readFromFile("C:\\Users\\PC\\IdeaProjects\\AI_algo_ex\\src\\input.txt");

        BayesBall bayesBall = new BayesBall(net);
        bayesBall.goOverQ();
        EliminateAlgo eliminateAlgo = new EliminateAlgo(net);
        eliminateAlgo.goOverE();

        System.out.println(eliminateAlgo.getFactors());
















    }
}

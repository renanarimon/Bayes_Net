import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;

public class main {


    public static void main(String[] args) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        Net net = new Net();
        net.readFromFile("C:\\Users\\PC\\IdeaProjects\\AI_algo_ex\\src\\input.txt");

        BayesBall bayesBall = new BayesBall(net);
        bayesBall.goOverQ();
        EliminateAlgo eliminateAlgo = new EliminateAlgo(net);
        eliminateAlgo.goOverE();
//        System.out.println(net.getBayesNet().get("E"));
//        String p = net.getBayesNet().get("E").Parents.get(0);
//        System.out.println(net.getBayesNet().get(p));
//        System.out.println(net.getBayesNet().get(p).getOutcomes().size());
//        System.out.println(net);
        System.out.println(eliminateAlgo.getHidden());
        System.out.println(eliminateAlgo.getQuery());
        System.out.println(eliminateAlgo.getEvidence());
        System.out.println(net);

        System.out.println(eliminateAlgo.isAncestor(net.getBayesNet().get("E"), net.getBayesNet().get("J")));






    }
}

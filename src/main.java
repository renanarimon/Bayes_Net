import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class main {

    public static void main(String[] args) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
//        Net n = new Net();
//        n.readFromFile("C:\\Users\\PC\\IdeaProjects\\AI_algo_ex\\src\\input.txt");
////        n.my_xpath("C:\\Users\\PC\\IdeaProjects\\AI_algo_ex\\alarm_net.xml");
//        System.out.println(n.getBayesNet().values());
//        System.out.println(n.getQueryBayesBall());
//        System.out.println(n.getQueryEliminate());

        BayesBall bayesBall = new BayesBall();
        bayesBall.goOverQ();
    }
}

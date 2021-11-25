import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileWriter;
import java.io.IOException;


public class Main {


    public static void main(String[] args) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        Net net = new Net();
        net.readFromFile("C:\\Users\\PC\\IdeaProjects\\AI_algo_ex\\src\\input.txt");
        System.out.println(net);
        BayesBall bayesBall = new BayesBall(net);
        bayesBall.goOverQ();
        EliminateAlgo eliminateAlgo = new EliminateAlgo(net);
        eliminateAlgo.goOverE(net);

        try {
            FileWriter fw = new FileWriter("C:\\Users\\PC\\IdeaProjects\\AI_algo_ex\\src\\output.txt");
            for (String s : bayesBall.getAnsBayesBall()) {
                fw.write(s + "\n");
            }
            for (String s : eliminateAlgo.getAnsEliminate()) {
                fw.write(s + "\n");
            }
            fw.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
}

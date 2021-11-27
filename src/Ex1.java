/*
  @project AI_algo_ex
 * @author Renana Rimon
 */
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Ex1 {


    public static void main(String[] args) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        Net net = new Net();
        Path pathIn = Paths.get("input.txt");
        net.readFromFile(""+pathIn.toAbsolutePath());
        BayesBall bayesBall = new BayesBall(net);
        bayesBall.goOverQ();
        EliminateAlgo eliminateAlgo = new EliminateAlgo(net);
        eliminateAlgo.goOverE(net);

        try {
            Path pathOut = Paths.get("output.txt");
            FileWriter fw = new FileWriter("" + pathOut.toAbsolutePath());
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

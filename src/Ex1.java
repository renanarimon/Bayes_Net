/*
  @project AI_algo_ex
 * @author Renana Rimon
 */
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Ex1 {


    public static void main(String[] args) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        Net net = new Net();
        Path pathIn = Paths.get("input.txt");
        try {
            net.readFromFile(""+pathIn.toAbsolutePath());
        }catch (FileNotFoundException e){
            System.out.println("file not found");
        }

        BayesBall bayesBall = new BayesBall(net);
        try{
            bayesBall.goOverQ();
        } catch (Exception e){
            bayesBall.getAnsBayesBall().add("");
        }
        EliminateAlgo eliminateAlgo = new EliminateAlgo(net);
        try{
            eliminateAlgo.goOverE(net);
        }catch (Exception e){
            eliminateAlgo.getAnsEliminate().add("");
        }

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

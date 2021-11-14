import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class BayesBall {
    private Net net;
    private Stack<NetNode> stack;

    public BayesBall() throws XPathExpressionException, IOException, ParserConfigurationException, SAXException {
        this.stack = new Stack<NetNode>();
        this.net = new Net();
        net.readFromFile("C:\\Users\\PC\\IdeaProjects\\AI_algo_ex\\src\\input.txt");
    }


    public void goOverQ() {
        for (int j=0; j<net.getQueryBayesBall().size(); j++){
            for (NetNode n:net.getBayesNet().values()) {
                n.setGiven(null);
                n.fromChild = false;
                n.fromParent = false;
            }
            String q = net.getQueryBayesBall().get(j);
            NetNode start = net.getBayesNet().get(q.charAt(0)+"");
            NetNode end = net.getBayesNet().get(q.charAt(2)+"");
            for (int i=4; i<q.length(); i+=4){
                net.getBayesNet().get(q.charAt(i)+"").setGiven(q.charAt(i+2)+"");
            }
            dfs(start,end);
        }

    }

    public void dfs(NetNode start, NetNode end) {
        Stack<NetNode> stack = new Stack<NetNode>();
        stack.push(start);
        while (!stack.isEmpty()) {
            if (stack.contains(end)){ //there is a path --> depended
                System.out.println("no");
                return;
            }
            NetNode curr = stack.pop();

            if (curr.getGiven() == null) { //not colored
                for (String c : curr.Children) { //push children to stack
                    NetNode child = net.getBayesNet().get(c);
                    if (!child.fromParent) {
                        child.fromParent = true;
                        stack.push(child);
                    }
                }
                if (curr.fromChild) {
                    for (String p : curr.Parents) {
                        NetNode parent = net.getBayesNet().get(p);
                        if (!parent.fromChild) {
                            parent.fromChild = true;
                            stack.push(parent);
                        }
                    }
                }
            } else { //colored
                if (curr.fromParent) {
                    for (String p : curr.Parents) {
                        NetNode parent = net.getBayesNet().get(p);
                        if (!parent.fromChild) {
                            parent.fromChild = true;
                            stack.push(parent);
                        }
                    }
                }
            }

        }
        System.out.println("yes"); // no path --> not depended
    }


}


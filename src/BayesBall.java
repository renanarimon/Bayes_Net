import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class BayesBall {
    private Net net;
    private Stack<NetNode> stack;

    public BayesBall() {
        this.stack = new Stack<NetNode>();
        this.net = new Net();
    }



    private void goOverQ(){
    }

    public void dfs(NetNode start, NetNode end) {
        Stack<NetNode> stack = new Stack<NetNode>();
        stack.push(start);
        while (!stack.isEmpty()) {
            NetNode curr = stack.pop();

            if (!curr.getGiven()){ //not colored
                for (String c: curr.Children){ //push children to stack
                    NetNode child = net.getBayesNet().get(c);
                    if (!child.fromParent) {
                        child.fromParent = true;
                        stack.push(child);
                    }
                }
                if (curr.fromChild){
                    for (String p : curr.Parents){
                        NetNode parent = net.getBayesNet().get(p);
                        if (!parent.fromChild) {
                            parent.fromChild = true;
                            stack.push(parent);
                        }
                    }
                }
            }
            else { //colored
                if (curr.fromParent){
                    for (String p : curr.Parents){
                        NetNode parent = net.getBayesNet().get(p);
                        if (!parent.fromChild) {
                            parent.fromChild = true;
                            stack.push(parent);
                        }
                    }
                }
            }


        }

    }



}


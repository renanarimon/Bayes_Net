import java.util.Arrays;
import java.util.Stack;

public class BayesBall {
    private Net net;
    private Stack<NetNode> stack;

    public BayesBall(Net net){
        this.stack = new Stack<>();
        this.net = net;
    }


    public void goOverQ() {
        for (int i = 0; i < net.getQueryBayesBall().size(); i++) {
            for (NetNode n : net.getBayesNet().values()) {
                n.setGiven(null);
            }

            String[] splitQ;
            String q = net.getQueryBayesBall().get(i);
            splitQ = q.split("\\|");

            NetNode start = net.getBayesNet().get(splitQ[0].split("-")[0]);
            NetNode end = net.getBayesNet().get(splitQ[0].split("-")[1]);

            if (splitQ.length>1) { // if there are given
                String[] splitE;
                splitE = splitQ[1].split(",");
                for (String s : splitE) {
                    String[] tmp;
                    tmp = s.split("=");
                    net.getBayesNet().get(tmp[0]).setGiven(tmp[1]);
                }
            }

            System.out.println(dfs(start, end));


        }
    }

    /**
     * find if 2 vars are independent.
     * independent --> "yes"
     * dependent --> "no"
     * the algorithm is DFS with BayesBall rules.
     * @param start
     * @param end
     *
     */
    public String dfs(NetNode start, NetNode end) {
        stack.push(start);
        NetNode curr = stack.pop();
        pushChildren(curr);
        pushParents(curr);

        while (!stack.isEmpty()) {
            if (stack.contains(end)) { //there is a path --> depended
                cleanNet();
                return "no";
            }
            curr = stack.pop();

            if (curr.getGiven() == null) { //not given
                pushChildren(curr);
                if (curr.fromChild) {
                    pushParents(curr);
                }
            } else { //given
                if (curr.fromParent) {
                    pushParents(curr);
                }
            }

        }
        cleanNet();
        return "yes"; // no path --> not depended
    }

    private void cleanNet(){
        this.stack.clear();
        for (NetNode n : net.getBayesNet().values()) {
//            n.setGiven(null);
            n.fromChild = false;
            n.fromParent = false;
        }
    }

    /**
     * help function for dfs():
     * push parents to stack
     * @param curr
     */
    private void pushParents(NetNode curr) {
        for (String p : curr.Parents) {
            NetNode parent = net.getBayesNet().get(p);
            if (!parent.fromChild) {
                parent.fromChild = true;
                this.stack.push(parent);
            }
        }
    }
    /**
     * help function for dfs():
     * push children to stack
     * @param curr
     */
    private void pushChildren(NetNode curr) {
        for (String c : curr.Children) { //push children to stack
            NetNode child = net.getBayesNet().get(c);
            if (!child.fromParent) {
                child.fromParent = true;
                this.stack.push(child);
            }
        }
    }


}


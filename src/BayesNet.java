import java.util.ArrayList;

public class BayesNet {
    private ArrayList<NetNode> network;

    public BayesNet(ArrayList<NetNode> netNodes) {
        this.network = new ArrayList<NetNode>();
    }

    public void add(NetNode n) {
        this.network.add(n);
    }

    public NetNode get(String name) {
        if (!network.isEmpty()) {
            for (NetNode n: network) {
                if (n.getName().equals(name)){
                    return n;
                }
            }

        }
    return null;
    }
}

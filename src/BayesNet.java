import java.util.ArrayList;

public class BayesNet {
    private ArrayList<NetNode> network;
    private int size;

    public BayesNet() {
        this.network = new ArrayList<NetNode>();
        size = 0;
    }

    public ArrayList<NetNode> getNetwork() {
        return network;
    }

    public int getSize() {
        return size;
    }

    public void add(NetNode n) {
        this.network.add(n);
        size++;
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

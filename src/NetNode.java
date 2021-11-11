import java.util.ArrayList;

public class NetNode {

    private final String Name;
    private String Given;
    private ArrayList<String> outcomes;
    private boolean fromParent;
    private boolean fromChild;
    public ArrayList<NetNode> Children;
    public ArrayList<NetNode> Parents;
    private CPT cpt;



    public NetNode(String name, ArrayList<String> outcomes) {
        Name = name;
        Given = null;
        fromChild = false;
        fromParent = false;
        Children = new ArrayList<NetNode>();
        Parents = new ArrayList<NetNode>();
        this.outcomes = outcomes;
        cpt = null;
    }

    public void setCpt(ArrayList<Double> arr) {
        this.cpt = new CPT(arr);
    }

    public CPT getCpt() {
        return cpt;
    }

    public void setGiven(String given) {
        Given = given;
    }

    public void setChildren(ArrayList<NetNode> children) {
        Children = children;
    }

    public void setParents(ArrayList<NetNode> parents) {
        Parents = parents;
    }

    public String getName() {
        return Name;
    }
}

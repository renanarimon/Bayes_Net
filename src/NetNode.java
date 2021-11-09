import java.util.ArrayList;

public class NetNode {

    private String Name;
    private String Given;
    private Observed observed;
    public ArrayList<NetNode> Children;
    public ArrayList<NetNode> Parents;
    private CPT cpt;

    enum Observed{
        WHITE,
        GRAY,
        BLACK
    }

    public NetNode(String name) {
        Name = name;
        Given = null;
        observed = Observed.WHITE;
        Children = new ArrayList<NetNode>();
        Parents = new ArrayList<NetNode>();
        cpt = new CPT();
    }


    public void setObserved(Observed observed) {
        this.observed = observed;
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

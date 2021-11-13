import java.util.ArrayList;

public class NetNode {

    private final String Name;
    private boolean Given;
    private ArrayList<String> outcomes;
    public boolean fromParent;
    public boolean fromChild;
    public ArrayList<String> Children;
    public ArrayList<String> Parents;
    private CPT cpt;



    public NetNode(String name, ArrayList<String> outcomes) {
        Name = name;
        Given = false;
        fromChild = false;
        fromParent = false;
        Children = new ArrayList<String>();
        Parents = new ArrayList<String>();
        this.outcomes = outcomes;
        cpt = null;
    }

    public void setCpt(ArrayList<Double> arr) {
        this.cpt = new CPT(arr);
    }

    public CPT getCpt() {
        return cpt;
    }

    public boolean getGiven() {
        return Given;
    }


    public void setGiven(boolean given) {
        Given = given;
    }

    public void setChildren(ArrayList<String> children) {
        Children = children;
    }

    public void setParents(ArrayList<String> parents) {
        Parents = parents;
    }

    public String getName() {
        return Name;
    }

    @Override
    public String toString() {
        return "NetNode{" +
                "Name='" + Name + '\'' +
                ", Given='" + Given + '\'' +
                ", outcomes=" + outcomes +
                ", fromParent=" + fromParent +
                ", fromChild=" + fromChild +
                ", Children=" + Children +
                ", Parents=" + Parents +
                ", cpt=" + cpt +
                '}';
    }
}

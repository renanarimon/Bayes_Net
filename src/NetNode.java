import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class NetNode {

    private final String Name;
    private String Given;
    private ArrayList<String> outcomes;
    public boolean fromParent;
    public boolean fromChild;
    public ArrayList<String> Children;
    public ArrayList<String> Parents;
    private CPT cpt;



    public NetNode(String name, ArrayList<String> outcomes) {
        Name = name;
        Given = null;
        fromChild = false;
        fromParent = false;
        Children = new ArrayList<String>();
        Parents = new ArrayList<String>();
        this.outcomes = outcomes;
        cpt = new CPT(name);
    }


    public CPT getCpt() {
        return cpt;
    }

    public String getGiven() {
        return Given;
    }

    public void setGiven(String given) {
        Given = given;
    }

    public ArrayList<String> getOutcomes() {
        return outcomes;
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
                ", cpt=" + cpt.toString() +
                "}";
    }
}

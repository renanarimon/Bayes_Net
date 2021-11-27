/*
 * @project AI_algo_ex
 * @author Renana Rimon
 */
import java.util.*;

public class NetNode {

    private String Name;
    private String Given;
    public boolean fromParent;
    public boolean fromChild;
    public ArrayList<String> Children;
    public ArrayList<String> Parents;
    private ArrayList<String> outcomes;
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

    public NetNode(NetNode other){
        this.Name = other.getName();
        this.Given = other.Given;
        this.fromChild = other.fromChild;
        this.fromParent = other.fromParent;
        this.Children = new ArrayList<>();
        this.Children.addAll(other.Children);
        this.Parents = new ArrayList<>();
        this.Parents.addAll(other.Parents);
        this.outcomes = other.outcomes;
        this.cpt = new CPT(other.cpt);
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

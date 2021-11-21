import java.util.*;

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

//    public void removeValues(Net net) {
//        Set<String> keySet = cpt.getTable().keySet();
//        String[] keyArray = keySet.toArray(new String[keySet.size()]);
//        if (Given != null) {
//            for(String s: keyArray){ //go over every key
//                String[] split = s.split("-");
//                if (!Objects.equals(split[split.length - 1], Given)){ //remove all keys that not given
//                    cpt.getTable().remove(s);
//                }
//            }
//
//        }
//        else {
//            for(String s: keyArray){ //go over every key
//                String[] split = s.split("-");
//                if (Objects.equals(split[split.length - 1], outcomes.get(outcomes.size()-1))){ //remove all all complementary values
//                    cpt.getTable().remove(s);
//                }
//            }
//        }
//        int i=0;
//        if (Parents.size()>0){
//            for (String p: Parents){
//                if (net.getBayesNet().containsKey(p)){
//                    NetNode parent = net.getBayesNet().get(p);
//                    if (parent.Given != null){
//                        for(String s: keyArray){ //go over every key
//                            String[] split = s.split("-");
//                            if (!Objects.equals(split[i], parent.Given)){
//                                cpt.getTable().remove(s);
//                            }
//                        }
//                    }
//                    i++;
//                }
//                }
//        }
//
//    }


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

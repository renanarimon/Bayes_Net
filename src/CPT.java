import java.util.*;

public class CPT implements Comparable<CPT> {
    private LinkedHashMap<String, Double> table;
    private String name;
    ArrayList<String> Given;

    public CPT(String name) {
        this.table = new LinkedHashMap<>();
        this.name = name;
        Given = new ArrayList<>();
    }
    public CPT(CPT other){
        this.name = other.name;
        this.Given = new ArrayList<>();
        this.Given.addAll(other.Given);
        this.table = new LinkedHashMap<>();
        for (String s: other.table.keySet()){
            this.table.put(s, other.table.get(s));
        }
    }


    public void add(String s, Double d){
        table.put(s,d);
    }

    public LinkedHashMap<String, Double> getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public String toString(){
        if (table.isEmpty()){
            return "[]";
        }
        return table.toString();
    }

    @Override
    public int compareTo(CPT o) {
        if (this.table.size() < o.table.size()){
            return 1;
        }
        else if (this.table.size() > o.table.size()){
            return -1;
        }
        else{
            int sumCurr = 0;
            for(String s: this.Given){
                for(int i=0; i<s.length(); i++){
                    int asciiValue = s.charAt(i);
                    sumCurr += asciiValue;
                }
            }
            int sumO = 0;
            for(String s: o.Given){
                for(int i=0; i<s.length(); i++){
                    int asciiValue = s.charAt(i);
                    sumCurr += asciiValue;
                }
            }
            if (sumCurr > sumO){
                return 1;
            }
            else {
                return -1;
            }
        }
    }
}

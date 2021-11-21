import java.util.*;

public class CPT {
    private LinkedHashMap<String, Double> table;
    private String name;
    ArrayList<String> Given;

    public CPT(String name) {
        this.table = new LinkedHashMap<>();
        this.name = name;
        Given = new ArrayList<>();
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

}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CPT {
    private LinkedHashMap<String, Double> table;

    public CPT() {
        this.table = new LinkedHashMap<String, Double>();
    }

    public void add(String s, Double d){
        table.put(s,d);
    }



    public String toString(){
        if (table.isEmpty()){
            return "[]";
        }
        return table.toString();
    }

}

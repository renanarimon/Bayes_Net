import java.util.ArrayList;
import java.util.Arrays;

public class CPT {
    private ArrayList<Double> table;

    public CPT(ArrayList<Double> arr) {
        this.table = arr;
    }

    public ArrayList<Double> getTable() {
        return table;
    }
    public String toString(){
//        String st = "";
        String st = table.toString();
//        for (int i = 0; i < table.size(); i++) {
//            st += ta
//        }
        return st;
    }

}

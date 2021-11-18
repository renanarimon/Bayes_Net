import java.util.Arrays;

public class EliminateAlgo implements EliminateAlgorithem{
private Net net;

    public EliminateAlgo(Net net) {
        this.net = net;
    }

    public void goOverE(){
        for (int i = 0; i < net.getQueryEliminate().size(); i++) {
            String q = net.getQueryEliminate().get(i); //
            String[] splitE;
            splitE = q.split(" ");
            String[] hidden = splitE[1].split("-"); //hidden vars
            String p = splitE[0].substring(splitE[0].indexOf("(") + 1, splitE[0].indexOf(")"));
            System.out.println(p);
            System.out.println(Arrays.toString(hidden));
        }
    }

//    private boolean isAncestor(){
//
//    }

    @Override
    public CPT join(CPT cpt1, CPT cpt2) {
        return null;
    }

    @Override
    public CPT eliminate(CPT cpt1, String var) {
        return null;
    }

    @Override
    public CPT normalize(CPT cpt1) {
        return null;
    }
}

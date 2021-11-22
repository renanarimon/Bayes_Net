import java.util.*;

public class EliminateAlgo implements EliminateAlgorithem {
    private Net tmpNet;
    private HashMap<String, CPT> factors;
    private ArrayList<String> hidden;
    private String query;
    private ArrayList<String> evidence;

    public EliminateAlgo(Net net) {
        this.tmpNet = new Net(net);
        hidden = new ArrayList<>();
        query = "";
        evidence = new ArrayList<>();
        this.factors = new HashMap<>();
        setFactors();
    }

    public HashMap<String, CPT> getFactors() {
        return factors;
    }

    public Net getTmpNet() {
        return tmpNet;
    }

    public ArrayList<String> getHidden() {
        return hidden;
    }

    public String getQuery() {
        return query;
    }

    public ArrayList<String> getEvidence() {
        return evidence;
    }

    private void setFactors(){
        for (String s: this.tmpNet.getBayesNet().keySet()){
            String str = s;
            for (String s1: this.tmpNet.getBayesNet().get(s).Parents){
                str += "-" + s1;
            }
            factors.put(str, this.tmpNet.getBayesNet().get(s).getCpt());
        }
    }

    /**
     * main function
     */
    private void answerQ() {
        notRelevant(); // remove not relevant factors
        for (NetNode n : tmpNet.getBayesNet().values()) {  //remove evidence
            removeValues(n, tmpNet);
        }
//        join();
//        eliminate();

    }

    /**
     * Initializes the data of the new question
     */
    public void goOverE() {
        for (int i = 0; i < tmpNet.getQueryEliminate().size(); i++) {
            cleanNet(); //clean net before each Question
            String q = tmpNet.getQueryEliminate().get(i);
            String[] splitE;
            splitE = q.split(" ");
            String[] tmphidden = splitE[1].split("-"); //hidden vars
            Collections.addAll(hidden, tmphidden);
            String p = splitE[0].substring(splitE[0].indexOf("(") + 1, splitE[0].indexOf(")"));
            String[] splitP = p.split("\\|");
            String[] splitQ = splitP[0].split("=");
            query = splitQ[0];
            tmpNet.getBayesNet().get(query).setGiven(splitQ[1]);

            if (splitP.length > 1) {
                String[] e = splitP[1].split(",");
                for (String s : e) {
                    String[] tmp = s.split("=");
                    if (tmp[0] != null) {
                        evidence.add(tmp[0]);
                        tmpNet.getBayesNet().get(tmp[0]).setGiven(tmp[1]);
                    }
                }
            }
            answerQ();
        }
    }

    /**
     * clean net & factors before each Query
     */
    private void cleanNet() {
        for (String s : tmpNet.getBayesNet().keySet()) {
            tmpNet.getBayesNet().put(s, tmpNet.getBayesNet().get(s));
        }
        hidden = new ArrayList<>();
        query = "";
        evidence = new ArrayList<>();
        for (NetNode n : tmpNet.getBayesNet().values()) {
            n.setGiven(null);
        }
        setFactors();
    }



    /**
     * Not Relevant var:
         * hidden var is NOT an ancestor of q or e,
         * hidden var is NOT dependent on q, given e
     * delete all the factors in which it appears in.
     */
    private void notRelevant() {
        BayesBall bayesBall = new BayesBall(tmpNet);
        for (String s : hidden) {
            for (String e : evidence) {
                if (!isAncestor(tmpNet.getBayesNet().get(s), tmpNet.getBayesNet().get(e)) &&
                        !isAncestor(tmpNet.getBayesNet().get(s), tmpNet.getBayesNet().get(query))) {
                    tmpNet.getBayesNet().remove(s);
                } else if (Objects.equals(bayesBall.dfs(tmpNet.getBayesNet().get(query), tmpNet.getBayesNet().get(s)), "yes")) {
                    tmpNet.getBayesNet().remove(s);
                }
            }

        }

    }



    /**
     * @param p = NetNode parent
     * @param c = NetNode child
     * @return true if p is ancestor of c
     */
    public boolean isAncestor(NetNode p, NetNode c) {
        Stack<NetNode> stack = new Stack<>();
        stack.push(p);

        while (!stack.isEmpty()) {
            if (stack.contains(c)) {
                return true;
            }
            NetNode tmp = stack.pop();
            if (tmp.Children.size() > 0) {
                for (String ch : tmp.Children) {
                    stack.push(tmpNet.getBayesNet().get(ch));
                }
            }

        }

        return false;
    }


    /**
     * remove from CPT hat contains evidencs:
     * remove every NOT given lines
     * @param n = NetNode
     * @param tmpNet
     */
    public void removeValues(NetNode n, Net tmpNet) {
        Set<String> keySet = n.getCpt().getTable().keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        for (String e:this.evidence){
            if (Objects.equals(n.getName(), e)){ //the evidence factor
                for (String s : keyArray) { //go over every key
                    String[] split = s.split("-");
                    if (!Objects.equals(split[split.length - 1], n.getGiven())) { //remove all keys that not given
                        n.getCpt().getTable().remove(s);
                    }
                }
            }else if (n.Parents.contains(e)){ //the factor's parent is evidence
                int index = evidence.indexOf(e);
                for (String s : keyArray) { //go over every key
                    String[] split = s.split("-");
                    if (!Objects.equals(split[index], n.getGiven())) { //remove all keys that not given
                        n.getCpt().getTable().remove(s);
                    }
                }
            }
        }


    }

    @Override
    public CPT join(CPT cpt1, CPT cpt2) {
        String[] given1 = cpt1.getName().split("-");
        String[] given2 = cpt2.getName().split("-");
//        int i
//        for ()
//
//
//        for (String s1: cpt1.getTable().keySet()){
//            for (String s2: cpt2.getTable().keySet()){
//
//            }
//        }
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
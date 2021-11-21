import java.util.*;

public class EliminateAlgo implements EliminateAlgorithem {
    private Net net;
    private HashMap<String, CPT> factors;
    private ArrayList<String> hidden;
    private String query;
    private ArrayList<String> evidence;

    public EliminateAlgo(Net net) {
        this.net = net;
        factors = new HashMap<>();
        for(String s: net.getBayesNet().keySet()){
            factors.put(s, net.getBayesNet().get(s).getCpt());
        }
        hidden = new ArrayList<>();
        query = "";
        evidence = new ArrayList<>();

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

    /**
     * Initializes the data of the new question
     */
    public void goOverE() {
        for (int i = 0; i < net.getQueryEliminate().size(); i++) {
            cleanNet();
            String q = net.getQueryEliminate().get(i); //
            String[] splitE;
            splitE = q.split(" ");
            String[] tmphidden = splitE[1].split("-"); //hidden vars
            Collections.addAll(hidden, tmphidden);
            String p = splitE[0].substring(splitE[0].indexOf("(") + 1, splitE[0].indexOf(")"));
            String[] splitP = p.split("\\|");
            String[] splitQ = splitP[0].split("=");
            query = splitQ[0];
            net.getBayesNet().get(query).setGiven(splitQ[1]);

            if (splitP.length > 1) {
                String[] e = splitP[1].split(",");
                for (String s : e) {
                    String[] tmp = s.split("=");
                    if (tmp[0] != null) {
                        evidence.add(tmp[0]);
                        net.getBayesNet().get(tmp[0]).setGiven(tmp[1]);
                    }
                }
            }

            answerQ();

        }
    }

    /**
     * clean net & factors before each Query
     */
    private void cleanNet(){
        for(String s: net.getBayesNet().keySet()){
            factors.put(s, net.getBayesNet().get(s).getCpt());
        }
        hidden = new ArrayList<>();
        query = "";
        evidence = new ArrayList<>();
        for (NetNode n : net.getBayesNet().values()) {
            n.setGiven(null);
        }
    }

    /**
     * main function
     */
    private void answerQ() {
        notRelevant(); // remove not relevant factors
//        join();
//        eliminate();

    }

    /**
     * Not Relevant var:
     *   if hidden var is NOT an ancestor of q or e,
     *   or if hidden var is NOT dependent on q, given e
     *      delete all the factors in which it appears in.
     *
     */
    private void notRelevant() {
        BayesBall bayesBall = new BayesBall(net);
        for (String s : hidden) {
            for (String e : evidence) {
                if (!isAncestor(net.getBayesNet().get(s), net.getBayesNet().get(e)) &&
                        !isAncestor(net.getBayesNet().get(s), net.getBayesNet().get(query))){
                    factors.remove(s);
                }
                else if (Objects.equals(bayesBall.dfs(net.getBayesNet().get(query), net.getBayesNet().get(s)), "yes")){
                    factors.remove(s);
                }
            }

        }

    }

    /**
     * remove unnecessary CPT from factors
     * @param name
     */
    public void removeCPT(String name){
        for (String s: factors.keySet()){
            if (Objects.equals(s, name) || factors.get(s).Given.contains(name)){
                factors.remove(factors.get(s).getName());
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
            if(tmp.Children.size()>0){
                for (String ch : tmp.Children) {
                    stack.push(net.getBayesNet().get(ch));
                }
            }

        }

        return false;
    }

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

import java.util.*;

public class EliminateAlgo implements EliminateAlgorithem {
    private Net tmpNet;
    private ArrayList<String> hidden;
    private String query;
    private ArrayList<String> evidence;

    public EliminateAlgo(Net net) {
        this.tmpNet = net;
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
        for (int i = 0; i < tmpNet.getQueryEliminate().size(); i++) {
            cleanNet();
            String q = tmpNet.getQueryEliminate().get(i); //
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
    }

    /**
     * main function
     */
    private void answerQ() {
        notRelevant(); // remove not relevant factors
        for (NetNode n : tmpNet.getBayesNet().values()) {
            removeValues(n, tmpNet);
        }
//        join();
//        eliminate();

    }

    /**
     * Not Relevant var:
     * if hidden var is NOT an ancestor of q or e,
     * or if hidden var is NOT dependent on q, given e
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
     * remove unnecessary CPT from factors
     *
     * @param name
     */
    public void removeCPT(String name) {
        for (String s : tmpNet.getBayesNet().keySet()) {
            if (Objects.equals(s, name) || tmpNet.getBayesNet().get(s).Parents.contains(name)) {
                tmpNet.getBayesNet().remove(tmpNet.getBayesNet().get(s).getName());
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

    public void removeValues(NetNode n, Net tmpNet) {
        Set<String> keySet = n.getCpt().getTable().keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        if (n.getGiven() != null && !Objects.equals(n.getName(), query)) {
            for(String s: keyArray){ //go over every key
                String[] split = s.split("-");
                if (!Objects.equals(split[split.length - 1], n.getGiven())){ //remove all keys that not given
                    n.getCpt().getTable().remove(s);
                }
            }

        }
        else {
            for(String s: keyArray){ //go over every key
                String[] split = s.split("-");
                if (Objects.equals(split[split.length - 1], n.getOutcomes().get(n.getOutcomes().size()-1))){ //remove all all complementary values
                    n.getCpt().getTable().remove(s);
                }
            }
        }
        int i=0;
        if (n.Parents.size()>0){
            for (String p: n.Parents){
                if (tmpNet.getBayesNet().containsKey(p)){
                    NetNode parent = tmpNet.getBayesNet().get(p);
                    if (parent.getGiven() != null){
                        for(String s: keyArray){ //go over every key
                            String[] split = s.split("-");
                            if (!Objects.equals(split[i], parent.getGiven())){
                                n.getCpt().getTable().remove(s);
                            }
                        }
                    }
                    i++;
                }
            }
        }

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
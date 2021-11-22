import java.math.BigDecimal;
import java.util.*;

public class EliminateAlgo {
    private Net tmpNet;
    private LinkedHashMap<String, CPT> factors;
    private ArrayList<String> hidden;
    private String query;
    private ArrayList<String> evidence;

    public EliminateAlgo(Net net) {
        this.tmpNet = new Net(net);
        hidden = new ArrayList<>();
        query = "";
        evidence = new ArrayList<>();
        this.factors = new LinkedHashMap<>();
        setFactors();
        sortFactors();
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

    private void setFactors() {
        for (String s : this.tmpNet.getBayesNet().keySet()) {
            String str = s;
            for (String s1 : this.tmpNet.getBayesNet().get(s).Parents) {
                str += "-" + s1;
            }

            factors.put(str, this.tmpNet.getBayesNet().get(s).getCpt());

        }
    }

    private void sortFactors() {
        List<Map.Entry<String, CPT>> list = new LinkedList<Map.Entry<String, CPT>>(this.factors.entrySet());
        Collections.sort(list, Map.Entry.comparingByValue());
        this.factors.clear();
        for (Map.Entry<String, CPT> l : list) {
            factors.put(l.getKey(), l.getValue());
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
        for (String s: factors.keySet()){ //set cpt's name
            factors.get(s).setName(s);
        }
        for (String h: hidden){
            sendToJoin(h);
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
     *
     * @param n      = NetNode
     * @param tmpNet
     */
    public void removeValues(NetNode n, Net tmpNet) {
        Set<String> keySet = n.getCpt().getTable().keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        for (String e : this.evidence) {
            if (Objects.equals(n.getName(), e)) { //the evidence factor
                for (String s : keyArray) { //go over every key
                    String[] split = s.split("-");
                    if (!Objects.equals(split[split.length - 1], n.getGiven())) { //remove all keys that not given
                        n.getCpt().getTable().remove(s);
                    }
                }
            } else if (n.Parents.contains(e)) { //the factor's parent is evidence
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

    private void sendToJoin(String currHidden) {
        CPT cpt1 = null;
        CPT cpt2 = null;
        for (String s : this.factors.keySet()) {
            if (s.contains(currHidden)) {
                cpt1 = factors.get(s);
                break;
            }
        }
        for (String s : this.factors.keySet()) {
            if (s.contains(currHidden)) {
                assert cpt1 != null;
                if (!Objects.equals(cpt1, factors.get(s))) {
                    cpt2 = factors.get(s);
                    break;
                }
            }
        }
        if (cpt1 != null && cpt2 != null) {
            join(cpt1, cpt2);
            factors.remove(cpt1.getName());
            factors.remove(cpt2.getName());
        }

    }

    public void join(CPT cpt1, CPT cpt2) {
        List<String> giv1 = Arrays.asList(cpt1.getName().split("-"));
        List<String> giv2 = Arrays.asList(cpt2.getName().split("-"));
        ArrayList<Integer> index1 = new ArrayList<>();
        ArrayList<Integer> index2 = new ArrayList<>();
        for (String s : giv1) {
            if (giv2.contains(s)) {
                index1.add(giv1.indexOf(s));
                index2.add(giv2.indexOf(s));
            }
        }
        String tmpName = cpt1.getName(); //new name !!!!
        for (String s : giv2) {
            if (!tmpName.contains(s)) {
                tmpName += "-" + s;
            }
        }
        CPT tmpCpt = new CPT(tmpName); //create new CPT with joined name

        Set<String> key1 = cpt1.getTable().keySet();
        String[] keyArr1 = key1.toArray(new String[key1.size()]);

        Set<String> key2 = cpt2.getTable().keySet();
        String[] keyArr2 = key2.toArray(new String[key2.size()]);

        for (String s1 : keyArr1) {
            String[] split1 = s1.split("-"); //curr key of cpt1 e.g. [T,F]
            for (String s2 : keyArr2) {
                String[] split2 = s2.split("-"); //curr key of cpt2
                String currKey1 = ""; //sub-string of key in given index
                String currKey2 = "";
                for (int i = 0; i < index1.size(); i++) {
                    currKey1 += split1[i];
                }
                for (int i = 0; i < index2.size(); i++) {
                    currKey2 += split2[i];
                }
                if (currKey1.equals(currKey2)) { //if equals --> join
                    Double mul = cpt1.getTable().get(s1) * cpt2.getTable().get(s2); // mul 2 rows
                    String k = ""; //key of new row
                    String[] nameArr = tmpName.split("-");

                    for (int i = 0; i < nameArr.length; i++) {
                        if (giv1.contains(nameArr[i])) { //if var is in cpt1
                            k += split1[giv1.indexOf(nameArr[i])];
                        } else if (giv2.contains(nameArr[i])) { //if var is in cpt2
                            k += split2[giv2.indexOf(nameArr[i])];
                        }
                    }
                    tmpCpt.add(k, mul);
                }

            }
        }
        factors.put(tmpName, tmpCpt);
        this.sortFactors();
    }

    public CPT eliminate(CPT cpt1, String var) {
        return null;
    }


    public CPT normalize(CPT cpt1) {
        return null;
    }
}
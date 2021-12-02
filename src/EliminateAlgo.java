/*
 * @project AI_algo_ex
 * @author Renana Rimon
 *
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class EliminateAlgo {
    private Net tmpNet; //copy of Net
    private LinkedHashMap<String, CPT> factors;
    private ArrayList<String> hidden;
    private String query;
    private ArrayList<String> evidence;
    private int mult; //Summarize the multiplication operation
    private int sum; //Summarize the addition operation
    private ArrayList<String> ansEliminate;

    public EliminateAlgo(Net net) {
        this.tmpNet = new Net(net);
        this.factors = new LinkedHashMap<>();
        hidden = new ArrayList<>();
        query = "";
        evidence = new ArrayList<>();
        this.mult = 0;
        this.sum = 0;
        setFactors();
        sortFactors();
        ansEliminate = new ArrayList<>();
    }


    public void setMult(int mult) {
        this.mult = mult;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public ArrayList<String> getAnsEliminate() {
        return ansEliminate;
    }

    /**
     * Initializes the data of the new question
     */
    public void goOverE(Net net) {
        for (int i = 0; i < tmpNet.getQueryEliminate().size(); i++) {
            cleanNet(net); //clean net before each Question
            String q = tmpNet.getQueryEliminate().get(i);
            String[] splitE;
            splitE = q.split(" ");
            if (splitE.length > 1) {
                String[] tmphidden = splitE[1].split("-"); //hidden vars
                Collections.addAll(hidden, tmphidden);
            }
            String p = splitE[0].substring(splitE[0].indexOf("(") + 1, splitE[0].indexOf(")"));
            String[] splitP = p.split("\\|");
            String[] splitQ = splitP[0].split("=");
            query = splitQ[0];
            if (query == "") {
                this.getAnsEliminate().add("");
            } else {
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
    }


    /**
     * main function:
     * set Factors
     * remove not Relevant factors
     * set CPTs name
     * remove CPTs with one value
     * removeValues - remove unnecessary rows
     * for each hidden:
     * join
     * eliminate
     * remove CPTs with one value
     * join last CPTs to one
     * normalize
     * find ans
     */
    private void answerQ() {
        setFactors();
        notRelevant(); // remove not relevant factors
        Set<String> keySet = factors.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);

        //set CPTs name & remove if one valued
        for (String s : keyArray) {
            factors.get(s).setName(s);
            removeIfOneValued(factors.get(s));
        }

        for (NetNode n : tmpNet.getBayesNet().values()) {  //remove evidence
            removeValues(n, tmpNet);
        }

        CPT tmpCpt;
        for (String h : hidden) {
            tmpCpt = sendToJoin(h);
            if (tmpCpt == null) {
                for (String hid : factors.keySet()) {
                    if (hid.contains(h)) {
                        tmpCpt = factors.get(hid);
                    }
                }
            }
            assert tmpCpt != null;
            eliminate(tmpCpt, h);
            removeIfOneValued(tmpCpt);
        }

        CPT cptFinal = sendToJoin(query);
        if (cptFinal == null) {
            String k = "";
            for (String s : factors.keySet()) {
                k = s;
            }
            cptFinal = factors.get(k);
        }
        if (mult != 0) {
            normalize(cptFinal);
        }

        String wanted = "";
        String[] split = cptFinal.getName().split("-");
        for (String s : split) {
            String tmp = this.tmpNet.getBayesNet().get(s).getGiven();
            wanted += tmp + "-";
        }
        wanted = wanted.substring(0, wanted.length() - 1);

        Double ans = cptFinal.getTable().get(wanted);

        BigDecimal ansFinal = new BigDecimal(ans).setScale(5, RoundingMode.HALF_UP);

        this.ansEliminate.add(ansFinal + "," + sum + "," + mult);
    }

    /**
     * clean net & factors before each Query
     */
    private void cleanNet(Net net) {
        this.tmpNet = new Net(net);
        hidden = new ArrayList<>();
        query = "";
        evidence = new ArrayList<>();
        this.sum = 0;
        this.mult = 0;
        for (NetNode n : tmpNet.getBayesNet().values()) {
            n.setGiven(null);
        }
    }

    /**
     * put in HashMap 'factors': (name, cpt)
     * NOTE: name contains all given
     */
    private void setFactors() {
        factors.clear();
        for (String s : this.tmpNet.getBayesNet().keySet()) {
            String str = "";
            for (String s1 : this.tmpNet.getBayesNet().get(s).Parents) {
                str += s1 + "-";
            }
            factors.put(str + s, this.tmpNet.getBayesNet().get(s).getCpt());
        }
    }

    /**
     * sort factors by CPT.compare (size & ASCII)
     */
    private void sortFactors() {
        List<Map.Entry<String, CPT>> list = new LinkedList<>(this.factors.entrySet());
        Collections.sort(list, Map.Entry.comparingByValue());
        this.factors.clear();
        for (Map.Entry<String, CPT> l : list) {
            factors.put(l.getKey(), l.getValue());
        }
    }

    /**
     * Not Relevant var:
     * hidden var is NOT an ancestor of q or e,
     * hidden var is NOT dependent on q, given e
     * delete all the factors in which it appears in.
     */


    private void notRelevant() {
        BayesBall bayesBall = new BayesBall(tmpNet);
        boolean flag = true; // true --> remove

        // if is NOT ancestor --> remove
        String[] hiddenArray = hidden.toArray(new String[hidden.size()]);
        for (String h : hiddenArray) {
            //if h *isAncestor* of query --> NOT remove
            if (!isAncestor(tmpNet.getBayesNet().get(h), tmpNet.getBayesNet().get(query))) {
                for (String e : evidence) {
                    // if h is ancestor of any e --> NOT remove
                    if (isAncestor(tmpNet.getBayesNet().get(h), tmpNet.getBayesNet().get(e))) {
                        flag = false;
                        break;
                    }
                }
            } else {
                flag = false;
            }
            // if independent on query --> remove *every* factor 'h' in it

            if (Objects.equals(bayesBall.dfs(tmpNet.getBayesNet().get(query), tmpNet.getBayesNet().get(h)), "yes")) {
                flag = true;
            }
            if (flag) {
                removeContainsNotRelevant(h);
            }
            flag = true;

        }
    }

    /**
     * help function to 'notRelevant':
     * remove every factor that contains not relevant var
     *
     * @param h
     */
    private void removeContainsNotRelevant(String h) {
        hidden.remove(h);
        Set<String> factorsKeySet = factors.keySet();
        String[] factorsArray = factorsKeySet.toArray(new String[factorsKeySet.size()]);
        for (String f : factorsArray) {
            if (f.contains(h)) {
                factors.remove(f);
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
     * remove from CPT that contains evidencs:
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
                int index = n.Parents.indexOf(e);
                for (String s : keyArray) { //go over every key
                    String[] split = s.split("-");
                    if (!Objects.equals(split[index], tmpNet.getBayesNet().get(e).getGiven())) { //remove all keys that not given
                        n.getCpt().getTable().remove(s);
                    }
                }
            }
        }
        removeIfOneValued(n.getCpt());
    }


    /**
     * go over every 2 CPTs that contains 'currHidden',
     * and send them to join().
     * after join --> remove from 'factors'
     *
     * @param currHidden = hidden var
     */
    private CPT sendToJoin(String currHidden) {
        sortFactors();
        CPT tmpCpt = null;
        while (true) {
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
                tmpCpt = join(cpt1, cpt2);
            } else {
                break;
            }
        }

        return tmpCpt;

    }

    /**
     * join 2 CPT:
     * build a new factor over the union of hidden variable
     * add new cpt to 'factors'
     *
     * @param cpt1
     * @param cpt2
     */
    public CPT join(CPT cpt1, CPT cpt2) {
        List<String> giv1 = Arrays.asList(cpt1.getName().split("-"));
        List<String> giv2 = Arrays.asList(cpt2.getName().split("-"));

        // save indexes of given
        ArrayList<Integer> index1 = new ArrayList<>();
        ArrayList<Integer> index2 = new ArrayList<>();
        for (String s : giv1) {
            if (giv2.contains(s)) {
                index1.add(giv1.indexOf(s));
                index2.add(giv2.indexOf(s));
            }
        }

        // cpt name
        String tmpName = cpt1.getName();
        for (String s : giv2) {
            if (!tmpName.contains(s)) {
                tmpName += "-" + s;
            }
        }
        // initialize CPT
        CPT tmpCpt = new CPT(tmpName); //create new CPT with joined name

        //create array of keys e.g. [T-T, T-F]
        Set<String> key1 = cpt1.getTable().keySet();
        String[] keyArr1 = key1.toArray(new String[key1.size()]);
        Set<String> key2 = cpt2.getTable().keySet();
        String[] keyArr2 = key2.toArray(new String[key2.size()]);

        // for each key -> if 2 keys have same 'given' -> join
        for (String s1 : keyArr1) {
            String[] split1 = s1.split("-"); //curr key of cpt1 e.g. [T,F]
            for (String s2 : keyArr2) {
                String[] split2 = s2.split("-"); //curr key of cpt2
                String currKey1 = ""; //sub-string of key in given index
                String currKey2 = "";
                for (int i = 0; i < index1.size(); i++) {
                    currKey1 += split1[index1.get(i)]; //
                }
                for (int i = 0; i < index2.size(); i++) {
                    currKey2 += split2[index2.get(i)]; //
                }

                //if equals --> join
                if (currKey1.equals(currKey2)) {
                    // mul 2 rows
                    Double mul = cpt1.getTable().get(s1) * cpt2.getTable().get(s2);
                    setMult(mult + 1);
                    //key of new row
                    String k = "";
                    String[] nameArr = tmpName.split("-");
                    for (int i = 0; i < nameArr.length; i++) {
                        if (giv1.contains(nameArr[i])) { //if var is in cpt1
                            k += split1[giv1.indexOf(nameArr[i])];
                            k = !k.equals("") ? (k + "-") : k;

                        } else if (giv2.contains(nameArr[i])) { //if var is in cpt2
                            k += split2[giv2.indexOf(nameArr[i])];
                            k = !k.equals("") ? (k + "-") : k;
                        }
                    }
                    k = k.length() > 0 ? k.substring(0, k.length() - 1) : k;
                    tmpCpt.add(k, mul);
                }

            }
        }
        factors.remove(cpt1.getName());
        factors.remove(cpt2.getName());
        factors.put(tmpName, tmpCpt);
        removeIfOneValued(tmpCpt);
        this.sortFactors();
        return tmpCpt;
    }

    /**
     * remove one valued cpt from 'factors'
     */
    private void removeIfOneValued(CPT cpt) {
        if (cpt.getTable().size() <= 1) {
            factors.remove(cpt.getName());
        }
    }

    /**
     * Eliminate Hidden var from CPT:
     * Sum up all the complementary lines of the hidden variable
     *
     * @param cpt
     * @param currHidden
     */
    public void eliminate(CPT cpt, String currHidden) {
        //create array of keys e.g. [T-T, T-F]
        Set<String> key = cpt.getTable().keySet();
        String[] keyArr = key.toArray(new String[key.size()]);

        String[] g = cpt.getName().split("-");
        List<String> giv = Arrays.asList(g);
        int indexH = giv.indexOf(currHidden);

        //CPTs new name - without currHidden NOTE: can be null!
        g[indexH] = "";
        String tmpName = "";
        for (String s : g) {
            if (s != "") {
                tmpName += s + "-";
            }
        }
        tmpName = tmpName.substring(0, tmpName.length() - 1);
        CPT tmpCpt = new CPT(tmpName);
        for (int i = 0; i < keyArr.length; i++) {
            String currKey1 = "";
            String[] split1 = keyArr[i].split("-");
            for (int j = 0; j < split1.length; j++) {
                if (j != indexH) {
                    currKey1 += split1[j] + "-";
                }
            }
            for (int j = i + 1; j < keyArr.length; j++) {
                String currKey2 = "";
                String[] split2 = keyArr[j].split("-");
                for (int k = 0; k < split2.length; k++) {
                    if (k != indexH) {
                        currKey2 += split2[k] + "-";
                    }
                }
                if (currKey1.equals(currKey2)) {
                    Double sum1 = cpt.getTable().get(keyArr[i]) + cpt.getTable().get(keyArr[j]);
                    setSum(sum + 1);
                    currKey1 = currKey1.length() > 0 ? currKey1.substring(0, currKey1.length() - 1) : currKey1;
                    tmpCpt.add(currKey1, sum1);
                    break;

                }

            }
        }
        factors.remove(cpt.getName());
        factors.put(tmpName, tmpCpt);
        this.sortFactors();
    }


    /**
     * normalize values in last CPT:
     * normVal = currVal/sum(allVal)
     *
     * @param cpt1 = last CPT
     */
    public void normalize(CPT cpt1) {
        Set<String> keySet = cpt1.getTable().keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);


        Double norm = cpt1.getTable().get(keyArray[0]);
        for (int i = 1; i < keyArray.length; i++) {
            norm += cpt1.getTable().get(keyArray[i]);
            setSum(sum + 1);
        }

        for (String s : cpt1.getTable().keySet()) {
            Double tmp = cpt1.getTable().get(s) / norm;
            cpt1.add(s, tmp);
        }

    }
}
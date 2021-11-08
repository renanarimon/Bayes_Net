public interface EliminateAlgo {

    /**
     * join 2 CPTs by multiple
     * @param cpt1
     * @param cpt2
     * @return new CPT
     */
    public CPT join(CPT cpt1, CPT cpt2);

    /**
     *
     * @param cpt1 after join
     * @param var hidden to eliminate
     * @return new smaller CPT without the hidden var
     */
    public CPT eliminate(CPT cpt1, String var);

    /**
     *
     * @param cpt1 after elimination
     * @return normalized CPT
     */
    public CPT normalize(CPT cpt1);

}

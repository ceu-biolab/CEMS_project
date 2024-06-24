/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cems_project;

/**
 * @author maria
 */

import chemicalFormula.PeriodicTable;
import patternFinders.PatternFinder;

import java.util.Objects;
//import patternFinders.PatternFinder;
//import utilities.Utilities;

/**
 * @author alberto.gildelafuent
 */
public class Compound {

    private Integer compound_id;
    private final String compoundName;
    private final String casId;
    private final String formula;
    private final Double monoisotopicMass;
    private final Integer charge_type;
    private final Integer charge_number;
    private final String formula_type;
    private final Integer formula_type_int;
    private final Integer compound_type;
    private final Integer compound_status;
    private final Double logP;
    private final Identifier identifiersOwn;
    private final Identifier identifiersParent;


    public Compound(Integer compound_id, String name, String casId, String formula, Double monoisotopicMass,
                    Integer compound_status, Integer compound_type, Double logP,
                    Identifier identifiersOwn, Identifier identifiersParent) {
        this.compound_id = compound_id;
        this.compoundName = name;
        this.casId = casId;
        this.formula = formula;
        this.monoisotopicMass = monoisotopicMass;
        this.compound_status = compound_status;
        this.logP = logP;
        this.identifiersOwn = identifiersOwn;
        this.identifiersParent = identifiersParent;
        int[] charges = PatternFinder.getChargeFromSmiles(identifiersOwn.getSmiles());
        this.charge_type = charges[0];
        this.charge_number = charges[1];
        this.formula_type = PatternFinder.getTypeFromFormula(formula);
        this.formula_type_int = PeriodicTable.getIntChemAlphabet(formula_type);
        this.compound_type = compound_type;
    }

    public Compound(Integer compound_id, String name, String casId, String formula, Double monoisotopicMass,
                    Integer compound_status, Integer compound_type, Double logP,
                    Identifier identifiersOwn) {
        this(compound_id, name, casId, formula, monoisotopicMass, compound_status, compound_type, logP, identifiersOwn, null);
    }

    public Compound(String name) {
        this(null, name, null, null, null, null, null, null, null);
    }

    public Compound(String name, Identifier identifiers) {
        this(null, name, null, null, null, null, null, null, identifiers);
    }

    public Compound(String name, String cas_id) {
        this(null, name, cas_id, null, null, null, null, null, null);
    }

    public Compound(Integer id, String name, String cas_id, Identifier identifiers) {
        this(id, name, cas_id, null, null, null, null, null, identifiers);
    }

    public Compound(Identifier identifiers) {
        this(null, null, null, null, null, null, null, null, identifiers);
    }

    public Compound() {
        this(null, null, null, null, null, null, null, null, null);
    }

    public Compound(Integer id, Identifier i) {
        this(id, null, null, null, null, null, null, null, i);
    }

    public Integer getCompound_id() {
        return compound_id;
    }

    public void setCompound_id(Integer compound_id) {
        this.compound_id = compound_id;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public String getCasId() {
        return casId;
    }

    public String getFormula() {
        return formula;
    }

    public Double getMonoisotopicMass() {
        return monoisotopicMass;
    }

    //    public Integer getCharge_type() {
//        return charge_type;
//    }
//
//    public Integer getCharge_number() {
//        return charge_number;
//    }
//
//    public String getFormula_type() {
//        return formula_type;
//    }
//
//    public Integer getFormula_type_int() {
//        return formula_type_int;
//    }
    public Integer getCompound_status() {
        return compound_status;
    }

    public Integer getCompound_type() {
        return compound_type;
    }

    public Double getLogP() {
        return logP;
    }

    public Identifier getIdentifiersOwn() {
        return identifiersOwn;
    }

    public Identifier getIdentifiersParent() {
        return this.identifiersParent;
    }

    public String getINCHI(){
        return this.identifiersOwn.getInchi();
    }

    public String getINCHIKey(){
        return this.identifiersOwn.getInchi_key();
    }

    public String getRefHMDB(){
        return this.identifiersOwn.getHmdb_id();
    }

    public Integer getRefPubChem(){
        return this.identifiersOwn.getPc_id();
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.compound_id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Compound other = (Compound) obj;
        if (!Objects.equals(this.compound_id, other.compound_id)) {
            return false;
        }
        if (!Objects.equals(this.identifiersOwn, other.identifiersOwn)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Compound{" +
                "compound_id=" + compound_id +
                ", name='" + compoundName + '\'' +
                ", casId='" + casId + '\'' +
                ", formula='" + formula + '\'' +
                ", mass=" + monoisotopicMass +
                ", compound_type=" + compound_type +
                ", compound_status=" + compound_status +
                ", logP=" + logP +
                ", identifiers=" + identifiersOwn +
                '}';
    }
}

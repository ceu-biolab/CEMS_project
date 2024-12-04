package GCMS;

import cems_project.Compound;
import cems_project.Identifier;

import java.util.List;

public class CompoundGC extends Compound {
    private Double RI;
    private Double RT;

    //lista de las derivatizaciones de un compuesto
    private List<DerivatizationType> derivatizationTypeList = null;

    //Some cases on excel that dont have the RT
    public CompoundGC(String name, double RI) {
        super(name);
        this.RI = RI;
        this.RT = null;
    }

    public CompoundGC(String name,
                      Double RI, Double RT) {
        super(name);
        this.RI = RI;
        this.RT = RT;
    }

    public CompoundGC(Integer compound_id, String name, String casId, String formula, Double monoisotopicMass,
                      Integer compound_status, Integer compound_type, Double logP,
                      Identifier identifiersOwn, Identifier identifiersParent,
                      Double RI, Double RT) {
        super(compound_id, name, casId, formula, monoisotopicMass,
                compound_status, compound_type, logP, identifiersOwn, identifiersParent);
        this.RI = RI;
        this.RT = RT;
    }

    public double getRT() {
        return RT;
    }

    public void setRT(double RT) {
        this.RT = RT;
    }

    public double getRI() {
        return RI;
    }

    public void setRI(double RI) {
        this.RI = RI;
    }

    public List<DerivatizationType> getDerivatizationTypeList() {
        return this.derivatizationTypeList;
    }

    public void setDerivatizationTypeList(List<DerivatizationType> derivatizationTypeList) {
        this.derivatizationTypeList = derivatizationTypeList;
    }

    @Override
    public String toString() {
        return "name=" + this.compoundName +
                ", RI=" + this.RI +
                ", RT=" + this.RT;
    }
}

package GCMS;

import cems_project.Compound;
import cems_project.Identifier;

import java.util.List;

public class CompoundGC extends Compound {
    private Double RI;
    private Double RT;

    //lista de las derivatizaciones de un compuesto
    //private List<DerivatizationType> derivatizationTypeList = null;
    //private DerivatizationType dertype = DerivatizationType.ALKYLATION;
    private final String dertype = "chloroformate";

    private final String gcColumn = "unkown";

    //WORKING
    private GCMS_Spectrum gcmsSpectrum;

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

    //For the RT and RI
    public CompoundGC(Integer compound_id, String name, String casId, String formula, Double monoisotopicMass,
                      Integer compound_status, Integer compound_type, Double logP,
                      Identifier identifiersOwn, Identifier identifiersParent,
                      Double RI, Double RT) {
        super(compound_id, name, casId, formula, monoisotopicMass,
                compound_status, compound_type, logP, identifiersOwn, identifiersParent);
        this.RI = RI;
        this.RT = RT;
    }
    //For the Spectrum
    public CompoundGC(Integer compound_id, String name, String casId, String formula, Double monoisotopicMass,
                      Integer compound_status, Integer compound_type, Double logP,
                      Identifier identifiersOwn, Identifier identifiersParent, GCMS_Spectrum gcmsSpectrum
                      ) {
        super(compound_id, name, casId, formula, monoisotopicMass,
                compound_status, compound_type, logP, identifiersOwn, identifiersParent);
        this.gcmsSpectrum = gcmsSpectrum;
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

    public String getDertype() {
        return dertype;
    }

    public String getGcColumn() {
        return this.gcColumn;
    }

    public GCMS_Spectrum getGcmsSpectrum() {
        return gcmsSpectrum;
    }

    public void setGcmsSpectrum(GCMS_Spectrum gcmsSpectrum) {
        this.gcmsSpectrum = gcmsSpectrum;
    }

    /*public DerivatizationType getDertype() {
        return dertype;
    }

    public void setDertype(DerivatizationType dertype) {
        this.dertype = dertype;
    }*/

    /*public List<DerivatizationType> getDerivatizationTypeList() {
        return this.derivatizationTypeList;
    }

    public void setDerivatizationTypeList(List<DerivatizationType> derivatizationTypeList) {
        this.derivatizationTypeList = derivatizationTypeList;
    }*/

    @Override
    public String toString() {
        return "name=" + this.compoundName +
                ", RI=" + this.RI +
                ", RT=" + this.RT +
                ", Spectrum=" + this.gcmsSpectrum;
    }
}

package GCMS;

import cems_project.Compound;
import cems_project.Identifier;

import java.util.List;

public class CompoundGC extends Compound {
    private Double RI;
    private Double RT;

    private DerivatizationType dertype;

    private GCColumn gcColumn;

    private List<GCMS_Spectrum> gcmsSpectra;

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

    public CompoundGC(String name, String casId,
                      Double RI, List<GCMS_Spectrum> gcms_spectrum) {
        super(name, casId);
        this.RI = RI;
        this.gcmsSpectra = gcms_spectrum;

        this.dertype = DerivatizationType.METHYL_CHLOROFORMATE;
        this.gcColumn = GCColumn.STANDARD_NON_POLAR;
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

        this.dertype = DerivatizationType.METHYL_CHLOROFORMATE;
        this.gcColumn = GCColumn.STANDARD_NON_POLAR;
    }

    //For the Spectrum
    public CompoundGC(Integer compound_id, String name, String casId, String formula, Double monoisotopicMass,
                      Integer compound_status, Integer compound_type, Double logP,
                      Identifier identifiersOwn, Identifier identifiersParent, List<GCMS_Spectrum> gcmsSpectrum) {
        super(compound_id, name, casId, formula, monoisotopicMass,
                compound_status, compound_type, logP, identifiersOwn, identifiersParent);
        this.gcmsSpectra = gcmsSpectrum;

        this.dertype = DerivatizationType.METHYL_CHLOROFORMATE;
        this.gcColumn = GCColumn.STANDARD_NON_POLAR;
    }

    //For the RI & spectrum
    public CompoundGC(Integer compound_id, String name, String casId, String formula, Double monoisotopicMass,
                      Integer compound_status, Integer compound_type, Double logP,
                      Identifier identifiersOwn, Identifier identifiersParent,
                      Double RI, List<GCMS_Spectrum> gcmsSpectrum) {
        super(compound_id, name, casId, formula, monoisotopicMass,
                compound_status, compound_type, logP, identifiersOwn, identifiersParent);
        this.RI = RI;
        this.RT = null;

        this.gcmsSpectra = gcmsSpectrum;

        this.dertype = DerivatizationType.METHYL_CHLOROFORMATE;
        this.gcColumn = GCColumn.STANDARD_NON_POLAR;
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

    public DerivatizationType getDertype() {
        return dertype;
    }

    public void setDertype(String dertype) {
        if(dertype.equalsIgnoreCase("METHYL_CHLOROFORMATE")){
            this.dertype = DerivatizationType.METHYL_CHLOROFORMATE;
        }if(dertype.equalsIgnoreCase("TMS")){
            this.dertype = DerivatizationType.TMS;
        }if(dertype.equalsIgnoreCase("TBDMS_DERIVATIZATION")){
            this.dertype = DerivatizationType.TBDMS_DERIVATIZATION;
        }
    }

    public GCColumn getGcColumn() {
        return this.gcColumn;
    }

    public void setGcColumn(String gcColumn) {
        if(gcColumn.equalsIgnoreCase("STANDARD_NON_POLAR")){
            this.gcColumn = GCColumn.STANDARD_NON_POLAR;
        }
        if(gcColumn.equalsIgnoreCase("SEMISTANDARD_NON_POLAR")){
            this.gcColumn = GCColumn.SEMISTANDARD_NON_POLAR;
        }
        if(gcColumn.equalsIgnoreCase("STANDARD_POLAR")){
            this.gcColumn = GCColumn.STANDARD_POLAR;
        }
    }

    public List<GCMS_Spectrum> getGcmsSpectrum() {
        return gcmsSpectra;
    }

    public void setGcmsSpectrum(List<GCMS_Spectrum> gcmsSpectrum) {
        this.gcmsSpectra = gcmsSpectrum;
    }

    @Override
    public String toString() {
        return "CompoundGC{" +
                "compound_id=" + this.getCompound_id() +
                ", Name=" + this.getCompoundName() +
                ", CasId=" + this.getCasId() +
                ", Formula=" + this.getFormula() + "\n" +
                ", Charge_type="+ this.getCharge_type() +
                ", Charge_number="+ this.getCharge_number() +
                ", formula_type="+ this.getFormula_type() +
                ", formula_type_int=" + this.getFormula_type_int() +
                ", compound_type=" + this.getCompound_type() +
                ", compound_status=" + this.getCompound_status() +
                ", logP=" + this.getLogP() +
                ", Mass=" + this.getMonoisotopicMass() + ", \n   " +
                "Identifiers=" + this.getIdentifiersOwn() + ", \n   " +
                "RI=" + RI +
                ", RT=" + RT +
                ", dertype=" + dertype +
                ", gcColumn='" + gcColumn + ", \n   " +
                '}';
    }

}
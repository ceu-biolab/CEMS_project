package GCMS;

import java.util.List;

//CLASS WITH THE COMPOUND INFORMATION OF THE TXT
public class CompoundGCInformationTxt {
    private Integer numberOriginalListTxt;
    private String CName;
    private Double RI;
    private String casId;
    //private GCMS_Spectrum gcmsSpectrum; //NORMALIZED SPECTRUM
    private List<GCMS_Spectrum> gcmsSpectra; //NORMALIZED SPECTRUM
    private String inchi;
    private String inchiKey;
    private String smiles;
    private Double monoisotopicMass;

    public CompoundGCInformationTxt(){
    }

    public CompoundGCInformationTxt(Integer numberOriginalListTxt, String CName, Double RI, String casId,
                                    List<GCMS_Spectrum> gcmsSpectra) {
        this.numberOriginalListTxt = numberOriginalListTxt;
        this.CName = CName;
        this.RI = RI;
        this.casId = casId;
        this.gcmsSpectra = gcmsSpectra;
    }

    public CompoundGCInformationTxt(Integer numberOriginalListTxt, String CName, Double RI, String casId,
                                    List<GCMS_Spectrum> gcmsSpectra, String inchi) {
        this.numberOriginalListTxt = numberOriginalListTxt;
        this.CName = CName;
        this.RI = RI;
        this.casId = casId;
        this.gcmsSpectra = gcmsSpectra;
        this.inchi = inchi;
    }

    public CompoundGCInformationTxt(Integer numberOriginalListTxt, String CName, Double RI, String casId,
                                    List<GCMS_Spectrum> gcmsSpectra, String inchi, String smiles) {
        this.numberOriginalListTxt = numberOriginalListTxt;
        this.CName = CName;
        this.RI = RI;
        this.casId = casId;
        this.gcmsSpectra = gcmsSpectra;
        this.inchi = inchi;
        this.smiles = smiles;
    }

    public CompoundGCInformationTxt(Integer numberOriginalListTxt, String CName, Double RI, String casId,
                                    List<GCMS_Spectrum> gcmsSpectra, String inchi, String inchiKey, String smiles,
                                    Double monoisotopicMass) {
        this.numberOriginalListTxt = numberOriginalListTxt;
        this.CName = CName;
        this.RI = RI;
        this.casId = casId;
        this.gcmsSpectra = gcmsSpectra;
        this.inchi = inchi;
        this.inchiKey = inchiKey;
        this.smiles = smiles;
        this.monoisotopicMass = monoisotopicMass;
    }

    public Integer getNumberOriginalListTxt() {
        return numberOriginalListTxt;
    }

    public void setNumberOriginalListTxt(Integer numberOriginalListTxt) {
        this.numberOriginalListTxt = numberOriginalListTxt;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }


    public Double getRI() {
        return RI;
    }

    public void setRI(Double RI) {
        this.RI = RI;
    }

    public String getCasId() {
        return casId;
    }

    public void setCasId(String casId) {
        this.casId = casId;
    }

    public List<GCMS_Spectrum> getGcmsSpectra() {
        return gcmsSpectra;
    }

    public void setGcmsSpectra(List<GCMS_Spectrum> gcmsSpectra) {
        this.gcmsSpectra = gcmsSpectra;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey;
    }

    public Double getMonoisotopicMass() {
        return monoisotopicMass;
    }

    public void setMonoisotopicMass(Double monoisotopicMass) {
        this.monoisotopicMass = monoisotopicMass;
    }

    @Override
    public String toString() {
        return "CompoundGCInformationTxt{" +
                "numberOriginalListTxt=" + numberOriginalListTxt +
                ", CName='" + CName + '\'' +
                ", RI=" + RI +
                ", casId='" + casId + '\'' +
                ", monoisotopicMass='" + monoisotopicMass + '\'' +
                ", \ninchi='" + inchi + '\'' +
                ", \ninchiKey='" + inchiKey + '\'' +
                ", \nsmiles='" + smiles + '\'' +
                ", \ngcmsSpectra=" + gcmsSpectra +
                '}';
    }
}

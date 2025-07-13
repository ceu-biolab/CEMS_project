package GCMS;

//CLASS WITH THE COMPOUND INFORMATION OF THE TXT
public class CompoundGCInformationTxt {
    private Integer numberOriginalListTxt;
    private String CName;
    private Double RI;
    private String casId;
    private GCMS_Spectrum gcmsSpectrum; //NORMALIZED SPECTRUM
    private String inchi;
    private String smiles;

    public CompoundGCInformationTxt(){
    }

    public CompoundGCInformationTxt(Integer numberOriginalListTxt, String CName, Double RI, String casId, GCMS_Spectrum gcmsSpectrum) {
        this.numberOriginalListTxt = numberOriginalListTxt;
        this.CName = CName;
        this.RI = RI;
        this.casId = casId;
        this.gcmsSpectrum = gcmsSpectrum;
    }

    public CompoundGCInformationTxt(Integer numberOriginalListTxt, String CName, Double RI, String casId,
                                    GCMS_Spectrum gcmsSpectrum, String inchi) {
        this.numberOriginalListTxt = numberOriginalListTxt;
        this.CName = CName;
        this.RI = RI;
        this.casId = casId;
        this.gcmsSpectrum = gcmsSpectrum;
        this.inchi = inchi;
    }

    public CompoundGCInformationTxt(Integer numberOriginalListTxt, String CName, Double RI, String casId,
                                    GCMS_Spectrum gcmsSpectrum, String inchi, String smiles) {
        this.numberOriginalListTxt = numberOriginalListTxt;
        this.CName = CName;
        this.RI = RI;
        this.casId = casId;
        this.gcmsSpectrum = gcmsSpectrum;
        this.inchi = inchi;
        this.smiles = smiles;
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

    public GCMS_Spectrum getGcmsSpectrum() {
        return gcmsSpectrum;
    }

    public void setGcmsSpectrum(GCMS_Spectrum gcmsSpectrum) {
        this.gcmsSpectrum = gcmsSpectrum;
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

    @Override
    public String toString() {
        return "CompoundGCInformationTxt{" +
                "numberOriginalListTxt=" + numberOriginalListTxt +
                ", CName='" + CName + '\'' +
                ", RI=" + RI +
                ", casId='" + casId + '\'' +
                ", \ninchi='" + inchi + '\'' +
                ", \nsmiles='" + smiles + '\'' +
                ", \ngcmsSpectrum=" + gcmsSpectrum +
                '}';
    }
}

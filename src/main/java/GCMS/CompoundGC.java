package GCMS;

public class CompoundGC {
    private String name;
    private double RI;
    private double RT;

    public CompoundGC() {
    }

    //Some cases on excel that dont have the RT
    public CompoundGC(String name, double RI) {
        this.name = name;
        this.RI = RI;
    }

    //First 23 elements have the RI and RT
    public CompoundGC(String name, double RI, double RT) {
        this.name = name;
        this.RI = RI;
        this.RT = RT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "name=" + this.name +
                ", RI=" + this.RI +
                ", RT=" + this.RT;
    }
}

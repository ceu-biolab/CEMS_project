package GCMS;

import java.util.List;

public class GCMS_Peaks {
    private double mz;
    private double intensity;

    public GCMS_Peaks() {
        this.mz = -1;
        this.intensity = -1;
    }

    public GCMS_Peaks(double mz, double intensity) {
        this.mz = mz;
        this.intensity = intensity;
    }

    public double getMz() {
        return mz;
    }

    public void setMz(double mz) {
        this.mz = mz;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return "\n\t\t\tGCMS_Peaks{" +
                "mz=" + mz +
                ", intensity=" + intensity +
                '}';
    }
}

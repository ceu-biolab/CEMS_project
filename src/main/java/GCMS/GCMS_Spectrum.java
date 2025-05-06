package GCMS;

import java.util.List;

public class GCMS_Spectrum {
    //list of Peaks of the Spectrum - mz & intensity
    private List<GCMS_Peaks> gcms_peaksList = null;

    public GCMS_Spectrum(List<GCMS_Peaks> gcms_peaksList) {
        this.gcms_peaksList = gcms_peaksList;
    }

    public List<GCMS_Peaks> getGcms_peaksList() {
        return gcms_peaksList;
    }

    public void setGcms_peaksList(List<GCMS_Peaks> gcms_peaksList) {
        this.gcms_peaksList = gcms_peaksList;
    }

    @Override
    public String toString() {
        return "GCMS_Spectrum{" +
                "gcms_peaksList=" + gcms_peaksList +
                '}';
    }
}

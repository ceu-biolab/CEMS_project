package GCMS;

import java.util.ArrayList;
import java.util.List;

public class GCMS_Spectrum {
    private List<GCMS_Peaks> gcms_peaksList;// = null;

    public GCMS_Spectrum() {
        this.gcms_peaksList = new ArrayList<>();
    }

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
        return "\n\tGCMS_Spectrum{" +
                "\n\t\tgcms_peaksList=" + gcms_peaksList +
                '}';
    }
}

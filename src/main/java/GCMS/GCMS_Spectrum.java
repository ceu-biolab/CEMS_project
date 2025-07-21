package GCMS;

import java.util.ArrayList;
import java.util.List;

public class GCMS_Spectrum {
    //list of Peaks of the Spectrum - mz & intensity
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

        /*String information ="";
        for(int i=0; i<this.gcms_peaksList.size(); i++){
            double mz = this.gcms_peaksList.get(i).getMz();
            double intensity = this.gcms_peaksList.get(i).getIntensity();
            information = information +
        }*/

        return "\n\tGCMS_Spectrum{" +
                //"gcms_peaksList=" + gcms_peaksList.get(0) +
                "\n\t\tgcms_peaksList=" + gcms_peaksList +
                '}';
    }
}

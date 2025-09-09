package GCMS;

import java.util.List;

public class GCMS {
    private List<CompoundGC> compoundgcList = null;

    public GCMS(List<CompoundGC> compoundgcAlkanesList) {
        this.compoundgcList = compoundgcAlkanesList;
    }

    public List<CompoundGC> getCompoundgcList() {
        return this.compoundgcList;
    }

    public void setCompoundgcList(List<CompoundGC> compoundgcList) {
        this.compoundgcList = compoundgcList;
    }

    private String getinfolistcompounds(){
        int i;
        String info ="";
        for (i=1; i<=this.compoundgcList.size(); i++){
            info = info + "\n" + i + ": "+this.compoundgcList.get(i-1);
        }
        return info;
    }

    @Override
    public String toString() {
        String info = getinfolistcompounds();
        return info;
    }
}

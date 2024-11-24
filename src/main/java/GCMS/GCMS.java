package GCMS;

import java.util.List;

public class GCMS {
    private List<CompoundGC> compoundgcList = null;

    public GCMS(List<CompoundGC> compoundgcAlkanesList) {
        this.compoundgcList = compoundgcAlkanesList;
    }

    public List<CompoundGC> getCompoundgcList() {
        return compoundgcList;
    }

    public void setCompoundgcList(List<CompoundGC> compoundgcList) {
        this.compoundgcList = compoundgcList;
    }

    private String getinfolistcompounds(){
        int i;
        String info ="";
        /*A list goes from 0 to n-1 (from a list of n elements)
        The loop starts from 1 so that it can save the index of the list starting from 1
        This means that to look for the element we need to do i-1 so that we can look for the correct element
         */
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

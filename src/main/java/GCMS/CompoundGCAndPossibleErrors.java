package GCMS;

import java.util.*;

public class CompoundGCAndPossibleErrors {

    public Map<Integer, CompoundGC> compoundGC; //FOUND COMPOUNDS
    public Map<Integer, CompoundGCInformationTxt> casIdNotFound; //COMPOUNDS NOT ACCESSIBLE WITH THEIR CASID (IF GIVEN)
    public Map<Integer, CompoundGCInformationTxt> nameNotFound; //COMPOUNDS NOT ACCESSIBLE WITH THEIR NAME
    public Map<Integer, CompoundGCInformationTxt> inchiNotFound; //COMPOUNDS NOT ACCESSIBLE WITH THEIR INCHI (IF GIVEN)
    public Map<Integer, CompoundGCInformationTxt> smilesNotFound; //COMPOUNDS NOT ACCESSIBLE WITH THEIR SMILES (IF GIVEN)

    public List<String> error; //GROUP WITH A LIST OF ALL THE COMPOUNDS THAT ARE NOT FOUND

    public CompoundGCAndPossibleErrors() {
        this.compoundGC = new TreeMap<>();
        this.casIdNotFound = new TreeMap<>();
        this.nameNotFound = new TreeMap<>();
        this.inchiNotFound = new TreeMap<>();
        this.smilesNotFound = new TreeMap<>();
        this.error = new ArrayList<>();
    }

    public Map<Integer,CompoundGC> getCompoundGC() {
        return compoundGC;
    }

    public void setCompoundGC(Map<Integer,CompoundGC> compoundGC) {
        this.compoundGC = compoundGC;
    }

    public Map<Integer, CompoundGCInformationTxt> getCasIdNotFound() {
        return casIdNotFound;
    }

    public void setCasIdNotFound(Map<Integer, CompoundGCInformationTxt> casIdNotFound) {
        this.casIdNotFound = casIdNotFound;
    }

    public Map<Integer, CompoundGCInformationTxt> getNameNotFound() {
        return nameNotFound;
    }

    public void setNameNotFound(Map<Integer, CompoundGCInformationTxt> nameNotFound) {
        this.nameNotFound = nameNotFound;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }

    public Map<Integer, CompoundGCInformationTxt> getInchiNotFound() {
        return inchiNotFound;
    }

    public void setInchiNotFound(Map<Integer, CompoundGCInformationTxt> inchiNotFound) {
        this.inchiNotFound = inchiNotFound;
    }

    public Map<Integer,CompoundGCInformationTxt> getSmilesNotFound() {
        return smilesNotFound;
    }

    public void setSmilesNotFound(Map<Integer,CompoundGCInformationTxt> smilesNotFound) {
        this.smilesNotFound = smilesNotFound;
    }
}

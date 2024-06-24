/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cems_project;

import patternFinders.RegexInChI;

import java.util.List;

/**
 *
 * @author maria
 */
public class CEMSCompound extends Compound{

    private final Double experimentalMZ;
    private final Double MT;
    private final Double RMT;
    private final Integer compound_id_reference_MT;
    private final List<Fragment> fragments;
    private final Double eff_mobility;
    private final Integer identificationLevel;
    private final Integer sampleTypeInt;

    /**
     * @param compound_id
     * @param compound_name
     * @param casId
     * @param RefHMDB
     * @param RefPubChem
     * @param formula
     * @param monoisotopic_mass
     * @param experimentalMZ
     * @param identifierOwn
     * @param MT
     * @param fragments
     * @param eff_mobility
     */
    //constructor
    public CEMSCompound(Integer compound_id, String compound_name, String casId, String RefHMDB, Integer RefPubChem, String formula,
                        Double monoisotopic_mass, Double experimentalMZ, Identifier identifierOwn, Integer sampleTypeInt,
                        Double MT, Double RMT, Integer compound_id_reference_MT,
                        List<Fragment> fragments, Double eff_mobility, Integer identificationLevel) {
        super(compound_id, compound_name, casId, RegexInChI.getFormulaFromInChI(identifierOwn.getInchi()), monoisotopic_mass, 2,
                0, null, identifierOwn);
        this.experimentalMZ = experimentalMZ;
        this.MT = MT;
        this.RMT = RMT;
        this.fragments = fragments;
        this.eff_mobility = eff_mobility;
        this.compound_id_reference_MT = compound_id_reference_MT;
        this.identificationLevel = identificationLevel;
        this.sampleTypeInt = sampleTypeInt;
    }

    @Override
    public String toString() {
        return "CEMSCompound{" +
                "MTcompnd=" + MT +
                ", RMTmets=" + RMT +
                ", compound_id_reference_MT=" + compound_id_reference_MT +
                ", fragments=" + fragments +
                ", eff_mobility=" + eff_mobility +
                ", identificationLevel=" + identificationLevel +
                "} " + super.toString();
    }

    public List<Fragment> getFragments() {
        return this.fragments;
    }

    public Double getEff_mobility() {
        return eff_mobility;
    }

    public Double getExperimentalMZ() {
        return experimentalMZ;
    }

    public Double getMT() {
        return MT;
    }

    public Double getRMT() {
        return RMT;
    }

    public Integer getCompound_id_reference_MT() {
        return compound_id_reference_MT;
    }

    public Integer getIdentificationLevel() {
        return identificationLevel;
    }

    public Integer getSampleTypeInt() {
        return sampleTypeInt;
    }
}

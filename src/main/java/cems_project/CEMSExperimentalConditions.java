package cems_project;

import exceptions.EffMobNotAvailable;

import static dbmanager.ConstantQueries.*;

public class CEMSExperimentalConditions {
    public enum IonizationMode {
        POSITIVE,
        NEGATIVE;
    }
    public enum Polarity {
        DIRECT,
        REVERSE;
    }
    private final String label;
    private final BufferEnum buffer;
    private final Integer temperature;
    private final IonizationMode ionizationMode;
    private final Polarity polarity;
    private final Integer ref_compound_id_RMT;
    private final Integer sampleType_int;
    private final Double mtCompoundA; // minutes
    private final Double effMobCompoundA;
    private final Double mtCompoundB; // minutesf
    private final Double effMobCompoundB;
    private final Integer capillaryLength; // milimeters
    private final Integer voltage; // Kilo Volts
    private final Double electricField; // Kilo Volts
    private final Double mtEOF; // minutes
    private final Double timeRamp; // minutes
    private Integer eff_mob_exp_prop_id;
    private Integer ce_exp_prop_id;

    /**
     * @param buffer see enum
     * @param temperature
     * @param ionizationMode
     * @param polarity
     * @param mtCompoundA
     * @param effMobCompoundA
     * @param mtCompoundB
     * @param effMobCompoundB
     * @param capillaryLength
     * @param voltage
     * @param mtEOF
     */
    public CEMSExperimentalConditions(String label, BufferEnum buffer, Integer temperature, IonizationMode ionizationMode, Polarity polarity,
                                      Integer ref_compound_id_RMT, Integer sampleType_int,
                                      Double mtCompoundA, Double effMobCompoundA, Double mtCompoundB, Double effMobCompoundB,
                                      Integer capillaryLength, Integer voltage, Double electricField, Double mtEOF, Double timeRamp,
                                      Integer eff_mob_exp_prop_id, Integer ce_exp_prop_id) {
        this.label = label;
        this.buffer = buffer;
        this.temperature = temperature;
        this.ionizationMode = ionizationMode;
        this.polarity = polarity;
        this.ref_compound_id_RMT = ref_compound_id_RMT;
        this.sampleType_int = sampleType_int;
        this.mtCompoundA = mtCompoundA;
        this.effMobCompoundA = effMobCompoundA;
        this.mtCompoundB = mtCompoundB;
        this.effMobCompoundB = effMobCompoundB;
        this.capillaryLength = capillaryLength;
        this.voltage = voltage;
        if (voltage != null) {
            this.electricField = (double) (voltage / capillaryLength);
        } else {
            this.electricField = null;
        }
        this.mtEOF = mtEOF;
        if(timeRamp == null)
        {
            timeRamp = 0d;
        }
        this.timeRamp = timeRamp;
        this.eff_mob_exp_prop_id = null;
        this.ce_exp_prop_id = null;
    }

    /**
     * Constructor for 2 markers with no experimental conditions information
     * @param buffer
     * @param temperature
     * @param ionizationMode
     * @param polarity
     * @param mtCompoundA
     * @param effMobCompoundA
     * @param mtCompoundB
     * @param effMobCompoundB
     */
    public CEMSExperimentalConditions(String label, BufferEnum buffer, Integer temperature, IonizationMode ionizationMode, Polarity polarity,
                                      Integer ref_compound_id_RMT, Integer sampleType_int,
                                      Double mtCompoundA, Double effMobCompoundA,
                                      Double mtCompoundB, Double effMobCompoundB) {
        this(label,buffer,temperature,ionizationMode,polarity,ref_compound_id_RMT, sampleType_int,
                mtCompoundA,effMobCompoundA,mtCompoundB, effMobCompoundB,
                null, null, null, null, 0d, null, null);
    }


    /**
     * Constructor with no markers
     * @param buffer
     * @param temperature
     * @param ionizationMode
     * @param polarity
     * @param capillaryLength
     * @param voltage
     * @param mtEOF
     */
    public CEMSExperimentalConditions(String label, BufferEnum buffer, Integer temperature, IonizationMode ionizationMode, Polarity polarity,
                                      Integer ref_compound_id_RMT, Integer sampleType_int,
                                      Integer capillaryLength, Integer voltage, Double electricField, Double mtEOF) {
        this(label, buffer,temperature,ionizationMode,polarity,ref_compound_id_RMT, sampleType_int,
                null,null,null, null,
                capillaryLength, voltage, electricField,mtEOF, 0d, null, null);
    }

    public CEMSExperimentalConditions(String label, BufferEnum buffer, Integer temperature, IonizationMode ionizationMode, Polarity polarity,
                                      Integer ref_compound_id_RMT, Integer sampleType_int,
                                      Double mtCompoundA, Double effMobCompoundA,
                                      Double mtCompoundB, Double effMobCompoundB,
                                      Integer capillaryLength, Integer voltage, Double mtEOF) {
        this(label, buffer,temperature,ionizationMode,polarity,ref_compound_id_RMT, sampleType_int,
                 mtCompoundA, effMobCompoundA, mtCompoundB, effMobCompoundB,capillaryLength, voltage,
                null, mtEOF, 0d, null, null);
    }

    public String getLabel(){return label;}

    public BufferEnum getBuffer() {
        return buffer;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public IonizationMode getIonizationMode() {
        return ionizationMode;
    }

    public Polarity getPolarity() {
        return polarity;
    }

    public Integer getRef_compound_id_RMT() {
        return ref_compound_id_RMT;
    }

    public Double getMtCompoundA() {
        return mtCompoundA;
    }

    public Double getEffMobCompoundA() {
        return effMobCompoundA;
    }

    public Double getMtCompoundB() {
        return mtCompoundB;
    }

    public Integer getCapillaryLength() {
        return capillaryLength;
    }

    public Double getElectricField() {
        return electricField;
    }

    public Double getMtEOF() {
        return mtEOF;
    }

    public Double getEffMobCompoundB() {
        return effMobCompoundB;
    }

    public Double getTimeRamp() {
        return timeRamp;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public Integer getSampleType_int() {
        return sampleType_int;
    }

    public Integer getEff_mob_exp_prop_id() {
        return eff_mob_exp_prop_id;
    }

    public void setEff_mob_exp_prop_id(Integer eff_mob_exp_prop_id) {
        this.eff_mob_exp_prop_id = eff_mob_exp_prop_id;
    }

    public Integer getCe_exp_prop_id() {
        return ce_exp_prop_id;
    }

    public void setCe_exp_prop_id(Integer ce_exp_prop_id) {
        this.ce_exp_prop_id = ce_exp_prop_id;
    }

    /**
     * @param mtCompoundOfInterest
     * @return
     * @throws EffMobNotAvailable
     */
    public Double getEffMob2Markers(double mtCompoundOfInterest) throws EffMobNotAvailable {
        if(this.mtCompoundA == null || this.mtCompoundB == null
                || effMobCompoundA == null || effMobCompoundB == null)
        {
            throw new EffMobNotAvailable("The experimental conditions are not enough to calculate the effective" +
                    "mobility with two markers");
        }
        double numerator = (mtCompoundOfInterest - mtCompoundB) * (mtCompoundA - timeRamp / 2) * effMobCompoundA
                - (mtCompoundOfInterest - mtCompoundA) * (mtCompoundB - timeRamp / 2) * effMobCompoundB;
        double denominator = (mtCompoundA - mtCompoundB) * (mtCompoundOfInterest -timeRamp / 2);
        double effMob = numerator / denominator;
        return effMob;
    }




    /**
     * @param mtCompoundOfInterest
     * @return
     * @throws EffMobNotAvailable
     */
    public Double getEffMobFromExpConditions(double mtCompoundOfInterest) throws EffMobNotAvailable{
        if(this.capillaryLength == null || this.electricField == null
                || this.mtEOF == null)
        {
            throw new EffMobNotAvailable("The experimental conditions are not enough to calculate the effective" +
                    "mobility without the capillary length, electrifield and the migration time of EOF");
        }
        double effMob = ((capillaryLength*capillaryLength)/voltage) *
                (1/(mtCompoundOfInterest-timeRamp/2) - 1/(mtEOF-timeRamp/2));
        return effMob;
    }



}

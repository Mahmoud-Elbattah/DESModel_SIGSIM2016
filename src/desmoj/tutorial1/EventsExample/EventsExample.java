package desmoj.tutorial1.EventsExample;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This is the model class. It is the main class of a simple event-oriented
 * model of the loading zone of a container terminal. Trucks arrive at the
 * terminal to load containers. They wait in line until a van carrier is
 * available to fetch a certain container and load it onto the truck. After
 * loading is completed, the truck leaves the terminal while the van carrier
 * serves the next truck.
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class EventsExample extends Model {
DiscreteDistEmpirical hospital;
DiscreteDistEmpirical residence;
DiscreteDistEmpirical age;
DiscreteDistEmpirical fragility;
DiscreteDistEmpirical fractureType;
DiscreteDistEmpirical diagnosisType;
ContDistConstant malePatientArrivalTime;
ContDistConstant femalePatientArrivalTime;
Map<String, List<Double>> yearPopulations ; 
Map<String, Double> choPercent;   
Map<String, List<Double>> diagnosisList ; 

	public EventsExample(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
		super(owner, modelName, showInReport, showInTrace);
	}
	/**
	 * Returns a description of the model to be used in the report.
	 * @return model description as a string
	 */
	public String description() {
		return "Dischrge Destination Sim Model of the Hip Fracture Care";
	}
	/**
	 * Activates dynamic model components (events).
	 *
	 * This method is used to place all events or processes on the
	 * internal event list of the simulator which are necessary to start
	 * the simulation.
	 *
	 * In this case, the truck generator event will have to be
	 * created and scheduled for the start time of the simulation.
	 */
    public void doInitialSchedules() {
        // create the TruckGeneratorEvent
        MPatientGeneratorEvent malePatientGenerator
                = new MPatientGeneratorEvent(this, "Male Patient Generator", true);

        FPatientGeneratorEvent femalePatientGenerator
                = new FPatientGeneratorEvent(this, "Female Patient Generator", true);

        // schedule for start of simulation
        malePatientGenerator.schedule(new TimeSpan(0));
        femalePatientGenerator.schedule(new TimeSpan(0));
    }
	/**
	 * Initialises static model components like distributions and queues.
	 */
    public void init() {

        InitCHOs();
        InitPopulations();
        InitMPatientArrival();
        InitFPatientArrival();
        //Init Age
        InitHospDistribution();
        InitResidenceDistribution();
        InitAgeDistribution();
        InitFragilityDistribution();
        InitFracTypeDistribution();
        InitDiagDistribution();
    }
    public void InitCHOs() {
        choPercent = new HashMap<>();// Male population, then female population
        choPercent.put("CHO1", 9.5);
        choPercent.put("CHO2", 10.6);
        choPercent.put("CHO3", 8.9);
        choPercent.put("CHO4", 15.5);
        choPercent.put("CHO5", 11.7);
        choPercent.put("CHO6", 8.8);
        choPercent.put("CHO7", 12.0);
        choPercent.put("CHO8", 11.7);
        choPercent.put("CHO9", 11.3);
    }
    public void InitPopulations()
    {
        yearPopulations = new HashMap<>();// Male population, then female population
        yearPopulations.put("2013", Arrays.asList(381041.0, 425799.0));// 2013 will be used for model validation
        yearPopulations.put("2016", Arrays.asList(422965.0, 465222.0));
        yearPopulations.put("2017", Arrays.asList(437907.0, 479079.0));
        yearPopulations.put("2018", Arrays.asList(453122.0, 493389.0));
        yearPopulations.put("2019", Arrays.asList(468454.0, 507616.0));
        yearPopulations.put("2020", Arrays.asList(484698.0, 523315.0));
        yearPopulations.put("2021", Arrays.asList(501711.0, 539759.0));
        yearPopulations.put("2022", Arrays.asList(518177.0, 555410.0));
        yearPopulations.put("2023", Arrays.asList(535249.0, 571876.0));
        yearPopulations.put("2024", Arrays.asList(552724.0, 588662.0));
        yearPopulations.put("2025", Arrays.asList(570513.0, 606098.0));
        yearPopulations.put("2026", Arrays.asList(588297.0, 622986.0));
    }
      public void InitMPatientArrival()
      {
       double maleFractureRate = 140.0 / 100000.0;//140 per 100K

        double totalElderlyMalePop= yearPopulations.get(GlobalObjects.simYear).get(0);
  
      //  double totalElderlyMalePop = 422965.0;

        double choPopulationPercent =choPercent.get(GlobalObjects.simCHO) / 100.0;

        double choElderlyMalePop = totalElderlyMalePop * choPopulationPercent;

        double choCases = Math.round(choElderlyMalePop * maleFractureRate);

        System.out.println("Male Total Count:" + choCases);
        double x = (365.0 * 24.0 * 60.0) / choCases;
        malePatientArrivalTime = new ContDistConstant(this, "TruckArrivalTimeStream", x, true, false);
        malePatientArrivalTime.setNonNegative(true); 
      }        
    public void InitFPatientArrival() {
        double femaleFractureRate = 407.0 / 100000.0;//140 per 100K

        double totalElderlyFemalePop = yearPopulations.get(GlobalObjects.simYear).get(1);

        double choPopulationPercent =choPercent.get(GlobalObjects.simCHO) / 100.0 ;//CHO percent of the total elderly population

        double choElderlyFemalePop = totalElderlyFemalePop * choPopulationPercent;

        double choCases = Math.round(choElderlyFemalePop * femaleFractureRate);

        System.out.println("Female Total Count:" + choCases);
        double x = (365.0 * 24.0 * 60.0) / choCases;
        femalePatientArrivalTime = new ContDistConstant(this, "TruckArrivalTimeStream", x, true, false);
        femalePatientArrivalTime.setNonNegative(true);
    }
   
    public void InitAgeDistribution() {
        age = new DiscreteDistEmpirical(this, "Patient Age", false, false);
        int[] ages = new int[]{60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102};
        int[] frequency = new int[]{10, 19, 16, 19, 23, 30, 28, 34, 29, 36, 26, 37, 40, 49, 53, 58, 52, 48, 60, 85, 67, 80, 84, 78, 92, 75, 98, 100, 73, 79, 69, 57, 31, 29, 29, 19, 11, 12, 5, 7, 5, 2, 2};
        for (int i = 0; i < ages.length; i++)
            age.addEntry(ages[i], frequency[i]);
    }
    public void InitFragilityDistribution() {
        fragility = new DiscreteDistEmpirical(this, "Fragility History", false, false);
        fragility.addEntry(1, 500);// Yes 
        fragility.addEntry(2, 500);// No Fragility 
    }
    public void InitFracTypeDistribution() {
        fractureType = new DiscreteDistEmpirical(this, "Fracture Type", false, false);
        fractureType.addEntry(1, 593);// Intracapsular-Displaced 
        fractureType.addEntry(2, 171);// Intracapsular-Undisplaced
        fractureType.addEntry(3, 697);// Intertrochanteric
        fractureType.addEntry(4, 194);// Subtrochanteric
    }     
    public void InitHospDistribution() {
        hospital = new DiscreteDistEmpirical(this, "Source Hospital", false, false);
        switch (GlobalObjects.simCHO) {
            case "CHO1":
                hospital.addEntry(500, 117);
                hospital.addEntry(501, 54);
                break;
            case "CHO2":
                hospital.addEntry(802, 50);
                break;
            case "CHO3":
                hospital.addEntry(300, 223);
                break;
            case "CHO4":
                hospital.addEntry(300, 100);//Extra
                hospital.addEntry(724, 25);
                hospital.addEntry(726, 3);
                break;
            case "CHO5":
                hospital.addEntry(600, 345);
                break;
            case "CHO6":
                hospital.addEntry(910, 241);
                break;
            case "CHO7":
                hospital.addEntry(904, 72);
                hospital.addEntry(1270, 144);
                break;
            case "CHO8":
                hospital.addEntry(203, 165);
                hospital.addEntry(922, 257);
                break;
            case "CHO9":
                hospital.addEntry(108, 93);
                hospital.addEntry(908, 59);
                hospital.addEntry(923, 8);
                break;
        }
    }
    public void InitResidenceDistribution() {
        residence = new DiscreteDistEmpirical(this, "Area of Residence", false, false);
        switch (GlobalObjects.simCHO) {
            case "CHO1":
                residence.addEntry(2100, 1);
                residence.addEntry(200, 1);
                residence.addEntry(1900, 1);
                residence.addEntry(3305, 1);
                residence.addEntry(3311, 1);
                residence.addEntry(100, 2);
                residence.addEntry(3310, 2);
                residence.addEntry(2000, 4);
                residence.addEntry(2600, 18);
                residence.addEntry(2700, 26);
                residence.addEntry(2800, 114);
                break;
            case "CHO2":
                residence.addEntry(107, 1);
                residence.addEntry(109, 1);
                residence.addEntry(1900, 1);
                residence.addEntry(3501, 1);
                residence.addEntry(2000, 2);
                residence.addEntry(3303, 3);
                residence.addEntry(2100, 41);
                break;
            case "CHO3":
                residence.addEntry(1000, 1);
                residence.addEntry(100, 1);
                residence.addEntry(200, 1);
                residence.addEntry(3100, 1);
                residence.addEntry(3501, 1);
                residence.addEntry(800, 4);
                residence.addEntry(1700, 34);
                residence.addEntry(1401, 38);
                residence.addEntry(1500, 69);
                residence.addEntry(1600, 73);
                break;
            case "CHO4":
                residence.addEntry(3303, 1);
                residence.addEntry(1300, 3);
                residence.addEntry(1101, 8);
                residence.addEntry(1200, 16);
                /// extra from CHO3
                 residence.addEntry(1700, 34);
                residence.addEntry(1401, 38);
                residence.addEntry(1500, 69);
                residence.addEntry(1600, 73);
                break;
            case "CHO5":
                residence.addEntry(200, 1);
                residence.addEntry(212, 1);
                residence.addEntry(216, 1);
                residence.addEntry(300, 1);
                residence.addEntry(1200, 1);
                residence.addEntry(2500, 1);
                residence.addEntry(2700, 1);
                residence.addEntry(3307, 1);
                residence.addEntry(3501, 1);
                residence.addEntry(1700, 2);
                residence.addEntry(400, 3);
                residence.addEntry(3303, 6);
                residence.addEntry(500, 29);
                residence.addEntry(901, 29);
                residence.addEntry(1000, 35);
                residence.addEntry(700, 56);
                residence.addEntry(800, 76);
                residence.addEntry(600, 100);
                break;
            case "CHO6":
                residence.addEntry(100, 1);
                residence.addEntry(103, 1);
                residence.addEntry(105, 1);
                residence.addEntry(109, 1);
                residence.addEntry(208, 1);
                residence.addEntry(1500, 1);
                residence.addEntry(2100, 1);
                residence.addEntry(2700, 1);
                residence.addEntry(3303, 1);
                residence.addEntry(3501, 1);
                residence.addEntry(202, 3);
                residence.addEntry(600, 3);
                residence.addEntry(217, 10);
                residence.addEntry(216, 11);
                residence.addEntry(206, 13);
                residence.addEntry(218, 15);
                residence.addEntry(214, 15);
                residence.addEntry(204, 21);
                residence.addEntry(400, 63);
                residence.addEntry(200, 77);
                break;
            case "CHO7":
                residence.addEntry(901, 1);
                residence.addEntry(1500, 1);
                residence.addEntry(1600, 1);
                residence.addEntry(1900, 1);
                residence.addEntry(2100, 1);
                residence.addEntry(2500, 1);
                residence.addEntry(3200, 1);
                residence.addEntry(3303, 1);
                residence.addEntry(3320, 1);
                residence.addEntry(3501, 1);
                residence.addEntry(3100, 2);
                residence.addEntry(500, 2);
                residence.addEntry(2300, 2);
                residence.addEntry(220, 2);
                residence.addEntry(202, 3);
                residence.addEntry(200, 4);
                residence.addEntry(210, 7);
                residence.addEntry(400, 9);
                residence.addEntry(214, 10);
                residence.addEntry(222, 14);
                residence.addEntry(208, 15);
                residence.addEntry(224, 16);
                residence.addEntry(206, 17);
                residence.addEntry(216, 18);
                residence.addEntry(212, 25);
                residence.addEntry(300, 60);
                break;
            case "CHO8":
                residence.addEntry(200, 1);
                residence.addEntry(500, 1);
                residence.addEntry(1500, 1);
                residence.addEntry(2100, 1);
                residence.addEntry(2600, 1);
                residence.addEntry(3303, 1);
                residence.addEntry(3305, 1);
                residence.addEntry(3310, 1);
                residence.addEntry(100, 4);
                residence.addEntry(300, 5);
                residence.addEntry(1700, 6);
                residence.addEntry(2200, 31);
                residence.addEntry(2400, 34);
                residence.addEntry(2500, 34);
                residence.addEntry(3000, 42);
                residence.addEntry(2300, 52);
                residence.addEntry(2900, 58);
                residence.addEntry(3200, 66);
                residence.addEntry(3100, 82);
                break;
            case "CHO9":
                residence.addEntry(210, 1);
                residence.addEntry(400, 1);
                residence.addEntry(800, 1);
                residence.addEntry(2000, 1);
                residence.addEntry(2200, 1);
                residence.addEntry(2600, 1);
                residence.addEntry(2800, 1);
                residence.addEntry(3303, 1);
                residence.addEntry(3311, 1);
                residence.addEntry(208, 2);
                residence.addEntry(101, 3);
                residence.addEntry(105, 4);
                residence.addEntry(220, 4);
                residence.addEntry(200, 5);
                residence.addEntry(100, 6);
                residence.addEntry(109, 11);
                residence.addEntry(103, 11);
                residence.addEntry(300, 15);
                residence.addEntry(115, 18);
                residence.addEntry(3200, 21);
                residence.addEntry(107, 25);
                residence.addEntry(111, 26);
                break;
        }
    }
    public void InitDiagDistribution() {
        diagnosisList = new HashMap<>();// Diagnosis ID, then frequency
        diagnosisList.put("S7205", Arrays.asList(1.0, 17.0));
        diagnosisList.put("S7203", Arrays.asList(2.0, 210.0));
        diagnosisList.put("S7211", Arrays.asList(3.0, 529.0));
        diagnosisList.put("S7200", Arrays.asList(4.0, 612.0));
        diagnosisList.put("S7210", Arrays.asList(5.0, 43.0));
        diagnosisList.put("S7201", Arrays.asList(6.0, 182.0));
        diagnosisList.put("S722", Arrays.asList(7.0, 90.0));
        diagnosisList.put("S7204", Arrays.asList(8.0, 28.0));
        diagnosisList.put("S729", Arrays.asList(9.0, 13.0));
        diagnosisList.put("Z491", Arrays.asList(10.0, 1.0));
        diagnosisList.put("S7240", Arrays.asList(11.0, 18.0));
        diagnosisList.put("S066", Arrays.asList(12.0, 1.0));
        diagnosisList.put("S681", Arrays.asList(13.0, 1.0));
        diagnosisList.put("S723", Arrays.asList(14.0, 21.0));
        diagnosisList.put("R296", Arrays.asList(15.0, 1.0));
        diagnosisList.put("S4221", Arrays.asList(16.0, 1.0));
        diagnosisList.put("S023", Arrays.asList(17.0, 1.0));
        diagnosisList.put("N179", Arrays.asList(18.0, 1.0));
        diagnosisList.put("I500", Arrays.asList(19.0, 2.0));
        diagnosisList.put("M8445", Arrays.asList(20.0, 5.0));
        diagnosisList.put("T856", Arrays.asList(21.0, 1.0));
        diagnosisList.put("S5250", Arrays.asList(22.0, 2.0));
        diagnosisList.put("S7208", Arrays.asList(23.0, 25.0));
        diagnosisList.put("M7966", Arrays.asList(24.0, 1.0));
        diagnosisList.put("I212", Arrays.asList(25.0, 1.0));
        diagnosisList.put("R568", Arrays.asList(26.0, 2.0));
        diagnosisList.put("S7243", Arrays.asList(27.0, 3.0));
        diagnosisList.put("J440", Arrays.asList(28.0, 2.0));
        diagnosisList.put("R000", Arrays.asList(29.0, 1.0));
        diagnosisList.put("K297", Arrays.asList(30.0, 1.0));
        diagnosisList.put("I638", Arrays.asList(31.0, 1.0));
        diagnosisList.put("T840", Arrays.asList(32.0, 1.0));
        diagnosisList.put("S798", Arrays.asList(33.0, 2.0));
        diagnosisList.put("M5496", Arrays.asList(34.0, 1.0));
        diagnosisList.put("K294", Arrays.asList(35.0, 1.0));
        diagnosisList.put("M8094", Arrays.asList(36.0, 1.0));
        diagnosisList.put("I639", Arrays.asList(37.0, 2.0));
        diagnosisList.put("T842", Arrays.asList(38.0, 1.0));
        diagnosisList.put("S8282", Arrays.asList(39.0, 1.0));
        diagnosisList.put("S923", Arrays.asList(40.0, 1.0));
        diagnosisList.put("C9000", Arrays.asList(41.0, 1.0));
        diagnosisList.put("I460", Arrays.asList(42.0, 1.0));
        diagnosisList.put("S4220", Arrays.asList(43.0, 1.0));
        diagnosisList.put("I214", Arrays.asList(44.0, 2.0));
        diagnosisList.put("J189", Arrays.asList(45.0, 1.0));
        diagnosisList.put("T1408", Arrays.asList(46.0, 1.0));
        diagnosisList.put("N390", Arrays.asList(47.0, 3.0));
        diagnosisList.put("R53", Arrays.asList(48.0, 1.0));
        diagnosisList.put("M161", Arrays.asList(49.0, 3.0));
        diagnosisList.put("E86", Arrays.asList(50.0, 1.0));
        diagnosisList.put("S7241", Arrays.asList(51.0, 1.0));
        diagnosisList.put("S324", Arrays.asList(52.0, 1.0));
        diagnosisList.put("S7300", Arrays.asList(53.0, 1.0));
        diagnosisList.put("M8095", Arrays.asList(54.0, 2.0));
        diagnosisList.put("S5251", Arrays.asList(55.0, 1.0));
        diagnosisList.put("H353", Arrays.asList(56.0, 1.0));
        diagnosisList.put("E1171", Arrays.asList(57.0, 1.0));
        diagnosisList.put("M8435", Arrays.asList(58.0, 1.0));
        diagnosisList.put("C61", Arrays.asList(59.0, 1.0));
        diagnosisList.put("I620", Arrays.asList(60.0, 1.0));
        diagnosisList.put("M7965", Arrays.asList(61.0, 1.0));
        diagnosisList.put("I48", Arrays.asList(62.0, 1.0));

        diagnosisType = new DiscreteDistEmpirical(this, "Diagnosis Type", false, false);
        diagnosisList.entrySet().stream()
                .forEach(diagnosis -> {
                    double diagID = diagnosis.getValue().get(0);
                    double frequency = diagnosis.getValue().get(1);
                    diagnosisType.addEntry(diagID, frequency);
                }
                );
    }    
    
    public int SampleAge() {
        return age.sample().intValue();
    }
    public int SampleFragility() {
        return fragility.sample().intValue();
    }
    public int SampleFracType() {
        return fractureType.sample().intValue();
    }
    public int SampleHospital() {
        return hospital.sample().intValue();
    }
    public int SampleResidence() {
        return residence.sample().intValue();
    }
    public int SampleDiagnosis() {
        return diagnosisType.sample().intValue();
    }
    public double getMPatientArrivalTime() {
        return malePatientArrivalTime.getConstantValue();
    }
    public double getFPatientArrivalTime() {
        return femalePatientArrivalTime.getConstantValue();
    }

	/**
	 * Runs the model.
	 *
	 * In DESMO-J used to
	 *    - instantiate the experiment
	 *    - instantiate the model
	 *    - connect the model to the experiment
	 *    - steer length of simulation and outputs
	 *    - set the ending criterion (normally the time)
	 *    - start the simulation
	 *    - initiate reporting
	 *    - clean up the experiment
	 *
	 * @param args is an array of command-line arguments (will be ignored here)
	 */
	public static void main(java.lang.String[] args) {

		// create model and experiment
		EventsExample model = new EventsExample(null, "EventsExample", true, true);
                // null as first parameter because it is the main model and has no mastermodel
      
                Experiment exp = new Experiment("EventExampleExperiment", TimeUnit.SECONDS, TimeUnit.MINUTES, null);
      
                // ATTENTION, since the name of the experiment is used in the names of the
                // output files, you have to specify a string that's compatible with the
                // filename constraints of your computer's operating system. The remaing three
                // parameters specify the granularity of simulation time, default unit to
                // display time and the time formatter to use (null yields a default formatter).
		// connect both
		model.connectToExperiment(exp);

		// set experiment parameters
		exp.setShowProgressBar(true);  // display a progress bar (or not)
		exp.stop(new TimeInstant(365, TimeUnit.DAYS));   // set end of simulation at 1500 minutes
		exp.tracePeriod(new TimeInstant(0), new TimeInstant(100, TimeUnit.MINUTES));  // set the period of the trace
		exp.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES));   // and debug output
			// ATTENTION!
			// Don't use too long periods. Otherwise a huge HTML page will
			// be created which crashes Netscape :-)

		// start the experiment at simulation time 0.0
		exp.start();

		// --> now the simulation is running until it reaches its end criterion
		// ...
		// ...
		// <-- afterwards, the main thread returns here

		// generate the report (and other output files)
		exp.report();

		// stop all threads still alive and close all output files
		exp.finish();
                System.out.println("Generated Count:"+GlobalObjects.patientCount);
	}
} /* end of model class */

package desmoj.tutorial1.EventsExample;

import desmoj.core.simulator.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class represents an entity (and event) source, which continually
 * generates trucks (and their arrival events) in order to keep the simulation
 * running.
 *
 * It will create a new truck, schedule its arrival at the terminal (i.e. create
 * and schedule an arrival event) and then schedule itself for the point in time
 * when the next truck arrival is due.
 *
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class MPatientGeneratorEvent extends ExternalEvent {

    public MPatientGeneratorEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    /**
     * The eventRoutine() describes the generating of a new male patient.
     *
     * It creates a new truck, a new TruckArrivalEvent and schedules itself
     * again for the next new truck generation.
     */
    public void eventRoutine() {
        // get a reference to the model
        EventsExample model = (EventsExample) getModel();
        // create a new truck
        Patient malePatient = new Patient(model, "Male Patient", true);
        // create a new truck arrival event
        PaientArrivalEvent truckArrival = new PaientArrivalEvent(model, "Male Patient ArrivalEvent", true);
        // and schedule it for the current point in time
        truckArrival.schedule(malePatient, new TimeSpan(0.0));
        // schedule this truck generator again for the next truck arrival time
        schedule(new TimeSpan(model.getMPatientArrivalTime(), TimeUnit.MINUTES));
        //System.out.println("New Male Patient");
        //Setting patient properties
       malePatient.Hospital = model.SampleHospital();
       malePatient.Sex = 1;// 1-> Male
        malePatient.ResID = model.SampleResidence();
        malePatient.Age = model.SampleAge();
        int diagID = model.SampleDiagnosis();
        malePatient.Diag1 = model.diagnosisList.entrySet().stream()
                .filter(x -> x.getValue().get(0) == diagID)
                .map(x -> x.getKey()).reduce("", String::concat);
        malePatient.FracType = model.SampleFracType();
        malePatient.Fragility = model.SampleFragility();
        malePatient.CHO = GlobalObjects.simCHO;
           
    //Inserting patient into DB
    GlobalObjects.dbHandler.InsertPatient(malePatient);
    //    GlobalObjects.dbHandler.ExecuteCmd("Insert into LOSPredictions (HospID,Sex) values(910,1)");
    }
}

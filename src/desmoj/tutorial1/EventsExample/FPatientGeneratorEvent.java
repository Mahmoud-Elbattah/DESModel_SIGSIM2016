/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desmoj.tutorial1.EventsExample;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;
import java.util.concurrent.TimeUnit;

public class FPatientGeneratorEvent extends ExternalEvent{
        public FPatientGeneratorEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

    /**
     * The eventRoutine() describes the generating of a new female patient.
     */
    public void eventRoutine() {
        // get a reference to the model
        EventsExample model = (EventsExample) getModel();
        // create a new truck
        Patient femalePatient = new Patient(model, "Female Patient", true);
        // create a new truck arrival event
        PaientArrivalEvent truckArrival = new PaientArrivalEvent(model, "Female Patient ArrivalEvent", true);
        // and schedule it for the current point in time
        truckArrival.schedule(femalePatient, new TimeSpan(0.0));
        // schedule this truck generator again for the next truck arrival time
        schedule(new TimeSpan(model.getFPatientArrivalTime(), TimeUnit.MINUTES));
       //  System.out.println("New Female Patient");
        //Setting patient properties
        femalePatient.Hospital = model.SampleHospital();
        femalePatient.Sex = 2;// 2-> Female
        femalePatient.ResID = model.SampleResidence();
        femalePatient.Age = model.SampleAge();
        int diagID = model.SampleDiagnosis();
        femalePatient.Diag1 = model.diagnosisList.entrySet().stream()
                .filter(x -> x.getValue().get(0) == diagID)
                .map(x -> x.getKey()).reduce("", String::concat);
        femalePatient.FracType = model.SampleFracType();
        femalePatient.Fragility = model.SampleFragility();
        femalePatient.CHO = GlobalObjects.simCHO;
        //Inserting patient into DB
        GlobalObjects.dbHandler.InsertPatient(femalePatient);

    }
}

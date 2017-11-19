package desmoj.tutorial1.EventsExample;

import desmoj.core.simulator.*;

/**
 * This class represents the truck arrival event
 * in the EventsExample model.
 * It occurs when a truck arrives at the terminal
 * to request loading of a container.
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class PaientArrivalEvent extends Event<Patient> {

	/** a reference to the model this event is a part of.
	 * Useful shortcut to access the model's static components
	 */
	private EventsExample myModel;

	/**
	 * Constructor of the truck arrival event
	 *
	 * Used to create a new truck arrival event
	 *
	 * @param owner the model this event belongs to
	 * @param name this event's name
	 * @param showInTrace flag to indicate if this event shall produce output for the trace
	 */
	public PaientArrivalEvent(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		// store a reference to the model this event is associated with
		myModel = (EventsExample)owner;
	}

	/**
	 * This eventRoutine() describes what happens when a truck
	 * enters the terminal.
	 *
	 * On arrival, the truck will enter the queue (parking lot). It will then
	 * check if the van carrier is available.
	 * If this is the case, it will occupy the van carrier and schedule a
	 * service end event.
	 * Otherwise the truck just waits (does nothing).
	 */
	public void eventRoutine(Patient patient) {

		// truck enters parking-lot
		//myModel.truckQueue.insert(truck);
		//sendTraceNote("TruckQueueLength: "+ myModel.truckQueue.length());
GlobalObjects.patientCount++;
         //   System.out.println("New Patient");

	}
}
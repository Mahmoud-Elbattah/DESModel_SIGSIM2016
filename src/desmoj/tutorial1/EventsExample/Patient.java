package desmoj.tutorial1.EventsExample;

import desmoj.core.simulator.*;

/**
 * The Truck entity encapsulates all information associated with a truck. Due to
 * the fact that the only thing a truck wants in our model is a single
 * container, our truck has no special attributes. All necessary statistical
 * information are collected by the queue object.
 *
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class Patient extends Entity {

    /**
     * Constructor of the patient entity.
     */
    public int Sex;
    public int Hospital;
    public int ResID;
    public int Age;
    public String Diag1;
    public int FracType;
    public int Fragility;
    public String CHO;

    public Patient(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
    }

}

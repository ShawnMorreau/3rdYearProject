package XML;

import Model.ModelContainer;
import View.ShapesContainer;

/**
 * Stores the state of the Model and the Shapes contained in the view in this object.
 */
public class XMLState {

	private ModelContainer modelContainer;
	private ShapesContainer shapesContainer;

	/**
	 * Set the model container of the model to be saved/restored
	 * @param model the NetworkRoutingSimulatorModel to use
	 */
	public void setModelContainer(ModelContainer modelContainer) {
		this.modelContainer = modelContainer;
	}

	/**
	 * @return the modelContainer
	 */
	public ModelContainer getModelContainer() {
		return modelContainer;
	}

	/**
	 * @return the shapesContainer
	 */
	public ShapesContainer getShapesContainer()
	{
		return shapesContainer;
	}

	/**
	 * Set the shapes container (from view) to be saved/restored
	 * @param shapesContainer the shapesContainer to set
	 */
	public void setShapesContainer(ShapesContainer shapesContainer)
	{
		this.shapesContainer = shapesContainer;
	}

}

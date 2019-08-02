package XML;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Serializes the Model and the TopologyPanel to be saved and restored at any 
 * state.
 */
public class XMLStateSerializer {

	private XStream xstream;

	/**
	 * State Serializer constructor
	 */
	public XMLStateSerializer() {	
		xstream = new XStream(new StaxDriver());
		xstream.setMode(XStream.ID_REFERENCES);
	}

	/**
	 * Creates an xml file containing the current state of the model and panel
	 * @param file the file instance to save content to
	 */
	public void serialize(File file, XMLState xmlState) {
		if (xmlState != null && xmlState.getModelContainer() != null && xmlState.getShapesContainer() != null) {
			try {
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				xstream.marshal(xmlState, new PrettyPrintWriter(new OutputStreamWriter(bos)));
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initializes the model and the GUI panel states with the given xml 
	 * @param file the file of the xml
	 */
	public void deserialize(File file, XMLState xmlState) {
		XMLState newState = null;
		try {
			FileReader fr = new FileReader(file.getAbsolutePath());
			newState = (XMLState) xstream.fromXML(fr);
			xmlState.setModelContainer(newState.getModelContainer());
			xmlState.setShapesContainer(newState.getShapesContainer());
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

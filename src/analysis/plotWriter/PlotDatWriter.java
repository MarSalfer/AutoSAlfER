/**
 * 
 */
package analysis.plotWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author Martin Salfer
 * @created 08.04.2017 19:25:23
 *
 */
public class PlotDatWriter {

	private FileWriter file;
	private final String exportFileName;
	private final String dataName;

	public PlotDatWriter(String dataName, String exportFileName, String... colums) {
		this.exportFileName = exportFileName;
		this.dataName = dataName;
		try {
			this.file = new FileWriter(exportFileName);
			file.write("# " + dataName + "\n");
			file.write("# Written on " + LocalDateTime.now() + "\n");
			for (String s: colums) {
				file.write(s + "\t");
			}
			file.write("\n");
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeData(String... data) {
		try {
			for (String s: data) {
					file.write(s + "\t");
			}
			file.write("\n");
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		try {
			file.write("# JvmMaxMemory=" + (Runtime.getRuntime().maxMemory() / 1_000_000) + " MB");
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Written " + dataName + " to " + exportFileName + ".");
	}
	
}

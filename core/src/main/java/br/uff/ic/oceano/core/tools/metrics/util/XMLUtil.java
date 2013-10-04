package br.uff.ic.oceano.core.tools.metrics.util;

import br.uff.ic.oceano.util.file.PathUtil;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Daniel
 */
public class XMLUtil {

    public static void writeXML(Document doc, String path) throws IOException {
        writeXML(doc, path, "UTF-8");
    }

    public static void writeXML(Document doc, String path, String encoding) throws IOException{

        //create path
        PathUtil.mkDirs(path);

        FileWriter writer = new FileWriter(path);
        Format format = Format.getPrettyFormat();
        format.setEncoding(encoding);
        XMLOutputter outputter = new XMLOutputter(format);
        outputter.output(doc, writer);
        writer.close();

        //test if file was created
        File file = new File(path);
        if(!file.isFile()){
            throw new IOException("Not a file path: "+ path);
        }
    }

    public static Document readXml(String path) throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        return builder.build(new File(path));
    }


}

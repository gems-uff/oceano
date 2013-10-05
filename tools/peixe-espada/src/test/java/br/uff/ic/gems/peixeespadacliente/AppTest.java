package br.uff.ic.gems.peixeespadacliente;

import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


/**
 * Unit test for simple App.
 */
public class AppTest{
    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */

     @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

//    @Before
//    public void setUp() {
//    }
//
//    @After
//    public void tearDown() {
//    }

//    @Test
    public void test() throws Throwable {
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", ProjectVCS.class);

//        System.out.println("Lendo JSON :" + xstream.fromXML((writer(args))));
//        xstream = new XStream
//        ModelType mt = new ModelType();
//        mt.
//        JsonReader x = new JsonReader(null)
//        String xml = sugaXML("http://localhost:8080/oceano/JSONServlet");
//        System.out.println(xml);

//        testeLista();
//        testeUnico();

//        injetaJSON("http://localhost:8080/oceano/JSONServlet", xstream.fromXML((writer(args))).toString());


    }

    public static String writer(String[] args) {

        ProjectVCS project = new ProjectVCS();
        project.setName("IDUFF");
//        project.setVCS("SVN");
        project.setRepositoryUrl("file:///e:/rep_uff/proac/academico-graduacao/iduff/academico");
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", ProjectVCS.class);

        return xstream.toXML(project);
    }

    public static String sugaXML(String urlSt) throws IOException {

        URL url = new URL(urlSt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer strXml = new StringBuffer();
        String linha = null;
        while ((linha = reader.readLine()) != null) {
            strXml.append(linha);
        }
        reader.close();
        return strXml.toString();
    }

    public static void testeUnico() {
        String json = "{\"product\":{\"name\":\"Banana\",\"repositoryUrl\":\"URLALALAL\"" + "}}";

        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", ProjectVCS.class);
        ProjectVCS product = (ProjectVCS) xstream.fromXML(json);
        System.out.println(product);

    }

    public static void testeLista() throws IOException, JSONException {
        System.out.println("Entrou no Testa Lista");
        int fim = 10;
        List<JSONObject> lista = new ArrayList(fim);
        for (int i = 0; i < fim; i++) {
            ProjectVCS p = new ProjectVCS();
//            p.setVCS("VCS "+i);
            p.setName("Name " + i);
            p.setRepositoryUrl("Url " + i);
            JSONObject jo = new JSONObject();
            JSONObject jo2 = new JSONObject();
            jo.put("name", p.getName());
            jo.put("repositoryUrl", p.getRepositoryUrl());
            jo2.put("product", jo);
            lista.add(jo2);
        }

        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", ProjectVCS.class);

        JSONArray jSONArray = new JSONArray(lista);
        for (int i = 0; i < jSONArray.length(); i++) {
            System.out.println("Objeto = " + (ProjectVCS) xstream.fromXML(jSONArray.get(i).toString()));
            System.out.println("Objeto = " + jSONArray.get(i));
        }

    }

    private static void injetaJSON(String urlSt, String json) throws MalformedURLException, IOException {
        URL url = new URL(urlSt);
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(urlc.getOutputStream()));
        XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("product", ProjectVCS.class);
        xstream.toXML(json, out);
//        out.write(json);
        out.flush();
        out.close();









        OutputStreamWriter wr = new OutputStreamWriter(urlc.getOutputStream());
        wr.write(json);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String line;
        StringBuffer resultado = new StringBuffer("");
        while ((line = rd.readLine()) != null) {
            resultado.append(line);
        }
        wr.close();
        rd.close();
        System.out.println("resultado = " + resultado);







    }

    private static void testeListaServlet() throws IOException, JSONException {
        String xmlLista = sugaXML("http://localhost:8080/oceano/JSONServlet");
        JSONArray jSONArray = new JSONArray(xmlLista);
        for (int i = 0; i < jSONArray.length(); i++) {
            System.out.println("Objeto = " + jSONArray.get(i));
        }

////        jSONArray.
//
//         XStream xstream = new XStream(new JettisonMappedXmlDriver());
//        xstream.setMode(XStream.NO_REFERENCES);
//        xstream.alias("product", Project.class);
    }
}

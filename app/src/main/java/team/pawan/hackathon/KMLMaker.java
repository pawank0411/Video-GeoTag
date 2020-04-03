package team.pawan.hackathon;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class KMLMaker {

    public String getPlacemark(int num, String starttime, String latitude, String longitude, String altitude) {
        String name = "<name> Point # " + num + " </name>\n";
        String coord = "<Point>\n<coordinates>" + latitude + "," + longitude + "," + altitude + "</coordinates>\n</Point>\n";
        String timespan = "<TimeSpan>\n<begin>" + starttime + "</begin>\n</TimeSpan>\n";
        return "<Placemark>\n" + name + timespan + coord + "</Placemark>\n";
    }

    public String getCoords(String KML) {
        Pattern pattern = Pattern.compile("(?<=<coordinates>).*?(?=<\\/coordinates>)");
        Matcher matcher = pattern.matcher(KML);
        List<String> l = new ArrayList<>();
        while (matcher.find()) {
            l.add(matcher.group());
        }
        String[] coords = l.toArray(new String[0]);
        StringBuilder content = new StringBuilder();
        for (String coord : coords) {
            content.append(coord).append("\n");
        }

        return "<Folder>\n<Placemark>\n<name> Point # 0 </name>\n<styleUrl>#line-1267FF-5000-nodesc</styleUrl>\n<LineString>\n<tessellate>1</tessellate>\n<coordinates>" + content + "</coordinates>\n</LineString>\n</Placemark>\n</Folder>";
    }

    public static String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String prettyFormat(String input) {
        return prettyFormat(input, 0);
    }
}

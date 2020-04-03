package team.pawan.hackathon;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Map;
import org.jcodec.containers.mp4.boxes.MetaValue;
import org.jcodec.movtool.MetadataEditor;

public class MetaEmbed
{
    File mediaFile;
    File kmlFile;

    public Context context;
    public MetaEmbed(Context context, File kmlFile, File mediaFile) {
        this.context = context;
        this.kmlFile = kmlFile;
        this.mediaFile = mediaFile;
        final String kmlString = SerializeObject.objectToString(kmlFile);
        try {
            printData();
            writeData(kmlString);
            printData();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String printData() throws Exception
    {
        MetadataEditor mediaMeta = MetadataEditor.createFrom(mediaFile);
        Map<String, MetaValue> keyedMeta = mediaMeta.getKeyedMeta();
        if (keyedMeta != null)
        {
            System.out.println("Keyed metadata:");
//            for (Map.Entry<String, MetaValue> entry : keyedMeta.entrySet())
//            {
//                System.out.println(entry.getKey() + ": " + entry.getValue());
//            }
        }
        return keyedMeta.entrySet().toString();
    }
    public void writeData(String data) throws Exception
    {
        MetadataEditor mediaMeta = MetadataEditor.createFrom(mediaFile);
        Map<String, MetaValue> meta = mediaMeta.getKeyedMeta();
        meta.put("GeoTag_DATA", MetaValue.createString(data));
        mediaMeta.save(false);

        MetadataEditor metax = MetadataEditor.createFrom(mediaFile);
        Log.d("", "writeData: " + metax.getKeyedMeta());
    }


}
package com.fonoster.sipio.ctl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import java.nio.file.NoSuchFileException;
import java.util.Iterator;

import static java.lang.System.out;

public class CmdCreate {

    private static CtlUtils ctlUtils;
    private Gson gson;

    public CmdCreate(Subparsers subparsers, CtlUtils ctlUtils) {
        Subparser create = subparsers.addParser("create").aliases("crea").help("creates new resource(s)");
        create.addArgument("-f").metavar("FILE").help("path to yaml file with a resources(s)");

        create.epilog(String.join(
            System.getProperty("line.separator"),
            "`Examples:",
            "  # Creates a new agent from a yaml file",
            "  $ sipioctl crea -f agent.yaml\n\n",
            "  # Creates a set of gateways from a yaml file\n",
            "  $ sipioctl create -f gws.yaml\n"
        ));

        this.ctlUtils = ctlUtils;
        gson = new Gson();
    }

    void run(String path) {
        String data = "";

        if (path.isEmpty()) {
            out.print("You must indicate the path to the resource");
            System.exit(1);
        }

        try {
            data = new FileUtils().getJsonString(path);
        } catch(Exception ex) {
            if (ex instanceof NoSuchFileException) {
                out.print("Please ensure file '" + ex.getMessage() + "' exist and has proper permissions");
            } else if (ex instanceof NullPointerException) {
                out.print("You must indicate a file :(");
            } else {
                out.print("Unexpected Exception: " + ex.getMessage());
            }
            System.exit(1);
        }

        JsonElement je = gson.fromJson(data, JsonElement.class);

        if(je.isJsonArray()) {
            Iterator i = je.getAsJsonArray().iterator();

            while(i.hasNext()) {
                create((JsonElement) i.next());
            }
        } else {
            create(je);
        }
    }

    private void create(JsonElement je) {
        JsonObject jo = je.getAsJsonObject();
        String kind = jo.getAsJsonObject().get("kind").getAsString();
        HttpResponse result = ctlUtils.postWithToken(kind.toLowerCase() + "s", gson.toJson(je));
        JsonObject jObject = gson.fromJson(result.getBody().toString(), JsonObject.class);
        String message = jObject.get("message").getAsString();
        out.println(message);
    }
}

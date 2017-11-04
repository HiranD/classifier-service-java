/**
 * Created by hiran on 5/19/16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;


@Path("/classifier")
public class ClassifierService {
    private static final Logger log = Logger.getLogger(ClassifierService.class.getName());

    @GET
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson(@PathParam("param") String msg) throws FileNotFoundException {
//        msg = msg.toLowerCase();
        long tStart = System.currentTimeMillis();
        JsonObject root = new JsonObject();
        int noOfSynonyms = 0;
//        List<String> result = ServletContextClass.Data.get(msg);

//        System.out.println("size of ConceptID_: " + ServletContextClass.ConceptID_.size());
//        System.out.println("size of Synonyms_: " + ServletContextClass.Synonyms_.size());

        List<Integer> concept_ids = new ArrayList<>();
        List<String> concepts_ = new ArrayList<>();
//        System.out.println(msg);
        for (Map.Entry<Integer, String> e : ServletContextClass.ConceptID_.entrySet()) {
//            if (e.getValue().matches(".*\\b" + msg + "\\b.*")) {
//                concept_ids.add(e.getKey());
//            }
            if (e.getValue().equalsIgnoreCase(msg)) {
                concept_ids.add(e.getKey());
                concepts_.add(e.getValue());
            }
        }
//        System.out.println(concept_ids.toString());

        if (concept_ids.size() <= 0) {
            root.addProperty("message", "No Results");
        }else {
            root.addProperty("message", "Success");
            Set<String> result_set = new HashSet<>();
            for (Integer concept_id : concept_ids) {
                for(int synonym_id : ServletContextClass.Synonyms_.get(concept_id)){
                    result_set.add(ServletContextClass.ConceptID_.get(synonym_id));
                }
            }
//            System.out.println(result.toString());
            JsonArray concepts = new GsonBuilder().create().toJsonTree(concepts_).getAsJsonArray();
            root.add("concepts", concepts);
            JsonArray synonyms = new GsonBuilder().create().toJsonTree(result_set).getAsJsonArray();
            root.add("synonyms", synonyms);

            noOfSynonyms = result_set.size();
        }

        String json = new Gson().toJson(root);
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        log.info(msg + " : " + concept_ids.size() + " concepts & " + noOfSynonyms + " synonyms in " + tDelta + "ms");
        return Response.status(200).entity(json).build();

    }

}

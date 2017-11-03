import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

public class ServletContextClass implements ServletContextListener {
//    public static Map<String, List<String>> Data = new HashMap<String, List<String>>();
    public static Map<Integer, Integer[]> Synonyms_ ;
    public static Map<Integer, String> ConceptID_ ;

    public void contextInitialized(ServletContextEvent arg0)
    {
//        GetSyno GS = new GetSyno();
//        GS.doCash();
//
//        ConceptID_ = GS.getConceptID();
//        Synonyms_ = GS.getSynonyms();
    }//end contextInitialized method


    public void contextDestroyed(ServletContextEvent arg0)
    {

    }//end constextDestroyed method

}
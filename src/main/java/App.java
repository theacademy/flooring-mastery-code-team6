import controller.FlooringMasterController;
import dao.FlooringMasterDao;
import dao.FlooringMasterDaoImpl;
import dto.Product;
import service.FlooringMasterServiceLayer;
import service.FlooringMasterServiceLayeriImpl;
import ui.FlooringMasterView;
import ui.UserIO;
import ui.UserIOImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class App {

    public static void main(String[] args) throws FileNotFoundException {
        UserIO io = new UserIOImpl();
        FlooringMasterView view = new FlooringMasterView(io);
        FlooringMasterDao dao = new FlooringMasterDaoImpl();
        FlooringMasterServiceLayer service = new FlooringMasterServiceLayeriImpl(dao);
        FlooringMasterController fmc = new FlooringMasterController(service, view);
        fmc.run();

        /*
        FlooringMasterDaoImpl fm = new FlooringMasterDaoImpl();
        Map<String, Product> p = fm.getAllProducts();
        for(String type: p.keySet()) {
            System.out.println(p.get(type).getProductType());
        */


    }
}
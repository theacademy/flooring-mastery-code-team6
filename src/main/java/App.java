import controller.FlooringMasterController;
import dao.FlooringMasterDao;
import dao.FlooringMasterDaoImpl;
import service.FlooringMasterServiceLayer;
import service.FlooringMasterServiceLayeriImpl;
import ui.FlooringMasterView;
import ui.UserIO;
import ui.UserIOImpl;

import java.io.IOException;

public class App {

    public static void main(String[] args){
        UserIO io = new UserIOImpl();
        FlooringMasterView view = new FlooringMasterView(io);
        FlooringMasterDao dao = new FlooringMasterDaoImpl();
        FlooringMasterServiceLayer service = new FlooringMasterServiceLayeriImpl(dao, view);
        FlooringMasterController fmc = new FlooringMasterController(service, view);
        fmc.run();

    }
}
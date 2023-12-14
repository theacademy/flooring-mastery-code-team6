package service;

public class FlooringMasterServiceLayeriImpl implements FlooringMasterServiceLayer{
    OrderDAO OrderDao;
    OrderView OrderView;

    public FlooringMasterServiceLayeriImpl (OrderDAO OrderDao,OrderView OrderView){
        this.OrderDao= OrderDao;
        this. OrderView= OrderView;
    }

}

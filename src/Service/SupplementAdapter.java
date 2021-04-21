package Service;

import Domain.Bag;
import Domain.SupplementInterface;

public class SupplementAdapter {
    //TODO create field of external system?

    SupplementInterface supplementInterface;
    public SupplementAdapter(SupplementInterface supplementInterface) {
        this.supplementInterface = supplementInterface;

    }

    public boolean supply(Bag bag, String address) { //TODO fix args & implement
      return this.supplementInterface.supply(bag,address);
    }

}

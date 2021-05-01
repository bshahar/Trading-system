package Service;

import Domain.Bag;
import Domain.SupplementInterface;

public class SupplementAdapter {

    SupplementInterface supplementInterface;
    public SupplementAdapter(SupplementInterface supplementInterface) {
        this.supplementInterface = supplementInterface;

    }

    public boolean supply(Bag bag, String address) {
      return this.supplementInterface.supply(bag,address);
    }

}

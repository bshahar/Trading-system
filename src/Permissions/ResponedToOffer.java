package Permissions;

import Domain.Member;
import Domain.Result;
import Domain.Store;

public class ResponedToOffer {

    final private Member member;
    final private Store store;

    public ResponedToOffer(Member member, Store store) {
        this.store = store;
        this.member = member;
    }

    public Result action(int prodId, int offerId , String responed, double counterOffer, String option) {
            return this.store.responedToOffer(prodId, offerId, responed, counterOffer, option, this.member.getId());
    }
}

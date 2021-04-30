package Domain.DiscountPolicies;

import java.util.List;

public class PolicyCondition {

    private String policyName;
    private List<String> policyParams;

    public PolicyCondition(String policyName, List<String> policyParams) {
        this.policyName = policyName;
        this.policyParams = policyParams;
    }

    public String getPolicyName() { return this.policyName; }

    public List<String> getPolicyParams() { return this.policyParams; }


}

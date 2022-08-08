package com.zcdy.dsc.modules.settle.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.zcdy.dsc.modules.settle.entity.Contract;

/**
 * 合同到期事件
 * 
 * @author Roberto
 * @date 2020/05/26
 */
public class ContractExpireEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5856071365499418135L;

    private Contract contract;

    private List<Contract> contracts;

    /**
     * @param source
     */
    public ContractExpireEvent(Object source) {
        super(source);
    }

    public ContractExpireEvent(Object source, List<Contract> contracts) {
        this(source);
        this.contracts = contracts;
    }
    
    public ContractExpireEvent(Object source, Contract contract) {
        this(source);
        this.contract = contract;
    }

    /**
     * @return the contracts
     */
    public List<Contract> getContracts() {
        if(null==this.contracts){
            this.contracts = new ArrayList<Contract>(1);
        }
        if(null!=this.contract){
            this.contracts.add(contract);
        }
        return contracts;
    }

    /**
     * @param contracts
     *            the contracts to set
     */
    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    /**
     * @param contract
     *            the contract to set
     */
    public void setContract(Contract contract) {
        this.contract = contract;
    }
}

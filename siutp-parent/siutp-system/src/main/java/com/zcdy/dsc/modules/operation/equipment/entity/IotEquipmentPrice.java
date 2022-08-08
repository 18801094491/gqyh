 package com.zcdy.dsc.modules.operation.equipment.entity;

 /**
  * 客户合同水价组合信息
  * @author songguang.jiao
  * @date 2020/05/20 11:05:29
  */
 public class IotEquipmentPrice {
     private String price;
     
     private String iotId;
     
     private String optId;
     
     private String variableName;

     private String contractId;
     
     private String customerId;
     
     public String getPrice() {
         return price;
     }

     public void setPrice(String price) {
         this.price = price;
     }

     public String getIotId() {
         return iotId;
     }

     public void setIotId(String iotId) {
         this.iotId = iotId;
     }

     public String getOptId() {
         return optId;
     }

     public void setOptId(String optId) {
         this.optId = optId;
     }

     public String getVariableName() {
         return variableName;
     }

     public void setVariableName(String variableName) {
         this.variableName = variableName;
     }

     public String getContractId() {
         return contractId;
     }

     public void setContractId(String contractId) {
         this.contractId = contractId;
     }

     public String getCustomerId() {
         return customerId;
     }

     public void setCustomerId(String customerId) {
         this.customerId = customerId;
     }
}

package com.zcdy.dsc.waterwatch.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.kafka.producer.KafkaProducerBean;
import com.zcdy.dsc.waterwatch.entity.RealDataItem;
import com.zcdy.dsc.waterwatch.entity.RealDataParam;
import com.zcdy.dsc.waterwatch.entity.RealDataResult;
import com.zcdy.dsc.waterwatch.entity.VariableData;
import com.zcdy.dsc.waterwatch.inf.constant.FlagConstant;
import com.zcdy.dsc.waterwatch.util.CallApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: Roberto
 * @CreateTime:2020年3月12日 下午5:34:13
 * @Description: <p>定时采集水表信息并发送数据到kafka</p>
 */
//@Profile({"uat"})
@Slf4j
@Component
public class CollectionJob {

	@Value("${com.zcdy.dsc.meter.api.url}")
	private String apiUrl;

	@Resource
	private KafkaProducerBean kafkaProducerBean;

	@Value("${kafka.consumer.topic}")
	private String kafkaTopic;

	@Value("${com.zcdy.dsc.kafka.key}")
	private String kafkaKey;

	@Value("${com.zcdy.dsc.meter.company}")
	private String orgName;

	@Scheduled(cron = "${com.zcdy.dsc.meter.fetch.cron}")
	public void execute() throws Exception {
		String REAL_DATA_URL = apiUrl + "api/RealData/Post";
		// 接口含义：根据大运河名字获取所有设备实时数据
		RealDataParam par = new RealDataParam();
		par.setOrg(orgName);
		log.info(REAL_DATA_URL+orgName);

		int page = 0;

		boolean hasNext = true;

		while (hasNext) {
			page++;
			par.setPage(page + "");
			String jsonParam = JSONObject.toJSONString(par);
			String result = CallApi.sendPost(REAL_DATA_URL, jsonParam);
			if(log.isDebugEnabled()) {
			    log.debug("User meter request for the data, result is ->" + result);
			}

			long timestamp = System.currentTimeMillis();

			List<VariableData> variableList = new ArrayList<>();

			// 获取返回参数集合
			RealDataResult realDataResult = JSON.parseObject(result, RealDataResult.class);
			if(null==realDataResult) {
			    if(log.isDebugEnabled()) {
			        log.debug("The request has not get any data……");
			    }
			    continue;
			}			
			
			Integer flagInteger = realDataResult.getFlag();
			
			if (null!=flagInteger && flagInteger.intValue() == FlagConstant.FLAG_SUCCESS.intValue()) {
				List<RealDataItem> data = realDataResult.getData();
				hasNext = (null != data && data.size() > 0);
				if (hasNext) {
					data.forEach(item -> {
						String addressCode = item.getAddressCode();
						// kafka数据准备
						VariableData variableData = VariableData.template.clone();
						variableData.setVarId(0);
						variableData.setVarName("JY_ALM_" + addressCode);
						variableData.setQualityStamp(192);
						variableData.setCreated(timestamp);
						variableData.setTimestamp(timestamp);
						variableData.setVarValue(item.getWarnStatus());
						variableList.add(variableData);

						VariableData bv = VariableData.template.clone();
						bv.setVarId(0);
						bv.setVarName("JY_BV_" + addressCode);
						bv.setQualityStamp(192);
						bv.setCreated(timestamp);
						bv.setTimestamp(timestamp);
						bv.setVarValue(item.getCelVal());
						variableList.add(bv);

						VariableData flow = VariableData.template.clone();
						flow.setVarId(0);
						flow.setVarName("JY_F_" + addressCode);
						flow.setQualityStamp(192);
						flow.setCreated(timestamp);
						flow.setTimestamp(timestamp);
						flow.setVarValue(item.getRealValue());
						variableList.add(flow);

						VariableData pft = VariableData.template.clone();
						pft.setVarId(0);
						pft.setVarName("JY_PFT_" + addressCode);
						pft.setQualityStamp(192);
						pft.setCreated(timestamp);
						pft.setTimestamp(timestamp);
						pft.setVarValue(item.getForValue());
						variableList.add(pft);

						VariableData nft = VariableData.template.clone();
						nft.setVarId(0);
						nft.setVarName("JY_NFT_" + addressCode);
						nft.setQualityStamp(192);
						nft.setCreated(timestamp);
						nft.setTimestamp(timestamp);
						String nftStr = "0";
						if(!StringUtils.isEmpty(item.getRevValue())){
							BigDecimal revBg = new BigDecimal(item.getRevValue());
							if(revBg.compareTo(BigDecimal.ZERO)>0){
								nftStr = "-" + revBg.toString();
							}else{
								nftStr = revBg.toString();
							}
						}
						nft.setVarValue(nftStr);
						variableList.add(nft);

						// 发送消息到kafka
						if (variableList.size() == 20) {
							String messageContent = JSON.toJSONString(variableList);
							this.kafkaSend(messageContent);
						}
					});
					// 发送消息到kafka
					if (variableList.size() > 0) {
						String messageContent = JSON.toJSONString(variableList);
						this.kafkaSend(messageContent);
					}
				}
			} else {
				log.warn("请求设备实时数据信息失败……");
				break;
			}
		}
	}

	/**
	 * @author:Roberto
	 * @create:2019年12月31日 下午5:26:19
	 * @Description:<p>发送kafka消息</p>
	 */
	private void kafkaSend(String messageContent) {
	    try {
	        kafkaProducerBean.send(kafkaTopic, kafkaKey, messageContent);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
	}
}

package com.zcdy.dsc.modules.centre.vo;

import com.zcdy.dsc.modules.centre.constant.CentreTypeEnum;
import lombok.Data;

@Data
public class CentreVo {
    private String text;
    private String value;

    public CentreVo()
    {

    }

    public CentreVo(CentreTypeEnum centreTypeEnum)
    {
        this.text = centreTypeEnum.getText();
        this.value = centreTypeEnum.getValue();
    }
}

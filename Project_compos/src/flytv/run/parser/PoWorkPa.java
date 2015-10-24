package flytv.run.parser;

import org.json.JSONException;

import com.alibaba.fastjson.JSON;

import flytv.ext.network.BaseParser;
import flytv.run.bean.PoetryBean;

public class PoWorkPa extends BaseParser<PoetryBean> {

	@Override
	public PoetryBean parseJSON(String paramString) throws JSONException {
		// TODO Auto-generated method stub
		PoetryBean itemList;
		try {
			itemList = JSON.parseObject(paramString, PoetryBean.class);
			return itemList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}

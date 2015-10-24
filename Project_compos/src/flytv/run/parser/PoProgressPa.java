package flytv.run.parser;

import org.json.JSONException;

import com.alibaba.fastjson.JSON;

import flytv.ext.network.BaseParser;
import flytv.run.bean.PoProgress;

public class PoProgressPa extends BaseParser<PoProgress> {

	@Override
	public PoProgress parseJSON(String paramString) throws JSONException {
		// TODO Auto-generated method stub
		PoProgress itemList;
		try {
			itemList = JSON.parseObject(paramString, PoProgress.class);
			return itemList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}

}

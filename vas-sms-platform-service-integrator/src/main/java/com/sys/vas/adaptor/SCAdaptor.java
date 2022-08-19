/**
 * @author : amila hettiarachchi
 * @email  : amila.hettiarachchi@axiatadigitallabs.com
 * @date   : Jan 15, 2019
 */
package com.sys.vas.adaptor;

import org.json.simple.JSONObject;

public interface SCAdaptor {
    /*
	public JSONObject post(JSONObject params);
	public JSONObject put(JSONObject params);
	public JSONObject get(JSONObject params);
	public JSONObject delete(JSONObject params);
	public JSONObject dummy(JSONObject params);
	*/
	public JSONObject invokeFunction(String functionName, JSONObject params);
}

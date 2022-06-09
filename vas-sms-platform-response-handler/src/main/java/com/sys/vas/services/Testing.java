package com.sys.vas.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Testing {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		 try {
		String jsonip = "{\"rs\" : \"123\",\"data\":[{\"a\":\"A1\", \"b\":\"B1\"},{\"a\":\"A2\", \"b\":\"B2\"}]}";
		JSONParser parser = new JSONParser();
		JSONObject job = (JSONObject) parser.parse(jsonip);
		JSONArray ja = (JSONArray) job.get("data");
		job.remove("data");
		for (int i = 0; i < ja.size(); i++) {
			job.put("data", (JSONObject) ja.get(i));
		    System.out.println("OUT"+i+":"+job.toJSONString());
		}
		
		String ipjson = "{ \"balanceRecordList\": [ { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190503122538\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190503122538\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_997_7_14\", \"initialQuota\": 33071248180, \"quotaExpiryDate\": \"20190503121755\", \"remainingQuota\": 33071248180, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_997_7_14\", \"initialQuota\": 16535624090, \"quotaExpiryDate\": \"20190503121755\", \"remainingQuota\": 16535624090, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_397_2_4\", \"initialQuota\": 9448928052, \"quotaExpiryDate\": \"20190503121722\", \"remainingQuota\": 9448928052, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_397_2_4\", \"initialQuota\": 4724464026, \"quotaExpiryDate\": \"20190503121722\", \"remainingQuota\": 4724464026, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_197_1_2\", \"initialQuota\": 4724464026, \"quotaExpiryDate\": \"20190503121635\", \"remainingQuota\": 4724464026, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_197_1_2\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190503121635\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190503121508\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190503121508\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190503121445\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190503121445\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190503121205\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190503121205\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190503121148\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190503121148\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190503114923\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190503114923\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190501152205\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190501152205\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190501151957\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190501151957\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 }, { \"quotaType\": \"N\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 2362232014, \"quotaExpiryDate\": \"20190501131852\", \"remainingQuota\": 2362232014, \"dataUsage\": 0 }, { \"quotaType\": \"D\", \"sachetPackageName\": \"LTE_Bundle_97_0.5_1\", \"initialQuota\": 1181116008, \"quotaExpiryDate\": \"20190501131852\", \"remainingQuota\": 1181116008, \"dataUsage\": 0 } ] }";
		job = (JSONObject) parser.parse(ipjson);
		ja = (JSONArray) job.get("balanceRecordList");
		for (int i = 0; i < ja.size(); i++) {
			job.put("data", (JSONObject) ja.get(i));
		    System.out.println("OUT"+i+":"+job.toJSONString());
		}
		 }catch (Exception e){}
	}

}

package com.nr.instrumentation.jgroups;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.jgroups.Header;
import org.jgroups.util.Util;

import com.newrelic.api.agent.NewRelic;

public class NRHeader extends Header {
	
	private Map<String, String> map = new HashMap<String, String>();
	
	public void addKeyValue(String key, String value) {
		map.put(key, value);
	}
	
	public String getValue(String key) {
		String value = map.get(key);
		return value;
	}
	
	@Override
	public int size() {
		try {
			byte[] byteArray = Util.objectToByteBuffer(this);
			return byteArray.length;
		} catch (Exception e) {
			NewRelic.getAgent().getLogger().log(Level.FINE, e,"error getting size of NRHeader");
		}
		return -1;
	}

	@Override
	public void writeTo(DataOutput out) throws Exception {
		Util.writeObject(map, out);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readFrom(DataInput in) throws Exception {
		Object obj = Util.readObject(in);
		
		if(obj instanceof Map) {
			Map<Object,Object> tmp = (Map<Object, Object>)obj;
			Set<Object> keys = (Set<Object>) tmp.keySet();
			map = new HashMap<String, String>();
			for(Object key :  keys) {
				Object value =  tmp.get(key);
				map.put(key.toString(), value.toString());
			}
		}
	
	}

	
	@Override
	public String toString() {
		String tmp = super.toString();
		
		tmp += " map=" + map.toString();
		// TODO Auto-generated method stub
		return tmp;
	}

    
    
}

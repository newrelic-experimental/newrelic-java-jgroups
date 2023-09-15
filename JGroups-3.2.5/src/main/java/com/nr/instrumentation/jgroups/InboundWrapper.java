package com.nr.instrumentation.jgroups;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.InboundHeaders;

public class InboundWrapper implements InboundHeaders {
	
	private NRHeader header = null;
	
	public InboundWrapper(NRHeader h) {
		header = h;
	}

	@Override
	public String getHeader(String key) {
		return header.getValue(key);
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

}

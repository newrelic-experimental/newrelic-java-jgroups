package com.nr.instrumentation.jgroups;

import com.newrelic.api.agent.HeaderType;
import com.newrelic.api.agent.OutboundHeaders;

public class OutboundWrapper implements OutboundHeaders {
	
	private NRHeader header = null;
	
	public OutboundWrapper(NRHeader h) {
		header = h;
	}

	@Override
	public HeaderType getHeaderType() {
		return HeaderType.MESSAGE;
	}

	@Override
	public void setHeader(String key, String value) {
		header.addKeyValue(key, value);
	}

}

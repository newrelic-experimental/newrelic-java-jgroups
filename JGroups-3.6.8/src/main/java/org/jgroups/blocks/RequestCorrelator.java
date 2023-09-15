package org.jgroups.blocks;

import java.util.Collection;

import org.jgroups.Address;
import org.jgroups.Event;
import org.jgroups.Message;
import org.jgroups.View;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.BaseClass)
public abstract class RequestCorrelator {

	@Trace(dispatcher=true)
	protected void handleRequest(Message req, Header hdr) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RequestCorrelator","handleRequest"});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	protected void prepareResponse(Message rsp) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RequestCorrelator","prepareResponse"});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public boolean receive(Event evt) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RequestCorrelator","receive",Event.type2String(evt.getType())});
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public boolean receiveMessage(Message msg) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RequestCorrelator","receiveMessage"});
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public void receiveView(View new_view) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RequestCorrelator","receiveView"});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public void sendRequest(Collection<Address> dest_mbrs, Message msg, Request req, RequestOptions options) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RequestCorrelator","sendRequest"});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public void sendUnicastRequest(Address target, Message msg, Request req) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RequestCorrelator","sendUnicastRequest"});
		Weaver.callOriginal();
	}
	
	
	@Weave
	public static abstract class Header extends org.jgroups.Header {
		
	}
}

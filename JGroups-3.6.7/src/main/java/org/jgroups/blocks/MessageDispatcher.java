package org.jgroups.blocks;

import org.jgroups.Channel;
import org.jgroups.Event;
import org.jgroups.Message;
import org.jgroups.util.NotifyingFuture;

import com.newrelic.api.agent.DestinationType;
import com.newrelic.api.agent.MessageProduceParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.jgroups.NRHeader;
import com.nr.instrumentation.jgroups.OutboundWrapper;
import com.nr.instrumentation.jgroups.Utils;

@Weave(type=MatchType.BaseClass)
public abstract class MessageDispatcher {

	
	protected RequestHandler req_handler = Weaver.callOriginal();
	
	public abstract Channel getChannel();
	
	@Trace(dispatcher=true)
	public Object handle(Message msg) {
		
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public <T> T sendMessage(Message msg, RequestOptions opts)  {
		Channel channel = getChannel();
		String destName = Utils.getDestName(channel);
		if(Utils.isNRHeaderNeeded(msg)) {
			NRHeader nrHeader = new NRHeader();
			OutboundWrapper wrapper = new OutboundWrapper(nrHeader);
			NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(wrapper);
			MessageProduceParameters params = MessageProduceParameters.library("JGroups").destinationType(DestinationType.EXCHANGE).destinationName(destName).outboundHeaders(wrapper).build();
			NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
			msg.putHeaderIfAbsent(Utils.NRID, nrHeader);
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","MessageDispatcher",getClass().getSimpleName(),"sendMessage",destName});
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public <T> NotifyingFuture<T> sendMessageWithFuture(Message msg, RequestOptions options) {
		Channel channel = getChannel();
		String destName = Utils.getDestName(channel);
		if(Utils.isNRHeaderNeeded(msg)) {
			NRHeader nrHeader = new NRHeader();
			OutboundWrapper wrapper = new OutboundWrapper(nrHeader);
			NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(wrapper);
			MessageProduceParameters params = MessageProduceParameters.library("JGroups").destinationType(DestinationType.EXCHANGE).destinationName(destName).outboundHeaders(wrapper).build();
			NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
			msg.putHeaderIfAbsent(Utils.NRID, nrHeader);
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","MessageDispatcher",getClass().getSimpleName(),"sendMessageWithFuture",destName});
		return Weaver.callOriginal();
	}
	
	@Weave
	static class ProtocolAdapter {
		
		@Trace(dispatcher=true)
		public Object up(Event evt) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","ProtocolAdapter","up",Event.type2String(evt.getType())});
			return Weaver.callOriginal();
		}
		
		@Trace(dispatcher=true)
		public Object down(Event evt) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","ProtocolAdapter","down",Event.type2String(evt.getType())});
			return Weaver.callOriginal();
		}
		
	}
		
}

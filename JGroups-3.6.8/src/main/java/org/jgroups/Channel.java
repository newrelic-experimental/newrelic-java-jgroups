package org.jgroups;

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
public abstract class Channel {

	protected Receiver receiver = Weaver.callOriginal();
	
	public abstract String getClusterName();
	public abstract String getName();

	public void setReceiver(Receiver r) {
		if(r instanceof ReceiverAdapter) {
			ReceiverAdapter ra = (ReceiverAdapter)r;
			if(ra.clusterName == null) {
				ra.clusterName = getClusterName();
			}
		}
		
		Weaver.callOriginal();
	}
	
	
	@Trace(dispatcher=true)
	public Object down(Event evt) {
		if(!Utils.initialized) {
			Utils.init();
		}
		String destName = Utils.getDestName(this);
		if(evt.getType() == Event.MSG) {
			Message msg = (Message)evt.getArg();
			if(Utils.isNRHeaderNeeded(msg)) {
				NRHeader nrHeader = new NRHeader();
				OutboundWrapper wrapper = new OutboundWrapper(nrHeader);
				NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(wrapper);
				MessageProduceParameters params = MessageProduceParameters.library("JGroups").destinationType(DestinationType.EXCHANGE).destinationName(destName).outboundHeaders(wrapper).build();
				NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
				msg.putHeaderIfAbsent(Utils.NRID, nrHeader);
			}
			
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","JChannel","send","Message",destName});
			
		} else {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","Channel","down","Event", Event.type2String(evt.getType()),destName});
		}
		return Weaver.callOriginal();
	}
	
}

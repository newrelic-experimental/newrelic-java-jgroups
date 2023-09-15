package org.jgroups;

import java.util.logging.Level;

import com.newrelic.api.agent.DestinationType;
import com.newrelic.api.agent.MessageConsumeParameters;
import com.newrelic.api.agent.MessageProduceParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TransactionNamePriority;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.jgroups.InboundWrapper;
import com.nr.instrumentation.jgroups.NRHeader;
import com.nr.instrumentation.jgroups.OutboundWrapper;
import com.nr.instrumentation.jgroups.Utils;

@Weave(type=MatchType.BaseClass)
public abstract class JChannel extends Channel {
	
	
	@Trace
	protected Object invokeCallback(int type, Object arg) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","JChannel","invokeCallback",Event.type2String(type));
		return Weaver.callOriginal();
	}

	@Trace(leaf=true)
	public void send(Message msg) {
		String destName = Utils.getDestName(this);
		if(Utils.isNRHeaderNeeded(msg)) {
			NRHeader nrHeader = new NRHeader();
			OutboundWrapper wrapper = new OutboundWrapper(nrHeader);
			NewRelic.getAgent().getTracedMethod().addOutboundRequestHeaders(wrapper);
			MessageProduceParameters params = MessageProduceParameters.library("JGroups").destinationType(DestinationType.EXCHANGE).destinationName(destName).outboundHeaders(wrapper).build();
			NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
			Header hdr = msg.putHeaderIfAbsent(Utils.NRID, nrHeader);
			NewRelic.getAgent().getLogger().log(Level.FINE, "Result of putHeaderIfAbsent {0}", hdr);
		}
		
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","JChannel","send","Message",destName});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public Object up(Event evt) {
		if(!Utils.initialized) {
			Utils.init();
		}
		if(evt.getType() == Event.MSG) {
			Message msg = (Message)evt.getArg();
			if(receiver != null && receiver instanceof ReceiverAdapter) {
				ReceiverAdapter ra = (ReceiverAdapter)receiver;
				if(ra.clusterName == null) {
					ra.clusterName = getClusterName();
				}
			}
			
			String destName = Utils.getDestName(this);
			Header header = msg.getHeader(Utils.NRID);
			if(header != null && header instanceof NRHeader) {
				NRHeader nrHeader = (NRHeader)header;
				InboundWrapper wrapper = new InboundWrapper(nrHeader);
				MessageConsumeParameters params = MessageConsumeParameters.library("JGroups").destinationType(DestinationType.EXCHANGE).destinationName(destName).inboundHeaders(wrapper).build();
				NewRelic.getAgent().getTracedMethod().reportAsExternal(params);
//				// set header to processed to avoid repeated processing
//				msg.putHeader(Utils.NRID, new EmptyHeader());
			}
			
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","JChannel","up","Message", destName});
			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "JGroups", new String[] {"JGroups","JChannel","Message",destName});
			
		} else {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","JChannel","up","Event", Event.type2String(evt.getType())});
			NewRelic.getAgent().getTransaction().setTransactionName(TransactionNamePriority.FRAMEWORK_LOW, false, "JGroups", new String[] {"JGroups","JChannel","Event",Utils.getDestName(this),Event.type2String(evt.getType())});
		}
		return Weaver.callOriginal();
	}
	
}

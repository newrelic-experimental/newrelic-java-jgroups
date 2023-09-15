package org.jgroups;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.jgroups.Utils;

@Weave(type=MatchType.BaseClass)
public abstract class ReceiverAdapter implements Receiver {
	
	@NewField
	public String clusterName = null;

	@Trace(dispatcher=true)
	public void receive(Message msg) {
		if(!Utils.initialized) {
			Utils.init();
		}
		String destName = clusterName != null ? clusterName : "Unknown";
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","MessageListener",getClass().getSimpleName(),"receive",destName});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public void viewAccepted(View new_view) {
		if(!Utils.initialized) {
			Utils.init();
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","MembershipListener",getClass().getSimpleName(),"viewAccepted"});
		Weaver.callOriginal();
	}


}

package org.jgroups.blocks;

import org.jgroups.Address;
import org.jgroups.View;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.Interface)
public abstract class RspCollector {

	@Trace(dispatcher=true)
    public void receiveResponse(Object response_value, Address sender, boolean is_exception) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RspCollector",getClass().getSimpleName(),"receiveResponse"});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
    public void suspect(Address mbr) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RspCollector",getClass().getSimpleName(),"suspect"});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
    public void viewChange(View new_view) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RspCollector",getClass().getSimpleName(),"viewChange"});
		Weaver.callOriginal();
	}

}

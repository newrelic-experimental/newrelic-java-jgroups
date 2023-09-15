package org.jgroups.blocks;

import org.jgroups.Message;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.Interface)
public abstract class RequestHandler {

	@Trace(dispatcher=true)
	public Object handle(Message msg) {
		NewRelic.getAgent().getTracedMethod().setMetricName("Custom","RequestHandler",getClass().getSimpleName(),"handle");
		return Weaver.callOriginal();
	}
}

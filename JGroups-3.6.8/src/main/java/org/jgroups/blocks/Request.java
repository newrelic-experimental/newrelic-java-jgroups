package org.jgroups.blocks;

import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.View;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.NewField;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave(type=MatchType.BaseClass)
public abstract class Request {
	
	@NewField
	private Token token = null;
	
	public Request(RequestCorrelator corr, RequestOptions options) {
		
	}

	@Trace(dispatcher=true)
	public boolean execute(Message req, boolean block_for_results) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","Request",getClass().getSimpleName(),"execute"});
		return Weaver.callOriginal();
	}
	
	@Trace(async=true)
	public boolean cancel(boolean mayInterruptIfRunning) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","Request",getClass().getSimpleName(),"cancel"});
		return Weaver.callOriginal();
	}
	
	@Trace(async=true)
	public void receiveResponse(Object response_value, Address sender, boolean is_exception) {
		if(token != null) {
			token.linkAndExpire();
			token = null;
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","Request",getClass().getSimpleName(),"receiveResponse"});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	protected void sendRequest(Message requestMsg) {
		if(token == null) {
			token = NewRelic.getAgent().getTransaction().getToken();
		}
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","Request",getClass().getSimpleName(),"sendRequest"});
		Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public void viewChange(View new_view) {
		NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","Request",getClass().getSimpleName(),"sendRequest"});
		Weaver.callOriginal();
	}
}

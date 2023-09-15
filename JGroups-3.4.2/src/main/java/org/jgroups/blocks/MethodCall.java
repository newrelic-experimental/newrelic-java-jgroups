package org.jgroups.blocks;

import java.lang.reflect.Method;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class MethodCall {

	public abstract String getName();
	public abstract Method getMethod();

	@Trace(dispatcher=true)
	public Object invoke(Object target) {
		String name = getName();
		if(name == null) {
			Method m = getMethod();
			if(m != null) {
				name = m.getName();
			}
		}
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","MethodCall","invoke",name});
		}
		return Weaver.callOriginal();
	}
}

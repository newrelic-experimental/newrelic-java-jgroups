package org.jgroups.blocks;

import java.lang.reflect.Method;
import java.util.Collection;

import org.jgroups.Address;
import org.jgroups.util.NotifyingFuture;
import org.jgroups.util.RspList;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class RpcDispatcher {

	@Trace(dispatcher=true)
	public <T> T callRemoteMethod(Address dest, MethodCall call, RequestOptions options) {
		String name = getName(call);
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RpcDispatcher","callRemoteMethod","SingleAddress",name});
		}
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public <T> RspList<T> callRemoteMethods(Collection<Address> dests,MethodCall method_call,RequestOptions options) {
		String name = getName(method_call);
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RpcDispatcher","callRemoteMethod","MultipleAddresses",name});
		}
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public <T> NotifyingFuture<RspList<T>> callRemoteMethodsWithFuture(Collection<Address> dests,MethodCall method_call,RequestOptions options) {
		String name = getName(method_call);
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RpcDispatcher","callRemoteMethodWithFuture","MultipleAddresses",name});
		}
		
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public <T> NotifyingFuture<T> callRemoteMethodWithFuture(Address dest, MethodCall call, RequestOptions options) {
		String name = getName(call);
		if(name != null && !name.isEmpty()) {
			NewRelic.getAgent().getTracedMethod().setMetricName(new String[] {"Custom","RpcDispatcher","callRemoteMethodWithFuture","SingleAddress",name});
		}
		return Weaver.callOriginal();
	}
	
	private String getName(MethodCall call) {
		String name = call.getName();
		if(name == null) {
			Method m = call.getMethod();
			if(m != null) {
				name = m.getName();
			}
		}
		return name;
	}
}

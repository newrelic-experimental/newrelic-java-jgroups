package org.jgroups.blocks.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jgroups.util.NotifyingFuture;

import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;

@Weave
public abstract class ExecutionCompletionService<V> {

	@Trace(dispatcher=true)
	public NotifyingFuture<V> take() {
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public NotifyingFuture<V> poll() {
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public NotifyingFuture<V> poll(long timeout, TimeUnit unit) {
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public Future<V> submit(Runnable task, V result) {
		return Weaver.callOriginal();
	}
	
	@Trace(dispatcher=true)
	public Future<V> submit(Callable<V> task) {
		return Weaver.callOriginal();
	}
}

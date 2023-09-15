package com.nr.instrumentation.jgroups;

import java.util.concurrent.Future;

import org.jgroups.util.FutureListener;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;

public class NRFutureListener<T> implements FutureListener<T> {
	
	private Segment segment = null;
	
	private FutureListener<T> delegate = null;
	
	public FutureListener<T> getDelegate() {
		return delegate;
	}

	public void setDelegate(FutureListener<T> delegate) {
		this.delegate = delegate;
	}

	public  NRFutureListener(String name) {
		segment = NewRelic.getAgent().getTransaction().startSegment(name);
	}

	@Override
	public void futureDone(Future<T> future) {
		if(segment != null) {
			segment.end();
			segment = null;
		}
		if(delegate != null) {
			delegate.futureDone(future);
		}

	}

}

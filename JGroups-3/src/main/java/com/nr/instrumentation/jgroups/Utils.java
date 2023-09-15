package com.nr.instrumentation.jgroups;

import java.util.logging.Level;

import org.jgroups.Channel;
import org.jgroups.Header;
import org.jgroups.Message;
import org.jgroups.conf.ClassConfigurator;

import com.newrelic.agent.config.AgentConfig;
import com.newrelic.agent.config.AgentConfigListener;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.api.agent.Config;
import com.newrelic.api.agent.NewRelic;

public class Utils implements AgentConfigListener {
	
	public static short NRID = 2002;
	private static final String CUSTOMHEADERID = "jgroups.header.id";
	private static final String CUSTOMHEADERENABLED = "jgroups.header.enabled";
	
	public static boolean enabled = true;
	
	public static boolean initialized = false;
	
	private static Utils instance = null;
	
	static {
		init();
	}

	public static void init() {
		initialized = true;
		Config config = NewRelic.getAgent().getConfig();
		Boolean enabledValue = config.getValue(CUSTOMHEADERENABLED);
		if(enabledValue != null) {
			enabled = enabledValue;
		}
		NewRelic.getAgent().getLogger().log(Level.FINE, "set value of send header to {0}", enabled);
		if(enabled) {
			Number s = config.getValue(CUSTOMHEADERID);
			if(s != null && s.shortValue() != NRID) {
				NRID = s.shortValue();
			}
			NewRelic.getAgent().getLogger().log(Level.FINE, "set value of send header id to {0}", NRID);
			ClassConfigurator.add(NRID, NRHeader.class);
		}
		instance = new Utils();
		ServiceFactory.getConfigService().addIAgentConfigListener(instance);
	}
	
	public static boolean isNRHeaderNeeded(Message msg) {
		if(!enabled) return false;
		Header header = msg.getHeader(NRID);
		// if header is null then header is needed otherwise no
		return header == null;
	}
	
	public static String getDestName(Channel channel) {
		StringBuffer sb = new StringBuffer();
		
		String cluster = channel != null ? channel.getClusterName() : null;
		if(cluster != null && !cluster.isEmpty()) {
			sb.append(cluster);
		}
		return sb.length() > 0 ? sb.toString() : "Unknown";

	}

	@Override
	public void configChanged(String appName, AgentConfig config) {
		NewRelic.getAgent().getLogger().log(Level.FINE, "Utils: processing configuration changes");
		Boolean enabledValue = config.getValue(CUSTOMHEADERENABLED);
		if(enabledValue != null) {
			enabled = enabledValue;
			NewRelic.getAgent().getLogger().log(Level.FINE, "set value of send header to {0}", enabled);
			if(enabled) {
				Number id = config.getValue(CUSTOMHEADERID,new Short((short) 2002));
				if(id != null && id.shortValue() != NRID) {
					NRID = id.shortValue();
				}
				NewRelic.getAgent().getLogger().log(Level.FINE, "set value of send header id to {0}", NRID);
				if(ClassConfigurator.get(NRID) == null) {
					ClassConfigurator.add(NRID, NRHeader.class);
				}
			}
		}
	}
}

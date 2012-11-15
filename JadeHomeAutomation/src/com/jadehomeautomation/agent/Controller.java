package com.jadehomeautomation.agent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.StringTokenizer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

public class Controller extends Agent {
	/*
	 * The list of discovered seller agents.
	 */
	private AID[] agents;
	
	@Override
	protected void setup() {
		
		log("I'm started.");
		
		addBehaviour(new TickerBehaviour(this, 5000) {
			
			@Override
			protected void onTick() {
				
				log("Trying to switch bulb ...");
				
				/*
				 * 1- Create the agent description template.
				 */
				DFAgentDescription template = new DFAgentDescription();
				/*
				 * 2- Create the service description template.
				 */
				ServiceDescription sd = new ServiceDescription();
				/*
				 * 3- Fill its fields you look for.
				 */
				sd.setType("bulb-control");
				/*
				 * 4- Add the service template to the agent template.
				 */
				template.addServices(sd);
				/*
				 * 5- Setup your preferred search constraints.
				 */
				SearchConstraints all = new SearchConstraints();
				all.setMaxResults(new Long(-1));
				DFAgentDescription[] result = null;
				try {
					/*
					 * 6- Query the DF about the service you look for.
					 */
					log("Searching '"+sd.getType()+"' service in the default DF...");
					
					result = DFService.search(myAgent, template, all);
					agents = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						/*
						 * 7- Collect found service providers' AIDs.
						 */
						agents[i] = result[i].getName();
						log("Agent '"+agents[i].getName()+"' found.");
					}
				} catch (FIPAException fe) {
					fe.printStackTrace();
				}

			}
			
		} );
	}

	@Override
	protected void takeDown() {
		log("I'm done.");
	}
	
	private void log(String msg) {
		System.out.println("["+getName()+"]: "+msg);
	}
}
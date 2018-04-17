/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.quantda.example.osgi.auction.seller.simple;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import vn.quantda.example.osgi.auction.api.Auction;

public class SellerActivator implements BundleActivator, /* Cohesion-free impl. */ ServiceListener {

	private BundleContext bundleContext;
	private Seller seller = new Seller("Seller 1");
	
    @SuppressWarnings("rawtypes")
	public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Starting the bundle");
        this.bundleContext = bundleContext;
        
        String filter = 
        		"(&(objectClass="
        		+ Auction.class.getName()
        		+ ")(" + Auction.TYPE
        		+"=Sealed-First-Price))";
        /*
         * A method on BundleContext may introduce method ambiguity.

		 * BundleContext.getServiceReferences(Class<S>, String)

		 * This new method will introduce ambiguity with the old 
		 * method BundleContext.getServiceReferences(String, String) 
		 * if you are using a null value for the first parameter. The fix is to 
		 * add a cast to (String) for the first argument (e.g. bc.getServiceReferences((String) null, filter))
 		 *
         */
        ServiceReference[] serviceReferences = bundleContext.getServiceReferences((String) null, filter);
        
        if (serviceReferences != null) {
        	ask(serviceReferences[0]);
        } else {
			/* Cohesion-free impl. */
			bundleContext.addServiceListener(this, filter);
		}
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void ask(ServiceReference serviceReference) {
		Auction auction = (Auction) bundleContext.getService(serviceReference);
		
		if (auction != null) {
			seller.ask(auction);
			bundleContext.ungetService(serviceReference);
		} 
		
	}

	public void stop(BundleContext bundleContext) {
		bundleContext.removeServiceListener(this);
    }

	@Override
	public void serviceChanged(ServiceEvent serviceEvent) {
		/* Cohesion-free impl. */
		switch(serviceEvent.getType()) {
			case ServiceEvent.REGISTERED: {
				ask(serviceEvent.getServiceReference());
				break;
			}
			default:
				// do nothing
		}
		
	}

}
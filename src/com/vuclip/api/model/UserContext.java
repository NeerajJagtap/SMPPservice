/**
 * 
 */
package com.vuclip.api.model;

/**
 * @author 
 *
 */
public class UserContext {

	/*private UserSubscription currentUserSubscription = new UserSubscription();
	private UserSubscription finalUserSubscription = new UserSubscription();
	private BillingPackage currentBillingPackage = new BillingPackage();
	private BillingPackage billingPackage = new BillingPackage();
	private ProviderInfo providerInfo = new ProviderInfo();
	private Customer customer = new Customer();
	private String transectionId;
	private PersistenceHolder persistenceHolder = new PersistenceHolder();
	private Object logHolder;
	private String accessMode;
	private String sessionId;
	private Map<Integer, BillingPackage> allBillingPackages;
	private List<EventType> appevents = AppEventProcessor.getDefaultEvents();
	private ActivityContext activityContext = new ActivityContext();
	private Map<EventType,EventResponse> logEventData = new HashMap<EventType,EventResponse>();
	private UserStateEnum overridedUserState;*/

	private String userId;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}

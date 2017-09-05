/**
 * Copyright 2010, The Sherwin-Williams Company - All rights reserved
 *
 * 
 *-------------------------------------------------------------------------------------
 *
 **/
package com.sherwin.polling;


public class PollingClient {

	/*private Environment env;
	private Prerequisite prerequisite;

	private String applicationId;
	private PollingDestMetadata destMetaData;
	private List<PollingFileMetadata> fileMetaData;

	public PollingClient() {
	}

	public PollingClient(String env, String applicationId) {
		this.applicationId = applicationId;
		this.fileMetaData = new ArrayList<PollingFileMetadata>();
		if (env != null && env.equalsIgnoreCase("NT")) {
			setEnv("dev");
		} else {
			setEnv(env);
		}
		if (applicationId != null && applicationId.equalsIgnoreCase("PhysicalInventory")) {
			setPrerequisite(RestAdapter.Prerequisite.NONE);
		}
	}
	
	 * Writes a files to one or multiple stores
	 

	public void write(File file) throws Exception {
		// generate MD5 checksum
		String checksum = null;
		try (InputStream in = new FileInputStream(file)) {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.reset();
			byte[] buffer = new byte[2048];
			for (int sz = in.read(buffer); sz != -1; sz = in.read(buffer)) {
				digest.update(buffer, 0, sz);
			}
			checksum = Hex.encodeHexString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO
			e.printStackTrace();
			return;
		}

		this.write(file, checksum);
	}

	
	 * Writes a files to one or multiple stores with the check sum. It adds the
	 * binary file and sets the stores .
	 * 
	 
	public void write(File f, String checksum) throws Exception {
		byte[] fileBytes = null;
		try (FileReader read = new FileReader(f);) {
			fileBytes = IOUtils.toByteArray(read);
		}
		PollingFileMetadata fmd = new PollingFileMetadata(fileBytes, checksum, "SHA-256", f.getName(), null);
		fileMetaData.add(fmd);
	}

	public void postToStores(List<String> stores, String destType) throws Exception {
		setStores(stores, destType);
	}

	
	 * Posts Maintenance to polling system.
	 * 
	 
	public String postMaintenance() throws Exception {
		PollingFileMetadata[] pfmd = fileMetaData.toArray(new PollingFileMetadata[fileMetaData.size()]);
		String requestId =null;
		try {
			requestId = RestAdapter.writeFileToPolling(getEnv(), getApplicationId(), getPrerequisite(),
					getDestMetaData(), pfmd);
		} catch (IOException | InvalidInputException | ServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestId;
	}

	
	 * Send the destType value as LIST or FULL_CHAIN If the Destination Mode is
	 * LIST then files will be sent over to the selected stores which are in the
	 * storeNbrs list else If Destination Mode is FULL_CHAIN then the files are
	 * sent over to all the stores
	 
	private void setStores(List<String> storeNbrs, String destType) throws Exception {
		PollingDestMetadata destMetaData = null;
		if (destType != null && destType.equalsIgnoreCase("LIST"))
			destMetaData = PollingDestMetadata.getDestination(DestinationMode.LIST, storeNbrs);
		else {
			destMetaData = PollingDestMetadata.getDestination(DestinationMode.FULL_CHAIN, null);
		}
		setDestMetaData(destMetaData);
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Environment getEnv() {
		return env;
	}

	public Prerequisite getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(Prerequisite prerequisite) {
		this.prerequisite = prerequisite;
	}

	public void setEnv(String env1) {
		if (env1 != null && env1.equalsIgnoreCase("DEV")) {
			this.env = RestAdapter.Environment.dev;
		} else if (env1 != null && env1.equalsIgnoreCase("QA")) {
			this.env = RestAdapter.Environment.qa;
		} else if (env1 != null && env1.equalsIgnoreCase("PROD")) {
			this.env = RestAdapter.Environment.prod;
		}
	}

	public PollingDestMetadata getDestMetaData() {
		return destMetaData;
	}

	public void setDestMetaData(PollingDestMetadata destMetaData) {
		this.destMetaData = destMetaData;
	}*/

}
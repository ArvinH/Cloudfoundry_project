package cloudfoundry.test.model.service;

import java.io.IOException;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public enum ServiceManager implements CloudFoundryServices {

	INSTANCE;
	private static final String NULL_STRING = "";

	public DBCollection getInstance(int service_type) throws Exception {
		if (service_type == Mongodb) {
			return getMongoDBConnection();
		} else {
			throw new IllegalArgumentException("Service for id " + service_type
					+ " not found...");
		}
	}
	
	private DBCollection getMongoDBConnection() {
		
		String vcap_services = System.getenv("VCAP_SERVICES");
		String hostname = NULL_STRING;
		String dbname = NULL_STRING;
		String username = NULL_STRING;
		String password = NULL_STRING;
		String port = NULL_STRING;
		if (vcap_services != null && vcap_services.length() > 0) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonFactory factory = mapper.getJsonFactory();
				JsonParser jp;
				try {
					jp = factory.createJsonParser(vcap_services);
					JsonNode actualObj = mapper.readTree(jp);
					JsonNode credentials = actualObj.get("mongodb-1.8").get(0)
							.get("credentials");
					dbname = credentials.get("name").toString();
					hostname = credentials.get("hostname").toString();
					username = credentials.get("username").toString();
					password = credentials.get("password").toString();
					port = credentials.get("port").toString();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("dbname : " + dbname);
				System.out.println("hostname : " + hostname);
				System.out.println("port : " + port);
				System.out.println("user : " + username);
				System.out.println("password : " + password);
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				Mongo mongo = new Mongo(hostname, Integer.parseInt(port));
				mongo.getConnector();
				CommandResult auth = mongo.getDB(dbname).authenticateCommand(username, password.toCharArray());
				System.out.println(auth);
				DB db = mongo.getDB(dbname);
				//db.authenticate(username, password.toCharArray());
				DBCollection collection = db.getCollection("cloudfoundry");
				BasicDBObject doc = new BasicDBObject("name", "MongoDB")
						.append("type", "database")
						.append("count", 1)
						.append("info",
								new BasicDBObject("x", 201).append("y", 102));
				collection.insert(doc);
				DBObject myDoc = collection.findOne();
				System.out.println(myDoc);
				return collection;
			}  catch (UnknownHostException e) {  
	            e.printStackTrace();  
	        } catch (MongoException e) {  
	            e.printStackTrace();  
	        }  
		} else {

			
			try {
				Mongo mongo = new Mongo("192.168.1.172", 25004);
				mongo.getConnector();
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				CommandResult auth = mongo.getDB("3829864b-7a61-4bd7-be70-7627b7e044ad").authenticateCommand("ecf963e6-92e7-49d6-a3c5-0ea6ee4cf586", "cb3c03ad-8731-4d8e-ac81-c139ce2ed4c3".toCharArray());
				System.out.println(auth);
				DB db = mongo.getDB("3829864b-7a61-4bd7-be70-7627b7e044ad");
				DBCollection collection = db.getCollection("cloudfoundry");
				/*
				BasicDBObject doc = new BasicDBObject("name", "MongoDB")
						.append("type", "database")
						.append("count", 1)
						.append("info",
								new BasicDBObject("x", 203).append("y", 102));
				collection.insert(doc);
				DBObject myDoc = collection.findOne();
				System.out.println(myDoc); 
				*/ 
				return collection;
			} catch (UnknownHostException e) {  
	            e.printStackTrace();  
	        } catch (MongoException e) {  
	            e.printStackTrace();  
	        }  
		}
		return null;
	}
}
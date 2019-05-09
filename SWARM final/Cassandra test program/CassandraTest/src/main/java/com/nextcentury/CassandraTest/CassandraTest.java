package com.nextcentury.CassandraTest;

import java.util.Arrays;
import java.util.UUID;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;


public class CassandraTest 
{
	static int replicationFactor = 1;

	//localhost:9160
	public static void main( String[] args )
	{
		Cluster cluster = null;
		try {
		    cluster = Cluster.builder()                                                    
		            .addContactPoint("127.0.0.1")
		            .build();
		    Session session = cluster.connect();                                           

		    ResultSet rs = session.execute("select release_version from system.local");    
		    Row row = rs.one();
		    System.out.println(row.getString("release_version"));                          
		   
		    UUID UUID = UUIDs.timeBased();
		    
		   // session.execute("DROP KEYSPACE IF EXISTS stax");
		    session.execute("CREATE KEYSPACE IF NOT EXISTS stax WITH REPLICATION  = { 'class' : 'SimpleStrategy', 'replication_factor' : 2 };");
		    session.execute("USE stax");
		    session.execute("CREATE TABLE IF NOT EXISTS stax.testData2 ( id TIMEUUID, name text PRIMARY KEY, updateTime timestamp );");
		    //https://docs.datastax.com/en/cql/3.3/cql/cql_using/useCreateTable.html
		    session.execute("INSERT INTO stax.testData2 (id, name, updateTime) VALUES (" + UUID + ", 'Sample text 1', 1020202);");
			ResultSet res = session.execute("select * from stax.testData2 WHERE name = 'Sample text 1' ");
			System.out.println(res.all());
			
		    System.out.println("Using: " + session.getLoggedKeyspace());  
		} finally {
		    if (cluster != null) cluster.close();                                          
		}
	}
}

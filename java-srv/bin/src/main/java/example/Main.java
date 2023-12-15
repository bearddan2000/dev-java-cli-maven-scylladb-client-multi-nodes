package example;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import example.model.User;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.insert.InsertInto;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspaceStart;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableStart;
import com.datastax.oss.driver.api.querybuilder.select.Select;

public class Main {

    private String keySpaceName;
    private String tableName;

    public Main(String keySpaceName, String tableName) {
        this.keySpaceName = keySpaceName;
        this.tableName = tableName;
        CreateKeyspaceStart createKeyspace = createKeyspace(keySpaceName);
        SimpleStatement keypaceStatement = createKeyspace.ifNotExists()
          .withSimpleStrategy(3)
          .build();

        CreateTableStart createTable = createTable(keySpaceName, tableName);
        SimpleStatement tableStatement = createTable.ifNotExists()
          .withPartitionKey("id", DataTypes.BIGINT)
          .withColumn("name", DataTypes.TEXT)
          .build();
        
        try (CqlSession session = getSession()) {
            ResultSet rs = session.execute(keypaceStatement);
            if (null == rs.getExecutionInfo().getErrors() || rs.getExecutionInfo().getErrors().isEmpty()) {
                rs = session.execute(tableStatement);
            }
        }
    }

    private CqlSession getSession(){
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress("node1", 19042))
                .addContactPoint(new InetSocketAddress("node2", 19042))
                .addContactPoint(new InetSocketAddress("node3", 19042))
                .withLocalDatacenter("datacenter1")
                .build();
    }

    public List<String> getAllUserNames() {
        List<String> userNames = new ArrayList<>();
        try (CqlSession session = getSession()) {
            String query = String.format("select * from %s.%s",keySpaceName,tableName);
            ResultSet rs = session.execute(query);
            for (Row r : rs.all())
                userNames.add(r.getString("name"));
        }
        return userNames;
    }

    public List<User> getUsersByUserName(String userName) {
        List<User> userList = new ArrayList<>();
        try (CqlSession session = getSession()) {
            Select query = selectFrom(keySpaceName, tableName).all()
              .whereColumn("name")
              .isEqualTo(literal(userName))
              .allowFiltering();
	        SimpleStatement statement = query.build();
            ResultSet rs = session.execute(statement);
            for (Row r : rs)
                userList.add(new User(r.getLong("id"), r.getString("name")));
        }
        return userList;
    }

    public boolean addNewUser(User user) {
        boolean response = false;
        try (CqlSession session = getSession()) {
            InsertInto insert = insertInto(keySpaceName, tableName);
            SimpleStatement statement = insert.value("id", literal(user.getId()))
              .value("name", literal(user.getName()))
              .build();
            ResultSet rs = session.execute(statement);
            response = null == rs.getExecutionInfo().getErrors() || rs.getExecutionInfo().getErrors().isEmpty();
        }
        return response;
    }
    
    public static void main(String[] args){
        Main o = new Main("employee", "salary");
        o.addNewUser(new User(1L, "Adam"));
        o.addNewUser(new User(2L, "Fred"));
        o.addNewUser(new User(3L, "Steve"));

        for(String x : o.getAllUserNames())
            System.out.println(x);

        for(User x : o.getUsersByUserName("Fred"))
            System.out.println(x.toString());

    }

}
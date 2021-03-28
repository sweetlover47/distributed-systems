package ru.nsu.lebedeva.dao;

import ru.nsu.lebedeva.entity.NodeEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static ru.nsu.lebedeva.Main.connection;

public class NodeDAO {
    private static void prepareStatement(PreparedStatement statement, NodeEntity node) throws SQLException {
        statement.setLong(1, node.getId());
        statement.setString(2, node.getUser());
        statement.setDouble(3, node.getLongitude());
        statement.setDouble(4, node.getLatitude());
    }

    private static NodeEntity mapNode(ResultSet rs) throws SQLException {
        return new NodeEntity(rs.getLong("id"), rs.getString("username"),
                rs.getDouble("longitude"), rs.getDouble("latitude"));
    }


    public NodeEntity getNode(long nodeId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SqlQueries.SQL_GET);
        statement.setLong(1, nodeId);
        ResultSet resultSet = statement.executeQuery(SqlQueries.SQL_GET);
        return resultSet.next() ? mapNode(resultSet) : null;
    }


    public void insertNode(NodeEntity node) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into nodes(id, username, longitude, latitude) " +
                "values (" + node.getId() + ", '" + node.getUser().replaceAll("'", "''") + "', " + node.getLongitude() +
                ", " + node.getLatitude() + ")";
        statement.execute(sql);
    }


    public void insertPreparedNode(NodeEntity node) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SqlQueries.SQL_INSERT);
        prepareStatement(statement, node);
        statement.execute();
    }

    public void batchInsertNodes(List<NodeEntity> nodes) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SqlQueries.SQL_INSERT);
        for (NodeEntity node : nodes) {
            prepareStatement(statement, node);
            statement.addBatch();
        }
        statement.executeBatch();
    }

    private static class SqlQueries {
        private static final String SQL_GET = "" +
                "select id, username, longitude, latitude " +
                "from nodes " +
                "where id = ?";

        private static final String SQL_INSERT = "" +
                "insert into nodes(id, username, longitude, latitude) " +
                "values (?, ?, ?, ?)";

    }
}

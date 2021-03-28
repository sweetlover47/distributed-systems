package ru.nsu.lebedeva.dao;

import ru.nsu.lebedeva.entity.TagEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.lebedeva.Main.connection;

public class TagDAO {
    private static void prepareStatement(PreparedStatement statement, TagEntity tag) throws SQLException {
        statement.setLong(1, tag.getNodeId());
        statement.setString(2, tag.getKey());
        statement.setString(3, tag.getValue());
    }

    private static TagEntity mapTag(ResultSet rs) throws SQLException {
        return new TagEntity(rs.getLong("node_id"), rs.getString("key"),
                rs.getString("value"));
    }

    public List<TagEntity> getTags(long nodeId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SqlQueries.SQL_GET);
        statement.setLong(1, nodeId);
        ResultSet resultSet = statement.executeQuery(SqlQueries.SQL_GET);
        List<TagEntity> tags = new ArrayList<>();
        while (resultSet.next()) {
            tags.add(mapTag(resultSet));
        }
        return tags;
    }

    public void insertTag(TagEntity tag) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into tags(node_id, key, value) " +
                "values (" + tag.getNodeId() + ", '" + tag.getKey() +
                "', '" + tag.getValue().replaceAll("'", "") + "')";
        statement.execute(sql);
    }

    public void insertPreparedTag(TagEntity tag) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(TagDAO.SqlQueries.SQL_INSERT);
        prepareStatement(statement, tag);
        statement.execute();
    }

    public void batchInsertTags(List<TagEntity> tags) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(TagDAO.SqlQueries.SQL_INSERT);
        for (TagEntity tag : tags) {
            prepareStatement(statement, tag);
            statement.addBatch();
        }
        statement.executeBatch();
    }

    private static class SqlQueries {
        private static final String SQL_GET = "" +
                "select node_id, key, value " +
                "from tags " +
                "where node_id = ?";

        private static final String SQL_INSERT = "" +
                "insert into tags(node_id, key, value) " +
                "values (?, ?, ?)";

    }
}

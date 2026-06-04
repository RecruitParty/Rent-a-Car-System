package reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerRDAO {

    public boolean existsById(Connection conn, int userId)
            throws Exception {

        String sql =
                "SELECT 1 " +
                "FROM customer " +
                "WHERE user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        }
    }
}
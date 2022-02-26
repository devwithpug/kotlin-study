package ch4

import org.springframework.jdbc.core.JdbcTemplate

val jdbcTemplate = JdbcTemplate()

fun findAll(): List<User> {
    val sql = "select u.id, u.username, u.password from users u"
    return jdbcTemplate.query(sql) {rs, rowNum -> User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password")
        )
    }
}


data class User(
    val id: Long,
    val username: String,
    val password: String
)

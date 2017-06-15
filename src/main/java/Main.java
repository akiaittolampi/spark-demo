import static spark.Spark.*;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

import spark.Route;
import spark.Request;
import spark.Response;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.Db2DatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.LocalLog;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.*;
import com.j256.ormlite.table.TableUtils;

public class Main {

	private static Dao<Database, String> databaseDao;

	public static void initMySQL() throws SQLException {

		final String dbUrl = "jdbc:mysql://localhost/cars";

		final ConnectionSource conn = new JdbcConnectionSource(dbUrl);
		((JdbcConnectionSource) conn).setUsername("admin");
		((JdbcConnectionSource) conn).setPassword("admin");

		initDao(conn);
		initDatabaseTables(conn);

	}

	private static void initDao(final ConnectionSource conn)
			throws SQLException {
		databaseDao = DaoManager.createDao(conn, Database.class);
	}

	private static void initDatabaseTables(final ConnectionSource conn)
			throws SQLException {
		TableUtils.createTableIfNotExists(conn, Database.class);
	}

	public static void main(String[] args) throws SQLException {
		initMySQL();

		get("/cars", new Route() {
			public Object handle(Request req, Response res) throws SQLException {

				List<Database> builder = databaseDao.queryForAll();
				return JsonUtil.toJson(builder);

			}
		});

		get("/cars/:id", new Route() {
			public Object handle(Request req, Response res) throws Exception {
				final Database db = databaseDao.queryForId(req.params(":id"));
				if (db != null) {
					return JsonUtil.toJson(db);
				} else {
					final int httpNotFound = 404;
					final String msg = "User not found!";
					res.status(httpNotFound);
					return msg;
				}
			}
		});

		post("/cars", new Route() {
			public Object handle(Request req, Response res) throws SQLException {
				String car = req.queryParams("car");
				String model = req.queryParams("model");
				String regnum = req.queryParams("regnum");

				final Database db = new Database();
				db.setCar(car);
				db.setModel(model);
				db.setRegisternumber(regnum);

				final int createUserId = databaseDao.create(db);
				final int httpStatus = 201;
				res.status(httpStatus);

				return createUserId;
			}
		});

		put("/cars/:id", new Route() {
			public Object handle(Request req, Response res) throws SQLException {				
				final Database db = databaseDao.queryForId(req.params(":id"));
						
					String car = req.queryParams("car");
					String model = req.queryParams("model");
					String regnum = req.queryParams("regnum");

					db.setCar(car);
					db.setModel(model);
					db.setRegisternumber(regnum);
				
				final int updateUserId = databaseDao.update(db);
				final int httpStatus = 200;
				res.status(httpStatus);

				return updateUserId;
			}
		});

		after((req, res) -> {
			res.type("application/json");
		});
	}
}

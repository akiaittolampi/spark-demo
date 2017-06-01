import com.j256.ormlite.table.*;
import com.j256.ormlite.field.*;

@DatabaseTable(tableName = "cars")
public class Database {

	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField
	private String car;

	@DatabaseField
	private String model;

	public Database() {
		// ORMLite needs a no-arg constructor
	}

	public int getId() {
		return this.id;
	}

	public void setId() {
		this.id = id;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public String getCar() {
		return this.car;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModel() {
		return this.model;
	}
}

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class RunVacuumJob {

	public static void main(String[] args) {

		// 실행 스크립트 
		// java -jar Va.jar 7 E:\\offsetapp.db
		
		if (args.length != 2) {
			System.err.println("input option");
			System.err.println("args[0] : remove ago days (ex. 7)");
			System.err.println("args[1] : offsetapp.db full path (ex. /home/dean/offsetapp.db");
			System.exit(1);
		}
		
		int days = Integer.parseInt(args[0]) * -1;
		String path = args[1];

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, days);
		long timestamp = new Timestamp(cal.getTimeInMillis()).getTime();

		DataReader dr = new DataReader();
		dr.open(path);
		dr.selectData(timestamp);
		dr.deleteData(timestamp);
		dr.vacuumData();
		dr.close();

	}

}

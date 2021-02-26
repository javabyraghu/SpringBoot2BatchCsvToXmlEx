package in.nareshit.raghu.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyJobRunner 
	implements CommandLineRunner
{
	@Autowired
	private Job jobA;
	
	@Autowired
	private JobLauncher launcher;

	public void run(String... args) throws Exception {
		System.out.println("ABOUT TO START JOB EXECUTION...");
		launcher.run(jobA, new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters());
		
	}
}

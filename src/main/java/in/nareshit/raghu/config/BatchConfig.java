package in.nareshit.raghu.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import in.nareshit.raghu.model.Product;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	//1. reader
	@Bean
	public ItemReader<Product> reader() {
		return new FlatFileItemReader<Product>() {{
			setResource(new FileSystemResource("E:/myouts/products.csv"));
			setLineMapper(new DefaultLineMapper<Product>() {{
				setLineTokenizer(new DelimitedLineTokenizer() {{
					setDelimiter(",");
					setNames("prodId","prodName","prodCost");
				}});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
					setTargetType(Product.class);
				}});
			}});
		}};
	}
	
	//2. processor
	@Bean
	public ItemProcessor<Product, Product> processor() {
		return item -> {
			double cost = item.getProdCost();
			item.setProdDisc(cost * 12/100);
			item.setProdGst(cost * 18/100);
			return item;
		};
	}
	
	//3. writer
	@Bean
	public ItemWriter<Product> writer(){
		return new StaxEventItemWriter<Product>() {{
			setResource(new FileSystemResource("E:/myouts/data.xml"));
			setMarshaller(new Jaxb2Marshaller() {{
				setClassesToBeBound(Product.class);
			}});
			setRootTagName("products-info");
		}};
	}
	
	
	//4. listener
	@Bean
	public JobExecutionListener listener() {
		return new JobExecutionListener() {
			
			public void beforeJob(JobExecution je) {
				System.out.println("STARTED WITH =>"+je.getStatus());
			}
			
			public void afterJob(JobExecution je) {
				System.out.println("FINISHED WITH =>"+je.getStatus());
			}
		};
	}
	
	//5. step builder factory
	@Autowired
	private StepBuilderFactory sf;
	
	//6. step
	public Step stepA() {
		return sf.get("stepA")
				.<Product,Product>chunk(3)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	//7. job builder factory
	@Autowired
	private JobBuilderFactory jf;
	
	//8. job 
	@Bean
	public Job jobA() {
		return jf.get("jobA")
				.listener(listener())
				.incrementer(new RunIdIncrementer())
				.start(stepA())
				.build();
	}
}

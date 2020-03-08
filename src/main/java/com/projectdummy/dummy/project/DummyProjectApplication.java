package com.projectdummy.dummy.project;

import com.projectdummy.dummy.project.Scanner.SecugenScanner;
import com.projectdummy.dummy.project.component.WelcomePage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DummyProjectApplication {

	static SecugenScanner scanner;


	public static void main(String[] args) {
//		SpringApplication.run(DummyProjectApplication.class, args);

		ConfigurableApplicationContext context = new SpringApplicationBuilder(DummyProjectApplication.class).headless(false).run(args);
		WelcomePage welcomePage = context.getBean(WelcomePage.class);
		welcomePage.setVisible(true);
//		FrontEnd frontEnd = context.getBean(FrontEnd.class);
//		frontEnd.setVisible(true);

		initializeScanner();
		//endScanner();
	}

	private static void initializeScanner(){
		scanner = SecugenScanner.getInstance();
		scanner.initialize();
	}

	private static void endScanner(){
		scanner.stop();
	}

}

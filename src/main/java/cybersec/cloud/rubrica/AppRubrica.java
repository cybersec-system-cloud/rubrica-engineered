package cybersec.cloud.rubrica;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class AppRubrica extends Application<RubricaConfig> {
    
    public static void main(String[] args) throws Exception {
        new AppRubrica().run(args);
    }
    
    @Override
    public void run(RubricaConfig configuration, Environment environment) {
        final Rubrica risorsaRubrica = new Rubrica(
                configuration.getNomePredefinito()
        );
        
        environment.jersey().register(risorsaRubrica);
    }
    
}
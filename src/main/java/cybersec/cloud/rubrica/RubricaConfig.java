package cybersec.cloud.rubrica;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class RubricaConfig extends Configuration {
    
    private String nomePredefinito;
    
    @JsonProperty
    public String getNomePredefinito() { return nomePredefinito; }
    
}
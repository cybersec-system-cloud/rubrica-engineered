package cybersec.cloud.rubrica;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NumeroTelefono {
    
    private String cognome;
    private String nome;
    private String numero;
    
    public NumeroTelefono() { }
    
    public NumeroTelefono(String cognome, String nome, String numero){
        this.cognome = cognome;
        this.nome = nome;
        this.numero = numero;
    }
    
    @JsonProperty
    public String getCognome() { return cognome; }

    @JsonProperty
    public String getNome() { return nome; }    
    
    @JsonProperty
    public String getNumero() { return numero; }
    
}

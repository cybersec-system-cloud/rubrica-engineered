package cybersec.cloud.rubrica;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/rubrica")
@Produces(MediaType.APPLICATION_JSON)
public class Rubrica {

    private final String nomePredefinito;
    private final List<NumeroTelefono> numeri;
    
    public Rubrica(String nomePredefinito) {
        this.nomePredefinito = nomePredefinito;
        numeri = new ArrayList<NumeroTelefono>();
    }
    
    @POST
    public void aggiungiNumero(
        @QueryParam("cognome") String cognome, 
        @QueryParam("nome") Optional<String> nome, 
        @QueryParam("numero") String numero
    ) {
        // Considera "nome" se definito, altrimenti prende quello predefinito
        String n = this.nomePredefinito;
        if(nome.isPresent())
            n = nome.get();
        // Aggiunge un nuovo numero di telefono nella rubrica
        this.numeri.add(new NumeroTelefono(cognome,n,numero));
    }
    
    // Metodo privato per la ricerca dell'indice in "numeri" 
    // del contatto avente "cognome" e "nome" indicati
    private int indiceNumero(String cognome, String nome) {
        for(int i=0; i<this.numeri.size(); i++) {
            NumeroTelefono n = this.numeri.get(i);
            if(n.getCognome().equals(cognome) && n.getNome().equals(nome))
                return i;
        }
        return -1;
    }
    
    @GET
    @Path("/{cognome}/{nome}")
    public NumeroTelefono recuperaNumero(
        @PathParam("cognome") String cognome, 
        @PathParam("nome") String nome 
    ) {
        // Recupera l'indice "i" di "cognome" e "nome" in "numeri"
        int i = indiceNumero(cognome,nome);
        // Se "cognome" e "nome" sono presenti
        if (i>-1)
            // restituisce il numero di telefono corrispondente
            return this.numeri.get(i);
        // Altrimenti, restituisce "null"
        return null;
    }
    
    @PUT
    @Path("/{cognome}/{nome}")
    public void aggiornaNumero(
        @PathParam("cognome") String cognome, 
        @PathParam("nome") String nome, 
        @QueryParam("numero") String numero
    ) {
        // Recupera l'indice "i" di "cognome" e "nome" in "numeri"
        int i = indiceNumero(cognome,nome);
        // Se "cognome" e "nome" sono presenti
        if (i>-1) {
            // rimuove il numero di telefono corrispondente
            this.numeri.remove(i);
            // e inserisce un nuovo numero di telefono (corrispondente
            // al numero aggiornato)
            this.numeri.add(new NumeroTelefono(cognome,nome,numero));
        }
    }
    
    @DELETE
    @Path("/{cognome}/{nome}")
    public void aggiornaNumero(
        @PathParam("cognome") String cognome, 
        @PathParam("nome") String nome 
    ) {
        // Recupera l'indice "i" di "cognome" e "nome" in "numeri"
        int i = indiceNumero(cognome,nome);
        // Se "cognome" e "nome" sono presenti
        if (i>-1)
            // rimuove il numero di telefono corrispondente
            this.numeri.remove(i);
        
    }
    
}
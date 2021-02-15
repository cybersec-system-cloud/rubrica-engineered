package cybersec.cloud.rubrica;

import java.net.URI;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

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
    public Response aggiungiNumero(
        @QueryParam("cognome") Optional<String> cognome, 
        @QueryParam("nome") Optional<String> nome, 
        @QueryParam("numero") Optional<String> numero
    ) {
        // Se "cognome" o "numero" non sono specificati,
        // restituisce un messaggio di errore (BAD REQUEST)
        if (!cognome.isPresent() || !numero.isPresent()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Cognome e numero devono sempre essere indicati")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Considera "nome" se definito, altrimenti prende quello predefinito
        String n = this.nomePredefinito;
        if(nome.isPresent())
            n = nome.get();
        
        // Se "cognome" e "nome" già associati ad un numero,
        // restituisce un messaggio di errore (CONFLICT)
        if (indiceNumero(cognome.get(),n) != -1) {
            return Response.status(Status.CONFLICT)
                    .entity("Numero di " + cognome + " " + nome + " già inserito")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Aggiunge un nuovo numero di telefono nella rubrica
        this.numeri.add(new NumeroTelefono(cognome.get(),n,numero.get()));
        
        // Restituisce un messaggio di conferma aggiunta numero
        URI uri = UriBuilder.fromResource(Rubrica.class).path(cognome.get()).path(n).build();
        return Response.created(uri).build();
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
    public Response recuperaNumero(
        @PathParam("cognome") String cognome, 
        @PathParam("nome") String nome 
    ) {
        // Recupera l'indice "i" di "cognome" e "nome" in "numeri"
        int i = indiceNumero(cognome,nome);
        
        // Se "cognome" e "nome" non sono in rubrica
        // restituisce un messaggio di errore (NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity(cognome + " " + nome + " non presente in rubrica")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Altrimenti, restituisce il numero di telefono corrispondente
        return Response.ok().entity(numeri.get(i)).build();
    }
    
    @PUT
    @Path("/{cognome}/{nome}")
    public Response aggiornaNumero(
        @PathParam("cognome") String cognome, 
        @PathParam("nome") String nome, 
        @QueryParam("numero") Optional<String> numero
    ) {
        // Recupera l'indice "i" di "cognome" e "nome" in "numeri"
        int i = indiceNumero(cognome,nome);
        
        // Se "cognome" e "nome" non sono in rubrica
        // restituisce un messaggio di errore (NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity(cognome + " " + nome + " non presente in rubrica")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Se "numero" non è specificato,
        // restituisce un messaggio di errore
        if (!numero.isPresent()) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Il numero deve essere sempre indicato")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Aggiorna il numero di telefono
        this.numeri.remove(i);
        this.numeri.add(new NumeroTelefono(cognome,nome,numero.get()));
        // Restituisce messaggio di conferma aggiornamento
        return Response.ok()
                .entity("numero di telefono aggiornato")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
    @DELETE
    @Path("/{cognome}/{nome}")
    public Response cancellaNumero(
        @PathParam("cognome") String cognome, 
        @PathParam("nome") String nome 
    ) {
        // Recupera l'indice "i" di "cognome" e "nome" in "numeri"
        int i = indiceNumero(cognome,nome);
        
        // Se "cognome" e "nome" non sono in rubrica
        // restituisce un messaggio di errore (NOT FOUND)
        if (i == -1) {
            return Response.status(Status.NOT_FOUND)
                    .entity(cognome + " " + nome + " non presente in rubrica")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Altrimenti, elimina il numero corrispondente
        this.numeri.remove(i);
        
        // Restituisce di conferma eliminazione (OK)
        return Response.ok()
                .entity(cognome + " " + nome + " eliminato dalla rubrica")
                .type(MediaType.TEXT_PLAIN)
                .build();
        
    }
    
}
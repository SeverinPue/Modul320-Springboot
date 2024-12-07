package ch.bbw.m320.restintro.api.v1;

import java.util.List;

import ch.bbw.m320.restintro.dto.PonyDto;
import ch.bbw.m320.restintro.service.PonyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pony API", description = "API zum Verwalten von Ponys")
@CrossOrigin
@RestController
@RequestMapping("/api/ponies")
public class PonyController {

	@Autowired
	private PonyService ponyService;

	@Operation(summary = "Alle Ponys abrufen", description = "Liefert eine Liste aller Ponys")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Liste der Ponys erfolgreich abgerufen")
	})
	@GetMapping
	public ResponseEntity<Object> getPonies() {
		try {
			List<PonyDto> ponies = ponyService.getPonies();
			return ResponseEntity.ok(ponies);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Fehler beim Abrufen der Ponys", e.getMessage()));
		}
	}

	@Operation(summary = "Pony nach ID abrufen", description = "Liefert ein Pony anhand seiner ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Pony erfolgreich abgerufen",
					content = {@Content(mediaType = "application/json",
							schema = @Schema(implementation = PonyDto.class))}),
			@ApiResponse(responseCode = "404", description = "Pony nicht gefunden")
	})
	@GetMapping("/{id}")
	public ResponseEntity<Object> getPonyById(@PathVariable int id) {
		try {
			ResponseEntity<PonyDto> response = ponyService.getPonyById(id);
			if (response.getStatusCode() == HttpStatus.OK) {
				return ResponseEntity.ok(response.getBody());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Fehler beim Abrufen des Ponys", e.getMessage()));
		}
	}

    @Operation(summary = "Pony nach Name abrufen", description = "Liefert eine Liste von Ponys anhand ihres Namens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ponys erfolgreich abgerufen",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PonyDto.class))}),
            @ApiResponse(responseCode = "404", description = "Keine Ponys mit diesem Namen gefunden")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Object> getPonyByName(@PathVariable String name) {
        try {
            List<PonyDto> ponies = ponyService.getPoniesByName(name);
            if (!ponies.isEmpty()) {
                return ResponseEntity.ok(ponies);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Fehler beim Abrufen der Ponys", e.getMessage()));
        }
    }

	@Operation(summary = "Neues Pony erstellen", description = "Erstellt ein neues Pony")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Pony erfolgreich erstellt"),
			@ApiResponse(responseCode = "400", description = "Ungültige Anfragedaten")
	})
	@PostMapping
	public ResponseEntity<Object> newPony(@RequestBody PonyDto newPony) {
		try {
			ponyService.createPony(newPony);
			return ResponseEntity.status(HttpStatus.CREATED).body("Pony erfolgreich erstellt.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("Ungültige Eingabedaten", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Fehler beim Erstellen des Ponys", e.getMessage()));
		}
	}

	@Operation(summary = "Mehrere neue Ponys erstellen", description = "Erstellt mehrere neue Ponys")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Ponys erfolgreich erstellt"),
			@ApiResponse(responseCode = "400", description = "Ungültige Anfragedaten")
	})
	@PostMapping("/batch")
	public ResponseEntity<Object> newPonies(@RequestBody List<PonyDto> newPonies) {
		try {
			for (PonyDto newPony : newPonies) {
				ponyService.createPony(newPony);
			}
			return ResponseEntity.status(HttpStatus.CREATED).body("Ponys erfolgreich erstellt.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("Ungültige Eingabedaten", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Fehler beim Erstellen der Ponys", e.getMessage()));
		}
	}

	@Operation(summary = "Pony aktualisieren", description = "Aktualisiert ein bestehendes Pony anhand seiner ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Pony erfolgreich aktualisiert"),
			@ApiResponse(responseCode = "404", description = "Pony nicht gefunden"),
			@ApiResponse(responseCode = "400", description = "Ungültige Anfragedaten")
	})
	@PutMapping("/{id}")
	public ResponseEntity<Object> updatePony(@PathVariable int id, @RequestBody PonyDto updatedPony) {
		try {
			ResponseEntity<PonyDto> response = ponyService.updatePony(id, updatedPony);
			if (response.getStatusCode() == HttpStatus.OK) {
				return ResponseEntity.ok().body("Pony erfolgreich aktualisiert.");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ErrorResponse("Ungültige Eingabedaten", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Fehler beim Aktualisieren des Ponys", e.getMessage()));
		}
	}

	@Operation(summary = "Pony löschen", description = "Löscht ein Pony anhand seiner ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Pony erfolgreich gelöscht"),
			@ApiResponse(responseCode = "404", description = "Pony nicht gefunden")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletePony(@PathVariable int id) {
		try {
			ResponseEntity<Void> response = ponyService.deletePony(id);
			if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Fehler beim Löschen des Ponys", e.getMessage()));
		}
	}

	public record ErrorResponse(String message, String details) { }
}

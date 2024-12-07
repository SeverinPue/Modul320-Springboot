package ch.bbw.m320.restintro.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ch.bbw.m320.restintro.dto.PonyDto;
import ch.bbw.m320.restintro.entity.PonyEntity;
import ch.bbw.m320.restintro.repository.PonyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class PonyService {

    private final PonyRepository ponyRepository;

    // prefer constructor injection to @Autowired (sonar claims this as well)
    public PonyService(PonyRepository ponyRepository) {
        this.ponyRepository = ponyRepository;
    }

    public List<PonyDto> getPonies() {
        return ponyRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<PonyDto> getPonyById(int id) {
        Optional<PonyEntity> ponyEntity = ponyRepository.findById(id);
        return ponyEntity.map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public List<PonyDto> getPoniesByName(String name) {
        List<PonyEntity> ponyEntities = ponyRepository.findByName(name);
        return ponyEntities.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void createPony(PonyDto newPony) {
        PonyEntity ponyEntity = new PonyEntity(
                newPony.name,
                newPony.age,
                newPony.height,
                newPony.weight,
                newPony.isRidden,
                newPony.birthday,
                newPony.favouriteFood);
        ponyRepository.save(ponyEntity);
    }

    public ResponseEntity<PonyDto> updatePony(int id, PonyDto updatedPony) {
        // notice: ResponseEntity is something from a Controller: it has to do with REST and doesn't belong in a service
        // we anyway would like to avoid using ResponseEntity<> wrappers whenever possible.
        // instead return PonyDto here and for the exception case throw a custom exception annotated with @ResponseCode
        Optional<PonyEntity> existingPony = ponyRepository.findById(id);
        if (existingPony.isPresent()) {
            PonyEntity ponyEntity = existingPony.get();
            ponyEntity.setName(updatedPony.getName()); // use getters instead of public attributes
            ponyEntity.setAge(updatedPony.age);
            ponyEntity.setHeight(updatedPony.height);
            ponyEntity.setWeight(updatedPony.weight);
            ponyEntity.setRidden(updatedPony.isRidden);
            ponyEntity.setBirthday(updatedPony.birthday);
            ponyEntity.setFavouriteFood(updatedPony.favouriteFood);
            ponyRepository.save(ponyEntity);
            return ResponseEntity.ok(mapToDto(ponyEntity));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deletePony(int id) {
        Optional<PonyEntity> existingPony = ponyRepository.findById(id);
        if (existingPony.isPresent()) {
            ponyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // for production-like setups we would use
    private PonyDto mapToDto(PonyEntity ponyEntity) {
        return new PonyDto(
                ponyEntity.name,
                ponyEntity.age,
                ponyEntity.id,
                ponyEntity.height,
                ponyEntity.weight,
                ponyEntity.isRidden,
                ponyEntity.birthday,
                ponyEntity.favouriteFood);
    }

}

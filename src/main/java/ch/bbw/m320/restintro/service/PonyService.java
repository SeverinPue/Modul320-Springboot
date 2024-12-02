package ch.bbw.m320.restintro.service;

import ch.bbw.m320.restintro.dto.PonyDto;
import ch.bbw.m320.restintro.entity.PonyEntity;
import ch.bbw.m320.restintro.repository.PonyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PonyService {

    @Autowired
    private PonyRepository ponyRepository;

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
        Optional<PonyEntity> existingPony = ponyRepository.findById(id);
        if (existingPony.isPresent()) {
            PonyEntity ponyEntity = existingPony.get();
            ponyEntity.setName(updatedPony.name);
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
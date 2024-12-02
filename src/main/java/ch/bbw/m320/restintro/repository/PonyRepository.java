package ch.bbw.m320.restintro.repository;
import ch.bbw.m320.restintro.entity.PonyEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
public class PonyRepository {

    private final List<PonyEntity> ponies = new ArrayList<>();

    public List<PonyEntity> findAll() {
        return ponies;
    }

    public Optional<PonyEntity> findById(int id) {
        return ponies.stream().filter(p -> p.id == id).findFirst();
    }

    public PonyEntity save(PonyEntity pony) {
        if (pony.id == 0) {
            pony.id = generateId();
        }
        ponies.add(pony);
        return pony;
    }

    public void deleteById(int id) {
        ponies.removeIf(p -> p.id == id);
    }

    private int generateId() {

        return ponies.size() > 0 ? ponies.get(ponies.size() - 1).id + 1 : 1;
    }

    public List<PonyEntity> findByName(String name) {
        return ponies.stream()
                .filter(p -> p.name.equals(name))
                .collect(Collectors.toList());
    }
}


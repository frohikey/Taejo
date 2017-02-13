package co.orbu.taejo.version.repository;

import co.orbu.taejo.version.model.repository.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version, Long> {

    Version findByCommand(String command);

}

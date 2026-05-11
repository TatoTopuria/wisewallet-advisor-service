package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.EmbeddedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmbeddedDocumentJpaRepository extends JpaRepository<EmbeddedDocument, UUID> {

    Optional<EmbeddedDocument> findByUserIdAndChunkKey(UUID userId, String chunkKey);

    @Query(value = """
            SELECT *
            FROM embedded_documents e
            WHERE e.user_id = :userId
              AND (1 - (e.embedding <=> CAST(:queryVector AS vector))) >= :minSimilarity
            ORDER BY e.embedding <=> CAST(:queryVector AS vector)
            LIMIT :topK
            """, nativeQuery = true)
    List<EmbeddedDocument> findSimilar(@Param("userId") UUID userId,
                                       @Param("queryVector") String queryVector,
                                       @Param("topK") int topK,
                                       @Param("minSimilarity") double minSimilarity);
}

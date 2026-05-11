package com.wisewallet.advisor.infrastructure.persistence;

import com.wisewallet.advisor.domain.model.EmbeddedDocument;
import com.wisewallet.advisor.domain.repository.EmbeddedDocumentRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class EmbeddedDocumentRepositoryAdapter implements EmbeddedDocumentRepositoryPort {

    private final EmbeddedDocumentJpaRepository jpaRepository;

    public EmbeddedDocumentRepositoryAdapter(EmbeddedDocumentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<EmbeddedDocument> findSimilar(UUID userId, String queryVector, int topK, double minSimilarity) {
        return jpaRepository.findSimilar(userId, queryVector, topK, minSimilarity);
    }

    @Override
    public EmbeddedDocument upsert(EmbeddedDocument document) {
        return jpaRepository.findByUserIdAndChunkKey(document.getUserId(), document.getChunkKey())
                .map(existing -> {
                    existing.setContent(document.getContent());
                    existing.setEmbedding(document.getEmbedding());
                    existing.setYear(document.getYear());
                    existing.setMonth(document.getMonth());
                    int currentVersion = existing.getVersion() != null ? existing.getVersion() : 1;
                    existing.setVersion(currentVersion + 1);
                    return jpaRepository.save(existing);
                })
                .orElseGet(() -> jpaRepository.save(document));
    }
}

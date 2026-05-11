package com.wisewallet.advisor.domain.repository;

import com.wisewallet.advisor.domain.model.EmbeddedDocument;

import java.util.List;
import java.util.UUID;

public interface EmbeddedDocumentRepositoryPort {

    List<EmbeddedDocument> findSimilar(UUID userId, String queryVector, int topK, double minSimilarity);

    EmbeddedDocument upsert(EmbeddedDocument document);
}

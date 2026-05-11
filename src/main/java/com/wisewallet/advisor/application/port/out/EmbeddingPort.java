package com.wisewallet.advisor.application.port.out;

import java.util.List;

public interface EmbeddingPort {

    String embed(String text);

    List<String> embedBatch(List<String> texts);
}

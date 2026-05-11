package com.wisewallet.advisor.presentation.mapper;

import com.wisewallet.advisor.domain.model.Insight;
import com.wisewallet.advisor.presentation.dto.response.InsightResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InsightMapper {

    InsightResponse toResponse(Insight insight);
}

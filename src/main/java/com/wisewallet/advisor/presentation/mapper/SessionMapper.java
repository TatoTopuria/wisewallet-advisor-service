package com.wisewallet.advisor.presentation.mapper;

import com.wisewallet.advisor.domain.model.ChatMessage;
import com.wisewallet.advisor.domain.model.ConversationSession;
import com.wisewallet.advisor.presentation.dto.response.ChatMessageResponse;
import com.wisewallet.advisor.presentation.dto.response.SessionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    SessionResponse toResponse(ConversationSession session);

    ChatMessageResponse toResponse(ChatMessage chatMessage);
}

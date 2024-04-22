package com.fastcampus.simplesns.service;

import com.fastcampus.simplesns.exception.ErrorCode;
import com.fastcampus.simplesns.exception.SnsApplicationException;
import com.fastcampus.simplesns.model.AlarmArgs;
import com.fastcampus.simplesns.model.AlarmType;
import com.fastcampus.simplesns.model.entity.AlarmEntity;
import com.fastcampus.simplesns.model.entity.UserEntity;
import com.fastcampus.simplesns.repository.AlarmEntityRepository;
import com.fastcampus.simplesns.repository.EmitterRepository;
import com.fastcampus.simplesns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/*
SSE는 브라우저 connect 하나당 하나의 인스턴스가 생긴다. 해당 SSE를 찾아서 브라우저에 맞는 SSE를 보내줘야 함
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final String ALARM_NAME = "alarm";
    private final EmitterRepository emitterRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public void send(AlarmType type, AlarmArgs arg, Integer receiveUserId) {
        UserEntity user = userEntityRepository.findById(receiveUserId).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND));
        //alarm save
        AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(user, type, arg));

        emitterRepository.get(receiveUserId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmEntity.getId().toString()).name(ALARM_NAME).data("new alarm"));
            } catch (IOException e) {
                emitterRepository.delete(receiveUserId);
                throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("No emitter founded"));
    }

    public SseEmitter connectAlarm(Integer userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, sseEmitter);
        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            sseEmitter.send(SseEmitter.event().id("id").name(ALARM_NAME).data("connect completed"));
        } catch (IOException e) {
            throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
        }
        return sseEmitter;
    }
}

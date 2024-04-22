package com.fastcampus.simplesns.model.event;

import com.fastcampus.simplesns.model.AlarmArgs;
import com.fastcampus.simplesns.model.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {
    private Integer receiveUserId;
    private AlarmType alarmType;
    private AlarmArgs args;
}

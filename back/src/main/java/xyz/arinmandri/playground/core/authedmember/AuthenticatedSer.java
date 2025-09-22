package xyz.arinmandri.playground.core.authedmember;

import xyz.arinmandri.playground.core.PersistenceSer;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


/**
 * TODO 컨텍 패키지 나누기
 * Member, MKey
 * 
 * Member 생성시 반드시 Key 하나와 함께 생성한다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticatedSer extends PersistenceSer
{
}

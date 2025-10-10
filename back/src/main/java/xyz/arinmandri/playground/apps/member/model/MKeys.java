package xyz.arinmandri.playground.apps.member.model;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class MKeys
{

	final private MKeyBasicRepo mkeyBasicRepo;

	public MKeyBasic getKeyBasic ( String keyname ) {
		return mkeyBasicRepo.findByKeyname( keyname ).orElse( null );
	}
}

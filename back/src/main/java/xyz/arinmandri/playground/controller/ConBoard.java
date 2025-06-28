package xyz.arinmandri.playground.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.arinmandri.playground.service.SerBoard;


@RestController
public class ConBoard
{
	@Autowired SerBoard serBoard;

	@GetMapping( "/board" )
	public List<Map<String, Object>> board () {
		return serBoard.board();
	}
}

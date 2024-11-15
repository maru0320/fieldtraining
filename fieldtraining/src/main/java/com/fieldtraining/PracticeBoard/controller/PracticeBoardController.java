package com.fieldtraining.PracticeBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fieldtraining.PracticeBoard.service.PracticeBoardService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/Practice")
@Controller
@RequiredArgsConstructor
public class PracticeBoardController {
	
	@Autowired
	PracticeBoardService practiceBoardService;

}

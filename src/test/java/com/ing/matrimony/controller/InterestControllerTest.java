package com.ing.matrimony.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.matrimony.dto.ActionResponseDto;
import com.ing.matrimony.dto.ActionrequestDto;
import com.ing.matrimony.dto.InvitationDto;
import com.ing.matrimony.dto.InvitationRequestDto;
import com.ing.matrimony.dto.ViewInvitationDto;
import com.ing.matrimony.service.InvitationService;

@RunWith(SpringJUnit4ClassRunner.class)
public class InterestControllerTest {

	@Mock
	InvitationService invitationService;

	@InjectMocks
	InterestController interestController;

	private MockMvc mockMvc;

	InvitationRequestDto invitationRequest;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(interestController).build();
	}

	@Test
	public void invitationTest() throws Exception {

		InvitationRequestDto invitationRequest = new InvitationRequestDto();
		invitationRequest.setRecieverId(1L);
		invitationRequest.setSenderId(2L);

		InvitationDto invitationDto = new InvitationDto();

		invitationDto.setMessage("invitation sent");
		invitationDto.setStatusCode(200);

		Mockito.when(invitationService.invite(Mockito.any())).thenReturn(invitationDto);

		mockMvc.perform(MockMvcRequestBuilders.post("/raiseInterest").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.ALL).content(asJsonString(invitationRequest))).andReturn();

		ResponseEntity<InvitationDto> actual = interestController.invitation(invitationRequest);

		assertEquals("invitation sent", actual.getBody().getMessage());

	}

	@Test
	public void viewSentInterestTest() throws Exception {
		List<ViewInvitationDto> viewInvitationDtos = new ArrayList<ViewInvitationDto>();
		ViewInvitationDto viewInvitationDto = new ViewInvitationDto();

		viewInvitationDto.setReceiverId(1L);
		viewInvitationDto.setSenderId(2L);
		viewInvitationDtos.add(viewInvitationDto);

		Mockito.when(invitationService.viewSentInterest(2L)).thenReturn(viewInvitationDtos);
		ResponseEntity<List<ViewInvitationDto>> viewInvitationDtoRes = interestController.viewSentInterest(2L);
		List<ViewInvitationDto> dbviewInvitationDtos = viewInvitationDtoRes.getBody();
		assertEquals(1, dbviewInvitationDtos.size());
	}

	@Test
	public void viewReceivedInterest() {

		List<ViewInvitationDto> viewInvitationDtos = new ArrayList<ViewInvitationDto>();
		ViewInvitationDto viewInvitationDto = new ViewInvitationDto();

		viewInvitationDto.setReceiverId(1L);
		viewInvitationDto.setSenderId(2L);
		viewInvitationDtos.add(viewInvitationDto);

		Mockito.when(invitationService.viewReceivedInterest(1L)).thenReturn(viewInvitationDtos);

		ResponseEntity<List<ViewInvitationDto>> viewIniationDto = interestController.viewReceivedInterest(1L);
		List<ViewInvitationDto> dbviewInvitationDtos = viewIniationDto.getBody();
		assertEquals(1, dbviewInvitationDtos.size());
	}

	@Test
	public void action() {

		ActionrequestDto actionRequestDto = new ActionrequestDto();
		actionRequestDto.setRecieverId(2L);
		actionRequestDto.setSenderId(1L);
		actionRequestDto.setStatus("ACCEPTED");

		ActionResponseDto actionResponseDto = new ActionResponseDto();
		actionResponseDto.setMessage("Ac");

		Mockito.when(invitationService.action(actionRequestDto)).thenReturn(actionResponseDto);

		ResponseEntity<ActionResponseDto> actionResponse = interestController.action(actionRequestDto);
		ActionResponseDto actionRes = actionResponse.getBody();
		assertEquals(actionResponseDto.toString(), actionRes.toString());
	}

	public static String asJsonString(final Object obj) {

		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

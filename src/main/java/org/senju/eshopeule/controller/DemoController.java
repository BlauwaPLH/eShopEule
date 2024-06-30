package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.enums.NotificationType;
import org.senju.eshopeule.dto.NotificationDTO;
import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.dto.request.LoginRequest;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.exceptions.SendNotificationException;
import org.senju.eshopeule.repository.RoleRepository;
import org.senju.eshopeule.service.NotificationService;
import org.senju.eshopeule.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import static org.senju.eshopeule.constant.enums.NotificationType.ContentType.VERIFY_SIGNUP;
import static org.senju.eshopeule.constant.enums.NotificationType.SendMethodType.EMAIL;

@RestController
@RequiredArgsConstructor
public class DemoController {

}

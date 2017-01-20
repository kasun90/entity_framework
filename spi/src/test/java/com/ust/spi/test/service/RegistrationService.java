package com.ust.spi.test.service;

import com.ust.spi.test.UserResponse;
import com.ust.spi.test.command.UserRegisterRequest;
import com.ust.spi.test.command.PasswordResetRequest;
import java.util.concurrent.CompletableFuture;

public interface RegistrationService {
  CompletableFuture<UserResponse> registerUser(UserRegisterRequest request);
  CompletableFuture<UserResponse> resetPassword(PasswordResetRequest reqeust);
}


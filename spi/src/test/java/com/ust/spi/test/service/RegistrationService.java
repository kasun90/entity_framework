/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ust.spi.test.service;

import com.ust.spi.test.UserResponse;
import com.ust.spi.test.command.UserRegisterRequest;
import com.ust.spi.test.command.PasswordResetRequest;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author nuwan
 */
public interface RegistrationService {
  CompletableFuture<UserResponse> registerUser(UserRegisterRequest request);
  CompletableFuture<UserResponse> resetPassword(PasswordResetRequest reqeust);
}

